package io.github.yash777.xuggler;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * capture a rectangular screen area
 * @author yashwanth.m
 *
 */
public class ScreenToCapture {

	public static Rectangle FullScreen() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle screenBounds = new Rectangle( screenSize );
		return screenBounds;
	}
	
	public static void PieceOfScreen() {
		
	}
}
