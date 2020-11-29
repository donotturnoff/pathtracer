package net.donotturnoff.pathtracer;

import java.util.ArrayList;
import java.util.Arrays;

public class Cell {
	
	private Cell[] subcells;
	private Vector center;
	private double width, height, depth;
	private Entity[] entities;
	
	public Cell(Vector center, double width, double height, double depth, int remainingDepth) throws IllegalArgumentException {
		setCenter(center);
		setWidth(width);
		setHeight(height);
		setDepth(depth);
		addSubcells(remainingDepth);
	}
	
	public String toString() {
		return "Cell[center=" +
				center +
				", width=" +
				width +
				", height=" +
				height +
				", depth=" +
				depth +
				", entities=" +
				Arrays.toString(entities) +
				", subcells=" +
				Arrays.toString(subcells) +
				"]";
	}
	
	private void setCenter(Vector center) throws IllegalArgumentException {
		if (center.components() == 3) {
			this.center = center;
		} else {
			throw new IllegalArgumentException("Center vector must have 3 components");
		}
	}
	
	private void setWidth(double width) throws IllegalArgumentException {
		if (width > 0) {
			this.width = width;
		} else {
			throw new IllegalArgumentException("Width must be positive");
		}
	}
	
	private void setHeight(double height) throws IllegalArgumentException {
		if (height > 0) {
			this.height = height;
		} else {
			throw new IllegalArgumentException("Height must be positive");
		}
	}
	
	private void setDepth(double depth) throws IllegalArgumentException {
		if (depth > 0) {
			this.depth = depth;
		} else {
			throw new IllegalArgumentException("Depth must be positive");
		}
	}
	
	public Cell[] getSubcells() {
		return subcells;
	}
	
	public Vector getCenter() {
		return center;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getDepth() {
		return depth;
	}
	
	public Entity[] getEntities() {
		return entities;
	}
	
	private void addSubcells(int remainingDepth) throws IllegalArgumentException {
		if (remainingDepth > 0) {
			subcells = new Cell[8];
			for (int i = 0; i < 8; i++) {
				double[] components = center.getArray();
				boolean right = (i & 4) > 0;
				boolean top = (i & 2) > 0;
				boolean front = (i & 1) > 0;
				double x = components[0] + (right ? width/4 : -width/4);
				double y = components[1] + (top ? height/4 : -height/4);
				double z = components[2] + (front ? depth/4 : -depth/4);
				subcells[i] = new Cell(new Vector(x, y, z), width/2, height/2, depth/2, remainingDepth-1);
			}
		} else {
			subcells = new Cell[0];
		}
	}
	
	public ArrayList<Entity> takeEntities(ArrayList<Entity> potentialEntities) {
		ArrayList<Entity> takenEntities = new ArrayList<>();
		ArrayList<Entity> remainingEntities = new ArrayList<>();
		
		for (Entity entity: potentialEntities) {
			if (entity.inCell(this)) {
				takenEntities.add(entity);
			} else {
				remainingEntities.add(entity);
			}
		}
		
		for (Cell subcell: subcells) {
			takenEntities = subcell.takeEntities(takenEntities);
		}
		
		entities = new Entity[takenEntities.size()];
		entities = takenEntities.toArray(entities);
		
		return remainingEntities;
	}
}
