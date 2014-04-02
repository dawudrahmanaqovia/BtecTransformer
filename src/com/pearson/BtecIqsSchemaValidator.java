package com.pearson;

/**
 * Created by Dawud on 14/03/14.
 */
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BtecIqsSchemaValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BtecIqsSchemaValidator.class);

    public static void validate(File file) {
        Source xmlFile = null;
        try {
            File schemaFile = new File(Config.unit_xsd_iqs_schema_filename);
            xmlFile = new StreamSource(file);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            System.out.println("** Transformed WordXML -> IQS Xml : SUCCESS **");
            LOGGER.info("** Transformed WordXML -> IQS Xml : SUCCESS **");
            LOGGER.info("Schema Validation for [" + file.getName() + "] : PASSED");

        } catch (SAXParseException e) {
            System.out.println("** Transformed WordXML -> IQS Xml : FAILED **");
            System.out.println("Schema Validation for [" + file.getName() + "] : FAILED");
            LOGGER.info("** Transformed WordXML -> IQS Xml : FAILED **");
            LOGGER.info("Schema Validation for [" + file.getName() + "] : FAILED");
            LOGGER.info("Reason: " + e.getLocalizedMessage());
            LOGGER.info("IQS XML Filename: {}", e.getSystemId());
            LOGGER.info("Line number: {}", e.getLineNumber());
            LOGGER.info("Column number: {} ", e.getColumnNumber());
            LOGGER.info("Please correct the above errors in the Word XML Document and re-run the transformation tool.");
            continueProcess();
        } catch (SAXException e) {
            System.out.println("** Transformed WordXML -> IQS Xml : FAILED **");
            System.out.println("XML Validation for [" + file.getName() + "] : FAILED");
            LOGGER.info("** Transformed WordXML -> IQS Xml : FAILED **");
            LOGGER.info("XML Validation for [" + file.getName() + "] : FAILED");
            LOGGER.info("Reason: {} ", e.getLocalizedMessage());
            continueProcess();
        } catch (IOException e) {
            System.out.println("** Transformed WordXML -> IQS Xml : FAILED **");
            LOGGER.info("** Transformed WordXML -> IQS Xml : FAILED **");
            LOGGER.error("Unable to perform Schema Validation for [" + file.getName() + "]");
            LOGGER.error("Reason: No internet connection or File is missing or unreadable {} ", e.getLocalizedMessage());
            continueProcess();
        }
    }



    private static void continueProcess() {
        Toolkit.getDefaultToolkit().beep();
        Scanner keyIn = new Scanner(System.in);
        System.out.print("\r\nPress the enter key to continue");
        keyIn.nextLine();
    }

    public static void main(String args[]) {
        validate(new File("D:\\BtecTransformer\\input\\Development\\Writings\\Unit Drafts\\Unit 4 Creating Digital Animations\\BTEC_SPEC_XML\\M_444_1234.xml"));
        validate(new File("D:\\Aqovia\\Pearson PSON Schools 2014\\India IQS XML Schemas\\Wordxmltoiqs\\xslt\\M_503_6513.xml"));
    }
}
