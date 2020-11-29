package net.donotturnoff.pathtracer;

import java.awt.Color;

public class Material {
	
	protected String name;
	protected Vector albedo, opacity, emit;
	protected double specularIndex, refractiveIndex;
	
	public Material() {
		this("", new Vector(1, 1, 1), 0, new Vector(1, 1, 1), 1, new Vector(0, 0, 0));
	}
	
	public Material(Vector albedo) {
		this("", albedo, 0, new Vector(1, 1, 1), 1, new Vector(0, 0, 0));
	}
	
	public Material(String name, Vector albedo) {
		this(name, albedo, 0, new Vector(1, 1, 1), 1, new Vector(0, 0, 0));
	}
	
	public Material(Vector albedo, double specularIndex) {
		this("", albedo, specularIndex, new Vector(1, 1, 1), 1, new Vector(0, 0, 0));
	}
	
	public Material(String name, Vector albedo, double specularIndex) {
		this(name, albedo, specularIndex, new Vector(1, 1, 1), 1, new Vector(0, 0, 0));
	}
	
	public Material(Vector albedo, double specularIndex, Vector opacity, double refractiveIndex) {
		this("", albedo, specularIndex, opacity, refractiveIndex, new Vector(0, 0, 0));
	}
	
	public Material(String name, Vector albedo, double specularIndex, Vector opacity, double refractiveIndex) {
		this(name, albedo, specularIndex, opacity, refractiveIndex, new Vector(0, 0, 0));
	}
	
	public Material(Vector albedo, double specularIndex, Vector emit) {
		this("", albedo, specularIndex, new Vector(1, 1, 1), 1, emit);
	}
	
	public Material(String name, Vector albedo, double specularIndex, Vector emit) {
		this(name, albedo, specularIndex, new Vector(1, 1, 1), 1, emit);
	}
	
	public Material(Vector albedo, double specularIndex, Vector opacity, double refractiveIndex, Vector emit) {
		this("", albedo, specularIndex, opacity, refractiveIndex, emit);
	}
	
	public Material(String name, Vector albedo, double specularIndex, Vector opacity, double refractiveIndex, Vector emit) {
		setName(name);
		setAlbedo(albedo);
		setSpecularIndex(specularIndex);
		setOpacity(opacity);
		setRefractiveIndex(refractiveIndex);
		setEmit(emit);
	}
	
	public String toString() {
		return "Material[name=\"" +
				name +
				"\", albedo=" +
				albedo +
				", specularIndex=" +
				specularIndex +
				", opacity=" +
				opacity +
				", refractiveIndex=" +
				refractiveIndex +
				", emit=" +
				emit +
				"]";
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	protected void setAlbedo(Vector albedo) throws IllegalArgumentException {
		if (albedo.components() == 3) {
			this.albedo = albedo;
		} else {
			throw new IllegalArgumentException("Albedo vector must have 3 components");
		}
	}
	
	protected void setAlbedo(Color albedo) throws IllegalArgumentException {
		setAlbedo(new Vector(albedo));
	}
	
	protected void setSpecularIndex(double specularIndex) throws IllegalArgumentException {
		if (specularIndex >= 0) {
			this.specularIndex = specularIndex;
		} else {
			throw new IllegalArgumentException("Specular index cannot be negative");
		}
	}
	
	protected void setOpacity(Vector opacity) throws IllegalArgumentException {
		if (opacity.components() == 3) {
			this.opacity = opacity;
		} else {
			throw new IllegalArgumentException("Opacity vector must have 3 components");
		}
	}
	
	protected void setRefractiveIndex(double refractiveIndex) throws IllegalArgumentException {
		if (refractiveIndex >= 0) {
			this.refractiveIndex = refractiveIndex;
		} else {
			throw new IllegalArgumentException("Refractive index cannot be negative");
		}
	}
	
	protected void setEmit(Vector emit) throws IllegalArgumentException {
		if (emit.components() == 3) {
			this.emit = emit;
		} else {
			throw new IllegalArgumentException("Emit vector must have 3 components");
		}
	}
	
	protected void setEmit(Color emit) throws IllegalArgumentException {
		setEmit(new Vector(emit));
	}
	
	public String getName() {
		return name;
	}
	
	public Vector getAlbedo() {
		return albedo;
	}
	
	public double getSpecularIndex() {
		return specularIndex;
	}
	
	public Vector getOpacity() {
		return opacity;
	}
	
	public double getRefractiveIndex() {
		return refractiveIndex;
	}
	
	public Vector getEmit() {
		return emit;
	}
}
