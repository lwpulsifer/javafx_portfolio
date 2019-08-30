CompSci 308: VOOGASalad Analysis
===================

> This is the link to the assignment: [VOOGASalad](http://www.cs.duke.edu/courses/compsci308/current/assign/04_voogasalad/)


### Overall Design
Reflect on the coding details of the entire project by reading over all the code.
#### Describe the overall design of the complete program(s):
##### What is the high level design of each sub-part and how do they work together (i.e., what behavior, data, or resources does each part depends on from the others)?
* Authoring is divided into a few main parts, organized within a class hierarchy inherited from the GUINode interface.
These GUINodes are the Canvas and four different panes, the EntityPane, EventPane, Level Pane, and StoryBoard pane.
Generally, the Canvas deals with displaying Entities, the EntityPane creates them, the Event Pane defines relationships
between entities, the Level Pane defines features of a full level, and the StoryBoard pane defines the relationships
between levels. Authoring works closely with Engine to create entities and Events (using Engine's factories to 
do so), and it works closely with Data to save these collections of entities and events for loading by player.
There is very little direct interaction between Authoring and Player.
* Player is designed loosely as an MVC, with the Data Connect and Data Manager handling loading, Player View handling the
game display, and Engine acting as the Model. The other classes in Player are mostly utilities for generating the 
specific views necessary for the PlayerView. Player is reliant on Data to generate the information it needs to create 
the games, and relies on Engine to provide the interaction systems necessary, but it is very flexible in that it can 
display almost anything when provided the right information. 
* Engine follows the Entity, Component, System game design pattern. Entities have Components, and the way those 
Entities interact is defined by the System relating to specific ones of those Components. System also use Events, which 
are defined as groups of Conditions and Actions. Whenever those Conditions are true, those Actions will be carried out.
Engine is the most encapsulated of all of the subgroups in our project. It's capable of functioning almost entirely on its
own, as long as some entities exist.
* Data is the most simple of all of the subgroups here. It has a ManipData class which both saves and loads data into 
xml. It interacts with Authoring and Player to receive and pass back Game data, but has no interaction at all with 
Engine, which makes it very flexible and encapsulated.

##### How is the design tied (or not) to the chosen game genre. What assumptions need to be changed (if anything) to make it handle different genres?

The design is most tied to our game genre (scrolling platformer) through the Engine's Systems and Components. In theory,
our Authoring Environment, Player, and Game Data could work equally well for a Tower Defense game, an RPG, or another game type.
We would simply need to modify the existing Components and Systems to accommodate new types of interactions between entities.

##### What is needed to represent a specific game (such as new code or resources or asset files)?

All that's needed to represent a specific game is a folder containing three xml files: The main xml file (named 
GAMENAME.xml), the configuration file (named GAMENAMEconfig.xml), and the metadata file (named metaData.xml). In the future,
however, we would like to add a game's resources, such as images and soundfiles, to its data folder as well. 

### Describe two packages that you did not implement:
Package 1: GameData
Package 2: game_engine.systems.keyboard
##### Which is the most readable (i.e., whose classes and methods do what you expect and whose logic is clear and easy to follow) and give specific examples?

The Keyboard package is by far the more readable of the two. It has a really structured class hierarchy, including
a KeyboardMovementSystem abstract class with a well-commented act method which uses well-named constants to show 
that it moves an entity in response to keyboard movement. Then, there are subclasses for each direction of movement,
which are all elegant and easy to read, again using well-named constants to identify all relevant variables, and then
calling the superclass to actually carry out the behavior associated with that particular movement direction.

The Data package is also relatively readable, but the ManipData class is very long, and while the method names 
(like saveData and loadData) are good, there are some strange naming conventions throughout the code. For example, 
an Engine object is named "lilGuy" at one point, which makes it kind of hard to read later in the code. I would have liked
to see a Loader and a Saver class extracted from this to make it a little more readable. Positively, however, the methods
are pretty short and the method names are generally good. 

##### Which is the most encapsulated (i.e., with minimal and clear dependencies that are easy to find rather than through "back channels") and give specific examples?

The Data package is very much encapsulated. All that it needs to save data is the objects its trying to save, and all that it
needs for loading is the file path for the folder to be loaded. It's pretty much impossible to have a more encapsulated 
design than this one, unless you went so far as to build a generalized saver and loader that would work for any data
type, in which case you would essentially be building a better XStream. It could be better packaged into various 
subclasses, however, to make the data flow even easier to follow.

