package net.donotturnoff.pathtracer;

/*
 * To do:
 * - Write random point generator method
 */

public class Plane extends Entity {
	
	private Vector normal;
	
	public Plane(Vector refPoint, Rotation rotation, Material material) throws IllegalArgumentException {
		this("", refPoint, rotation, material, null);
	}
	
	public Plane(String name, Vector refPoint, Rotation rotation, Material material) throws IllegalArgumentException {
		this(name, refPoint, rotation, material, null);
	}
	
	public Plane(Vector refPoint, Rotation rotation, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		this("", refPoint, rotation, material, boundingBox);
	}
	
	public Plane(String name, Vector refPoint, Rotation rotation, Material material, BoundingBox boundingBox) throws IllegalArgumentException {
		super(name, refPoint, rotation, material, boundingBox);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Plane");
		sb.append("[name=\"");
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
	
	public double getIntersectionParameter(Vector origin, Vector direction) {
		double denom = normal.dot(direction);
		
		if (denom < 0.000001) {
			return Double.MAX_VALUE;
		} else {
			Vector pointToPlane = refPoint.difference(origin);
			double t = pointToPlane.dot(normal)/denom;
			
			if (boundingBoxContains(origin, direction, t)) {
				return t;
			} else {
				return Double.MAX_VALUE;
			}
		}
	}
	
	public Vector getNormal(Vector intersection) {
		return new Matrix(rotation).product(new Vector(0, 1, 0));
	}
}
