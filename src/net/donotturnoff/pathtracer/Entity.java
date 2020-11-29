package net.donotturnoff.pathtracer;

/*
 * Entities must define:
 * - A method for generating a random point on the entity's surface
 * - A method for calculating the surface normal at a given point on the entity's surface
 * - A method for calculating the intersection parameter between a ray and the entity
 */

public class Entity {
	
	protected String name;
	protected Vector refPoint;
	protected Rotation rotation;
	protected Material material;
	protected BoundingBox boundingBox;
	
	public Entity() {
		this("", new Vector(0, 0, 0), new Rotation(0, 0, 0), new DiffuseMaterial(new Vector(1, 1, 1)), null);
	}
	
	public Entity(String name, Vector refPoint, Rotation rotation, Material material, BoundingBox boundingBox) {
		setName(name);
		setRefPoint(refPoint);
		setRotation(rotation);
		setMaterial(material);
		setBoundingBox(boundingBox);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Entity[name=\"");
		sb.append(name);
		sb.append("\", refPoint=");
		sb.append(refPoint);
		sb.append(", rotation=");
		sb.append(rotation);
		sb.append(", material=");
		sb.append(material);
		sb.append(", boundingBox=");
		sb.append(boundingBox);
		sb.append("]");
		return sb.toString();
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	protected void setRefPoint(Vector refPoint) throws IllegalArgumentException {
		if (refPoint.components() == 3) {
			this.refPoint = refPoint;
		} else {
			throw new IllegalArgumentException("Reference point vector must have 3 components");
		}
	}
	
	protected void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
	
	protected void setMaterial(Material material) {
		this.material = material;
	}
	
	protected void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	public String getName() {
		return name;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	public Vector getRandomPoint() {
		return new Vector(0, 0, 0);
	}
	
	public Vector getNormal(Vector position) {
		return new Vector(0, 1, 0);
	}
	
	public double getIntersectionParameter(Vector origin, Vector direction) {
		return Double.MAX_VALUE;
	}
	
	public Vector getIntersection(Vector origin, Vector direction, double intersectionParameter) {
		return origin.sum(direction.product(intersectionParameter));
	}
	
	public boolean inCell(Cell cell) {
		
		/*
		 * To do:
		 * - Improve this to determine whether the whole entity is in a cell, rather than just the reference point
		 */
		
		double x = refPoint.getComponent(0);
		double y = refPoint.getComponent(1);
		double z = refPoint.getComponent(2);
		
		Vector cellCenter = cell.getCenter();
		
		double cx = cellCenter.getComponent(0);
		double cy = cellCenter.getComponent(1);
		double cz = cellCenter.getComponent(2);
		
		double width = cell.getWidth();
		double height = cell.getHeight();
		double depth = cell.getDepth();
		
		return ((x > cx-width/2 && x <= cx+width/2) && (y > cy-height/2 && y <= cy+height/2) && (z > cz-depth/2 && z <= cz+depth/2));
	}
	
	public boolean boundingBoxContains(Vector origin, Vector direction, double intersectionParameter) {
		if (boundingBox == null) {
			return true;
		} else {
			return boundingBox.contains(getIntersection(origin, direction, intersectionParameter));
		}
	}
	
	public Vector ttes(Vector vector) {
		return refPoint.negate().sum(vector);
	}
	
	public Vector translateToEntitySpace(Vector vector) {
		return ttes(vector);
	}
	
	public Vector rtes(Vector vector) {
		Matrix rotationMatrix = new Matrix(rotation.negate());
		return rotationMatrix.product(vector);
	}
	
	public Vector rotateToEntitySpace(Vector vector) {
		return rtes(vector);
	}
	
	public Vector ttws(Vector vector) {
		return refPoint.sum(vector);
	}
	
	public Vector translateToWorldSpace(Vector vector) {
		return ttws(vector);
	}
	
	public Vector rtws(Vector vector) {
		Matrix rotationMatrix = new Matrix(rotation);
		return rotationMatrix.product(vector);
	}
	
	public Vector rotateToWorldSpace(Vector vector) {
		return rtws(vector);
	}
}
