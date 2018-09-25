package io.github.yash777.xuggler;

import java.io.File;

import io.github.yash777.Commons;

public class ScreenRecorder extends Screen {
	//private ThreadGroup group = new ThreadGroup("ScreenRecorder");
	
	private File file = null;
	public File getFile() {
		return file;
	}
	public ScreenRecorder() {
		// Default temporary location file has to be picked.
		String defalultFileName = getFileName("ScreenRecorder_", "mp4");
		file = new File( defalultFileName );
	}
	
	final VideoFrame obj_Video = new VideoFrame("Video_");
	final AudioChannel obj_Audio = new AudioChannel("Audio_");
	public void start() {
		//obj_Video.captureImages();
		
		Thread videoThread = new Thread( ) {
			public void run() {
				obj_Video.captureImages();
			}
		};
		videoThread.setPriority( Thread.MAX_PRIORITY );
		
		Thread audioThread = new Thread( ) {
			public void run() {
				obj_Audio.start();
			}
		};
		audioThread.setPriority( Thread.MAX_PRIORITY );
		
		videoThread.start();
		audioThread.start();
	}
	
	public void stop( FFmpeg mergeFiles ) {
		obj_Video.haveMorePictures = false;
		obj_Audio.stop();
		Commons.sleepThread(1500);
		
		File videoFile = obj_Video.getFile();
		File audioFile = obj_Audio.getFile();
		
		boolean isFileCanRead = false;
		if( ( videoFile.isFile() && videoFile.canRead() ) &&
			( audioFile.isFile() && audioFile.canRead() ) ) {
			System.out.println("***** File available and can read it.");
			
			System.out.println("Video File : "+ videoFile.getAbsolutePath() );
			System.out.println("Audio File : "+ audioFile.getAbsolutePath() );
		}	
		
		if( !AudioChannel.isException && isFileCanRead ) {
		mergeFiles.mergeAudioVideo_FFmpeg(audioFile.getAbsolutePath(), videoFile.getAbsolutePath(), file.getAbsolutePath() );
		//mergeFiles.mergeAudioVideo_FFmpeg_PB(audioFile.getAbsolutePath(), videoFile.getAbsolutePath(), file.getAbsolutePath() );
		Commons.sleepThread(1000);
		} else {
			System.err.println("FFmpeg « AudioChannel DataLine is empty!, So no need to Merge Files.");
		}
	}
}
