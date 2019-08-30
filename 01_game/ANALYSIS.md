Analysis -- Breakout Game, lp139
=================

## Time Review
* Estimated time spent actively working: 15 hours
	* Start Date: January 15th
	* End Date: January 22nd
* I would estimate that over 90% of my time was spent coding new features, refactoring, and debugging. I spent very little time (perhaps 1-2 hours) doing preparatory reading, designing, or documenting. 
* My general workflow was as follows
	1. Develop a new "piece" of the game (i.e. add a paddle at the very beginning)
	2. Test it to see if it worked.
	3. Commit the change
	4. Develop a new piece, etc.
	* Using this general model, I would often have bugs develop far after the piece that was the root cause of the bug had been committed and tested because I wasn't paying attention to the long-term view of how my code should work, but was rather adding instance variables and methods whenever they seemed necessary to solve the immediate problem.
* The hardest thing for me during this process was calming down and trying to plan out new pieces of my game and how they would relate to the overall body of code rather than letting myself just go and code at full speed. I think I could have been far better served doing more reading of the JavaFX documentation and online game-development sources than just starting out coding as I did. Most of my major bugs were caused because I had a flawed understanding of JavaFX, which led to a small bug, which then led to me putting in unnecessary code to try to fix it, resulting in large design flaws.
	* For example, I thought that the JavaFX method
```java myScene.setOnKeyPressed(---)```
		set up a one-time key listener that would only accept the next keyboard input. This led to me put that method in my step() function, which not only led to the *instance variable* OnKeyPressed being set extraneously 60 times per second, but also led to a serious bug in which the game over screen was impossible to exit from without force-quitting the Java application.

## Commits

* My commits fluctuated in size, with the largest commit having over 250 modifications and the smallest, 11. The median commit was about 75 changes. 
* I made a total of 34 commits during the project. Looking back, I think I would have been better served with closer to 45. Some commits, especially  a71adcfc45e1f1a19c17015371c93259223bfae2 , were far too large and encompassed many changes to very distinct features. It was noticeable that at times I got tired, forgot to commit for a long period, then committed a large block of altered code with an nondescript message like "new src" (which is the commit message for the commit referenced above). In retrospect, I have absolutely no clue what "new src" is supposed to mean. 
* However, I think that most of my commits were reasonably sized and had descriptive messages associated with them. Most refer to one specific feature that I had recently implemented, like "Added working powerups" or "Add Ball and Bouncing Capability", or even "Add laser line for ball direction, implement catching mode on 1000 point intervals)", which describes two changes but in a clear and concise manner. 
* #### Two Example Commits
	1. *Add nonfunctional powerups*
		* This commit was a relatively large commit in terms of number of additions/deletions (104), and its size reflects its importance. The purpose of this commit was to create all of the infrastructure for the game's powerups (though the powerups that were created had no function other than to fall when their associated block was destroyed). It created a PowerUp class which extends Circle and three subclasses of PowerUp with different traits (i.e. colors and effects). It also created a list of the powerups present in a scene at a given time and initialized them randomly (with a certain percent chance of inhabiting a given block). I chose to package these functions as one large commit because I felt that there was a high likelihood of serious bugs when trying to implement the effects of the powerups. In retrospect, however, I think committing the work on the new classes in one commit and the work on the "main" class in another commit would have been easier to follow later.
	2. *Add Base Labels*
		* This was a very simple commit that added some basic, static labels to my game. It was an unimportant commit, but it was very effective because it's so small (21 changes) and easy to digest. The commit message really says all that needs to be said about the commit, so it's super easy to look back at the commit log and see exactly where that feature went in.

## Conclusions

* I think my work on this project overall was extremely inefficient and ineffective. I completely underestimated the size of the project and was far too ambitious with my PLAN.txt file. This led to me rushing to try to catch, which led to skimping on the design and planning element of the project and writing rushed code that I didn't fully understand. I think in future, I can estimate better by simply being more conservative with my planning and focusing on getting basic features right before moving on to anything more complex. 
* The parts of my code that require the most editing are the parts that are the most JavaFX heavy. I think my overall design (which I will elaborate on later in this document) was reasonably thought-out, but the issue was that I didn't spend as much time planning how to implement it within the necessary framework. This led to issues with Key Input, the step function, and the JavaFx Scenes and Animations, which led me to compromise design to avoid bugs. 
	* The main class of my code is the one that I would work on the most. It needs to be completely refactored in order to avoid redundancy and logic errors within the JavaFx framework.
* To be a better designer, I need to really focus on planning and the non-coding side of application design. I need to make sure that I know what every method and class I use does and why it works the way that it does, rather than relying on copying code from better programmers. Also, as I move into team-based projects, I need to make sure that my commits, documentation, and communication are clear and concise. 

## Design OverView

