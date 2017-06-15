package com.robson.imageuploader.processor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.robson.imageuploader.properties.Property;
import com.sun.media.sound.InvalidFormatException;

/**
 * ImageParser.
 */
public class ImageParser {

	public static final int DEFAULT_PIXEL_SIZE = 1;

	public int MAX_IMAGE_SIZE;
	public int PIXEL_SIZE;

	private Property prop;

	/**
	 * Create an image parser with a pixel size of 1
	 */
	public ImageParser() {
		init();
		PIXEL_SIZE = DEFAULT_PIXEL_SIZE;
	}

	/**
	 * Create an image parser with a specified pixel size.
	 * If the pixel size is not in range then the default of 1 will be used
	 */
	public ImageParser(int pixelSize) {
		init();
		if (pixelSize > 0)
			PIXEL_SIZE = pixelSize;
		else
			PIXEL_SIZE = DEFAULT_PIXEL_SIZE;
	}

	/**
	 * Return a BufferedImage from a url. If the image is too big, it is resized.
	 * 
	 * @param url
	 * @return Parsed bufferedImage
	 * @throws MalformedURLException
	 */
	public BufferedImage parse(String url) throws MalformedURLException {
		return analyzeImageSize(getImageFromUrl(url));
	}

	/**
	 * Return a BufferedImage from a File. If the image is too big, it is resized.
	 * 
	 * @param file
	 * @return Parsed bufferedImage
	 * @throws InvalidFormatException
	 */
	public BufferedImage parse(File file) throws InvalidFormatException {
		return analyzeImageSize(getImageFromFile(file));
	}

	private void init() {
		prop = new Property();
		MAX_IMAGE_SIZE = Integer.parseInt(prop.get("max_image_size"));
	}

	/*
	 * Check the image's size. If it is too big, resize it and return the resized BufferedImage,
	 * else return the parameter BufferedImage.
	 */
	private BufferedImage analyzeImageSize(BufferedImage img) {
		if (!isSmallSized(img))
			return resizeImage(img);
		else
			return img;
	}

	/*
	 * Resize a bufferedImage taking into account the specified pixel size
	 */
	private BufferedImage resizeImage(BufferedImage original) {

		double preWidth = original.getWidth() / ((double) original.getHeight() / MAX_IMAGE_SIZE);
		int width = (int) preWidth / PIXEL_SIZE;
		int height = MAX_IMAGE_SIZE / PIXEL_SIZE;

		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = resized.createGraphics();
		g.drawImage(original, 0, 0, width, height, null);
		g.dispose();

		return resized;
	}

	/**
	 * Check to see if the image is below the maximum size
	 * 
	 * @param img
	 * @return true if the image is below the maximum size
	 */
	private boolean isSmallSized(BufferedImage img) {
		return (img.getHeight() * PIXEL_SIZE) < MAX_IMAGE_SIZE && (img.getWidth() * PIXEL_SIZE) < MAX_IMAGE_SIZE;
	}

	/**
	 * @param fileName
	 * @return a bufferedImage from the specified fileName
	 * @throws InvalidFormatException
	 */
	private BufferedImage getImageFromFile(File file) throws InvalidFormatException {
		BufferedImage img = null;

		try {
			img = ImageIO.read(file);

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (img == null) {
			throw new InvalidFormatException();
		}

		return img;
	}

	/**
	 * @param url
	 * @return a bufferedImage from the specified url
	 * @throws MalformedURLException
	 */
	private BufferedImage getImageFromUrl(String url) throws MalformedURLException {
		BufferedImage img = null;

		try {
			img = ImageIO.read(new URL(url));

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (img == null) {
			throw new MalformedURLException();
		}

		return img;
	}

}
