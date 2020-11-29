package net.donotturnoff.pathtracer.material;

import net.donotturnoff.pathtracer.maths.Vector;

import java.awt.Color;

public class DiffuseMaterial extends Material {
	
	public DiffuseMaterial() {
		this("", new Vector(1, 1, 1));
	}
	
	public DiffuseMaterial(String name) {
		this(name, new Vector(1, 1, 1));
	}
	
	public DiffuseMaterial(Color albedo) {
		this("", new Vector(albedo));
	}
	
	public DiffuseMaterial(String name, Color albedo) {
		this(name, new Vector(albedo));
	}
	
	public DiffuseMaterial(Vector albedo) {
		this("", albedo);
	}
	
	public DiffuseMaterial(String name, Vector albedo) {
		setAlbedo(albedo);
		setSpecularIndex(0);
		setOpacity(new Vector(0, 0, 0));
		setRefractiveIndex(1);
		setEmit(new Vector(0, 0, 0));
	}
}
