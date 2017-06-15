package com.robson.imageuploader.processor;

import java.io.File;
import java.net.MalformedURLException;

import com.sun.media.sound.InvalidFormatException;

/**
 * ProcessorManager.
 */
public class ProcessorManager {

	private ImageParser imgParser;
	private ImageProcessor imgProcessor;

	public ProcessorManager(ProcessorProperties props, String url) throws MalformedURLException {
		imgParser = new ImageParser(props.getPixelSize());
		imgProcessor = new ImageProcessor(imgParser.parse(url), props);
	}

	public ProcessorManager(ProcessorProperties props, File file) throws InvalidFormatException {
		imgParser = new ImageParser(props.getPixelSize());
		imgProcessor = new ImageProcessor(imgParser.parse(file), props);
	}

	public String getImage() {
		return imgProcessor.process();
	}

}
