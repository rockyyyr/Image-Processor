package com.robson.imageuploader.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.robson.imageuploader.controller.Uploader;

/**
 * ProcessorProperties.
 */
public class ProcessorProperties {

	private static final int MAX_PIXEL_SIZE = 30;
	private static final String LOW_EXPOSURE_MARKER = "Low Exposure";
	private static final String DEFAULT_CHARSET = "0123456789abcdefghijklmnopqrstuvwxyz/*-+.<>\\|{}[]`~!@#$%^&*():';?";
	private static final int MAX_COLOR_DEPTH = 50;
	private static final int MIN_COLOR_DEPTH = 2;
	private static final int DEFAULT_COLOR_DEPTH = 4;
	private static final int DEFAULT_PIXEL_SIZE = 1;
	private static final boolean DEFAULT_EXPOSURE = false;

	private int colorDepth;
	private String charset;
	private boolean lowExposure;
	private int pixelSize;

	public ProcessorProperties() {
		setDefaultProperties();
	}

	public ProcessorProperties(HttpServletRequest request) throws IOException, ServletException {
		setDefaultProperties();
		setProperties(request);
	}

	public ProcessorProperties(String colorDepth, String charset, String lowExposure, String pixelSize) throws IOException, ServletException {
		setDefaultProperties();
		setProperties(colorDepth, charset, lowExposure, pixelSize);
	}

	private void setDefaultProperties() {
		colorDepth = DEFAULT_COLOR_DEPTH;
		charset = DEFAULT_CHARSET;
		lowExposure = DEFAULT_EXPOSURE;
		pixelSize = DEFAULT_PIXEL_SIZE;
	}

	/**
	 * Set the image processor properties
	 * 
	 * @param colorDepth
	 * @param charset
	 * @param lowExposure
	 * @param pixelSize
	 */
	public void setProperties(String colorDepth, String charset, String lowExposure, String pixelSize) {
		setColorDepth(colorDepth);
		setLowExposure(lowExposure);
		setCharset(charset);
		setPixelSize(pixelSize);
	}

	/**
	 * Set the ImageProcessor properties from a Multipart/form data servlet request
	 * 
	 * @param request
	 * @throws IOException
	 * @throws ServletException
	 */
	public void setProperties(HttpServletRequest request) throws IOException, ServletException {
		Part colorPart = request.getPart(Uploader.COLOR_DEPTH);
		Part exposurePart = request.getPart(Uploader.EXPOSURE);
		Part charsetPart = request.getPart(Uploader.CHARSET);
		Part pixelSizePart = request.getPart(Uploader.PIXEL_SIZE);

		setColorDepth(getValue(colorPart));
		setLowExposure(getValue(exposurePart));
		setCharset(getValue(charsetPart));
		setPixelSize(getValue(pixelSizePart));
	}

	/**
	 * Returns the colorDepth for this ProcessorProperties
	 * 
	 * @return colorDepth The colorDepth for this ProcessorProperties
	 */
	public int getColorDepth() {
		return colorDepth;
	}

	/**
	 * Returns the charset for this ProcessorProperties
	 * 
	 * @return charset The charset for this ProcessorProperties
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Returns the lowExposure for this ProcessorProperties
	 * 
	 * @return lowExposure The lowExposure for this ProcessorProperties
	 */
	public boolean isLowExposure() {
		return lowExposure;
	}

	/**
	 * Returns the pixelSize for this ProcessorProperties
	 * 
	 * @return pixelSize The pixelSize for this ProcessorProperties
	 */
	public int getPixelSize() {
		return pixelSize;
	}

	/**
	 * Sets the colorDepth for this ProcessorProperties
	 * 
	 * @param colorDepth
	 *            colorDepth to set
	 */
	public void setColorDepth(String colorDepth) {
		if (colorDepth != null && !colorDepth.isEmpty()) {
			int value = Integer.parseInt(colorDepth);

			if (value >= MIN_COLOR_DEPTH && value <= MAX_COLOR_DEPTH)
				this.colorDepth = value;
		}
	}

	/**
	 * Sets the charset for this ProcessorProperties
	 * 
	 * @param charset
	 *            charset to set
	 */
	public void setCharset(String charset) {
		if (charset != null && !charset.isEmpty()) {
			if (charset.length() >= colorDepth)
				this.charset = charset;
		}
	}

	/**
	 * Sets the lowExposure for this ProcessorProperties
	 * 
	 * @param lowExposure
	 *            lowExposure to set
	 */
	public void setLowExposure(String lowExposure) {
		if (lowExposure != null && !lowExposure.isEmpty()) {
			if (lowExposure.equals(LOW_EXPOSURE_MARKER))
				this.lowExposure = true;
		}
	}

	/**
	 * Sets the pixelSize for this ProcessorProperties
	 * 
	 * @param pixelSize
	 *            pixelSize to set
	 */
	public void setPixelSize(String pixelSize) {
		if (pixelSize != null && !pixelSize.isEmpty()) {
			int parsedSize = Integer.parseInt(pixelSize);

			if (parsedSize > 0 && parsedSize <= MAX_PIXEL_SIZE) {
				this.pixelSize = parsedSize;
			}
		}
	}

	/*
	 * Get values from multipart/form data request parts
	 */
	private String getValue(Part part) {

		StringBuilder sb = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream()));) {

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

}
