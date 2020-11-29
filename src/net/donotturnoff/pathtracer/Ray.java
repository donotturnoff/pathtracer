package net.donotturnoff.pathtracer;

/*
 * To do:
 * - Explicit direct illumination for efficiency (produce array of lights when making scene)
 * - Shadow rays
 * - Emitted light
 * - Refracted light
 * - Stratified sampling
 * - Attenuation in air and in materials
 * - Halt when minimum radiance is reached
 * - Make sure Monte Carlo method is followed correctly (e.g. divide by pi?)
 * - Make sure rendering equation is followed correctly
 */

public class Ray {
	
	public static final double EPSILON = 0.001;
	public static String entity = "null";
	
	private Scene scene;
	private Entity prevEntity;
	private Vector origin, direction;
	private int samples, recursionDepth;
	private double pathLength;
	
	public Ray(Scene scene, Entity prevEntity, Vector origin, Vector direction, int samples, int recursionDepth, double pathLength) throws IllegalArgumentException {
		setScene(scene);
		setPrevEntity(prevEntity);
		setOrigin(origin);
		setDirection(direction);
		setSamples(samples);
		setRecursionDepth(recursionDepth);
		setPathLength(pathLength);
	}
	
	public String toString() {
		return "Ray[origin=" +
				origin +
				", direction=" +
				direction +
				", samples=" +
				samples +
				", recursionDepth=" +
				recursionDepth +
				", pathLength=" +
				pathLength +
				"]";
	}
	
	private void setScene(Scene scene) {
		this.scene = scene;
	}
	
	private void setPrevEntity(Entity prevEntity) {
		this.prevEntity = prevEntity;
	}
	
	private void setOrigin(Vector origin) throws IllegalArgumentException {
		if (origin.components() == 3) {
			this.origin = origin;
		} else {
			throw new IllegalArgumentException("Origin vector must have 3 components");
		}
	}
	
	private void setDirection(Vector direction) throws IllegalArgumentException {
		if (direction.components() == 3) {
			this.direction = direction;
		} else {
			throw new IllegalArgumentException("Direction vector must have 3 components");
		}
	}
	
	private void setSamples(int samples) throws IllegalArgumentException {
		if (samples > 0) {
			this.samples = samples;
		} else {
			throw new IllegalArgumentException("Amount of samples must be positive");
		}
	}
	
	private void setRecursionDepth(int recursionDepth) throws IllegalArgumentException {
		if (recursionDepth >= 0) {
			this.recursionDepth = recursionDepth;
		} else {
			throw new IllegalArgumentException("Remaining recursion depth cannot be negative");
		}
	}
	
