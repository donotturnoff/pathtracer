package net.donotturnoff.pathtracer.util;

import net.donotturnoff.pathtracer.maths.Matrix;
import net.donotturnoff.pathtracer.maths.Rotation;
import net.donotturnoff.pathtracer.maths.Vector;

public class BoundingBox {
	
	private Vector center;
	private Rotation rotation;
	private double width, height, depth;
	
	public BoundingBox(Vector center, double width, double height, double depth) throws IllegalArgumentException {
		this(center, new Rotation(0, 0, 0), width, height, depth);
	}
	
	public BoundingBox(Vector center, Rotation rotation, double width, double height, double depth) throws IllegalArgumentException {
		setCenter(center);
		setRotation(rotation);
		setWidth(width);
		setHeight(height);
		setDepth(depth);
	}
	
	public String toString() {
		return "BoundingBox[center=" +
				center +
				", rotation=" +
				rotation +
				", width=" +
				width +
				", height=" +
				height +
				", depth=" +
				depth +
				"]";
	}
	
	private void setCenter(Vector center) throws IllegalArgumentException {
		if (center.components() == 3) {
			this.center = center;
		} else {
			throw new IllegalArgumentException("Center vector must have 3 components");
		}
	}
	
	private void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
	
	private void setWidth(double width) throws IllegalArgumentException {
		if (width >= 0) {
			this.width = width;
		} else {
			throw new IllegalArgumentException("Width cannot be negative");
		}
	}
	
	private void setHeight(double height) throws IllegalArgumentException {
		if (height >= 0) {
			this.height = height;
		} else {
			throw new IllegalArgumentException("Height cannot be negative");
		}
	}
	
	private void setDepth(double depth) throws IllegalArgumentException {
		if (depth >= 0) {
			this.depth = depth;
		} else {
			throw new IllegalArgumentException("Depth cannot be negative");
		}
	}
	
	public Vector getCenter() {
		return center;
	}
	
	public Rotation getRotation() {
		return rotation;
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
	
	public Vector toBoundingBoxSpace(Vector vector) throws IllegalArgumentException {
		if (vector.components() == 3) {
			Matrix rotationMatrix = new Matrix(rotation.negate());
			return center.negate().sum(rotationMatrix.product(vector));
		} else {
			throw new IllegalArgumentException("Vector must have 3 components");
		}
	}
	
	public boolean contains(Vector point) throws IllegalArgumentException {
		if (point.components() == 3) {
			point = toBoundingBoxSpace(point);
			double x = point.getComponent(0);
			double y = point.getComponent(1);
			double z = point.getComponent(2);
			return ((x > -width/2 && x < width/2) && (y > -height/2 && y < height/2) && (z > -depth/2 && z < depth/2));
		} else {
			throw new IllegalArgumentException("Point vector must have 3 components");
		}
	}
}
