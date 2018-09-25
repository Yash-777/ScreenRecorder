package io.github.yash777;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import io.github.yash777.xuggler.AudioChannel;
import io.github.yash777.xuggler.FFmpeg;
import io.github.yash777.xuggler.Recorder;
import io.github.yash777.xuggler.Screen;
import io.github.yash777.xuggler.ScreenRecorder;
import io.github.yash777.xuggler.VideoFrame;

/**
 * Main method to test all the functionalities of the application.
 * 
 * @author yashwanth.m
 *
 */
public class App {
	static boolean dataLine = true;
	public static void main( String[] args ) {
		
		//audioRecording();
		videoRecording();
		
		//audioVideoRecording();
		
		/*int x=421,y=338,width=635,height=159;
		Rectangle rectangle = new Rectangle(x, y, width, height);
		captureScreen( rectangle );*/
	}
	
	static void captureScreen( Rectangle rectangle ) {
		Screen obj = new Screen();
		try {
			File screenAsImage = obj.screenAsImage( rectangle );
			System.out.println("A full screenshot saved! \n " + screenAsImage.getAbsolutePath() );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void audioVideoRecording() {
		ScreenRecorder obj = new ScreenRecorder();
		obj.start();
		
		Commons.getUserInput();
		
		FFmpeg mergeFiles = new FFmpeg("E:\\ffmpeg-win64-static\\bin\\ffmpeg");
		obj.stop(mergeFiles);
		
		System.out.println("Final Recording File : "+ obj.getFile().getAbsolutePath() );
	}
	static void videoRecording() {
		Recorder video = new VideoFrame( "MySystemVideoStream" );
		video.start();
		
		Commons.getUserInput();
		
		video.stop();
		System.out.println("Recorded file name : "+ video.getFile().getAbsolutePath() );
	}
	static void audioRecording() {
		Recorder chanel = new AudioChannel();
		chanel.start();
		
		Commons.getUserInput();
		
		chanel.stop();
		System.out.println("Recorded file name : "+ chanel.getFile().getAbsolutePath() );
	}

}
