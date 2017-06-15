package com.robson.imageuploader.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.robson.imageuploader.processor.ProcessorManager;
import com.robson.imageuploader.processor.ProcessorProperties;
import com.robson.imageuploader.properties.Property;
import com.sun.media.sound.InvalidFormatException;

/**
 * Servlet implementation class Uploader
 */
@MultipartConfig
public class Uploader extends HttpServlet {

	private static final long serialVersionUID = -7832712145755792003L;

	private static Logger log = LogManager.getLogger();

	public static final String COLOR_DEPTH = "colorDepth";
	public static final String EXPOSURE = "exposure";
	public static final String CHARSET = "charset";
	public static final String PIXEL_SIZE = "pixelSize";
	public static final String URL = "image-url";

	private String FONT;
	private String FONT_SIZE;
	private double LETTER_SPACE;
	private int MAX_LETTER_SPACE;
	private String HOME_PAGE;
	private String FILE_DIR;

	private Property prop;

	@Override
	public void init() {
		prop = new Property();

		FONT = prop.get("font");
		FONT_SIZE = "1px";
		LETTER_SPACE = Double.parseDouble(prop.get("letter_spacing"));
		MAX_LETTER_SPACE = Integer.parseInt(prop.get("max_letter_spacing"));
		FILE_DIR = getInitParameter("FILE_DIR");
		HOME_PAGE = getInitParameter("HOME_PAGE");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("**********Link Uploaded**********");

		String url = request.getParameter(URL);

		if (url == null || url.isEmpty()) {
			response.sendRedirect(HOME_PAGE);

		} else {

			String colorDepth = request.getParameter(COLOR_DEPTH);
			String lowExposure = request.getParameter(EXPOSURE);
			String charset = request.getParameter(CHARSET);
			String pixelSize = request.getParameter(PIXEL_SIZE);

			ProcessorProperties props = new ProcessorProperties(colorDepth, charset, lowExposure, pixelSize);
			ProcessorManager proManager = null;

			try {
				proManager = new ProcessorManager(props, url);
				setPixelSize(props.getPixelSize(), request);

				sendImageAsResponse(response, proManager.getImage());

			} catch (MalformedURLException e) {
				sendUrlErrorResponse(response, url);
			} finally {
				log.info("**********End of Session**********");
			}

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("**********File Uploaded**********");

		Part filePart = request.getPart("file");

		if (filePart.getSubmittedFileName().isEmpty()) {
			response.sendRedirect(HOME_PAGE);

		} else {

			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			File file = new File(FILE_DIR + File.separator + fileName);

			try (InputStream input = filePart.getInputStream()) {
				Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}

			ProcessorProperties props = new ProcessorProperties(request);
			ProcessorManager proManager = null;

			try {
				proManager = new ProcessorManager(props, file);
				setPixelSize(props.getPixelSize(), request);
				sendImageAsResponse(response, proManager.getImage());

				file.delete();

			} catch (InvalidFormatException e) {
				sendFileErrorResponse(response);
			} finally {
				log.info("**********End of Session**********");
			}

		}
	}

	/**
	 * Send the image as a response to a new page int the browser
	 * 
	 * @param response
	 * @param imgPro
	 * @throws IOException
	 */
	private void sendImageAsResponse(HttpServletResponse response, String image) throws IOException {
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<body>");
		out.println(""
				+ "<div style="
				+ "'font-size: " + FONT_SIZE + "; "
				+ "font-family: " + FONT + "!important;"
				+ "letter-spacing: " + (LETTER_SPACE < MAX_LETTER_SPACE ? LETTER_SPACE : MAX_LETTER_SPACE) + "px'>"
				+ image
				+ "</div>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	private void sendUrlErrorResponse(HttpServletResponse response, String url) throws IOException {
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<body style='font-size: 22'>");
		out.println("<br>");
		out.println("<b>Error - URL does not direct to an image:</b> " + url);
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	private void sendFileErrorResponse(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<body style='font-size: 22'>");
		out.println("<br>");
		out.println("<b>Error - Uploaded file is not a valid image type</b>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	private void setMobilePixelSize(int size) {
		FONT_SIZE = size + "px";
		LETTER_SPACE = size;
		log.info("Using Mobile letter spacing: " + LETTER_SPACE);
	}

	private void setDesktopPixelSize(int size) {
		FONT_SIZE = size + "px";
		LETTER_SPACE = (double) size / 2;
		log.info("Using desktop letter spacing: " + LETTER_SPACE);
	}

	private void setPixelSize(int size, HttpServletRequest request) {
		if (requestFromMobile(request))
			setMobilePixelSize(size);
		else
			setDesktopPixelSize(size);
	}

	private boolean requestFromMobile(HttpServletRequest request) {
		log.info("User agent: " + request.getHeader("User-Agent"));
		return request.getHeader("User-Agent").indexOf("Mobile") != -1;
	}

}
