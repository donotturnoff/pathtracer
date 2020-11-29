package net.donotturnoff.pathtracer;

public class Rotation {

	private double x, y, z;
	
	public Rotation() {
		x = y = z = 0;
	}
	
	public Rotation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public final void setX(double x) {
		this.x = x;
	}
	
	public final void setY(double y) {
		this.y = y;
	}
	
	public final void setZ(double z) {
		this.z = z;
	}
	
	public final void setRotation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public final double getX() {
		return x;
	}
	
	public final double getY() {
		return y;
	}
	
	public final double getZ() {
		return z;
	}
	
	public final double[] getRotation() {
		return new double[]{x, y, z};
	}
	
	public final boolean equals(Rotation comparator) {
		return (x == comparator.getX() && y == comparator.getY() && z == comparator.getZ());
	}
	
	public final Rotation sum(Rotation addend) {
		final double x2 = addend.getX();
		final double y2 = addend.getY();
		final double z2 = addend.getZ();
		return new Rotation(x + x2, y + y2, z + z2);
	}
	
	public final Rotation difference(Rotation subtrahend) {
		return sum(subtrahend.negate());
	}
	
	public final Rotation negate() {
		return new Rotation(-x, -y, -z);
	}
	
	public final String toString() {
		return "Rotation[" + x + ", " + y + ", " + z + "]";
	}
	
	public static Rotation generateFromAxisAngle(Vector axis, double angle) {
		
		/* Converts an axis-angle rotation (rotation by a specified amount
		 * around a given axis) into Euler angles. */
		
		final double[] unit = axis.normalize().getArray();
		final double s = Math.sin(angle);
		final double c = Math.cos(angle);
		final double t = 1 - c;
		final double x = unit[0];
		final double y = unit[1];
		final double z = unit[2];
		
		/* North and south pole singularities (handled separately because 
		 * otherwise there is a division by zero. These are captured just before
		 * the pole is reached. */
		if ((x*y*t + z*s) > 0.998) {
			
			final double yComponent = 2*Math.atan2(x*Math.sin(angle/2), Math.cos(angle/2));
			final double zComponent = Math.PI/2;
			final double xComponent = 0;
			return new Rotation(xComponent, yComponent, zComponent);
			
		} else if ((x*y*t + z*s) < -0.998) {
			final double yComponent = -2*Math.atan2(x*Math.sin(angle/2), Math.cos(angle/2));
			final double zComponent = -Math.PI/2;
			final double xComponent = 0;
			return new Rotation(xComponent, yComponent, zComponent);
			
		} else {
			final double yComponent = Math.atan2(y*s - x*z*t, 1-(y*y + z*z)*t);
			final double zComponent = Math.asin(x*y*t + z*s) ;
			final double xComponent = Math.atan2(x*s - y*z*t , 1-(x*x + z*z)*t);
			return new Rotation(xComponent, yComponent, zComponent);
		}
	}
}
