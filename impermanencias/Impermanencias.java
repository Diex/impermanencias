package impermanencias;

import processing.core.PApplet;
import processing.core.PImage;
import controlP5.*;
import toxi.processing.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.volume.*;


public class Impermanencias extends PApplet {

	PImage image;
	Vec3D worldSize;

	VolumetricSpace volume;
	VolumetricBrush brush;
	TriangleMesh mesh;
	IsoSurface surface; 

	ToxiclibsSupport gfx;

	float th = 0.1f;

	public void setup() {
		size(800, 600, P3D);
		gfx = new ToxiclibsSupport(this);

		int scale=8;
		int depth=3;

		PImage img=loadImage("051-black.png");
		Vec3D worldSize = new Vec3D(img.width, img.height, depth).scale(scale);
		
		VolumetricSpace volume = new VolumetricSpaceArray(worldSize, img.width, img.height, depth);
		VolumetricBrush brush = new RoundBrush(volume, scale);
		for (int y = 0; y < img.height; y ++) {
		  for (int x = 0; x < img.width; x ++) {
		    if (0 == (img.pixels[y * img.width + x] & 0xff)) {
		      for (int z = 0; z < depth; z++) {
		        brush.drawAtGridPos(x, y, z, 0.5f);
		      }
		    }
		  }
		}
		volume.closeSides();
		TriangleMesh mesh = (TriangleMesh) new ArrayIsoSurface(volume).computeSurfaceMesh(null, 0.1f);
		mesh.saveAsSTL(sketchPath("test.stl"), true);
		
		System.exit(0);
		
		

		worldSize = new Vec3D(image.width, image.height, 2);

		volume = new VolumetricSpaceArray(worldSize, image.width, image.height, 2);

		brush = new BoxBrush(volume,0.05f);
		for (int y = 0; y < image.height; y ++) {
			for (int x = 0; x < image.width; x ++) {
				if (0 == (image.pixels[y * image.width + x] & 0xff)) {
					for (int z = 0; z < 2; z++) {
						Vec3D v = new Vec3D(x, y, z);
						brush.drawAtGridPos(x,y,z, 0.5f);
					}
				}
			}
		}

		volume.closeSides();
		surface = new ArrayIsoSurface(volume);
		mesh = new TriangleMesh();


	}


	public void draw() {


		background(127);  

		fill(255);
//		gfx.mesh(mesh);

	}

	void computeMesh() {
		surface.reset();
		mesh = (TriangleMesh) surface.computeSurfaceMesh(null, th); 
		println("th: " + th + " faces: " + mesh.getNumFaces());
	}

	public void keyPressed() {
		if (key == ' ') {
			computeMesh();
		}

		if (key == 'e') {
			mesh.saveAsSTL(sketchPath("test.stl"), true);
			exit();
		}
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { impermanencias.Impermanencias.class.getName() });
	}
}
