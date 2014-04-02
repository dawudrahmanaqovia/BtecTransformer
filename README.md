BtecTransformer
===============

Pearson BTEC Specification's WordXML to IQS transformation Java Command line tool

Here is the link to the Word XML workflow:

https://docs.google.com/a/pearson.com/presentation/d/10kaHYcBd2NWLzjWzvxy-bJr7yFbkicaoBTuZojCeZ6I/edit?usp=sharing

Link to the User guide: https://docs.google.com/a/pearson.com/document/d/192usNI5A6XZ8uB1NN342-zzok1NoQ0dOhFQfb3mLRjA/edit?usp=sharing

Link to the BTEC Specification Word Template to markup and output XML: https://drive.google.com/a/pearson.com/file/d/0BxwRRFulM4fkMm43bTA1UHB5UWc/edit?usp=sharing

Btec Specifications Transformer Tool App: https://drive.google.com/a/pearson.com/folderview?id=0BzQd1mB9QsMDRnNhaVlmV1lfVE0&usp=sharing

There are two folders: BtecTransformer contains the Java based app with an executable shortcut to run the transformation process. Development - contains your BTEC Unit Word documents and Word XML content. Would you be able to copy these to the network area \ukedxfilsrv04\Assessment so that we can prepare it for a demo. source code - you will find the Java source code in src folder. You can view in any text editor. I've commented code so it should be easy to follow. https://drive.google.com/a/pearson.com/#folders/0BzQd1mB9QsMDM3JGS0lzWlRFeFk

Transform BTEC SPECIFICATION Word XML generated from BTEC_SPECIFICATION Word template to IQS XML format.
Single Unit or batches of Unit's can be transformed at any one time.
XSL stylesheets configured with this tool will process each Word XML and generate IQS XML files for each.
XSD schema configured with this tool will validate against the IQS schema and report errors into a word document called btec_transformation_status_report
The HTML5 output is generated from the IQS XML Document once validation has been successful.
A failure is generated if transformation fails or fail to validate against the IQS Schema
The BtecTransformer tool will work locally on your machine. Makesure you the two folders in the same parent folder.

https://drive.google.com/a/pearson.com/folderview?id=0BzQd1mB9QsMDRnNhaVlmV1lfVE0&usp=sharing

Take Unit 18 for example.

https://drive.google.com/a/pearson.com/folderview?id=0BzQd1mB9QsMDMU45RDVvLWQyOTA&usp=sharing

Unit 18.xml is the Word XML output which looks very much like HTML.

H_504_5533.xml is the transformed IQS XML.

H_504_5533.html is the transformed HTML view of IQS.