The Keyboard package is also very encapsulated. It has minimal and clear dependencies, requiring nothing except the 
parameters in the KeyboardMovementSystem constructor to function as intended. I actually can't see how this could 
be made more encapsulated at all. 

##### What have you learned about design (either good or bad) by reading your team mates' code?

I've learned from both of these packages how helpful a well-encapsulated design can be. Both of these are so minimally
dependent on other classes or objects that whenever there's a bug with one of them, we've been able to identify it
in a second because it's so easy to see exactly which part of the code is at issue. 

They've also reiterated the importance of good documentation and naming conventions. Both are very well designed,
but the Keyboard package is much better named and documented, and that made it much easier for me to quickly read and
analyze the code. 

### Your Design
#### Reflect on the coding details of your part of the project.
#####Describe how your code is designed at a high level (focus on how the classes relate to each other through behavior (methods) rather than their state (instance variables)).
My code mostly dealt with the Entity and Event panes, so I'll divide this question in two using that division.
* Entity Pane: The entity pane creates entities, and the way that I did that was with a class called an EntityWrapper,
which contained an Entity, its associated ImageView, and a list of ComponentMenus. ComponentMenus are blocks of related 
MenuElements, which are user entry fields that have a one-to-one correspondence with Engine Components. This allows for 
convenient divisions of these MenuElements so that the user can easily see related things, without the Engine having
to modify its design at all. The ComponentMenus are then displayed to the user, and edits to the MenuElements are reflected
in the components. All of the instantiated ComponentMenus and MenuElements are read in from resource files, making the 
design very flexible. When an EntityWrapper is created (by clicking the screen), its Entity is added to the EntityController
and its ImageView is added to the Canvas. This allows for easy export of all of the Entities to the LevelController, which saves
the game when needed.
* Event pane: The Event pane defines how Entities relate to one another. The Event pane has two sub-panes, the AddAction and 
AddComponent panes, because these are the two things you need to add to an Event to make it work. I tried to make this
pane follow a similar structure to the EntityPane in that there are various input fields which define what a Condition
or an Action need in order to be instantiated, and those are read in from resource files and then displayed on the screen. 
The Event Pane also communicates with the Level Controller on a save game by passing it the list of all of the created
Events. 

##### Describe two features that you implemented in detail — one that you feel is good and one that you feel could be improved:
######Justify why the code is designed the way it is or what issues you wrestled with that made the design challenging.
###### Are there any assumptions or dependencies from this code that impact the overall design of the program? If not, how did you hide or remove them?

1. ComponentMenu and MenuElement system. 

The ComponentMenu system, as I described a little bit above, is the way I 
organized MenuElements into groups based on user experience considerations. The way that this works is that the 
ComponentMenuFactory reads an Arguments.properties file, which has keys of the names of ComponentMenus, and values that
are lists including the name, Menu Element type, and default value of several Components. The factory then creates a 
ComponentMenu which includes entry fields for each of those components. Each MenuElement is in turn created by a 
MenuElementFactory, which creates an input field of some type, while also creating the corresponding component using
reflection. This makes it easy to create components and their displays simultaneously. One example from the Arguments.properties file is this: 
*YPhysics=YVel,d,0.0;DefaultYVel,d,300;YAccel,d,-500*

This means, make a ComponentMenu called YPhysics, with three menu elements. Their names are YVel, DefaultYVel, and 
YAccel, all are of type double (meaning create a NumberMenuElement), and they have the specified default values. 

I designed this way of handling different components at the beginning of the project, when our priority was making sure
that no matter how many components were added, authoring would be able to handle it. We settled on properties files
and reflection as a way of doing that because we wanted it to be easy for Engine to add new Components as they saw fit
without having to consult authoring. This design makes it really easy to do that.

There are some assumptions going into this code: the format of the properties file assumes a certain set of properties 
of a component, namely that each component has only a name, a type, and a value. If Engine were to add any other 
parameters for Component instantiation, we would need to change our interface. However, this isn't a big problem 
because Engine had a very strict definition of what a component should be, and it didn't seem likely that that would
change regularly, if at all. 

I think this is really good design because it's incredibly flexible for adding new components or changing the types of 
components. Nothing is assumed beyond the type and number of a component's parameters, and so this design can accomodate
almost any change that Engine could want to make to their components.

2. AddActionPane

The design of the AddActionPane is frankly terrible. It relies on one massive switch statement to determine which Action
to add and how to add it. This makes it very rigid to any addition of a new Action, and since the switch case is based
on a String, any small change to the String would break the functionality of the pane.

