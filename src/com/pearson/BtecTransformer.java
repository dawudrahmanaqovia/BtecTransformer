package com.pearson;

/**
 * Created by Dawud on 10/03/14.
 */
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BtecTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BtecTransformer.class);

    /**
     * Simple transformation method.
     * @param sourcePath - Absolute path to source xml file.
     * @param xsltPath - Absolute path to xslt file.
     * @param resultDir - Directory where you want to put resulting files.
     */
    public static void simpleTransform(String sourcePath, String xsltPath,
                                       String resultDir) {
        LOGGER.debug("Transforming... simpleTransform -> sourcePath[{}] xsltPath[{}] resultDir[{}]", sourcePath, xsltPath, resultDir  );

        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = tFactory.newTransformer(new StreamSource(new File(xsltPath)));
            transformer.transform(new StreamSource(new File(sourcePath)),
                    new StreamResult(new File(resultDir)));
            //System.out.println("    ... Transformed WordXML -> IQS Xml : Success");
            //LOGGER.info("WordXML file:[{}]", sourcePath);
            //LOGGER.info("IQS XML file saved to folder:[{}]", resultDir);
        } catch (TransformerException e) {
            LOGGER.error("WordXML file:[{}]", sourcePath);
            LOGGER.error("Technical Error message: {}", e.getMessage());
            LOGGER.error("Transformed WordXML -> IQS Xml : FAILED");
        }
    }

    /**
     * @param filesLevel1
     */
    // Unit Drafts/xxxxxxx
    private void processFolderLevel1(File[] filesLevel1) {
        System.out.println("Found Units...");
        for(File fileLevel1 : filesLevel1) {
            if(fileLevel1.isDirectory()) {
                System.out.println("\r\nLevel1 folder[" + fileLevel1.getName() + "]");
                LOGGER.info("");
                LOGGER.info("_________________________________________________________________________");
                LOGGER.info("Unit folder[{}]", fileLevel1.getName());
                processFolderLevel2(fileLevel1.listFiles());
            } else if(fileLevel1.isFile()) {
                // Todo: Process files
            }
        }
    }

    /**
     * @param filesLevel2
     */
    // Unit Drafts/Unit 4 Creating Digital Animations/xxxxxxx
    private void processFolderLevel2(File[] filesLevel2) {
        for(File fileLevel2 : filesLevel2) {
            if(fileLevel2.isDirectory()) {
                System.out.println("+-- Level2 folder[" + fileLevel2.getName() + "]");
                processFile(fileLevel2.listFiles());
            } else if(fileLevel2.isFile()) {
                // Todo: Process files
            }
        }
    }

    /**
     * @param files
     */
    // Unit Drafts/User1 - Paul/A Technology Business/xxxxxxx
    private void processFile(File[] files) {

        for(File file : files) {
            // Only files should exist here
            if (file.isFile() && file.getName().startsWith("Unit") && file.getName().endsWith(".xml")) {
                System.out.println("    +-- WordXML File [" + file.getName() + "]");
                LOGGER.info("WordXML File [" + file.getName() + "]");
                // simpleTransform("input/Unit 4.xml", "xslt/wordxml_unit.xsl", "output");
                simpleTransform(file.getPath(), Config.unit_xslt_wordxml_to_iqs_filename, file.getParent());
                File iqsxmlfile = null;

                try {

                    XPathFactory factory = net.sf.saxon.xpath.XPathFactoryImpl.newInstance();
                    XPath xpath = factory.newXPath();
                    XPathExpression expr = xpath.compile("/specification/unit/uan");
                    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.getPath());
                    String iqsxmlfilename = expr.evaluate(doc);
                    iqsxmlfile = new File(file.getParent() + "\\" + iqsxmlfilename.replaceAll("/", "_") + ".xml");
                    BtecIqsSchemaValidator.validate(iqsxmlfile);

                    simpleTransform(iqsxmlfile.getPath(), Config.unit_xslt_iqs_to_html_filename, iqsxmlfile.getParent());

                } catch (XPathExpressionException e) {
                    Toolkit.getDefaultToolkit().beep();
                    System.out.println("Transformed WordXML -> IQS Xml : FAILED - No UAN number assigned." +
                            "\r\n Please correct Word XML document and re-run tranform tool");
                    LOGGER.info("Transformed WordXML -> IQS Xml : FAILED - ");
                    LOGGER.info("Reason:{} No UAN number assigned.", e.getLocalizedMessage());
                    LOGGER.info("Please correct UAN number in Word XML document and re-run transform tool");
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        //Set saxon as transformer.
        System.setProperty("javax.xml.transform.TransformerFactory","net.sf.saxon.TransformerFactoryImpl");
        String path = Config.unit_folder_path; //"/BtecTransformer/input/Development/Writings/Unit Drafts";

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("BTEC SPECIFICATION Units - Word XML -> IQS XML Transformation Tool");
        System.out.println("******************************************************************");
        LOGGER.info("BTEC SPECIFICATION Units - Word XML -> IQS XML Transformation Tool \r\n\r\n                             STATUS REPORT");
        LOGGER.info("");
        System.out.println("Usage: \r\n" +
                "- Transform BTEC SPECIFICATION Word XML generated from \r\n  BTEC_SPECIFICATION Word template to IQS XML format. \r\n" +
                "- Single Unit or batches of Unit's can be transformed at any one time. \r\n" +
                "- XSL stylesheets configured with this tool will process each Word XML \r\n  and generate IQS XML files for each.\r\n" +
                "- A failure is generated if transformation fails or fail to validate against the IQS Schema\r\n\r\n");

        System.out.println("To get started: ");
        System.out.println("1. Copy Unit's folder with word documents into folder \r\n  [" + path + "]");
        System.out.println("2. Generate Word XML files using the XML tab in the Word Template tool");
        System.out.println("3. Ensure the \"BTEC_SPEC_XML\" folder containing the Word XML file exists for each Unit folder.\r\n");
        System.out.println("Units folder structure should be as follows:- ");
        System.out.println("[" + path + "]");
        System.out.println(" +-- [Unit {unit No#} {unit title}] - This is the Level1 folder for the Unit");
        System.out.println("     +-- [BTEC_SPEC_XML] - This is the Level2 folder for the generated Word XML");
        System.out.println("         +-- [Unit {unit No#} {unit title}.xml] - The generated Word XML.");
        System.out.println("         |   N.B: Filename should start with \"Unit\" and end with \".xml\"");
        System.out.println("         +-- [{uan No#}.xml] - Transformed IQS XML will appear here");
        System.out.println("                               with the UAN No# as the filename.\r\n");
        System.out.println("4. Running this transformation tool will generate IQS XML file with the UAN being the filename.");
        System.out.println("5. Errors during transformation will be recorded in a Word document: \r\n  [" + Config.unit_status_report_filename + "]");
        System.out.println("\r\nPress <ENTER> key to continue......");
        try {           // Directory path here
            String input = in.readLine();
            if(!input.isEmpty()) {
                path = input;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // List files after "Unit Drafts"
        File[] files = new File(path).listFiles();
        System.out.println("Please wait..... \r\nProcessing files in folder tree: [" + path + "].....");

        BtecTransformer btecTransformer = new BtecTransformer();
        btecTransformer.processFolderLevel1(files);
        LOGGER.info("}");


        System.out.println("\r\nPress <ENTER> key to generate Transformation Report [" + Config.unit_status_report_filename + "] ........");
        try{System.in.read();}
        catch(Exception e){}

    }
}