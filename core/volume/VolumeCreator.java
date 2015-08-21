package core.volume;

import java.util.logging.Logger;

import impermanencias.Impermanencias;
import processing.core.PImage;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.TriangleMesh;
import toxi.volume.ArrayIsoSurface;
import toxi.volume.IsoSurface;
import toxi.volume.RoundBrush;
import toxi.volume.VolumetricBrush;
import toxi.volume.VolumetricSpace;
import toxi.volume.VolumetricSpaceArray;

public class VolumeCreator {

	VolumetricSpace volume;
	VolumetricBrush brush;
	TriangleMesh mesh;
	IsoSurface surface; 
	Vec3D volumeSize;
	Vec3D dimensions;
	
	int scale = 1;
	Logger log = Logger.getLogger(this.getClass().getName(), null);
	
	public VolumeCreator(int width, int height, int layers) {

		log.info("volume created: w: " + width + " h: "+ height + " z: "+ layers );
		int depth = 3 + layers;
		dimensions = new Vec3D(width, height, depth);		
		volumeSize = new Vec3D(width, height, depth);
		volumeSize.scaleSelf(scale);

		createVolume();
	}

	private void createVolume(){


		volume = new VolumetricSpaceArray(volumeSize, (int) dimensions.x, (int) dimensions.y, (int) dimensions.z);
		brush = new RoundBrush(volume, scale);


	}

	public int addLayer(PImage img, int layer){
		log.info("adding layer: " + layer);
		img.loadPixels();
		for (int y = 0; y < img.height; y ++) {
			for (int x = 0; x < img.width; x ++) {
				if (0 == (img.pixels[y * img.width + x] & 0xff)) {
					for (int z = layer; z < layer + 1; z++) {
						brush.drawAtGridPos(x, y, z, 0.5f);
					}
				}
			}
		}

		return 0;

	}

	public TriangleMesh generate(){
		log.info("generating...");
		volume.closeSides();
		mesh = (TriangleMesh) new ArrayIsoSurface(volume).computeSurfaceMesh(null, 0.1f);
		return mesh;
	}

}