The resign that I designed it this way was because the Engine's definition of what constitutes and Action was so 
scattered. The number and type of Parameters that an Action can take in is not at all standardized, so it was very 
difficult to make an effective factory that would create Actions based on a set number and type of parameters. 
Furthermore, I had to create input fields to address all of the types of those parameters, so I already had to do most
of the switch-case logic in the front-end. That's why I decided to just put all of that logic there. I considered trying
to use reflection to call different methods of another class, but I abandoned that because of time constraints and the 
aforementioned problem with nonstandard Actions.

If I had the opportunity to redo this class, I would work with Engine to refactor the Action interface and create an 
Action factory that would make it easy to create new Actions by simply passing in a few lists or doing something else 
like that. 


### Flexibility
#### Reflect on what makes a design flexible and extensible.

For me, the most important factor in a flexible, extensible, design, is the ease with which a programmer can add a new,
related feature to an existing codebase. However, for me this doesn't just mean a fancy inheritance structure or a 
sophisticated system of reflection calls. Something else this class has taught me is that an important part of what 
makes a design flexible and extensible is its readability, conciseness, and documentation. A spectacular design is 
useless if another programmer can't read and understand it in order to add to it. With the number of good tools that
exist today, a difficult to understand piece of code is a bad one, and you can't call a design flexible unless it's 
actually easy to work with, rather than just easy to work with in theory.

#####Describe how the project's final API, the one you were most involved with, balances
#####Power (flexibility and helping users to use good design practices) and
#####Simplicity (ease of understanding and preventing users from making mistakes)

I think that one of the biggest flaws in our final API, and what I would consider the biggest lesson from working on 
VOOGAsalad, is that "good" design doesn't always seem to entail ease of use. In fact, I think that's something that should be 
mentioned far more in this class. Our final project is extremely flexible and well-designed, especially in Engine. 
However because there's so much flexibility in terms of what you can do with our Entities, Components, and Actions, 
it can be overwhelming as a user or as another programmer trying to figure out how to make everything work. 
Our package hierarchy helps out with that a little bit, but in general it's difficult to interpret things like 
"ComponentCollisionEvent". I couldn't really tell you what that means, even though I've been working on this for 
weeks. I think the best solution to this is better documentation because it's inevitable to have some confusion in a
project of even this scale, and so the best way to make sure that Simplicity is maintained as Power increases is to 
make sure that every stage of the project is documented thoroughly.

#### Describe two features that you did not implement in detail — one that you feel is good and one that you feel could be improved:
1. PlayerView.java -- I thought this was good, but not spectacular
* What is interesting about this code (why did you choose it)
I was interested in this code because it's similar to what I did in authoring -- it is a view element, but one that 
interacts closely with the Engine. It does many of the things that Authoring does, but in reverse, where it's taking
in data from saved states and displaying it, rather than Authoring, which takes in user data and converts it to 
saved state.
* What classes or resources are required to implement this feature?
It appears that this feature requires an Engine, a View Manager, a Data Manager, and a Data Connect to operate fully.
* Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?).
As a display feature, this is by definition pretty closed to extension. It does, however, make use of the ViewManager to generate the 
necessary Javafx elements, and that ViewManager implements GUIBuilder, making it part of our team's overall frontend
design hierarchy. In terms of encapsulation, there are some definite code smells in here, like switch cases, hard-coded
cases for certain engine components, and assumptions about which components exist in the game. For example, this line
*primary = myEngine.getLevel().getEntitiesContaining(Arrays.asList(PrimeComponent.class)).get(0);* 
is problematic because it assumes that the level contains an entity with a Prime Component, the component used to 
indicate the character controlled by the user. This is a reasonable assumption to make because pretty much every game
has a player character, but since it is unchecked, it could create null pointers if the user forgot to create a 
character with a prime component. There are some other small problems like this, where the code assumes certain features
of the game that, while reasonable, might not always be there. 
* How extensible is the design for this feature (is it clear how to extend the code as designed or what kind of change might be hard given this design)?
It's not quite clear how one could extend this design to fit new types of games. It would be easy, I believe, to add 
components to Engine that would still work in Player. This is because most of the logic of the game's functionality is
within Engine. However, if the Engine wanted to make a drastically different game, some of the logic in this viewer 
would have to be reworked substantially. Let's say, for example, that the Engine wanted to make a multiplayer game, with
the camera following the center of the four players. This would require at least another few methods on Player's part 
to make work.

