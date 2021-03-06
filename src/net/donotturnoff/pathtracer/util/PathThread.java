package net.donotturnoff.pathtracer.util;

/*
 * To do:
 * - Check depth of field works
 */

import net.donotturnoff.pathtracer.PathTracer;
import net.donotturnoff.pathtracer.maths.Vector;

public class PathThread extends Thread {
	
	private final PathTracer pt;
	private final Scene scene;
	private final Camera camera;
	private final int width, height, recursionDepth, samples;
	private final boolean debug;
	
	public PathThread(PathTracer pt, Scene scene, int width, int height, int recursionDepth, int samples, boolean debug) {
		this.pt = pt;
		this.scene = scene;
		this.width = width;
		this.height = height;
		this.recursionDepth = recursionDepth;
		this.samples = samples;
		this.debug = debug;
		
		this.camera = scene.getCamera();
	}
	
	public void run() {
		Integer[] coords = pt.fetchTask();
		while (coords != null) {
			int x = coords[0];
			int y = coords[1];
			
			Vector[] results = new Vector[samples];
			
			double ox = x-((double) width)/2;
			double oy = y-((double) height)/2;
			
			double lensWidth = camera.getLensWidth();
			double lensHeight = camera.getLensHeight();
			
			double scaleX = lensWidth/width;
			double scaleY = lensHeight/height;
            double r = camera.getFocalLength();
			
			for (int i = 0; i < samples; i++) {
				double lx = lensWidth * (Math.random()-0.5);
				double ly = lensHeight * (Math.random()-0.5);
				double dx = (ox + Math.random() - lx) * scaleX;
				double dy = (oy + Math.random() - ly) * scaleY;
                double dz = r * r / Math.sqrt(r*r + dx*dx + dy*dy);
				Vector direction = new Vector(dx, dy, dz); //dz <--> r ?
				results[i] = startPathTrace(direction);
			}
			
			Vector average = Vector.average(results);
			
			pt.submitTask(x, y, average);
			
			if (debug) {
				System.out.println("    (" + (x+1) + ", " + (y+1) + "): color=" + average);
			}
			
			coords = pt.fetchTask();
		}
	}
	
	private Vector startPathTrace(Vector direction) {
		Ray ray = new Ray(scene, null, camera.getLocation(), camera.rtcs(direction), samples, recursionDepth, 0);
		Vector color = ray.getColor();
		for (int i = 0; i < 3; i++) {
			if (color.getComponent(i) > 1) {
				color.setComponent(i, 1);
			}
		}
		return color;
	}
}
