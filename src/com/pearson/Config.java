package com.pearson;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static Properties props = loadProperties();
	
	public static String unit_folder_path = props.getProperty("btectransformer.unit.folder.path");

    public static String unit_status_report_filename = props.getProperty("btectransformer.unit.status.report.filename");

	public static String unit_xslt_wordxml_to_iqs_filename = props.getProperty("btectransformer.unit.xsl.wordxml.to.iqs.filename");

    public static String unit_xsd_iqs_schema_filename = props.getProperty("btectransformer.unit.xsd.iqs.schema.filename");

    public static String unit_xslt_iqs_to_html_filename = props.getProperty("btectransformer.unit.xsl.iqs.to.html.filename");

	// get the configuration for the example
	private static Properties loadProperties() {		
	    try {
			String propsName = "Config.properties";
			InputStream propsStream =
				Config.class.getClassLoader().getResourceAsStream(propsName);
			if (propsStream == null)
				throw new IOException("Could not read config properties");

			Properties props = new Properties();
			props.load(propsStream);

			return props;

	    } catch (final IOException exc) {
	        throw new Error(exc);
	    }  
	}
}