### Status 
* My Code is generally consistent in its layout, style, and naming conventions, but it varies immensely in terms of descriptiveness and ease of reading. I would consider the code generally readable, especially within the subclasses for Game Objects. In the main class, however, there are many code blocks which were inserted hastily within the step function or level setup function which are not immediately clear. In fact, for many of them, it's not obvious why they're even necessary. One example of those is the following:
```java 
	if (score % 1000 == 0 && score != 0){
			catching = true;
			if (!myRoot.getChildren().contains(catchtext)){
				myRoot.getChildren().add(catchtext);
			}
		}
		else {
			catching = false;
			myRoot.getChildren().remove(catchtext);
```
In the step function, I hastily added a feature to turn on "Catching Mode" whenever the player's score is a multiple of 1000. However, I failed to ever document this change or make it into its own descriptive method. As it stands, it is just a lonely block of code with no obvious purpose. It's also completely unclear what the statements with myRoot do, when in reality they simply add text on the screen to alert the player that they're in catching mode. The initialization of the Text variable *catchtext* is way up in the setUpLevel method, where it is not apparent how that variable will be used. This dependency on instance variables and strange interplay between disparate methods characterizes many of the design problems in my project.

#### Two Pieces of Code

1. 
 ```java 
 public boolean hasIntersect(Ball ball){
		if (this.getX() <= ball.getCenterX() + ball.getRadius() / 2 && 
				this.getX() + this.getWidth() >= ball.getCenterX() - ball.getRadius() / 2){
			

			if (this.getBoundsInParent().intersects(ball.getBoundsInParent())){
				ball.setYSpeed(ball.getYSpeed() * -1);
				ball.setCenterY(ball.getCenterY() + 2 * ball.getYSpeed());
				return true;
			}
		}
		if (this.getY() <= ball.getCenterY() && 
				this.getY() + this.getHeight() >= ball.getCenterY()){
			

			if (ball.getBoundsInParent().intersects(this.getBoundsInParent())){
				ball.setXSpeed(ball.getXSpeed() * -1);
				ball.setCenterX(ball.getCenterX() + 2 * ball.getXSpeed());
				return true;
			}
		}
		return false;
	}
```
This method, which is within the Block class, checks whether the Block in question intersects a Ball object b which is passed to it. Right away, a problem with this code is that the method name is not at all an accurate summary of what the method does. The method name is hasIntersect(), but while the method does check if the Ball intersects with the Block, it also checks *how* and *where* that intersect happens, and then adjusts the ball's direction accordingly. More accurately, this method could be called bounce(Ball b). Another thing that could make this method easier to follow would be to make the two larger if statements into a method called isBetween(double lower, double upper, Ball b) which would check if the Ball is between the two passed values. This would replace the two confusing if statements checking whether the ball is within the X-range or Y-range of the block with an easy to understand named method that accomplishes the same thing. 

