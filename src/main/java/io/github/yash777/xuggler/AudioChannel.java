package io.github.yash777.xuggler;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * AudioChannel is class developed based on the class 
 * <a href="https://github.com/marytts/marytts/blob/master/marytts-signalproc/src/main/java/marytts/util/data/audio/SimpleAudioRecorder.java">
 * SimpleAudioRecorder</a>
 * 
 * <P>Sounds may have different numbers of audio channels: one for mono, two for stereo.
 * The sample rate measures how many "snapshots" (samples) of the sound pressure are taken per second, per channel.
 * </p>
 * 
 * <p>Audio channel - left, right channel</p>
 * <ol>
 * <li><b>Mono</b> - In monophonic sound has audio in a single channel. It can be reproduced through several speakers,
 * but all speakers are still reproducing the same copy of the signal.</li>
 * <li><b>stereo</b> - In stereophonic the reproduction of sound using two or more independent audio channels
 * in a way that creates the impression of sound heard from various directions, as in natural hearing.
 * Audio signals are routed through 2 or more channels to simulate depth/direction perception, like in the real world.</li>
 *</ol>
 * In addition to the encoding, the audio format includes other properties that further specify the exact arrangement
 * of the data. These include the number of channels, sample rate, sample size, byte order, frame rate, and frame size.
 * 
 * <ul>
 * <li><a href="https://www.image-line.com/support/FLHelp/html/recording_audio.html">Image Line</a></li>
 * </ul>
 * @author yashwanth.m
 *
 */
public class AudioChannel extends Screen implements Recorder {
	
	private File file = null;
	public File getFile() {
		return file;
	}
	
	public static boolean isException = true;
	
	AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
	
	/**
	 * It will use the default file as JavaCapturedFile_TimeStamp.mp3 file.
	 */
	public AudioChannel() {
		// Default temporary location file has to be picked.
		String defalultFileName = getFileName(null, "wav");
		file = new File( defalultFileName );
	}
	
	public AudioChannel( String fileName ) {
		if( fileName == null || fileName == "" ) {
			fileName = "MyScreenRecorder";
		}
		String defalultFileName = getFileName(fileName, "wav");
		file = new File( defalultFileName );
	}

	private TargetDataLine dataLine;
	private AudioInputStream audioStream;
	
	public void setDataLine(TargetDataLine dataLine) {
		this.dataLine = dataLine;
		this.audioStream = new AudioInputStream( dataLine );
	}
	
	/**
	 * Allocates a new Thread object. This thread is going to capture frames of screen and group them into a video.
	 */
	public void startRecording() {
		( new Thread() {
			public void run() {
				start(); // startAudioRecording
			}
		} ).start();
	}
	/**
	 * Start audio recording.
	 */
	public void start( ) {
		
		/* For simplicity, the audio data format used for recording is hard coded here.
		 * We use PCM 44.1 kHz, 16 bit signed, stereo.
		 */
		Encoding encodingAudio = AudioFormat.Encoding.PCM_SIGNED; // pulse-code modulation - PCM_SIGNED 44100.0 Hz
		int sampleSizeInBits = 16, // the number of bits in each sample
				channels = 2, // Mono[1], Stereo[2]- Audio channel [left, right channel's]
				frameSize = 4; // bytes/frame - the number of bytes in each frame
		float frameRate = 44100.0F, sampleRate = 44100.0F; // the number of frames,samples per second
		
		AudioFormat	audioFormat = 
				//new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false); 
				new AudioFormat(encodingAudio, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, false);
		
		/* Now, we are trying to get a TargetDataLine. The TargetDataLine is used later to read audio data from it.
		 * If requesting the line was successful, we are opening it (important!).
		 * 
		 * A target data line is a type of DataLine from which audio data can be read.
		 * The most common example is a data line that gets its data from an audio capture device.
		 */
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
		TargetDataLine targetDataLine = null;
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine( info );
			targetDataLine.open(audioFormat);
			
			setDataLine(targetDataLine);
			
			dataLine.start();
			
			// Writes a stream of bytes representing an audio file of the specified file type to the external file provided.
			AudioSystem.write(audioStream, targetType, file);
			isException = false;
		} catch ( LineUnavailableException e ) {
			System.err.println("Audio interface - Please plugin audio line and start recording again.");
			e.printStackTrace();
		} catch ( IllegalArgumentException e ) {
			System.err.println("Audio interface - "
					+ "Using a USB microphone or headset does't contains mouth to read audio stream.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Thread.interrupted();
		}
	}
	
	public void stop() {
		if( dataLine != null ) {
			dataLine.stop();
			dataLine.flush();
			dataLine.close();
		}
	}

}
