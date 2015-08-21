package impermanencias;

import java.util.Date;
import java.util.logging.Logger;

import com.sun.tools.internal.ws.util.ClassNameInfo;

import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Movie;
import controlP5.*;
import core.video.VideoPlayer;
import core.volume.VolumeCreator;
import toxi.processing.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.volume.*;


public class Impermanencias extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Logger log = Logger.getLogger( ClassNameInfo.class.getName());

	VideoPlayer vp ;
	VolumeCreator vc;		
	ToxiclibsSupport gfx;


	ControlP5 cp5;

	public static void main(String _args[]) {
		PApplet.main(new String[] { impermanencias.Impermanencias.class.getName() });
	}

	int in = 0;
	int out = 0;
	
	ControlListener cb;
	
	Slider inSlider; 
	PImage inFrame;
	
	Slider lenghtSlider;
	PImage outFrame;
	
	int length = 0;
	
	PImage currentFrame;
	
	public void setup() {		
		size(960, 600, P3D);		
		log.info("iniciando processing");		

		gfx = new ToxiclibsSupport(this);
		vp = new VideoPlayer(this, false);		
		vp.loadMovie("impermanencias.mov");
		
	
		currentFrame = vp.get();
		
		cp5 = new ControlP5(this);


		  cb = new ControlListener() {
			
			@Override
			
			    public void controlEvent(ControlEvent theEvent) {
			    	println(theEvent);
//			    	if(theEvent.() != ControlP5.ACTION_RELEASED) return;
			    	println(theEvent);
			    	
			    	if (theEvent.getController().getName().equals("in")) {
//			    		int frame = (int) theEvent.getController().getValue();
			    		vp.setFrame(in);
			    		inFrame = vp.get();
			    		println("in: " + in);
			    		
			    		int last = in + length < vp.getLength() ? in+length : vp.getLength();
			    		vp.setFrame(last);
			    		outFrame = vp.get();
			    		println("last: " + last);
			    	}
			    	
			    	
			    	if (theEvent.getController().getName().equals("length")) {
			    		int l = (int) theEvent.getController().getValue();
			    		int last = in + l < vp.getLength() ? in +l : vp.getLength();
			    		vp.setFrame(last);
			    		outFrame = vp.get();		
			    		println("last: " + last);
			    	}
			    }
			
			
		};
		 Group g1 = cp5.addGroup("g1")
	                .setPosition(50,260)
	                .setWidth(400)
	                .setBackgroundHeight(200)
	                .setBackgroundColor(color(255,50))
	                ;
		 
		// add a horizontal sliders, the value of this slider will be linked
		// to variable 'sliderValue' 
		
		inSlider = new Slider(cp5, "in");
		
		inSlider.setPosition(10,10)
		.setWidth(250)
		.setRange(0,vp.getLength())
		.setGroup(g1)		
		.addListener(cb)
		;

		
		lenghtSlider = new Slider(cp5, "length");
		lenghtSlider.setPosition(10,30)
		.setWidth(250)
		.setRange(0,500)
		.setGroup(g1)
		.addListener(cb)
		;
		
		
		
		cp5.addButton("render")
		.setPosition(10,50)
		.setGroup(g1);
	}

	public void render(int theValue){
		println("render");
		layer = 0;
		vc = new VolumeCreator(vp.getFrame().width, vp.getFrame().height, 500);//
		for(int i = in; i < in+length; i++){
			vp.setFrame(i);
			vc.addLayer(vp.getFrame(), layer);
			layer++;
		}
		
		TriangleMesh mesh = vc.generate();
		println(mesh.getNumFaces());
		String d = String.valueOf( (new Date()).getTime());
		mesh.saveAsOBJ(sketchPath("test" +d+".obj"));
		
	}

	public void draw() {		
		background(127);  	
		gui(vp.getFrame(), inFrame, outFrame);		
		fill(255);		
	}


	public void gui(PImage a, PImage b, PImage c){
		if(a != null) image(a, 0,0, 320, 240);
		if(b != null) image(b, 320,0, 320, 240);
		if(b != null) image(c, 640,0, 320, 240);
	}





	int layer = 0;
	float th = 0.1f;	
	public void movieEvent(Movie m){
		if(vp != null) {
			vp.movieEvent(m);			
		}
	}

	public void keyPressed() {
		if(key == ' ') 	{
			TriangleMesh mesh = vc.generate();
			println(mesh.getNumFaces());
			mesh.saveAsOBJ(sketchPath("test.obj"));
		}else{
			vp.next();
			vc.addLayer(vp.getFrame(), layer);
			layer++;
		}
	}


}