2. Navigation Pane -- could use a little work
* What is interesting about this code (why did you choose it)
I was interested in this code because it's code that I worked all around, but didn't actually have a hand in making.
It interacts with almost everything that I made, but again, I didnt' ever get a chance to work on it.
* What classes or resources are required to implement this feature?
It requires a MetaController, a Pane, a Stage, and a Loader. 
* Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?).
I think this class is open to extension. It's easy to imagine changing some of the implementation details slightly and 
having a navigation pane class that could easily show some buttons, given an array of titles and event handlers.
It's also quite encapsulated because all that its buttons are doing are notifying listeners or calling methods of the 
metacontroller. There are pretty limited dependencies in what this class actually does. It does, of course, assume
the extistence of certain methods in metacontroller, but other than that, i think it's free of unnecessary dependence.
* How extensible is the design for this feature (is it clear how to extend the code as designed or what kind of change might be hard given this design)?
This design is relatively extensible, especially in its main buttons which update the class's listeners. One would simply
need to add a new string or two to the ArrayLists at the top, and one would be good to go. It would perhaps have been
better to use static constants or to read from a resource file, but I don't mind the way it's done here. The less flexible 
part would be the if-else statement in the initialize buttons method. I would have liked to see some more methods pulled
out of there, perhaps even classes, so that the if statement would be more readable and less unwieldy. As it stands now,
a programmer would have to write several lines of code after interpreting the existing code to make an addition. 

### Alternate Designs
####Reflect on alternate designs for the project based on your analysis of the current design or project discussions.
#####Describe how the original APIs changed over the course of the project and how these changes were discussed and decisions ultimately made.
Our original APIs changed a good bit over the course of our project. Discussions of major changes were generally made as
a group, while smaller, more implementation-based decisions, were made at the sub-group level. Authoring made some large
changes to our API by adding additional controllers to our design. Since this wasn't a public API change, however, we 
were able to make that decision as a subteam. In fact, most of the changes made by authoring were not public-facing, 
and thus we made our decisions as a sub-group. Player was very similar in that regard, though Player and Authoring did
have significant meetings to establish a relatively unified GUI hierarchy using the GUIBuilder and GUINode classes.

Engine, however, was very communicative with the rest of the team in making their decisions because their decisions 
had such an immediate impact on the rest of the team. One large change that Engine made was to make components all take
in only one value as a parameter, essentially making them storage units for one piece of data alone. This changed how
authoring interacted with Engine dramatically, necessitating the creation of the MenuElement classes, rather than the
only ComponentMenu based versions before that decision. Whenever Engine made a decision, though, they were quick to 
arrange meetings with the other subteams that decision would affect, explain their thought-process, and solicit 
input as to what would implementation easier on the other members of the team.

Data, as well, made some modifications to their original API. Sometimes they sprung this on the rest of the team out of 
the blue, but overall they messaged in our slack with any substantial changes. Their API changed to accomodate meta-
data and settings data.

##### Describe a design decision discussed about two parts of the program in detail:

One design decision that the authoring team made was to use a system of multiple controllers to organize the creation
of entities and events.
* What alternate designs were proposed?
Another design that we considered was a more straightforward system of observer and observable, with a chain of observer
relationships dictating the creation of entities (a la Slogo). 
* What are the trade-offs of the design choice (describe the pros and cons of the different designs)?
The advantage of the multiple-controller system is that we could create several different controllers, each of which interacted
only with the classes it needed to, rather than one massive controller which would have access to almost every front-end
element, thus compromising encapsulation. The major benefit of this system over a generic observer-observable pattern 
or a pub-sub pattern was that it increased readability for us as we worked within our system. It wasn't necessary to 
say "Who's observing this again? Because if I send this Entity to the wrong element it'll mess things up". Instead, 
one could call easily identifiable methods of directly related front-end components. 
* Which would you prefer and why (it does not have to be the one that is currently implemented)?
I definitely prefer the method that we chose, but I also wonder if a larger controller might have been useful
because at the end of the project we needed to send data from almost everywhere in our code to almost everywhere else.
This meant the main advantage of the multiple controllers over one controller was minimized, though it did still help
with readability to be able to say "Oh of course, if I want to save a level I should call the level controller".

