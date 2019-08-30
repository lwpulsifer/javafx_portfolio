package authoring.controllers;


import java.util.ArrayList;
import java.util.List;

import authoring.Canvas;
import authoring.right_components.EventPane;
import authoring.right_components.EntityComponent.EntityPane;
import authoring.right_components.EntityComponent.EntityWrapper;
import frontend_utilities.ButtonFactory;
import game_engine.Entity;
import javafx.scene.control.Button;
/*
I think this is a well-designed class for a few main reasons.
First, it follows basic design principles
extremely well -- its methods are short, its variables and
methods are extremely intuitive and well-named, dependencies are clear and easy to find,
and it's well-documented.
Look especially at how readable the code is in methods like

	public void add(EntityWrapper wrapper){
		if (!entityWrapperList.contains(wrapper)) {
			entityWrapperList.add(wrapper);
		}
		addToLevel(wrapper.getEntity());
		wrapper.setLevel(levelController.getCurrentLevelId());
	}

This is pretty much like reading plain English.

The second major reason that I'm proud of this class's design is that it shows off our use of multiple
controllers. This makes design sense in cases involving interaction with the Level class of the Engine.
The entity controller is strictly supposed to deal with Entities, and Levels are not Entities,
so the Entity Controller can call on the Level Controller to perform that function. This increases
encapsulation and reduces dependencies because the EntityController doesn't need to interact with
unnecessary objects. In case of an error, this will make problems easier to debug. More generally,
this class is as free from dependencies as a controller can be. It allows its "children" to call
methods which add to its data structures, and then it performs operations mostly on its own data
structures, which reduces the chance of errors caused by changed state.

The only area I think this class could improve substantially would be that I think it could benefit
from inheriting from a basic controller class that we could define. Since we use multiple controllers,
it would be a good thing to standardize their behavior.
*/



/**
 * @author liampulsifer
 * manages interaction between EntityPane and Canvas
 * maintains a list of active Entity Wrappers and updates the level Controller
 * with new additions
 */
public class EntityController {
	List<EntityWrapper> entityWrapperList;
	private EntityPane entityPane;
	private Canvas canvas;
	private LevelController levelController;
	private EventPane eventPane;


	public EntityController(EntityPane pane, Canvas canvas, EventPane eventPane){
		this.entityPane = pane;
		this.eventPane = eventPane;
		this.canvas = canvas;
		entityWrapperList = new ArrayList<>();
		levelController = new LevelController();
	}

	/**
	 * Adds the passed Entity to the local list of entity wrappers
	 * adds the entity to the levelController
	 * @param wrapper the entityWrapper to be added to the list
	 */
	public void add(EntityWrapper wrapper){
		if (!entityWrapperList.contains(wrapper)) {
			entityWrapperList.add(wrapper);
		}
		addToLevel(wrapper.getEntity());
		wrapper.setLevel(levelController.getCurrentLevelId());
	}

	/**
	 * Adds the given entity to the level controller's active level
	 * @param entity
	 */
	private void addToLevel(Entity entity) {
		levelController.addEntity(entity);
	}
	/**
	 *
	 * @return a button which removes the currently selected entity
	 *          (Associated with the ImageView selected in canvas)
	 */
	public Button getRemoveButton(){
		return ButtonFactory.makeButton(e -> removeCurrentEntity());
	}

	/**
	 *  Removes the current wrapper from the entity list,
	 *  current level, and canvas, and calls entity pane to make a new wrapper
	 */
	public void removeCurrentEntity(){
		entityWrapperList.remove(entityPane.getCurrentWrapper());
		levelController.removeEntity(entityPane.getCurrentWrapper().getEntity());
		canvas.displayEntities(entityWrapperList);
		entityPane.createNewWrapper();
	}


	/**
	 * Makes all imageviews set to their default style
	 */
	public void resetImageViews(){
		entityWrapperList.stream().forEach(e -> {
			e.getImageView().setEffect(null);
		});
	}

	/**
	 * Updates the dummy image views used in all other panes except the entity pane
	 * (These image views have less robust on-click behavior)
	 */
	public void displayDummyEntities(){
		canvas.displayDummyEntities(entityWrapperList);
	}


	/**
	 * Creates a new entity at the user-clicked location
	 * called by Canvas on click
	 * @param sceneX -- X position of a user click on the canvas
	 * @param sceneY -- Y position of a user click on the canvas
	 */
	public void alertEntityPane(double sceneX, double sceneY) {
		EntityWrapper wrap = new EntityWrapper(entityPane.getCurrentWrapper(), entityPane);
		wrap.setPos(sceneX - wrap.getImageView().getFitWidth() / 2,
				sceneY - wrap.getImageView().getFitHeight() / 2);
		if (!entityWrapperList.contains(wrap)){
			add(wrap);
		}
		canvas.displayEntities(entityWrapperList);
		entityPane.newDuplicateEntity();
		this.resetImageViews();
	}

	/**
	 * Updates the canvas display with the current list of entities
	 */
	public void updateCanvas() {
		canvas.displayEntities(entityWrapperList);
	}

	/**
	 * Updates the canvas with an entity-wrapper list that's provided by the caller
	 * @param entList
	 */
	public void updateCanvas(List<EntityWrapper> entList){
		canvas.displayEntities(entList);
	}

	/**
	 * Adds the entity wrapper to the event pane box
	 * @param e -- an entity wrapper
	 */
	public void addToEventPaneBox(EntityWrapper e) {
		eventPane.addToEntityBox(e);
	}

	/**
	 * @return -- list of active entity wrappers
	 */
	public List<EntityWrapper> getEntities(){
		return entityWrapperList;
	}
	/**
	 * Adds a levelController for passing Entities to Data
	 * @param lc
	 */
	public void setLevelController(LevelController lc){
		levelController = lc;
	}
}
