package net.donotturnoff.pathtracer;

public class Camera {
	
	private Vector location;
	private Rotation rotation;
	private double lensWidth, lensHeight, focalLength;
	
	public Camera(Vector location, Rotation rotation, double lensWidth, double lensHeight, double focalLength) throws IllegalArgumentException {
		setLocation(location);
		setRotation(rotation);
		setLensWidth(lensWidth);
		setLensHeight(lensHeight);
		setFocalLength(focalLength);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Camera[location=");
		sb.append(location);
		sb.append(", rotation=");
		sb.append(rotation);
		sb.append(", lensWidth=");
		sb.append(lensWidth);
		sb.append(", lensHeight=");
		sb.append(lensHeight);
		sb.append(", focalLength=");
		sb.append(focalLength);
		sb.append("]");
		return sb.toString();
	}
	
	private void setLocation(Vector location) throws IllegalArgumentException {
		if (location.components() == 3) {
			this.location = location;
		} else {
			throw new IllegalArgumentException("Location vector must have 3 components");
		}
	}
	
	private void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
	
	private void setLensWidth(double lensWidth) throws IllegalArgumentException {
		if (lensWidth >= 0) {
			this.lensWidth = lensWidth;
		} else {
			throw new IllegalArgumentException("Lens width cannot be negative");
		}
	}
	
	private void setLensHeight(double lensHeight) throws IllegalArgumentException {
		if (lensHeight >= 0) {
			this.lensHeight = lensHeight;
		} else {
			throw new IllegalArgumentException("Lens height cannot be negative");
		}
	}
	
	private void setFocalLength(double focalLength) throws IllegalArgumentException {
		if (focalLength > 0) {
			this.focalLength = focalLength;
		} else {
			throw new IllegalArgumentException("Focal length must be positive");
		}
	}
	
	public Vector getLocation() {
		return location;
	}
	
	public Rotation getRotation() {
		return rotation;
	}
	
	public double getLensWidth() {
		return lensWidth;
	}
	
	public double getLensHeight() {
		return lensHeight;
	}
	
	public double getFocalLength() {
		return focalLength;
	}
	
	public Vector ttcs(Vector vector) {
		return location.negate().sum(vector);
	}
	
	public Vector translateToCameraSpace(Vector vector) {
		return ttcs(vector);
	}
	
	public Vector rtcs(Vector vector) {
		Matrix rotationMatrix = new Matrix(rotation.negate());
		return rotationMatrix.product(vector);
	}
	
	public Vector rotateToCameraSpace(Vector vector) {
		return rtcs(vector);
	}
	
	public Vector ttws(Vector vector) {
		return location.sum(vector);
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
