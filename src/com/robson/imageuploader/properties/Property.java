package com.robson.imageuploader.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * Property.
 */
public class Property {

	private static final String PROP_FILE = "app.properties";

	private Properties prop = new Properties();

	public Property() {
		prop = new Properties();

		try {
			prop.load(Property.class.getClassLoader().getResourceAsStream(PROP_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param param
	 *            property key
	 * @return The specified property
	 */
	public String get(String param) {
		return prop.getProperty(param);
	}

}
