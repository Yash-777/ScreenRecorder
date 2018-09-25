package io.github.yash777.xuggler;

import java.io.File;

public interface Recorder {
	void start();
	void stop();
	File getFile();
}
