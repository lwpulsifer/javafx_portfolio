# ANALYSIS
## CS 308

> This is the link to the assignment: [SLogo](http://www.cs.duke.edu/courses/compsci308/current/assign/03_slogo/)


Design Review
=======

### Overall Design

#### High Level

At the absolute highest level, our SLogo project is very simply designed. There is one Main class which contains the actual JavaFX application. This main class creates a SlogoView object which consists of all of the visualization elements (called SceneElements) present in our application. The SLogoView object is the master Observer of all of these SceneElements, so when one of the SceneElements receives input from the user, it will call its UpdateObservers method, updating the SlogoView and passing the command or other input to the Main class. 

The Main class then passes the relevant information to the  back end by taking the user text input, converting each whitespace-separated value into a SlogoNode object, its type depending on which kind of command it should be. That group of commands is then passed to the TreeBuilder, which builds the execution tree. Then, the TreeBuilder passes the tree to the TreeReader, which reads and executes the tree, changing the Turtles and other scene elements within the command (it is passed the Turtles). 


##### Front End


The Front End consists primarily of the SLogoView class and its elements. A SlogoView object contains a map of Integers to Turtles, representing the Turtles and their IDs. It also contains a List of SceneElements. These SceneElements represent all of the different fields for display in the main window, i.e. console, command history, current turtle states, and the turtle display. The relationship between these SceneElements and the SlogoView is an Observer-Observable relationship, so the SlogoView can respond to changes in the SceneElements instantaneously. Each SceneElement object does this through its GetField method because a call to the update() method of SlogoView (the update method is the mechanism by which Observables update their observers -- they call each of the Observers in their Observer list's update method) uses the getField method of a SceneElement to redraw all of the SceneElements. 

Unfortunately, this relationship is not perfect. The (Command) History object, for example, does not determine its state on its own, so the Console class *has a* History object, while this History is also a SceneElement. 

The map of Turtles is passed to the backend at command execution time, but each Turtle is an Observable, and the Observer of each Turtle is the TurtleDisplay, so when the map of Turtles is passed to the backend, modification of those turtles will trigger changes in the front end. In essence, there are two levels of changes, user changes and changes via commands. Both start at the bottom of the Observer heirarchy and move up in a ripple. A drawing of the basic design is below, though it is not exhaustive.

![](./doc/frontend_design.jpg)

##### Back End

The backend of our project consists of three main parts: The CommandFactory, the TreeBuilder, and the Commands themselves. 

1. Command Factory
    The Command Factory takes in a "sanitized" array of strings(all lowercase, no impermissible characters, -- this is the front end's job to clean it up and convert the user input to an array of strings) and converts it into an equally-sized array of SlogoNodes. SlogoNode implements Command, an interface which includes two methods, getValue and getExecute, which get the return value and perform necessary execution functions, respectively. 
    To do this, it needs access to the function map contained contained by the Main class. This is passed in the update() method of Main. 
    The Command Factory uses its own internal instance of the NodeBuilder class to check the strings to see which SlogoNode should be built, and then it calls the appropriate method in NodeBuilder to build that node. The NodeBuilder class uses reflection to do so. 
    
2. TreeBuilder
    The TreeBuilder then takes in the array and builds a tree for execution, in which each SlogoNode has the correct number of children (i.e. a Forward node has one child and an Add node has two). It does this recursively using the Build and BuildList methods. Essentially, the more complex expressions are handled using a massive switch statement which checks the type of the SlogoNode and then builds the appropriate single node and list groups. The built tree is then passed to the TreeReader, which eecutes the tree. 
    
3. Commands
    Each SlogoNode has a getValue and getExecute method. Generally, the getExecute method calls another, private method which performs any necessary operations (such as moving the turtle) while the getValue method calculates the return value (such as calculating a sum). The getValue method gets its value by calling getExecute on its children, so the execution proceeds "backwards". 
    Each SlogoNode requires the Variable Map, Function Map, and Turtle Map from main, because these are the data structures which can be directly affected by the commands. These are directly passed in by Main.
    
    
### Necessary Additions for the Following

#### New Command to the Language:

Adding a new command to the language has a range of possible difficulties and required changes.
The following would be completely mandatory:
    - A new SlogoNode class which specifies the behavior of the SlogoNode on execution
    - A small addition to the properties files with the new command's possible names as a string
For a command which takes atypical arguments (i.e. something like dotimes, for, to, etc.), one might also need to make some changes to TreeBuilder because TreeBuilder uses the type of the parent SlogoNode to decide how to build the tree. 


#### New Front-End Component:

This is easy -- one can simply add a new SceneElement subclass, determine its relationships to the other SceneElements (perhaps it needs to have another SceneElement or be had by one), then add it to the List of SceneElements in SlogoView. As long as the positions work out, it should work fine. Depending on what it does, you might need to add more functionality if it needs to access the back-end, but a basic element requires very little work.

#### Un-Implemented Feature

We failed to implement the Ask command because we ran out of time, but it would have been relatively simple to implement. We would implement a new Ask subclass of SlogoNode, create a new case in TreeBuilder, and we wouldn't even need to modify the properties files. Our Ask would simply create a list of the valid turtles at the beginning of execution, change the turtles to the appropriate states, and then because getExecute runs through the execution tree recursively, we would just restore those turtles to their original states after the call to getValue.

### Dependencies

Dependencies in this project are not perfect. There are some back-channels, and a lot of the functionality of the front-end/back-end relationship requires directly passing the data structures in question, which isn't great design. On the whole, however, it's relatively easy to follow the dataflow, partly because of that same direct-passing relationship. There's no need to decipher what exactly this "TurtleMap" is because it's the same TurtleMap used everywhere else in the program.


#### Two components I did NOT implement

1. SlogoNode/Subclasses

In my opinion, this set of classes is very well designed. They are quite readable -- see below.

```java 
public class Forward extends SlogoNode{  
     
// private double value = 0;  
// private double distance = 0;  
   public Forward() {  
      numchildren = 1;  
   }  
  
   private void forward(Map<Integer, Turtle> turtleMap, Map<String, Double> VarMap, Map<Integer, Double> map) {  
      for (int n : turtleMap.keySet()) {  
         if (turtleMap.get(n).isActive()) {  
              Point2D newlocation = new Point2D(turtleMap.get(n).getLocation().getX() - map.get(n) * Math.sin(Math.toRadians(turtleMap.get(n).getHeading())),  
   turtleMap.get(n).getLocation().getY() - map.get(n) * Math.cos(Math.toRadians(turtleMap.get(n).getHeading())));  
   turtleMap.get(n).setLocation(newlocation);  
   }  
      }  
   }  
  
   @Override  
   public double getExecute(Map<String, Double> VarMap, Map<String, SlogoNode> FunctMap, Map<Integer, Turtle> turtleMap) {  
      // TODO Auto-generated method stub  
   double step = 0;  
   HashMap<Integer, Double> map = new HashMap<>();  
 for (int n : turtleMap.keySet()) {  
            if (turtleMap.get(n).isActive()) {  
                VarMap.put("ID_RESERVED", (double) n);  
   step = getValue(VarMap, FunctMap, turtleMap);  
   map.put(n, step);  
   }  
        }  
//        for (Integer i : map.keySet()){  
//            System.out.println("Key: " + i + " Value: " + map.get(i));  
//        }  
   forward(turtleMap, VarMap, map);  
 return step; //returns the final value of the node  
   }  
  
  
   @Override  
   public double getValue(Map<String,Double> VarMap, Map<String, SlogoNode> FunctMap, Map<Integer, Turtle> turtleMap) {  
      // TODO Auto-generated method stub  
   List<SlogoNode> leaf = this.getChildren();  
   //System.out.println(leaf.get(0));  
   return leaf.get(0).getExecute(VarMap, FunctMap, turtleMap);  
   }  
     
}

```
  
It's not particularly well-commented, but the basic logic is quite easy to read. The getExecute method has a difficult section to read involving the HashMap, but the logic is concise -- the loop calls getValue for each Turtle in the Map. The rest is particularly easy to read --  calls forward() and returns the execution value. The only thing I can think of that would improve the readibility of that last section is to rename the "step" variable to "executionval".

This class is also a great showcase of how encapsulated the SlogoNode class heirarchy is. Modification of this command has essentially no effect on any other part of the code. For example, to make this command move the turtle at an angle of 45 degrees, one could simply make some slight modifications to the forward method, and it would work perfectly with no changes to any other code elsewhere in the project. 

This code helped me see how important it is to keep method behaviors separate. The three-method sequence of getExecute, performBehavior() (in this case forward), and getValue, keeps everything really tightly encapsulated and allows for easier modification than trying to do it all in one method.

2. NodeBuilder -- 

```java 
/*  
 * builds an individual Command Node 
 */
 public SlogoNode createNode(String input) {  
   String formalCommandName = null;  
  
     if (languageMap.containsKey(input)) { //if the map exists  
       formalCommandName = languageMap.get(input);  
     }  
     else{  
          throw new InvalidParameterException("NOT A COMMAND");  
     }  
       System.out.println(functionMap.keySet());  
       //if statement if the command is a predefined TO function.  

 /* \* create a method that checks if the function exists and returns the correct command object with parameters. */   Class<?> commandObject = null;  
    try { //try to create a new class object based on name.  
           commandObject =         Class.forName("Movement."+formalCommandName);  
    }  
   catch (ClassNotFoundException e1) {  
      try { //try to create a new class object based on name.  
   commandObject = Class.forName("Bools."+formalCommandName);  
   } catch (ClassNotFoundException e2) {  
         try { //try to create a new class object based on name.  
   commandObject = Class.forName("MathOps."+formalCommandName);  
   } catch (ClassNotFoundException e3) {  
            try { //try to create a new class object based on name.  
   commandObject = Class.forName("Query."+formalCommandName);  
   }catch (ClassNotFoundException e4) {  
               try {  
                  commandObject = Class.forName("VarOp." \+ formalCommandName);  
   }catch (ClassNotFoundException e5) {  
               e4.printStackTrace();  
   }  
         }  
      }  
  
      }  
   }  
   Constructor<?> c = commandObject.getConstructors()\[0\];  
   SlogoNode command = null;  
 try {  
      command = (SlogoNode) c.newInstance();  
   }  
   catch (InstantiationException | IllegalAccessException| IllegalArgumentException |InvocationTargetException e) {  
      // TODO Auto-generated catch block  
   e.printStackTrace();  
   }  
   return command;  
}

```

On the whole, this method is very readable. The Map logic is concise and easy to read. The only part that I would change is the large try-catch block, which, while clever, should probably be its own separate method so that it can be more readable. As it is, it's not obvious what the purpose of all those nested statements is.

In terms of encapsulation, this is again a well-desiged method. It takes in a String and returns a SlogoNode, and those are its only connections to the rest of the code. Changing the implementation of the method would have no effect on the rest of the project. The only thing that harms encapsulation in it are the stack traces that remain undealt with and should be resolved. 

From this code, I can see the importance of condensing longer methods into multiple methods. This method is just long enough that it starts to become distracting to me. 

#### Consistency

The code is generally consistent in its naming conventions and other stylistic traits. We used lowercase variable names for primitive types, and camelCase for Objects. We also used camelCase for methods, but we used verbs to distinguish them from Objects.

Liam's Code
```java=
public Node getField(){  
    return vBox;  
}  
public String\[\] getPassValue() {  
    return passValue;  
}  
public ToolBar getToolBar(){
```
Brandon's Code
```java=
public double getExecute(Map<String, Double> VarMap, Map<String, SlogoNode> FunctMap, Map<Integer, Turtle> turtleMap) {

public double getValue(Map<String,Double> VarMap, Map<String, SlogoNode> FunctMap, Map<Integer, Turtle> turtleMap) {
```
Jamie's Code
```java=
private void setRotate(double degrees){  
 turtleview.setRotate(-1 \* degrees);  
}  
private void addLine(Point2D newpos) {  
    if (!myPenUp) {  
        Line l = new Line();  
   l.setStroke(lineColor);  
   l.setStrokeWidth(2);  
   l.setStartX(currentpos.getX() + TURTLESIZEHALF);  
   l.setStartY(currentpos.getY() + TURTLESIZEHALF);  
   l.setEndX(newpos.getX() + TURTLESIZEHALF);  
   l.setEndY(newpos.getY() + TURTLESIZEHALF);  
   lines.add(l);  
   //System.out.println(lineColor);  
   }  
}
```
Ryan's Code
```java=
public SlogoNode\[\] convertStringtoNode(String\[\] commandList, Map<String, SlogoNode> functions){  
   SlogoNode\[\] nodeList = new SlogoNode\[commandList.length\];  
   NodeBuilder nodeBuilder = new NodeBuilder(functions);  
   System.out.println(functions.keySet());  
 for (int i = 0; i<commandList.length;i++) {   
      String current = commandList\[i\];  
   SlogoNode currentNode = null;
```


### Your Design

High-Level

- My Code is mostly in the front-end. The design consists of a main SlogoView object, which contains the group of SceneElements which constitute the display (i.e. the Toolbar, CommandHistory, Console, etc.). Each of these is an Observable object, with the SlogoView being its Observer. The idea is that user input to the SceneElements will ripple up to the SlogoView, and then on to Main, which will pass the input to the back-end. The Turtle objects are Observables of the TurtleDisplay, which is itself an Observable of SlogoView. Since the Turtle is passed to the back-end to be moved around, a kind of double-ripple forms from the SceneElements to the to back to the bottom again, then up to the SlogoView for display. It wasn't originally intended to be this way, but the structure of the back-end made it necessary. 
- Generally, the update() function of the observable paradigm functions as a signal and doesn't pass anything meaningful to its Observers. Instead, it simply tells them to update, and then the observers act on their own. 

Features

1. As I mentioned above, the Observer-Observable paradigm in my code isn't as robust as I'd like it to be, but it gets the job done. This was a sensible design choice because the SceneElements in my code need to be reactive to user input and alert the main view in order to update the display. However, something that I wrestled with that made it challenging was that some SceneElements seemed to need to do multiple different things in their update method. I.e. this example from the ToolBar class
```java=
    public void updateObservers(){  
    for (Observer o : observers){  
        if (o.getClass().getTypeName().equals("views.SceneElements.TurtleDisplay")) {  
            o.update(picker.getValue());  
   }  
        if (o.getClass().getTypeName().equals("turtle.Turtle")){  
            o.update(newpicker.getValue());  
   }  
        else {  
            o.update(new Rectangle());
```
I wanted this method to be able to use two different color picker objects to do two different things in the view, but this makes for some unwieldy conditional statements and creates additional dependencies on the object passed, rather than working regardless of the object passed.

Overall, however, I feel that this design was a good choice for this project and worked well as I designed it.

2. TreeBuilder

This feature was less well-designed. It's essentially one massive switch-statement to deal with the possible nodes and their arrangements of children. It works by building individual expressions and lists with the build() and buildList() methods, and the various conditionals just deal with different configurations of those expressions and lists. However, it is poorly designed because it doesn't split bloated methods well enough into other methods. For example, there are several kinds of commands which have the same configuration of syntax (i.e. command, expression, list) and should be able to use the same build method, but don't. This creates repeated code and redundancy. 

The root of this problem is a poor design choice I made at the very beginning, which was to create a buildcounter variable which is local to the TreeBuilder class. This keeps track of where the builder is in the array at all times. However, this is modified in so many places that it creates a shared-state problem. This meant that it was often easier to create new methods than to try to condense existing methods into workable methods with multiple applications.

However, this code is relatively free of damaging dependencies. It has little chance of affecting the rest of the code in a detrimental way if modified because it has access to very little data and doesn't modify the data that it does have access to. 

### Flexibilty

This project is flexible in some key areas, but inflexible in others. It is very flexible in terms of adding commands. One must simply add a new Command class with a getExecute and getValue method. You may have to add the command name to the properties files, and you might have to add a method to the tree builder if the command uses a unique set of parameters. 

However, it is rather inflexible in terms of adding new SceneElements, mostly because the locations of the SceneElements are static constants in the SlogoView class. This means, to add a new SceneElement you have to figure out its location directly and the add it to that list of locations. However, if that location system were fixed to be more flexible, it would be very flexible for adding SceneElements.

Unadded Features:

1. Multiple Windows

This feature is interesting because saving preferences for a new window requires adding a new element to the back-end. Creating the multiple windows could be accomplished by changing the one SlogoView in the Main class to an array of SlogoViews, then adding to that array using some kind of button and displaying those windows separately. This could be accomplished with the classes that already exist.

However, saving preferences for these windows would require a new set of classes dealing with saved data, perhaps in xml files. An XMLBuilder and Reader would be required, and the SlogoView class would have to read from the data files on initialization, as well as provide functionality to save to xml files. Luckily, this would be easy to make very encapsulated because these xml classes would not have to be incorporated heavily into the command functionality of the program. It could also be extended easily to allow for saving other forms of data, such as command histories, sets of functions, etc. 

2. The SetPalette Command

The SetPalette command would have required some method for the command nodes to get back to the SlogoView in order to access the palette data structure. This is one of the principle design flaws of our code -- the backend doesn't actually contain most of the data it works with. 

To really implement this well would require a major refactoring of our code, but as a start we could create a new class in the back-end which would function as a kind of data store, and then access the data in that storage from the front end. That would make the code less encapsulated but it would make it far easier than passing many data structures back and forth. It would also diminish problems with shared state. This would also enable more flexibility because we wouldn't have to add so much repeated code passing those variables around. 

### Alternate Designs

Our project's original design did a relatively good job of handling the extensions, but it did have trouble with the commands which dealt with things other than the turtle, i.e. variable map, function map, palettes, etc. This was because our original design just passed front-end elements to the back-end to be worked on. We didn't really make any changes to this -- instead, we just passed more and more things to the back-end. This worked as a stopgap, but probably isn't sustainable long term, as I mentioned above. 

#### Design Decisions

1. One design decision we made was to make the Commands into Classes and instantiate them as SlogoNodes for execution. Our original plan was to just have one large Command Controller class with methods for each command. Our original plan had the advantages of being simpler to think about, and it didn't require reflection. However, it was much less flexible and would have required a **massive** Command Controller class. Using reflection to create instances of subclasses was much more efficient and flexible. It allowed us to instantiate those objects directly, tie them together into a tree, and execute them all at once, rather than having to figure out how to execute the right methods in order with our original approach. Our design now is definitely what I would prefer.

2. Another decision we made was to separate the creation of the individual SlogoNodes and the tree composed of them. I built the TreeBuilder, so I can say with confidence that it was a complex task. Working with the full array means that you always need to be keeping track of which node doesn't have the right number of children yet. Another idea that we considered was building the tree within the CommandFactory class. I think that this could have simplified the tree building aspect of the program because you could consider each node as it was built, always working with the correct node by default without having to use the buildcounter variable I ended up using. However, it would have made the CommandFactory class much longer and harder to read. On the whole, I think our design decision was a justifiable one, but I would have liked to see how a different implementation could work as well. Each design was equally encapsulated because neither changes the nodes, just ties them together. On the whole, I would consider the two equally valid. 


### Conclusions

I think the best feature of our design is the reflection-based Command structure. I think this is encapsulation and polymorphism at their finest. Each node has distinct, yet related functions that can be called with the same syntax and interacted with in the same way. That's really amazing. Before helping to implement this, I don't think I'd really realized the true power of polymorphism as a tool to produce productive code. I'd thought of it mainly as a novelty that I kept hearing was useful but hadn't seen a really elegant implementation of. This, however, solved so many of the problems that would make this project horrifically difficult without it. 

The worst feature, in my opinion, is the tree builder. It takes entirely too long to add new commands to the tree builder, and it ruins the beauty of the SlogoNode command structure by destroying the flexibility which makes it so easy to add new commands. My ideal TreeBuilder would require no more than a few new lines of code for a new node type, or ideally none at all. I think working on this TreeBuilder has helped me to realize that it might be better to take a step back and try to rethink a tough coding question before writing a huge class full of repeated code just to make things work.

To become a better coder and designer for VOOGA, I want to 

Continue: Communicating well with my teammates, writing concise methods and classes (outside of Tree-Builder of course), using design patterns

Start: Focusing on refactoring during programming -- rather than never, think about dataflow, making sure my role is well-defined before I start working, using new design patterns 

Stop: Writing bloated classes with repeated code, taking shortcuts instead of using good design


