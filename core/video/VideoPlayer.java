package core.video;
import java.awt.print.Paper;
import java.util.logging.Logger;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.video.*;

public class VideoPlayer {

	/**
	 * Frames 
	 * by Andres Colubri. 
	 * 
	 * Moves through the video one frame at the time by using the
	 * arrow keys. It estimates the frame counts using the framerate
	 * of the movie file, so it might not be exact in some cases.
	 */


	//	Movie mov;
	private boolean useCam = false;
	private Capture cam;
	private Movie movie;
	private PImage buffer;

	public PApplet parent;

	private int newFrame = 0;
	private int movFrameRate = 30;

	private String movieFile;

	private Logger log = Logger.getLogger(this.getClass().getName());


	public VideoPlayer(PApplet parent, boolean useCam) {
		this.parent = parent;
		this.useCam = useCam;
		setupVideo();
	}


	void setupVideo() {
//		buffer = parent.createImage(640, 480, PApplet.RGB);
		if (useCam) {
			cam = new Capture (parent);
			cam.start();
			buffer = cam;
		} else {
			buffer = movie;
		}  
	}

	public void loadMovie(String movieFile){
		this.movieFile = movieFile;
		
		
		movie = new Movie(parent, movieFile);
	
		// Pausing the video at the first frame.		
		movie.play();
		movie.jump(0);
		movie.pause();
		
		while(!movie.available()){
		
		}
		
		buffer = movie;
	}

	void videoPause(){
		if(!useCam && movie != null) movie.pause();
	}

	void videoPlay(){
		if(!useCam && movie != null) movie.loop();
	}

	void videoStop(){
		if(!useCam && movie != null) movie.stop();
	}

	PImage updateVideo() {
		if(!useCam && movie != null && movie.available()) movie.read();
		if(useCam && cam.available()) cam.read();
		return buffer;
	}

	public PImage getFrame(){		
		
		return movie; //.read();
	}

	public void movieEvent(Movie m) {
		m.read();
	}


	public void next(){
		if (newFrame < getLength() - 1) newFrame++;
		setFrame(newFrame);  	
	}
	
	public void prev(){
		if (0 < newFrame) newFrame--;
		setFrame(newFrame);  	
	}
	
	public void keyPressed(int keyCode) {

		if(keyCode == PConstants.LEFT || keyCode == PConstants.RIGHT) {
			if (keyCode == PConstants.LEFT) {
				prev();
			} else if (keyCode == PConstants.RIGHT) {
				next();
			}
			log.info("setFrame: " + newFrame);
			
		}

	}


	public void setFrame(int n) {
		if(n > getLength()) return;
		// The duration of a single frame:
		float frameDuration = 1.0f / movFrameRate;
		// We move to the middle of the frame by adding 0.5:
		float where = (n + 0.5f) * frameDuration; 
		// Taking into account border effects:
		float diff = movie.duration() - where;
		if (diff < 0) {
			where += diff - 0.25f * frameDuration;
		}

		movie.play();
		movie.jump(where);
		movie.pause();  

	}  

	public int getFramesNumber() {    
		return PApplet.ceil(movie.time() * 30) - 1;
	}

	public int getLength() {
		return (int) (movie.duration() * movFrameRate);
	}  
	
	public PImage get(){
		return movie.get();
	}
}
