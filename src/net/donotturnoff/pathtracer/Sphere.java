package net.donotturnoff.pathtracer;

/*
 * To do:
 * - Write random point generator method
 */

public class Sphere extends Entity {
	
	private double radius;
	
	public Sphere(Vector center, double radius, Material material) throws IllegalArgumentException {
		this("", center, radius, new Rotation(), material, null);
	}
	
	public Sphere(String name, Vector center, double radius, Material material) throws IllegalArgumentException {
		this(name, center, radius, new Rotation(), material, null);
	}
	
	public Sphere(Vector center, double radius, Rotation rotation, Material material) throws IllegalArgumentException {
		this("", center, radius, rotation, material, null);
	}
	
	public Sphere(String name, Vector center, double radius, Rotation rotation, Material material) throws IllegalArgumentException {
		this(name, center, radius, rotation, material, null);
	}
	
	public Sphere(Vector center, double radius, Rotation rotation, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		this("", center, radius, rotation, material, boundingBox);
	}
	
	public Sphere(String name, Vector center, double radius, Rotation rotation, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		super(name, center, rotation, material, boundingBox);
		setRadius(radius);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sphere");
		sb.append("[name=\"");
		sb.append(name);
		sb.append("\", center=");
		sb.append(refPoint);
		sb.append(", rotation=");
		sb.append(rotation);
		sb.append(", radius=");
		sb.append(radius);
		sb.append(", material=");
		sb.append(material);
		sb.append(", boundingBox=");
		sb.append(boundingBox);
		sb.append("]");
		return sb.toString();
	}
	
	private void setRadius(double radius) throws IllegalArgumentException {
		if (radius >= 0) {
			this.radius = radius;
		} else {
			throw new IllegalArgumentException("Radius cannot be negative");
		}
	}
	
	public double getIntersectionParameter(Vector origin, Vector direction) {
		double k1 = direction.dot(direction);
		double k2 = 2 * origin.dot(direction);
		double k3 = origin.dot(origin) - radius*radius;
		
		double discriminant = k2*k2 - 4*k1*k3;
		
		if (discriminant < 0) {
			return Double.MAX_VALUE;
		} else {
			double s = Math.sqrt(discriminant);
			double t1 = (-k2 + s) / (2*k1);
			double t2 = (-k2 - s) / (2*k1);
			
			if (t1 < 0) {
				t1 = Double.MAX_VALUE;
			}
			
			if (t2 < 0) {
				t2 = Double.MAX_VALUE;
			}
			
			double min = Math.min(t1, t2);
			double max = Math.max(t1, t2);
			
			if (boundingBoxContains(origin, direction, min)) {
				return min;
			} else if (boundingBoxContains(origin, direction, max)) {
				return max;
			} else {
				return Double.MAX_VALUE;
			}
		}
	}
	
	public Vector getNormal(Vector intersection) {
		return intersection.normalize();
	}
}
