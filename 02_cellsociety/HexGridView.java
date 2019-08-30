package cellsociety_team09;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import simulations.GridModel;

/**
 * Class specifically tasked with creating hexagonal grids. I think this class is well designed because of its
 * conciseness and descriptiveness. Its methods are well-named, its naming conventions are consistent, and it
 * doesn't try to do anything that it shouldn't be doing. It only works with what it's tasked with doing,
 * drawing the grid and handling basic user input on those drawn objects.  
 * 
 * @author Liam
 *
 */
public class HexGridView extends GridView {

		private double gridxpos;
		private double gridypos;
		private double gridsize;
		private double gridheight;
		private boolean outline = true;
		private final double WIDTHMODIFIER = 1.4;
		private final double HEXHEIGHTMODIFIER = Math.sqrt(3) / 2;
		private final double EDGEMARGIN = .5;
		public HexGridView(double x, double y, double blocksize, double GRIDSIZE){
			gridxpos = x;
			gridypos = y;
			gridsize = GRIDSIZE;
			gridheight = HEXHEIGHTMODIFIER * gridsize;
		}
		
		public Group drawGrid(GridModel grid, int screenwidth, int screenheight, double width){
			Group retgroup = new Group();
			double hexwidth =  WIDTHMODIFIER;
			double hexheight = HEXHEIGHTMODIFIER * hexwidth;
			boolean offset = false;
			int x = 0, y = 0;
			for (double i = gridxpos - 15; i < ((gridxpos + gridsize - EDGEMARGIN) * .9); i += .75 * hexwidth){
				for (double j = gridypos; j < (gridypos + gridheight - EDGEMARGIN); j += hexheight){
					Polygon toAdd = new Hexagon(i, j, hexwidth, offset).getShape();
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
				offset = !offset;
			}
			return retgroup;
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
		public boolean getOutline(){
			return outline;
		}
		public void setOutline(boolean outline){
			this.outline = outline;
		}
		public double getY(){
			return gridypos;
		}
		public double getDimensions(){
			return gridsize;
		}
}
