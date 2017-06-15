package com.robson.imageuploader.processor;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * ImageProcessor.
 */
public class ImageProcessor {

	private static final int MAX_RGB = 255;

	private static final String NEW_LINE = "<br>";
	private static final String WHITE_PIXEL = "&nbsp";

	private char[] pixels;
	private int[] colorBounds;

	private int COLOR_DEPTH;
	private int RANGE;
	private int EXPOSURE;

	private BufferedImage img;

	/**
	 * Create an ImageProcessor with specified settings
	 */
	public ImageProcessor(BufferedImage img, ProcessorProperties props) {
		this.img = img;

		setColorDepth(props.getColorDepth());
		setLowExposure(props.isLowExposure());
		setCharset(props.getCharset());

		applySettings();
	}

	/**
	 * Sets the color depth parameter. Default color depth is 4
	 * 
	 * @param depth
	 *            Number of colors to process. Must be between 2 - 50
	 */
	public void setColorDepth(int depth) {
		COLOR_DEPTH = depth;
	}

	/**
	 * Sets the exposure parameter. Default setting is high exposure
	 * 
	 * @param lowExposure
	 *            True sets the exposure to low, false sets the exposure to high.
	 */
	public void setLowExposure(boolean lowExposure) {
		EXPOSURE = lowExposure ? 1 : 0;
	}

	/**
	 * Set the charset which contains characters that will replace pixels.
	 * The size of the charset must be equal to or greater than the color depth.
	 * 
	 * @param charset
	 *            A string containing characters which will replace pixels.
	 *            Characters in the beginning of the string will replace
	 *            dark pixels and characters at the end of the string will replace light pixels.
	 */
	public void setCharset(String charset) {
		pixels = charset.toCharArray();
	}

	/**
	 * Apply any settings chosen if using settings other than default.
	 * Any time settings are changed, this method must be called
	 */
	public void applySettings() {
		setRange();
		buildColorRange();
	}

	/**
	 * Process the image
	 */
	public String process() {
		StringBuilder render = new StringBuilder();

		int width = img.getWidth();
		int height = img.getHeight();

		int[] pixelMap = img.getRGB(0, 0, width, height, null, 0, width);

		int count = 0;
		for (int pixel : pixelMap) {

			Color color = new Color(pixel);

			int val = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

			for (int i = 0; i < colorBounds.length - 1; i++) {
				if (val >= colorBounds[i] && val < colorBounds[i + 1]) {
					render.append(pixels[i]);
					break;
				}
			}

			if (val > colorBounds[colorBounds.length - 1])
				render.append(WHITE_PIXEL);

			if (++count == img.getWidth()) {
				render.append(NEW_LINE);
				count = 0;
			}
		}
		return render.toString();
	}

	/**
	 * Set the color Range for each pixel. Must be called after the color depth has been changed
	 */
	private void setRange() {
		RANGE = MAX_RGB / COLOR_DEPTH;
	}

	/**
	 * Build array that holds the values for determining each color's range
	 */
	private void buildColorRange() {
		colorBounds = new int[COLOR_DEPTH + EXPOSURE];

		for (int i = 0; i < colorBounds.length; i++) {
			colorBounds[i] = RANGE * i;
		}
	}
}
