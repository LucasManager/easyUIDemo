package com.bmm.traffic.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtil {
	/**
	 * 
	 * @return prop 
	 */
	public static Properties getProperties(String name) {
		Properties prop = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(name);
		try {
			//in = new BufferedInputStream(new FileInputStream(name));
			prop.load(in);
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
