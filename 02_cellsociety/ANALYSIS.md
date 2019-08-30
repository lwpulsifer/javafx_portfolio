# CompSci 308: Cell Society  Analysis
## Liam Pulsifer, lp139


> This is the link to the assignment: [Cell Society](http://www.cs.duke.edu/courses/compsci308/current/assign/02_cellsociety/)


Design Review

=======

### Overall Design

The absolute highest-level design is as follows:
* The menu class governs all of the JavaFX visualization contained in the project. It gets the group of Shapes which make up the grid visualization from one of the GridView subclasses, which represent the different possible shapes.
* The GridView class simply takes in a two-dimensional data structure (in our implementation a 2-D list of Cell objects) and returns an appropriately-sized set of Shape objects in the correct shape. The subclasses include TriangleGridView, SquareGridView, and HexGridView, which visualize those shapes.
* The GridModel and its subclasses are what determine the actual data stored about the state of the various cells. There is a grid subclass for each simulation, which can update the state of its cells and then "move forward" by changing to those updated states.
* The CellModel class actually holds the state of any particular Cell in the grid and can update that state. There is a CellModel subclass for each simulation type. 
* The NeighborFinder class is a static class which, given a Grid object and the shape of the cells in the grid, can find the neighbors of a given cell. 
* The XML Manager and XML Parser classes read in XML files of our format.
* The XML Builder class deals with file creation, again in our specified format.

### Individual Components

---------

**Visualization**:
* The visualization component of our project is contained entirely within two full classes, the Menu and GridView classes (and the three subclasses of GridView). 
	* The Menu class works almost entirely independently of any other class. The only thing that it needs is for GridView to pass it a Group of JavaFx Nodes, which it then visualizes within the main display. Other than that, everything is handled within Menu or passed to the Menu's GridView instance variable for handling. 
	* The GridView class is abstract, and its three subclasses are only instantiated within Menu. The GridView class's two main jobs are:
			1. To create the set of JavaFx Shapes representing the states of each cell contained in the Grid object (which is also instantiated in Menu). 
			2. To set On-click properties for those shapes so that the user can change the state of a cell on click (calls a method of the CellModel class when clicked).
		The GridView class is heavily (in fact, completely) dependent on the GridModel class and its subclasses. Without those, the GridView class would be unable to do anything. 

**Simulation:**
* The simulation aspect of our project is contained mostly within two class families: GridModel and CellModel, with help from the NeighborFinder static class.
	* GridModel is responsible for keeping track of our data structure (a 2-D ArrayList) of CellModel objects. It has methods to find the next states of all the cells and to update all the cells (two separate methods because CA automation requires cell updates to be separate from the next state of their neighbors). It also has methods which allow for setting its own size, returning its kind, and finally getting input from the XML Reader to create a grid from an xml file. The GridModel class is dependent on each individual CellModel class to actually get and move to the next states of each cell -- the GridModel just manages the collection of CellModels.
	* CellModel is responsible for the individual cell states. Each cell can find its next state, set its next state, output its state for XML building, and add to its array of neighbors. There are subclasses for each simulation type, so each simulation type has different instance variables and auxiliary methods to govern its individual behavior. The CellModels are the lowest level class in our simulation, so they depend relatively little on any other class. However, they are instantiated within the GridModel classes. Each cell has a *neighbors* variable which holds a list of CellModels containing its neighbors.
	* Neighborfinder is a static class which is responsible for finding the neighbors of each cell in the grid.

**Configuration**
* The individual configurations of the simulation are controlled by the XMLReader class, which reads in initial configurations of Cells from an XML file. It then passes the size and type variable from the xml file to menu, which then instantiates a new GridModel of that size and type. Finally, it creates a list of "Cell Edits", which are the cells that should be set in a non-default state according to the XML file. It passes that list to the GridModel, which uses its getInput and each Cell within it's getInput method to edit the cells contained in the GridModel's data structure. 
* To Create configurations, the XMLBuilder class is instantiated in the Menu class. All that the XMLBuilder class needs to build a new xml file and save it is to be passed the current GridModel, so it has very few dependency relationships. 

**To add a new simulation:**
* Adding a new simulation to our project is a more complex process than it should be. First, you would have to create a new GridModel subclass and a new CellModel subclass. If the new simulation has any new requirements for how neighbors should be calculated, you'd need to add a new findNeighbors method with that configuration in the findNeighbors class. Finally, you'd have to make some small edits to the drop-down menus and the array of possible simulation types found in Main.

**Dependencies**

As a whole, the dependencies within this project are easy to find and clear, except perhaps within the Menu class. The Menu class has some methods which do too many things, so it's not always clear what is being instantiated by what. For example, I used the handleGridShapeInput method to instantiate my GridView object in Menu, when that's not clear from the method name. In the other components of the project, however, the design is clear about where and how it's sending data. The parameters of methods are clearly named to represent their functions, and methods that pass data are public. 

**Components I did *not* implement**
1. NeighborFinder class
* Readability
	* I found this class very hard to read and understand. There are four separate methods called GetNeighbors, which each have different parameters but are otherwise almost identical. Their purpose is to get the nieghbors of a cell with different cell shapes and boundary types considered, but it's difficult to see that from the code, especially since there are few comments. Helper methods are also confusingly named. For example, the method upside determines if a Triangle has its point up or not, but the name of the method does not make that clear
```
private static boolean upside(int r, int c)
	{
		return ((r%2 == 0 && c%2 == 0) || (r%2 != 0 && c%2 != 0));
	}
```
The method itself is a dense logical function which is confusing to read and wouldn't give an outside reader any clue as to what the function was trying to do. 

* Encapsulation
	* This program is relatively well encapsulated. It takes in the grid and finds the neighbors of all the cells within it. This means it's very resistant to changes in how the GridModel functions, unless the GridModel no longer has a 2-d list of cells, which would be a drastic change. One would need to add in new methods for any change in how neighbors are calculated, but that's essentially unavoidable. One area in which this program fails to follow encapsulation principles is in the helper methods like upside, which I copied above. Many of the getNeighbors classes rely heavily on our specific implementation of the simulations (i.e. how triangles in certain columns are always pointing up in our simulation) which, if changed, would throw a wrench in our program. 
* What I've learned from this code
	* I've learned how difficult code can become to read with even a single extra loop or conditional statement. I will definitely be making my methods even shorter and more concise after spending some time reading this code. 
```
public static void getNeighborsToroidal(List<List<CellModel>> grid, Triangle t)
	{
		int size=grid.get(0).size();
		for(int r=0; r<grid.get(0).size(); r++)
			for(int c=0; c<grid.get(0).size(); c++)
			{
				if(upside(r,c)){
					rs=rowindexu;
					cs=colindexu;
				}
				else{
					rs=rowindexd;
					cs=colindexd;
				}
				for(int a=0; a<rs.length; a++)
						if(rs[a]!=0||cs[a]!=0)
							try{
								grid.get(r).get(c).addNeighbor(grid.get((r+rs[a])).get(c+cs[a]));
							}
							catch(IndexOutOfBoundsException obobrow) {
								try {
									grid.get(r).get(c).addNeighbor(grid.get(size-Math.abs(r+rs[a])).get(c+cs[a]));
								}
								catch(IndexOutOfBoundsException obobcol) {								
									try {
										grid.get(r).get(c).addNeighbor(grid.get((r+rs[a])).get(size-Math.abs(c+cs[a])));
									}
										catch(IndexOutOfBoundsException obobcolrow) {
											grid.get(r).get(c).addNeighbor(grid.get(size-Math.abs(r+rs[a])).get(size-Math.abs(c+cs[a])));
										}
								}
							}
			}
	}
```

2. WatorGrid.java
* Readability
	* This code is far more readable than NeighborFinder. Most methods are fewer than 10 lines, and contain at most two for loops. The methods are also far less confusingly named. Compare "moveForward()" to "getNeighbors(List<List<CellModel>> list, Rectangle r, String nebtype, String gridtype". The logic of the different methods is also easy to follow. Look at update() for example.
```
@Override
	public void update()
	{
		for(int r=0; r<size; r++)
			for(int c=0; c<size; c++) {
				WatorCell temp = (WatorCell) gridCells.get(r).get(c);
				temp.getNextState(starverate, reporate1, reporate2);
			}
		NeighborFinder.getNeighbors(gridCells, new Rectangle(), "cross", "standard");

	}
```
It's very simple to see that this method moves through the GridModel's grid data structure, sets the next state of each cell, then finds that cells new set of neighbors. The submethods called are well named (getNextState and getNeighbors, even though getNeighbors is confusing in implementation).
* Encapsulation
	* This class is also well encapsulated because it works almost entirely with its own data. As long as the cells within the grid have the same methods that the WatorGrid can call, there won't be a problem integrating changes in cell behavior into the WatorGrid class. 
* What I've learned from this code
	* The one method that I had trouble reading in this class showed me a lot about how important good style conventions could be. The programmer left off a lot of brackets on for loops and if statements, and that really affected my ability read the code. I think that good style is an underrated aspect of design because a program can be impeccably designed, but if it's illegible what use is the design? It can never be modified by anyone else if they can't read it, so it's essentially dead code. Looking at the setSize() method in this class, that's how I would feel.

**Overall Style and Layout**
- The style and layout of this code is not completely unified. Looking again at the setSize() method of the WatorGrid class
```
	public void setSize(int t)
	{
	ArrayList<List<CellModel>>tempCells = new ArrayList<List<CellModel>>();
		
		for(int a=0; a < t; a++)
		{
			tempCells.add(new ArrayList<CellModel>());
			for(int b=0; b<t; b++)
			{
				tempCells.get(a).add(new WatorCell());
			}
		}
		if(t>size)
		{
			int center=(t-size)/2;
			for(int r=0; r<size; r++)
				for(int c=0; c<size; c++)
					tempCells.get(r+center).set(center+c, gridCells.get(r).get(c));
		}
		else
		{
			if(t<size)
			{
				int center=(size-t)/2;
				for(int r=0; r<t; r++)
					for(int c=0; c<t; c++)
						tempCells.get(r).set(c, gridCells.get(r+center).get(c+center));
			}
		}
		gridCells=tempCells;
		size=t;
		
	}
``` 
it's clear that these layout conventions (omitting for loop and if statement brackets, using a space after a larger conditional declaration before opening a set of brackets), are very different from this method, the drawBlankGrid() method from the SquareGridView class
```
public Group drawBlankGrid(int screenwidth, int screenheight, double blocksize){
		Group retgroup = new Group();
		for (double i = gridXPosition; i < gridXPosition + gridSize; i += blocksize){
			for (double j = gridYPosition; j < gridYPosition + gridSize; j += blocksize){
				Rectangle toAdd = new Rectangle(i, j, blocksize, blocksize);
				toAdd.setFill(Color.ANTIQUEWHITE);
				toAdd.setStroke(Color.BLACK);
				retgroup.getChildren().add(toAdd);
			}
		}
		return retgroup;
	}
```

This method uses full bracketing at all times, and keeps the brackets in the same line as each loop or conditional. The layout is very different as well, with the first method being more "vertical" and spaced out, while the second is more compact and tight. This discrepancy in style is common throughout the program. As we can see, as well, however, naming conventions are similar between both -- Object names use camel case, while primitives use lower-case words. 

**My Design** 

As was partly described above, my component of our project is contained entirely within two class families, Menu and GridView (and the three subclasses of GridView). 
	* The Menu class is the main creator of all JavaFx objects. It initializes the Stage, Scene, Nodes, and Shapes contained within the actual screen that a user sees. It also creates and steps through the animation by creating and interacting with Grid and GridView instance variables. 
		* The general process for interacting with the grid visualization is that on each step, the Menu calls its GridModel's update and moveForward functions and then calls its GridView's drawGrid method to redraw the grid based on the GridModel's new state. 
			* As that happens, the GridView takes in the GridModel as a parameter, then iterates through the grid data structure and creates a visual grid using each Cell in the grid's color value. It then returns a Group of JavaFx Shapes to the menu, which renders those shapes in the new step of the animation. 

**Two Implementations**
1. GridView class = Good
	* I think my GridView class and its subclasses are well implemented. The classes are well-encapsulated -- all they need in order to work is to be passed a GridModel object and the dimensions of the window to put it in and they'll do the rest. Their strength is that they do a few very specific things very well; mainly, they visualize grids in different shaped tessellations. They also exemplify the replace conditionals with polymorphism principle because one can easily display any grid in a variety of different shapes, just by passing it to different subclasses of GridView. This code also has very few dependencies on other existing code because its job is so simple. 

2. Menu class = Bad
	* Menu is a huge, bloated class that really needs to be split up into several different classes. As it is, the Menu class is responsible for so many different things that it often grows confusing as the reader to figure out whats going on. I think there should be a class responsible for configuring all of the auxiliary (non-grid) elements of the JavaFx scene because as it is, all of the individual methods which govern the possible buttons, menus, and sliders, are confusing and unnecessary. It would be great to have a class called ButtonGenerator which could generate buttons in predefined configurations, like rows or columns. Also, the Menu does several distinct classes of things at a time -- it initializes scene elements, it creates and steps through an animation, and it creates the Grid and GridView variables. Those should really be separated. Separatinng those into classes would also reduce the number of magic values present in the implementation. One element of this class that is positive, however, is that it has few dependencies on other classes. 

**Flexibility**
In my mind, this code is not very flexible. As I said above, to add a new simulation type to our simulator, one has to add at least two classes, a large method or two in NeighborFinder, and some code in Menu. That really doesn't seem sustainable, especially if simulations are very similar. 
* *Two Features of Interest*
1.   Ability to display two separate animations at the same time
	* This feature is interesting because it exemplifies the need for more classes within Menu that I described above. Without classes to handle the creation of buttons, the animation (or different animations if we wanted to have them run separately) , and the various scene variables, this would be a nearly impossible task, or at least a much more time-intensive one. 
	* We would need to add some new closed features, like perhaps an Animator class, which would create animations and attach certain scene features to them. The Animator would have to be closed to modification so that no one could mess with the Animations as they ran. Luckily, GridView would already be extensible to this new design. One would just have to instantiate a new GridView object for the second (or third or fourth, etc.) grid to be shown. If one had an Animator class and perhaps some helpful button generating and scene generating classes, this would be a very extensible design. One could easily visualize many different animations at once. 
2. Toroidal and Infinite Grids
	* A feature that is more flexible within our program is the finding of neighbors. Our NeighborFinder class, while not spectacularly designed overall, is relatively easy to add new features to. We actually implemented the functionality for toroidal grids within our neighborfinder, but we didn't have time to implement it within the menu class. To do so, however, wouldn't be difficult. We could easily add a gridshape instance variable within each grid to pass to the Neighborfinder class in the getNeighbors methods. To add Infinite grids would require only a couple of new methods within Neighborfinder. However, this doesn't follow encapsulation very well. As it is now, it would be a kind of hacky solution involving several instance variables. What would be better would be to add a NeighborType class or something of that nature which would define how Neighbors should be chosen.

**Alternate Designs**

* Our original design was mostly extensible. Our GridView subclasses prepared well for new shapes within the grid, while our different cell model subclasses prepared well for the new possible simulation types. However, a huge problem was that our findNeighbor methods were within each cell model. With more sophisticated simulation types, we would have needed to allow cells to access the next states of other cells, which made certain models (like Segregation) update in patterns, rather than randomly. To try to fix this, we changed our Grid class to an abstract class called GridModel. This made it easier to deal with complex simulations because we could have specific grid classes for each simulation, but it also made our overall code base much larger and more complex, which I think was a poor tradeoff.

*Two Design Decisions* 
1. One decision we made was to govern the finding of next cell states and the updating of cells within each cell type. This required there to be a CellModel subclass for each different simulation, but it also simplified the calculation of states within each cell because each cell type could have instance variables which could control its behavior. A slight problem with this design was that each Cell had a lot of control over its own destiny, as it were. The cells had to have access to the entire grid in order to calculate their next state. Another design that was proposed was to have a separate Model class which would exist solely to calculate the next states of a cell passed to it. This would remove the problems with the cells needing to access too much data, but it would add more problems with how the Model would gain access to the required information from Cell in more sophisticated models. On the whole, I think I prefer our current CellModel version because I think it allows the definition of "Cell" to be stretched to fit more abstract CA models like Ants or Langston Loops.
2. Another Decision we made was to make GridModel an abstract class and to have different GridModel subclasses for each simulation. This, as I said above, made certain simulations easier to program. For example, Wator-World requires that some aspect of the program have access to the whole grid because the positions of the various cells need to change based on the next position of other cells (i.e. two sharks can't share the same space after eating a fish). However, it added a huge layer of complexity to the program, which was in my opinion unnecessary. The different GridModels were now distinct for each simulation, but most of them have very similar functionality, so in my opinion, we could have cut down on repeated code and code complexity by leaving one central Grid class and adding to the different Cell abstractions in order to try to mitigate the problems of some of the tougher simulations. I think our program would have been better designed that way, but might have lost some higher-level functionality. In my opinion, that would have been an acceptable tradeoff for this project. 

Conclusions
Best Design Feature:

* I think the best feature of this project's current design are the XML Reader and XML Builder classes. They are concise and functional, and they allow the Menu class to read or write an XML file in fewer than 8 lines of code each, which is I think the peak of what an XML Reader or Writer should be able to do. A particularly good feature is the XML Manager class, which creates an XML Reader and performs all of the dirty work needed to get the one thing that we actually need, the list of edits to be returned from the file. This is a great example of good design because it simplifies what could be a difficult programming task into a few short expressions. The XML-related classes are perfectly encapsulated, have few dependencies, and do a few simple tasks very well. I've learned a lot from reading and working on these classes about how to combine discrete functionality into a few small classes which do everything that you really need and leave all of the behind the scenes work to be done within the class, hiding all of that data.
* I think the worst feature of our project is the Menu at the moment. It does everything that we need it to do, but it's not at all extensible or modifiable. Every change that one makes in Menu needs to be repeated several times throughout the code, and while its encapsulation is good, it is inelegant and could be divided much further for better functionality. A perfect example of this are the many methods like "getResetButton()" and "getPlayButton()" and getOutlineButton". These could easily be condensed or ported into a class to reduce extraneous lines of code. They mirror my thoughts about the class as a whole -- it's very repetitive and unnecessary, even though it does few things truly "wrong" per se.

**To do differently** 

* Don't confuse UI with just a display - treat is as a design problem in itself
* Don't be afraid to create new classes even if it might be a little hassle at the beginning
* Communicate my ideas and thoughts about the design with teammates better

**Keep doing**

* Communicate a *lot* i.e. ad nauseum
* Listen closely to teammates and be supportive of ideas proposed

**Stop doing** 

* Writing classes that are more than 200 lines
* Writing methods that are more than 20 lines
