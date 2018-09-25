package io.github.yash777.xuggler;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Screen {
	
	protected static Robot robot;
	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public File screenAsImage(Rectangle rectangle) throws IOException {
		File file = new File( getFileName( null, null) );
		BufferedImage screenshot = getScreenshot( rectangle );
		ImageIO.write( screenshot, "png", file);
		return file;
	}
	
	protected static BufferedImage getScreenshot( Rectangle screenBounds ) {
		return robot.createScreenCapture( screenBounds );
	}
	
	/**
	 * <p>File extension formats</p>
	 * <ul>
	 * <li>Audio Formats « [.mp3 .m4a .aac .ogg .wma .flac .wav]</li>
	 * <li>Video Formats « [.mp4 .m4v .mov .avi .flv .mpg .wmv]</li>
	 * </ul>
	 * 
	 * @param filename  « the name of the file
	 * @param extension « choose any extension File formats
	 * @return
	 */
	String getFileName( String filename, String extension ) {
		String samplePageName;
		if( filename == null ) {
			samplePageName = "JavaCapturedFile";
		} else {
			samplePageName = filename;
		}
		
		if( extension == null ) {
			extension = "png";
		}
		
		String tempDir = System.getProperty("java.io.tmpdir").replace("\\", "/");
		//System.out.println("USER HOME «Temp Dir : "+ tempDir);
		
		long nanoTime = System.nanoTime();
		//System.out.println("JVM'shigh-resolution time source, in nanoseconds : "+ nanoTime);
		
		String fileName = tempDir+samplePageName+"_"+nanoTime+"."+extension;
		//System.out.println("File Path : "+fileName);
		
		return fileName;
	}
	
	/**
	 * Convert a {@link BufferedImage} of any type, to {@link BufferedImage} of a
	 * specified type. If the source image is the same type as the target type,
	 * then original image is returned, otherwise new image of the correct type is
	 * created and the content of the source image is copied into the new image.
	 * 
	 * @param sourceImage « the image to be converted
	 * @param targetType « the desired BufferedImage type
	 * 
	 * @return a BufferedImage of the specified target type.
	 * 
	 * @see BufferedImage
	 */
	protected static BufferedImage convertToType( BufferedImage sourceImage, int targetType ) {
		BufferedImage image;
		/* If the source image is the same type as the target type, then original image is returned.
		 * */
		if (sourceImage.getType() == targetType) {
			return sourceImage;
		}
		/* otherwise new image of the correct type is created 
		 * and the content of the source image is copied into the new image.
		 * */
		else { 
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
	
	protected static class Capture {

		/**
		 * Constructs a new Rectangle whose top left corner is(0, 0) and
		 * whose width and height are specified by the Dimension argument.
		 * 
		 * @return java.awt.Rectangle object with full screen.
		 */
		public static Rectangle getFullScreen() {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			Rectangle screenBounds = new Rectangle( screenSize );
			return screenBounds;
		}
		
		/**
		 * Constructs a new Rectangle whose upper-left corner is specified as (x,y) and
		 * whose width and height are specified by the arguments of the same name.
		 * 
		 * @param x the specified X coordinate
		 * @param y the specified Y coordinate
		 * @param width the width of the Rectangle
		 * @param height the height of the Rectangle
		 * @return java.awt.Rectangle object with selected area of screen.
		 */
		public static Rectangle getPieceOfScreen(int x, int y, int width, int height) {
			return new Rectangle(x, y, width, height);
		}
	}
}
