package io.github.yash777.xuggler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A complete, cross-platform solution to record, convert and stream audio and video.
 * <p>
 * <a href="http://ffmpeg.org/">FFmpeg</a> and its 
 * <a href="https://github.com/adaptlearning/adapt_authoring/wiki/Installing-FFmpeg">Installing</a> process.
 * </p>
 * @author yashwanth.m
 *
 */
public class FFmpeg {
	
	static String commandPrompt;
	static {
		String OS = System.getProperty("os.name").toUpperCase();
		if ( OS.contains("WIN") ) {
			commandPrompt = "cmd.exe /c start "; // Windows
		} else {
			commandPrompt = "xterm -e "; // Non Wondows
		}
	}

	public FFmpeg(String FFmpegLocation) {
		this.FFmpegLocation = FFmpegLocation;
	}
	
	private String FFmpegLocation;
	public String getFFmpegLocation() {
		return FFmpegLocation;
	}
	public void setFFmpegLocation(String fFmpegLocation) {
		FFmpegLocation = fFmpegLocation;
	}
	public void mergeAudioVideo_FFmpeg_PB(String audioFile, String videoFile, String mergedFile) {
		String[] exeCmd = new String[]{FFmpegLocation, "-i", audioFile, "-i", videoFile ,
				" -c:v copy -c:a aac -strict experimental -y ", mergedFile };
		ProcessBuilder pb = new ProcessBuilder(exeCmd);
		pb.redirectErrorStream(true);
		
		try {
			Process process = pb.start();
			int waitFor = process.waitFor();
			System.out.println(" Wait for Command to Execute « " + waitFor);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	public void mergeAudioVideo_FFmpeg(String audioFile, String videoFile, String mergedFile) {
		
		try {
			File finalMergedFile = new File( mergedFile );
			
			String runCommand = commandPrompt
					+ FFmpegLocation
					+ " -i "
					+ audioFile
					+ " -i "
					+ videoFile
					+" -c:v copy -c:a aac -strict experimental -y "
					+ finalMergedFile ;
			System.out.println("Run Command : \n "+ runCommand);
			Process process = Runtime.getRuntime().exec( runCommand );
			
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line=null;
			while((line=input.readLine()) != null) {
				System.out.println(line);
			}
			
			int waitFor = process.waitFor();
			System.out.println(" Wait for Command to Execute « " + waitFor);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}