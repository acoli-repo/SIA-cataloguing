package org.acoli.sc.mods;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.acoli.sc.mods.classes.bak.Mods;
import org.acoli.sc.mods.classes.bak.ModsCollection;


	/**
	 * Implementation of an XML Parser for mods xml exports<p> 
	 * <p>
	 * Get java classes from an xsd schema representing a mods file by using the xjc tool included
	 * in the java jdk/bin folder. The xsd schema needed by xjc can be generated e.g. with the trang tool <p>
	 * see <a href="http://www.thaiopensource.com/relaxng/trang.html">http://www.thaiopensource.com/relaxng/trang.html</a>
	 * <p>
	 * Example for xml-schema and java class generation.
	 * <p><p>
	 * # XSD schema generation from xml (using the trang tool)<p>
	 * java -jar trang.jar filename.xml filename.xsd
	 * <p><p>
	 * # JAXB class generation (-p target package path, -d target directory)<p>
	 * xjc turcet.xsd -p org.acoli.sc.mods -d path where java classes are written
	 * 
	 * @author frank
	 *
	 */
	
	public class JAXBModsReaderOld {

		// Input path
		private String inputPath = "";
		
		// Root class of xml document
		private ModsCollection root;
		private Marshaller marshaller;
		
		
		/**
		 * Constructor for JAXB reader (which uses default jaxb classes)
		 * @param xmlFilePath Path to Starling XML dictionary
		 */
		public JAXBModsReaderOld(String xmlFilePath) {
			
			this("org.acoli.sc.mods",xmlFilePath);
		}

		
		/**
		 * Constructor for JAXB reader
		 * @param classesFolderPath Path to already generated java classes
		 * @param xmlFilePath Path to Starling XML dictionary
		 */
		public JAXBModsReaderOld (String classesFolderPath, String xmlFilePath) {
				
		try {
				JAXBContext jaxbContext = JAXBContext.newInstance (classesFolderPath);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				marshaller = jaxbContext.createMarshaller();

				
				System.out.println(xmlFilePath);
				// Read xml file into java classes
				
				Reader is = null;
				try {
					is = new InputStreamReader(getClass().getResourceAsStream(xmlFilePath));
				} catch (Exception e) {
					try {
						is = new InputStreamReader(new FileInputStream(xmlFilePath));
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				
				Object o = unmarshaller.unmarshal(is);
				//Object o = unmarshaller.unmarshal(new File(xmlFilePath));
				
				// Root class := root element of xml document
				root = (ModsCollection) o;

				
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		
		
		public List<Mods> getRecords() {
			return root.getMods();
		}

		
		public int getXMLRecordCount() {
			return getRecords().size();
		}

		
		public String getInputPath() {
			return inputPath;
		}
		
		public static void main(String[] args) {
			
			String inputXML = "/mods/048593834.xml";

			JAXBModsReaderOld test = new JAXBModsReaderOld(inputXML);
			System.out.println(test.getXMLRecordCount());
		}

	}