	private void setPathLength(double pathLength) throws IllegalArgumentException {
		if (pathLength >= 0) {
			this.pathLength = pathLength;
		} else {
			throw new IllegalArgumentException("Total path length cannot be negative");
		}
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Vector getOrigin() {
		return origin;
	}
	
	public Vector getDirection() {
		return direction;
	}
	
	public int getRecursionDepth() {
		return recursionDepth;
	}
	
	public double getPathLength() {
		return pathLength;
	}
	
	public Vector getColor() {
		Vector color = scene.getBackgroundColor();
		Entity entity = getNearestEntity();
		if (entity != null && recursionDepth > 0) {
			Material material = entity.getMaterial();
			
			Vector entitySpaceOrigin = entity.rtes(entity.ttes(origin));
			Vector entitySpaceDirection = entity.rtes(direction);
			double intersectionParameter = entity.getIntersectionParameter(entitySpaceOrigin, entitySpaceDirection);
			Vector entitySpaceIntersection = entity.getIntersection(entitySpaceOrigin, entitySpaceDirection, intersectionParameter);
			Vector entitySpaceNormal = entity.getNormal(entitySpaceIntersection);
			Vector intersection = entity.ttws(entity.rtws(entitySpaceIntersection));
			Vector normal = entity.rtws(entitySpaceNormal);
			
			color = monteCarlo(intersection, normal, entity, intersectionParameter).sum(material.getEmit());
		}
		return color;
	}
	
	private Entity getNearestEntity() {
		
		/*
		 * To do:
		 * - Make use of octree for performance
		 */
		
		Entity nearestEntity = null;
		double nearestIntersection = Double.MAX_VALUE;
		for (Entity entity: scene.getEntities()) {
			Vector entitySpaceOrigin = entity.rtes(entity.ttes(origin));
			Vector entitySpaceDirection = entity.rtes(direction);
			double thisIntersection = entity.getIntersectionParameter(entitySpaceOrigin, entitySpaceDirection);
			if (thisIntersection < nearestIntersection) {
				nearestIntersection = thisIntersection;
				nearestEntity = entity;
			}
		}
		return nearestEntity;
	}
	
	private Vector monteCarlo(Vector intersection, Vector normal, Entity entity, double intersectionParameter) {
		/*
		 * To do:
		 * - Look into division by pi (is it necessary?)
		 * - Look into divison by chance of reflection/refraction (https://computergraphics.stackexchange.com/questions/2482/choosing-reflection-or-refraction-in-path-tracing)
		 */
		Material material = entity.getMaterial();
		
		Vector[] results = new Vector[samples];
		for (int i = 0; i < samples; i++) {
			
			double n1 = 1;
			if (prevEntity != null){
				Material prevMaterial = prevEntity.getMaterial(); 
				n1 = prevMaterial.getRefractiveIndex();
			}
			double n2 = material.getRefractiveIndex();
			double c1 = direction.normalize().angle(normal);
			double c2 = n1/n2*c1;
			double fresnel = fresnel(n1, n2, c1, c2);
			boolean reflect = true;//Math.random() <= fresnel;
			
			if (reflect) {
				Vector newDirection = hemisphereReflectionDistribution(normal, material.getSpecularIndex());
				Ray newRay = new Ray(scene, entity, intersection.sum(newDirection.product(EPSILON)), newDirection, samples, recursionDepth-1, pathLength + intersectionParameter);
				results[i] = newRay.getColor().hadamard(material.getAlbedo());
			} else {
				Vector newDirection = hemisphereReflectionDistribution(normal, material.getSpecularIndex());
				Ray newRay = new Ray(scene, entity, intersection.sum(newDirection.product(EPSILON)), newDirection, samples, recursionDepth-1, pathLength + intersectionParameter);
				results[i] = newRay.getColor().hadamard(material.getOpacity());
			}
		}
		return Vector.average(results);
	}
	
	private double fresnel(double n1, double n2, double c1, double c2) {
		
		/*
		 * Compute Fresnel effect for both parallel and perpendicular polarisation and average them.
		 */
		
		double a1 = (n1*c1-n2*c2)/(n1*c1+n2*c2);
		double a2 = (n1*c2-n2*c1)/(n1*c2+n2*c1);
		
		double perpendicular = a1*a1;
		double parallel = a2*a2;
		
		//System.out.println(n1 + " " + n2 + " " + c1 + " " + c2 + " " + a1 + " " + a2);
		
		return (perpendicular+parallel)/2;
	}
	
	private Vector hemisphereReflectionDistribution(Vector normal, double specularIndex) { 
		
		/* 
		 * To do:
		 * - Confirm that u and v are in the correct range
		 */
		
		double u = Math.random();
		double v = Math.random();
		double phi = 2 * Math.PI * v;
		double sinTheta;
		
		if (specularIndex == 0) {
			/* This is an optimisation for uniform distributions (perfectly diffuse materials). */
			sinTheta = Math.sqrt(-u*(u-2));
		} else {
			//sinTheta = Math.sin(Math.arccos(Math.pow(1 - u, 1 / (1 + specularIndex)));
			double w = Math.pow(1-u, 1/(1+specularIndex));
			sinTheta = Math.sqrt(1-w*w);
		}
		
		/* Switch to Cartesian co-ordinates. */
		double x = sinTheta * Math.cos(phi);
		double z = sinTheta * Math.sin(phi);
		
		Vector randomVector = new Vector(x, Math.sqrt(Math.max(0, 1-u)), z);
		Matrix rotationMatrix = new Vector(0, 1, 0).toRotationMatrix(normal);
		return rotationMatrix.product(randomVector).normalize(); // Is normalize needed?
	} 
}
