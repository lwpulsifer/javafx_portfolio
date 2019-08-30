package cellsociety_team09;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import simulations.GridModel;

/**
 * This class is an abstract class which defines the general behavior of a class which renders grids visually.
 * I think it's well-designed because it's very easy to extend while still maintaining all of the functionality necessary. 
 * Using the drawGrid method, each subclass can define exactly how it should draw the grid it's given by being passed the grid 
 * in question, the width and height of the window to draw it in, and the size of each block. 
 * 
 * @author Liam
 *
 */
public abstract class GridView {
	private double gridXPosition;
	private double gridYPosition;
	private int gridSize;
	public GridView(){
	}
	public double getX(){
		return gridXPosition;
	}
	public double getY(){
		return gridYPosition;
	}
	public double getDimensions(){
		return gridSize;
	}
	public Group drawGrid(GridModel grid, int wIDTH, int hEIGHT, double blocksize) {
		return null;
	}
	public boolean getOutline() {
		return false;
	}
	public void setOutline(boolean b) {
	}
	private void setStateOnClick(int x, int y, GridModel g, Shape n) {
	}
	public Color getColor(GridModel grid, int x, int y){
		return grid.getCells().get(x).get(y).getColor();
	}
}
