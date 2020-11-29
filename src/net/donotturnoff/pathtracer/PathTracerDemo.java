package net.donotturnoff.pathtracer;

import net.donotturnoff.pathtracer.entity.Entity;
import net.donotturnoff.pathtracer.entity.Sphere;
import net.donotturnoff.pathtracer.material.DiffuseMaterial;
import net.donotturnoff.pathtracer.material.Material;
import net.donotturnoff.pathtracer.maths.Rotation;
import net.donotturnoff.pathtracer.maths.Vector;
import net.donotturnoff.pathtracer.util.Camera;
import net.donotturnoff.pathtracer.util.Scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class PathTracerDemo {
	public static void main(String[] args) {
		try {
			Material white = new DiffuseMaterial("white", new Vector(Color.WHITE));
			Material red = new Material("red", new Vector(Color.RED), 1, new Vector(10, 0, 0));
			Material green = new Material("green", new Vector(Color.GREEN), 1, new Vector(0, 10, 0));
			Material blue = new Material("blue", new Vector(Color.BLUE), 1, new Vector(0, 0, 10));
			
			double d = 5/Math.sqrt(2);
			
			Sphere sphere = new Sphere(new Vector(0, 0, 5), 2, new Rotation(0, 0, 0), white);
			Sphere redLamp = new Sphere(new Vector(-d, 0, 5-d), 1, new Rotation(0, 0, 0), red);
			Sphere greenLamp = new Sphere(new Vector(0, 0, 0), 1, new Rotation(0, 0, 0), green);
			Sphere blueLamp = new Sphere(new Vector(d, 0, 5-d), 1, new Rotation(0, 0, 0), blue);
			Sphere occluder = new Sphere(new Vector(1, 0, 3), 0.5, new Rotation(0, 0, 0), white);
			
			Entity[] entities = new Entity[]{sphere, redLamp, blueLamp, occluder};
			
			Camera camera = new Camera(new Vector(0, 0, 2), new Rotation(0, 0, 0), 1, 1, 0.3);
			
			Scene scene = new Scene(20, 20, 20, camera, entities, 2, new Vector(Color.GRAY));
			
			PathTracer pt = new PathTracer(8, true);
			BufferedImage image = pt.trace(scene, 100, 100, 2, 50);
			ImageIO.write(image, "png", new File("misc/images/test3.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
