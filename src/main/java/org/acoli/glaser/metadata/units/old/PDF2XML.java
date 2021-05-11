package org.acoli.glaser.metadata.units.old;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


/**
 * Container class for functions that transform .pdf files to .xml files. Basically a wrapper around poppler/pdf2html.
 * Also contains helper functions that deal with useless DTD annotations in the resulting xml files.
 */
public class PDF2XML {

	File tmpDir;
	private static Logger LOG = Logger.getLogger(PDF2XML.class.getName());

	public PDF2XML(String tmpPath) {
		File tmpDir = new File(tmpPath);
		if (!tmpDir.exists()) {
			LOG.info(tmpPath+" does not exist yet, created it.");
			boolean success = tmpDir.mkdir();
			if (success)
				LOG.info("Successfully created "+tmpDir.getAbsolutePath());
		}
		this.tmpDir = tmpDir;
	}

	String makeFileNameForXML(String nameOfPDF) {
		if (nameOfPDF.contains(".pdf")) {
			return nameOfPDF.replace(".pdf", ".xml");
		} else {
			nameOfPDF += ".xml";
			return nameOfPDF;
		}
	}
	/**
	 * pdftohtml "$SINGLE_PDF" -xml -i no images -c complex docs -q quiet -s into single file, name as arg tmp/"$SINGLE_PDF".html
	 * @param pdf
	 * @return
	 */

	public File pdfToXml(File pdf, File xml) throws IOException {
		LOG.info("Will convert "+pdf.getAbsolutePath()+", writing to "+ xml.getAbsolutePath());
		Runtime rt = Runtime.getRuntime();
		String shellCommand = "wsl \n  pdftohtml "+pdf.getAbsolutePath()+" -xml -i -c -q -s "+ xml.getAbsolutePath();
		Process conversion = rt.exec(shellCommand);
		try {
			LOG.info("Waiting for conversion to finish..");
			boolean didFinish = conversion.waitFor(10, TimeUnit.SECONDS);
			if (didFinish) {
				LOG.info("Conversion done.");
			} else {
				LOG.info("Conversion not done..?");
			}
		} catch (InterruptedException e) {
			return null;
		}
		return xml;
	}

	public File pdfToXml(File pdf) throws IOException {
		String xmlFileName = makeFileNameForXML(pdf.getName());
		File xmlFile = new File (this.tmpDir+"/"+xmlFileName);
		return pdfToXml(pdf, xmlFile);
	}

	/**
	 * Removes all '<!DOCTYPE ...>' annotations from an XML file.
	 * roughly equivalent to: grep -v DOCTYPE tmp/"$SINGLE_PDF".html.xml > tmp/"$SINGLE_PDF"-nodtd.html.xml
	 * @param xml the XML file in which to remove all '<!DOCTYPE ...>' annotations.
	 * @return True, if the original file has been replaced with a version without '<!DOCTYPE ...>'
	 * @throws IOException e.g. when file is missing.
	 */
	public boolean removeDtdFromFile(File xml) throws IOException {
		File tmpFile = new File(xml.getAbsolutePath()+".tmp");
		BufferedReader bin = new BufferedReader(new FileReader(xml));
		BufferedWriter bout = new BufferedWriter(new FileWriter(tmpFile));
		LOG.info("Removing DTD in "+xml.getAbsolutePath());
		for(String line = ""; line!=null; line=bin.readLine()) {
			if (line.length() > 1 && !line.startsWith("<!DOCTYPE")) {
				bout.write(line+"\n");
			}
		}
		bout.flush();
		bout.close();

		boolean success = tmpFile.renameTo(xml);
		if (success)
			LOG.info("Done.");
		else
			LOG.warning("Couldn't move file.");
		return success;
	}

	/**
	 * Simple test if an XML file has a '<!DOCTYPE ...>' annotation.
	 * @param xml the XML file in which to check for a '<!DOCTYPE ...>' annotation.
	 * @return if it contains the '<!DOCTYPE ...>' annotation or not.
	 * @throws IOException e.g. non-existent file.
	 */
	public boolean hasDoctypeAnnotation(File xml) throws IOException {
		BufferedReader bin = new BufferedReader(new FileReader(xml));
		for(String line = ""; line!=null; line=bin.readLine()) {
			if (line.startsWith("<!DOCTYPE")) {
				return true;
			}
		}
		return false;
	}

}
