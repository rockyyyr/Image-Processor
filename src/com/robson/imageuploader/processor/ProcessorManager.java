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

	/**
	 * Create a ProcessorManager. Prepare image from url
	 */
	public ProcessorManager(ProcessorProperties props, String url) throws MalformedURLException {
		imgParser = new ImageParser(props.getPixelSize());
		imgProcessor = new ImageProcessor(imgParser.parse(url), props);
	}

	/**
	 * Create a ProcessorManager. Prepare image from a file
	 */
	public ProcessorManager(ProcessorProperties props, File file) throws InvalidFormatException {
		imgParser = new ImageParser(props.getPixelSize());
		imgProcessor = new ImageProcessor(imgParser.parse(file), props);
	}

	/**
	 * Process an image
	 * 
	 * @return An image as a string
	 */
	public String getImage() {
		return imgProcessor.process();
	}

}