2.  ```java 
     private boolean areBlocks(){
		if (blocklist.size() == 0){
			return false;
		}
		for (int i = 0; i < blocklist.size(); i++){
			if (!blocklist.get(i).getKind().equals("impenetrable")){
				return true;
			}
		}
		return false;
	}
	
This method, however is much easier to read and understand. It simply checks the blocklist instance variable to see if there are any penetrable blocks in it. The method is well named because it simply returns whether there any blocks exist (whether there areBlocks). It also uses no bulky if-else statements, but instead follows a simple structure with easily readable methods.

## Design

### Outline

* The general outline of my game's design is quite simple. The initial setup calls a method which sets up the level from a block file which contains the position and type (moving, impenetrable, regular, two-hit, or three-hit) of all of the blocks in the level. It also initializes the paddle, ball, and random powerups within the blocks. The Ball, Paddle, Blocks, and Powerups are all their own Objects, and there are three kinds of PowerUp which are distinct Objects as well. 
* On each step through the animation, my code does (generally, there are some side-steps to deal with power-ups, the pausing functionality, and others) the following:
	1. Checks if the paddle has no lives left, and if so goes to the game over screen
	2. Updates the position of all the PowerUps on screen
	3.  Updates the position of moving blocks on screen (only level 3)
	4.  Check if the player has actually started the game yet, and if so updates the position of the ball
		* if not, updates the position of the ball as attached to the paddle
	5.  Checks for intersections between ball and paddle, ball and block, or ball and wall. 
	6. If there are no penetrable blocks left, moves to the next level. 
   * During this whole time, the native JavaFx OnKeyPressed feature is checking for key inputs for the different cheat codes 

### Adding a New Level

* Adding a new level **with the existing set of blocks and powerups** is very easy. One just needs to design and create a new blockfile which details the block positions and types, and then change the one line of code which sets the You Win screen to activate when the player reaches "Level 4" to activate at Level 5 instead.
* Adding a new level with new powerups or types of blocks would be difficult because the number and kind of powerups and the kinds of blocks are inflexibly designed. The method initPowerUp simply chooses randomly between the three existing PowerUps, so it's poorly suited to adding more PowerUps. 

### Design Strengths and Weaknesses

#### Strengths
* Flexible with new block configurations and levels
* Classes and Objects are generally sensible and relate to one another in expected ways 

#### Weaknesses
* Inflexible with regard to adding powerups or block types, and also with adding new features like additional paddles or balls.
* Many methods depend on their timing within the step method and so are difficult to debug and 
* Much of the data is accessed by getter and setter methods and doesn't follow data-hiding principles (keeping it shy)

### Assignment Spec Details

1. Making levels harder as they go along
	* One relatively easy implementation detail in this project was making successive levels harder each time. To implement that change all that was needed was to make the paddle smaller and the ball faster in each call to the setupLevel() method. One can just permute the paddle size and ball speed by level. For example 
```java
paddle.setWidth(200 / level);
ball.setSpeed(3 * level);
```

This implementation works well in my current game, but assumes a lot about the program. First, it assumes a small number of levels because as the level variable grows the paddle size and ball speed will quickly become unsustainable. Also, it assumes that there are only one ball and one paddle. This makes the design relatively unflexible if you wish to have many balls, perhaps of different speeds. Luckily, however, there are no extra classes or resources required to implement this feature aside from an existing Ball and Paddle. 

2. Moving Blocks
	* To implement moving blocks, I simply added a speed variable to my Block class and then when a Block of "type" (not really a type, but simply a string called type) 'mover' is initialized, the speed is set to either 1 or -1 rather than the default of 0;
	* To make the Movers actually work, I had to add several methods, including ones which check for collisions with other blocks and walls, and ones to simply update the position of the block. 
	* In retrospect, I think that making a Mover a subclass of Block would have been a better option because then I wouldn't have had to use so many if statements in my methods which determine what to do with the blocks. I could have simply relied on the different behavior of the Mover subclass to perform the function I needed.

## Alternate Designs

1. One alternate design decision that I wish I had made would be for the PowerUp functionalities to be integrated into the objects being powered up. For example, the AddLife powerup would trigger the PowerUp Method of the Paddle class when it intersected the paddle, which would then add a life to the Paddle's lives count. 

   As it stands now, what happens is each PowerUp has a String variable which is what type of PowerUp it is, and then when the PowerUp hits the paddle, I call a dense block of if statements in the getEffect() method of the GamePlay class (below) to determine what effect it has and then to implement that effect. 
```java
private boolean getEffect(PowerUp p){
		if (p != null) {
			if (p.getKind().equals("LoseLife")
					&& p.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
				paddle.subHalfLife();
				myRoot.getChildren().remove(p);
				powerups.remove(p);
				return true;
			} else if (p.getKind().equals("ExtraLife")
					&& p.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
				paddle.addHalfLife();
				myRoot.getChildren().remove(p);
				powerups.remove(p);
				return true;
			} else if (p.getKind().equals("WidePaddle")
					&& p.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
				if (!isbig) {
					paddle.setWidth(200);
					paddle.setColor(Color.AQUAMARINE);
				}
				currentscore = score;
				isbig = true;
				myRoot.getChildren().remove(p);
				powerups.remove(p);
				return true;
			} 
		}
		return false;
	}
```

This large block of code could then hopefully become something more like this 
```java
private boolean getEffect(PowerUp p) {
	p.getEffect(paddle);
}
```
This would also follow Object Oriented Design far better, following the principle of replacing if statements with polymorphism.



2. Another design option that I considered was having a separate class called Level, which would store the details of the level in instance variables read from a file, and would have its own scene creation method. I think this might be a better design option than the way I'm doing it now, which is simply creating the scene in the main method, because it would encapsulate the data of the level a little bit more and would allow for more of a black box kind of design. However, I like the way that I've implemented level creation because it's flexible and easy to understand for me, rather than for other people reading my code. 

### Bugs

1. The largest remaining bug is that the ball doesn't always bounce as it should when it's coming at a block from the corner. If, for example, the ball's path is like this, at the corner of the block,
```
         \    
          \
------------|
____________|     -- Block
          /
         /    -- Ball vector
        /
 ```
 The ball often bounces through the block and off of the inside wall as shown instead of bouncing back down as I would want. I think this can be fixed by paying closer attention to my bounce method. Also, this same bug happens sometimes with the Paddle.
 
2. A bug that I fixed while writing this analysis (though it should still be buggy for you fortunate graders) was that the game over screen wouldn't allow you to press space and restart the game as I had intended. I realized, however, that it was happening because the onKeyPressed variable was being overwritten during the step method. Also, I had forgotten to add 3 lives to the paddle during the restart method, so my paddle (which I had changed to be a local variable) would always have 0 lives and thus immediately go back to the game over screen.

3. Sometimes, when the paddle is in "catching mode", the paddle is in a corner, and the player loses a life, the starting angle of the ball's launch (which is randomly determined) will go into the paddle, which means the ball deflects off of the inside of the paddle and is the re-caught, forming a never-ending loop of launching and catching and making the game impossible to continue. 

# In Short

I'm relatively satisfied with the way this project turned out. I learned a lot, for sure. 

Key Takeaways
* Plan more in advance
* Think more, code less
* Be realistic about my time and my abilities