Another design decision we discussed at length was how to handle metadata. Data chose to save metadata and user
settings as two separate maps of strings to strings.
* What alternate designs were proposed?
A couple of alternate designs were proposed, including saving all of this data as one large map, creating a class to
handle these data, and saving the data as a new form of component called a GameComponent, fitting the ECS model. 
* What are the trade-offs of the design choice (describe the pros and cons of the different designs)?
The problem, in my opinion, with saving these data as maps of strings to strings, is that nothing can be done with the
data other than read it or display it. It's completely static. This is an advantage, however, in terms of how easy it
is to manipulate the data. It requires no methods other than iterating through a map, and as such it has no real 
dependencies. 
* Which would you prefer and why (it does not have to be the one that is currently implemented)?
I would prefer to create custom objects to save these data. I think that way Player wouldn't have to invest any energy
in getting the data and displaying it, but rather could instantiate an object and then call its methods to get 
different viewing configurations or ways of examining the data. I think this would also be more extensible because it 
could handle many types of data other than just string to string.

Conclusions
* Reflect on what you have learned about designing programs through this project, using any code in the project as examples:
There are a lot of lessons that I've learned from this project, but I think the most major one is that I truly feel
now how important readability and documentation are. I knew it before, but I didn't really feel it in a visceral 
sense. This project, however, was big enough that I lost track of my own code sometimes, and that was really tough.
I had to really focus on method names, conciseness, and documentation. That's not to say I suddenly became a wizard of
documentation and beautiful, concise code! In fact, I wrote some of my ugliest code ever in this project. But I 
knew when I was writing ugly code that I was going to have trouble reading later, and that's half the battle.

* Describe the best feature of the project's current design: what did you learn from reading or implementing it?
I think the best feature of this project's design is the component system. It's really amazing in its brevity and 
readability, while also being incredibly flexible. Look, for example, at the Collectible Component.

```java
public class CollectibleComponent extends Component<Double>{
	
	public CollectibleComponent(String arg){
		super(Double.parseDouble(arg));
	}

}
```
This is all that you need in order to make something collectible (true, it requires the Collectible system to actually
function, but it's still amazing how concise this is). The number and combination of Components allowed are limitless, 
and so it allows for almost any combination of behaviors. 

* Describe the worst feature that remains in the project's current design: what did you learn from reading or implementing it?
The worst feature remaining is certainly the AddActionPane. That thing is a behemoth, and I'm pretty embarrassed to have 
implemented it. However, I learned a lot from it: namely, the importance of designing a feature all the way through from
the front end to the back end. The Engine side of adding actions is really easy, but I didn't communicate well enough
to them how difficult it would be to handle all of the different kinds of actions without a sophisticated factory system 
to make it work, and so I ended up having to throw together a mess of code to make it functional, when if we had all been 
involved in the design from the start, we would have had a much better product all the way through. 

### Consider how your coding perspective and practice has changed during the semester:
##### What is your biggest strength as a coder/designer?
This, for some reason, is a really tough question for me to answer. I don't consider myself a front-end guy, but that's 
where I ended up doing most of my work this semester. I don't really consider myself a skilled designer, but I did a lot
of design this semester. I don't naturally like to lead, but I found myself a leader several times this semester. I think
the answer is that my biggest strength might be adaptability and willingness to do what needs to be done in order to 
achieve a project's goals. Thinking back to SLogo, I took on the responsibility of creating the instruction trees
from an array of instruction nodes, even though I was doing all of the front end. Even though that instruction tree 
class turned out to be a horrifically ugly piece of code, it worked as we expected, and rarely had problems. In this regard,
I think my greatest strength weakness might be the same thing. I'm willing to work as hard as needed to get things done,
but this limits me because I'm scared to ask others for help. 

##### What is your favorite part of coding/designing?
My favorite part of coding and designing is when a really slick new feature falls into place (like the first time I 
used a stream to accomplish a loop behavior). I realize, however, that this doesn't happen often, so I think my favorite
part of the process is thinking about how the user would actually interact with an application. I'm really interested
in behavioral science and human decision-making, and so I like especially to think about how unconscious choices that 
users make might be affected by an application's design.

##### What are two specific things you have done this semester to improve as a programmer?

1) I've coded an hour every day this entire semester, in a variety of languages (C, Java, Python). I'm hoping that sheer practice in a 
variety of paradigms will help me, and I think it really has. I definitely find myself approaching programming problems
in new and interesting ways that I would never have at the start of this semester.
2) I've worked to become better at the command line and with Git, which has been surprisingly helpful. On each team I've
been a part of, I've become the go-to Git guy who fixes everyone's Git problems, and while that's kind of a pain, I 
think I have a better fundamental understanding of computers than I would otherwise. 

