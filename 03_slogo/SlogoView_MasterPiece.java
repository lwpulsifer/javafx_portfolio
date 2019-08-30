/*
 * Why do I think this class shows off good design?
 * 
 * I think a large part of how this code is well-designed is subtle -- it fixes a lot of the issues I 
 * vowed to work on after Cell Society. In this code, there are no magic values, variables and methods
 * are clearly and descriptively named, data is passed in an encapsulated manner, and there are no 
 * major dependencies on other parts of the code. The code is exceptionally readable (look at the 
 * constructor for a great example of how readable the bare code is), 
 * well-commented, and follows a coherent system of capitalization, naming conventions, and style.
 * 
 * It also shows off (though due to size constraints, can't show the whole picture) the
 * Observer-Observable heirarchy I used for this front-end design. You can see how the SLogoView class
 * both accepts data from its Observables when they call its update method, then responds to that data 
 * in the update method and passes its own data to its Observer (Main) for further processing. This forms 
 * the responsive, ripple effect that was so useful in this project and is so useful in many other contexts. 
 * 
 * Finally, I think this code is well-designed because of its succinctness and clarity. The methods are
 * short and readable, there is low cognitive and cyclic complexity, and the code's purposes are 
 * short and well-defined. 
 */


package views;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import turtle.Turtle;
import views.SceneElements.Console;
import views.SceneElements.CurrentState;
import views.SceneElements.History;
import views.SceneElements.Observable;
import views.SceneElements.Palettes;
import views.SceneElements.SceneElement;
import views.SceneElements.Toolbar;
import views.SceneElements.TurtleDisplay;
import views.SceneElements.VariableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SlogoView implements Observer, Observable{
	/*
	 * Constants relating to the characteristics of the main window as a whole
	 */
	public static final int WINDOWHEIGHT = 650;
	public static final int WINDOWWIDTH = 1200;
	public static final Color BACKGROUND = Color.ANTIQUEWHITE;
	/*
	 * Constants relating to the tool bar and its dimensions
	 */
	public static final double TOOLBARHEIGHT = 1.0/10 * WINDOWHEIGHT;
	public static final double TOOLBARWIDTH = WINDOWWIDTH;
	/*
	 * Constants relating to the Command History section of the main window
	 */
	public static final double CMDHISTORYX = 0;
	public static final double CMDHISTORYY = 1.0 / 10 * WINDOWHEIGHT;
	public static final double CMDHISTORYWIDTH = 2.0 / 9 * WINDOWWIDTH;
	public static final double CMDHISTORYHEIGHT = 4.5 / 10 * WINDOWHEIGHT;

	/*
	 * Constants relating to the Variable View section of the main window
	 */
	public static final double VARIABLEVIEWX = 0;
	public static final double VARIABLEVIEWY = 1.0 / 10 * WINDOWHEIGHT + CMDHISTORYHEIGHT;
	public static final double VARIABLEVIEWWIDTH = 2.0 / 9 * WINDOWWIDTH;
	public static final double VARIABLEVIEWHEIGHT = 4.5 / 10 * WINDOWHEIGHT;
	/*
	 * Constants relating to the Console section of the main window
	 */
	public static final double TURTLEVIEWX = CMDHISTORYWIDTH;
	public static final double TURTLEVIEWY = TOOLBARHEIGHT;
	public static final double TURTLEVIEWWIDTH = WINDOWWIDTH - 2 * CMDHISTORYWIDTH;
	public static final double PERCENTHEIGHT = .8;
	public static final double TURTLEVIEWHEIGHT = PERCENTHEIGHT * (WINDOWHEIGHT - TOOLBARHEIGHT);
	/*
	 * Constants relating to the Command Prompt section of the main window
	 */
	public static final double CONSOLEX = CMDHISTORYWIDTH;
	public static final double CONSOLEY = TOOLBARHEIGHT + TURTLEVIEWHEIGHT;
	public static final double CONSOLEWIDTH = TURTLEVIEWWIDTH;
	public static final double CONSOLEHEIGHT = (1 - PERCENTHEIGHT) * (WINDOWHEIGHT - TOOLBARHEIGHT);
	/*
	 * Constants relating to the State Prompt section of the main window
	 */
	public static final double STATEX = CMDHISTORYWIDTH + TURTLEVIEWWIDTH;
	public static final double STATEY = 1.0 / 10 * WINDOWHEIGHT;
	public static final double STATEWIDTH = 1.7 / 7 * WINDOWWIDTH;
	public static final double STATEHEIGHT = 4.5 / 10 * WINDOWHEIGHT;
	/*
	 * Constants relating to the Palette section of the main window
	 */
	public static final double PALETTEX = CMDHISTORYWIDTH + TURTLEVIEWWIDTH;
	public static final double PALETTEY = 1.0 / 10 * WINDOWHEIGHT + STATEHEIGHT;
	public static final double PALETTEWIDTH = 1.7 / 7 * WINDOWWIDTH;
	public static final double PALETTEHEIGHT = 4.5 / 10 * WINDOWHEIGHT;

	/*
	 * Local variables governing JavaFX objects in the main window
	 */
	private Group myRoot;
	private Scene myScene;
	/*
	 * Local SceneElement variables
	 */
	private Console myConsole;
	private Toolbar myToolbar;
	private VariableView myVariableView;
	private Palettes myPalette;
	private CurrentState myCurrentState;
	/*
	 * Data structures for SceneElements, variables,
	 * functions
	 */
    private Map<Integer, Turtle> turtles;
	private List<SceneElement> sceneElements;
	private List<Observer> observers;
	
	/*
	 * Creates a new SlogoView object, which 
	 * contains SceneElements laid out in an 
	 * Observable-Observer paradigm and a Scene 
	 * with JavaFX nodes representing the SceneElements
	 */
	public SlogoView(Map<Integer, Turtle> TurtleMap){
		Collections.copy(TurtleMap, turtles);
		initializeSceneElements();
		createObserverHeirarchy();
		observers = new ArrayList<>();
		myScene = initializeWindow(WINDOWHEIGHT, WINDOWWIDTH, BACKGROUND);
	}
	/**
     * Get the current scene, containing all SceneElements
     * and their JavaFX representations
     */
	public Scene getMyScene() {
		return myScene;
	}
	/*
	 * Adds this object as an Observer of all of the SceneElements
	 * Adds the turtles as observers of the toolbar, so that the tolbar
	 * can manipulate the turtles' line color
	 * 
	 */
	private void createObserverHeirarchy() {
    	for (SceneElement element: sceneElements){
    	    element.addObserver(this);
        }
        for(int i = 0; i<turtles.size();i++) {
            myToolbar.addObserver(turtles.get(0));
    	}
	}
	/*
	Sets the return value field of the Console to the
	double value returned from the back end in Main
	 */
	public void setConsole(Double d){
	    myConsole.setLittleField(d.toString());
    }
	/*
	 * Initializes all of the SceneElements necessary for SLogo
	 * (console, history, variableview, toolbar, turtledisplay, currentstate, palette)
	 * with the correct Observer/Observable and has-a relationships
	 * Adds all to the sceneElements list
	 */
    private void initializeSceneElements() {
        sceneElements = new ArrayList<>();
        myConsole = new Console(turtles);
        History myHistory = myConsole.getHistory();
		myVariableView = new VariableView();
		myToolbar = new Toolbar();
        TurtleDisplay myTurtleDisplay = new TurtleDisplay(turtles);
        myToolbar.addObserver(myTurtleDisplay);
        myCurrentState = new CurrentState(turtles);
		myConsole.setCurrentState(myCurrentState);
		myPalette = new Palettes();
		sceneElements.addAll(myConsole, myHistory, myVariableView, myToolbar, myTurtleDisplay, 
				myCurrentState, myPalette);
	}
    /*
     * Creates the Scene
     * Called by Main to create the window used by the application
     */
	private Scene initializeWindow(int height, int width, Color background) {
		Group root = new Group();
		myRoot = root;
		root.getChildren().addAll(getElementsAsNodes());
		return new Scene(root, width, height, background);
	}
	/*
	 * Gets the current JavaFX Nodes associated with each SceneElement
	 * using the getField() method of each element.
	 */
	private Group getElementsAsNodes(){
		Group retgroup = new Group();
		for (SceneElement element : sceneElements){
			retgroup.getChildren().add(element.getField());
		}
		return retgroup;
	}
	/*
	 * Called by a SceneElement object when it gets some kind
	 * of new input -- i.e. a user entering a command or clicking a button
	 * or a command node modifying an Element
	 */
	public void update(Object o) {
        updateScreen();
        updateObservers();
	}
	/*
	 * Updates all of the JavaFX objects constituting the visual display
	 * with their new values obtained by the getElements() method
	 * Updates Turtle lines displayed by querying turtle map. 
	 */
	public void updateScreen(){
		myRoot.getChildren().removeAll(myRoot.getChildren());
		myRoot.getChildren().addAll(getElements());
        for (Integer i : turtles.keySet()) {
            myRoot.getChildren().addAll(turtles.get(i).getLines());
        }
	}
	/*
	 * Updates the observers of the SlogoView (the Main class) 
	 * and passes the pass value from the Console (a string array)
	 * for processing
	 */
    @Override
    public void updateObservers() {
        for (Observer o : observers){
			o.update(myConsole.getPassValue());
        }
    }
    /*
     * Adds an Observer to the list of observers
     * Only used to make Main an Observer of this SlogoView
     */
	@Override
	public void addObserver(Observer o) {
		observers.add(o);
	}
	/*
	 * Updates the Variable View SceneElement with a new Variable map
	 * passed by Main from the backend.
	 */
	public void updateVarView(Map<String, Double> variables) {
		myVariableView.updateVarView(variables);
	}
}
