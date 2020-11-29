package net.donotturnoff.pathtracer;

import java.util.ArrayList;
import java.util.Arrays;

public class Scene {
	
	private final Cell cell;
	private final Camera camera;
	private final Entity[] entities;
	private Entity[] emitters;
	private final Vector backgroundColor;
	
	public Scene(double width, double height, double depth, Camera camera, Entity[] entities, int octtreeDepth, Vector backgroundColor) throws IllegalArgumentException {
		this.cell = new Cell(camera.getLocation(), width, height, depth, octtreeDepth-1);
		this.camera = camera;
		this.entities = entities;
		this.backgroundColor = backgroundColor;
		findEmitters();
		cell.takeEntities(new ArrayList<>(Arrays.asList(entities)));
	}
	
	public String toString() {
		return "Scene[cell=" +
				cell +
				", camera=" +
				camera +
				", entities=" +
				Arrays.toString(entities) +
				", backgroundColor=" +
				backgroundColor +
				"]";
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
		ArrayList<Entity> emitterList = new ArrayList<>();
		for (Entity entity: entities) {
			if (!entity.getMaterial().getEmit().equals(new Vector(0, 0, 0))) {
				emitterList.add(entity);
			}
		}
		emitters = new Entity[emitterList.size()];
		emitters = emitterList.toArray(emitters);
	}
}
