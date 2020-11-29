package net.donotturnoff.pathtracer;

import java.util.ArrayList;
import java.util.Arrays;

public class Scene {
	
	private Cell cell;
	private Camera camera;
	private Entity[] entities;
	private Entity[] emitters;
	private Vector backgroundColor;
	
	public Scene(double width, double height, double depth, Camera camera, Entity[] entities, int octtreeDepth, Vector backgroundColor) throws IllegalArgumentException {
		this.cell = new Cell(camera.getLocation(), width, height, depth, octtreeDepth-1);
		this.camera = camera;
		this.entities = entities;
		this.backgroundColor = backgroundColor;
		findEmitters();
		cell.takeEntities(new ArrayList<Entity>(Arrays.asList(entities)));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Scene[cell=");
		sb.append(cell);
		sb.append(", camera=");
		sb.append(camera);
		sb.append(", entities=");
		sb.append(Arrays.toString(entities));
		sb.append(", backgroundColor=");
		sb.append(backgroundColor);
		sb.append("]");
		return sb.toString();
	}
	
	public Cell getCell() {
		return cell;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Entity[] getEntities() {
		return entities;
	}
	
	public Vector getBackgroundColor() {
		return backgroundColor;
	}
	
	public double getWidth() {
		return cell.getWidth();
	}
	
	public double getHeight() {
		return cell.getHeight();
	}
	
	public double getDepth() {
		return cell.getDepth();
	}
	
	public Vector getCenter() {
		return camera.getLocation();
	}
	
	private void findEmitters() {
		ArrayList<Entity> emitterList = new ArrayList<Entity>();
		for (Entity entity: entities) {
			if (!entity.getMaterial().getEmit().equals(new Vector(0, 0, 0))) {
				emitterList.add(entity);
			}
		}
		emitters = new Entity[emitterList.size()];
		emitters = emitterList.toArray(emitters);
	}
}
