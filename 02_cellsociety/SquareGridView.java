package cellsociety_team09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import UnusedReferences.Grid;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import simulations.GridModel;

/**
 * Tasked with specifically creating square grid cells. I think this class is well-designed because it does
 * only two things differently than the superclass, and that is reflected in the code. The drawGrid method
 * is concise and it's easy to follow the logic, and the setStateOnClck method is transparent and concise. It also excels at
 * the more basic aspects of design like not using magic values and being consistent with naming conventions. 
 * 
 * @author Liam
 *
 */
public class SquareGridView extends GridView{
	private double gridxpos;
	private double gridypos;
	private double gridsize;
	private ArrayList<Double> proportions;
	private boolean outline = true;
	private final double EDGEMARGIN = .5;
	
	public SquareGridView(double x, double y, double blocksize, double GRIDSIZE){
		gridxpos = x;
		gridypos = y;
		gridsize = GRIDSIZE;
		proportions = new ArrayList<Double>();
	}
	public Group drawGrid(GridModel grid, int screenwidth, int screenheight, double blocksize){
		Group retgroup = new Group();
		int x = 0, y = 0;
		for (double i = gridxpos; i < gridxpos + gridsize - EDGEMARGIN; i += blocksize){
			for (double j = gridypos; j < gridypos + gridsize - EDGEMARGIN; j += blocksize){
				Rectangle toAdd = new Rectangle(i, j, blocksize, blocksize);
				toAdd.setFill(super.getColor(grid, x, y));
				if (outline){
					toAdd.setStroke(Color.BLACK);
				}
				int xtemp = x;
				int ytemp = y;
				toAdd.setOnMouseClicked(e -> setStateOnClick(xtemp,ytemp,grid, toAdd));
				retgroup.getChildren().add(toAdd);
				y++;
			}
			x++;
			y = 0;
		}
		return retgroup;
	}
	public ArrayList<Double> getProportions(){
		return proportions;
	}
	public void setOutline(boolean outline){
		this.outline = outline;
	}
	public boolean getOutline(){
		return outline;
	}
	private void setStateOnClick(int x, int y, GridModel grid, Shape n) {
		List<Integer> list = new ArrayList<>();
		list.add(x);
		list.add(y);
		grid.getUserInput(list);
		n.setFill(super.getColor(grid, x, y));
	}
	public double getX(){
		return gridxpos;
	}
	public double getY(){
		return gridypos;
	}
	public double getDimensions(){
		return gridsize;
	}
}
