package net.donotturnoff.pathtracer;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.ArrayBlockingQueue;

public class PathTracer {
	
	private int recursionDepth;
	private int samples;
	private final int threads;
	private Vector origin;
	private final boolean debug;
	private int[][] data;
	private ArrayBlockingQueue<Integer[]> queue;
	
	public PathTracer() {
		this.debug = false;
		this.threads = 1;
	}
	
	public PathTracer(boolean debug) {
		this.debug = debug;
		this.threads = 1;
	}
	
	public PathTracer(int threads) {
		this.debug = false;
		this.threads = threads;
	}
	
	public PathTracer(int threads, boolean debug) {
		this.debug = debug;
		this.threads = threads;
	}
	
	public BufferedImage trace(Scene scene, int width, int height, int recursionDepth, int samples) {
		data = new int[height][width];
		
		queue = new ArrayBlockingQueue<>(width * height);
		Thread[] activeThreads = new Thread[threads];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				try {
					queue.put(new Integer[]{x, y});
				} catch (InterruptedException e) {
					System.exit(1);
				} 
			}
		}
		
		long start = System.nanoTime();
		if (debug) {
			System.out.println("[+] Tracing paths");
		}
		
		for (int i = 0; i < threads; i++) {
			activeThreads[i] = new PathThread(this, scene, width, height, recursionDepth, samples, debug);
			activeThreads[i].start();
		}
		
		/* Wait for active threads to finish before continuing. */
		
		for (int i = 0; i < threads; i++) {
			try {
				activeThreads[i].join();
			} catch (InterruptedException e) {
				System.exit(1);
			} 
		}
		
		if (debug) {
			System.out.println("[+] Producing image");
		}
		
		BufferedImage image = PathTracer.toImage(width, height, data);
		
		long end = System.nanoTime();
		if (debug) {
			System.out.println("[+] Completed in " + ((end-start)/1000000000) + " seconds");
		}
		
		return image;
	}
	
	public Integer[] fetchTask() {
		return queue.poll();
	}
	
	public void submitTask(int x, int y, Vector color) {
		data[y][x] = color.toColor().getRGB();
	}
	
	private static BufferedImage toImage(int width, int height, int[][] data) {
		int[] data1D = new int[width*height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int index = y*width + x;
				data1D[index] = data[y][x];
			}
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final int[] imageArray = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(data1D, 0, imageArray, 0, data1D.length);
		return image;
	}
}
