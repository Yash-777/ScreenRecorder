package io.github.yash777.xuggler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IRational;

import io.github.yash777.Commons;

/**
 * The class VideoFrame will capture the number of frames per given frame rate and encodes them into a video stream.
 * then save that stream into the given file.
 * <p>Using {@link IMediaWriter} « takes snapshots of your desktop and encodes them to video.</p>
 * 
 * <p>For reference see xuggle class: <a href="https://github.com/Yash-777/xuggle-xuggler/tree/master/src/com/xuggle/mediatool/demos">
 *  CaptureScreenToFile.java</a> file.
 * </p>
 * @author yashwanth.m
 *
 */
public class VideoFrame extends Screen implements Recorder {
	
	Integer frameRate = 5;
	public void setFrameRate(Integer frameRate) {
		this.frameRate = frameRate;
	}
	
	ID codecId = ICodec.ID.CODEC_ID_H264; // ICodec.ID.CODEC_ID_MPEG4
	public void setCodecId(ID codecId) {
		this.codecId = codecId;
	}
	
	boolean haveMorePictures = true;
	
	private File file = null;
	public File getFile() {
		return file;
	}

	private Rectangle screenBounds = null;
	
	/**
	 * It will use the default file as JavaCapturedFile_TimeStamp.mp4 file.
	 */
	public VideoFrame() {
		// Default temporary location file has to be picked.
		String defalultFileName = getFileName(null, "mp4");
		file = new File( defalultFileName );
		screenBounds = Capture.getFullScreen();
	}
	/**
	 * @param fileName the name of the file, like MyScreenRecorder. By taking this it will generate MyScreenRecorder_TimeStamp.mp4 file.
	 */
	public VideoFrame( String fileName ) {
		if( fileName == null || fileName == "" ) {
			fileName = "MyScreenRecorder";
		}
		String defalultFileName = getFileName(fileName, "mp4");
		file = new File( defalultFileName );
		screenBounds = Capture.getFullScreen();
	}
	/*public VideoFrame( String fileName, Rectangle rectangle ) {
		if( fileName == null || fileName == "" ) {
			fileName = "MyScreenRecorder";
		}
		String defalultFileName = getFileName(fileName, "mp4");
		file = new File( defalultFileName );
		screenBounds = rectangle;
	}*/
	
	/*public VideoFrame( File file ) {
		this.file = file;
		screenBounds = Capture.getFullScreen();
	}
	public VideoFrame( File file, Rectangle rectangle ) {
		this.file = file;
		screenBounds = rectangle;
	}*/
	
	public static void main(String[] args) {
		VideoFrame obj = new VideoFrame();
		obj.start();
		Commons.getUserInput();
		obj.stop();
		
		System.out.println("Final Video File : "+obj.getFile().getAbsolutePath() );
	}
	
	/**
	 * Allocates a new Thread object. This thread is going to capture frames of screen and group them into a video.
	 */
	public void start() {
		( new Thread() {
			public void run() {
				try {
					captureImages();
				} catch (Exception e) {
					System.out.println("UnSupported video format. leads to abnormal termination.");
					System.exit(0);
				}
			}
		} ).start();
	}
	/**
	 * It stops the execution of the Frames capturing thread.
	 */
	public void stop() {
		haveMorePictures = false;
	}
	
	void captureImages() {
		
		try {
			// Create a rational from a numerator and denominator.
			IRational FRAME_RATE = IRational.make( frameRate, 1 );
			
			// let's make a IMediaWriter to write the file.
			IMediaWriter mediaWriter = ToolFactory.makeWriter( file.getAbsolutePath() );
			
			int inputIndex = 0, streamId = 0, width = screenBounds.width, height = screenBounds.height;
		
			mediaWriter.addVideoStream(inputIndex, streamId, codecId, width, height);
		
			int videoStreamIndex = 0;
			long startTime = System.nanoTime();
			while( haveMorePictures ) {
				BufferedImage frame = robot.createScreenCapture( screenBounds );
				long videoFrameTime = System.nanoTime() - startTime;
				TimeUnit DEFAULT_TIME_UNIT = TimeUnit.NANOSECONDS;
				// convert to the right image type [ TYPE_3BYTE_BGR ]
				BufferedImage videoFrame = convertToType(frame, BufferedImage.TYPE_3BYTE_BGR);
				
				// encode the image[videoFrame] to stream at the specified videoFrameTime
				mediaWriter.encodeVideo(videoStreamIndex, videoFrame, videoFrameTime, DEFAULT_TIME_UNIT);
				// com.xuggle.xuggler.video.ConverterFactory.createConverter(ConverterFactory.java:313)
				
				// sleep for FrameRate milliseconds [milliseconds 1000 = 1 second]
				Commons.sleepThread( (long) (1000 / FRAME_RATE.getDouble()) );
			}
			// Tell the writer to close and write the data into file.
			mediaWriter.flush();
			mediaWriter.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
