package org.acoli.glaser.metadata.pdf;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class PDF2XML {

	File tmpDir;

	public PDF2XML(String tmpPath) {
		File tmpDir = new File(tmpPath);
		if (!tmpDir.exists()) {
			System.err.println(tmpPath+" does not exist yet, created it.");
			boolean success = tmpDir.mkdir();
			System.err.println("Success: "+success);
		}
		this.tmpDir = tmpDir;
	}


	private boolean deleteTempFolder() {
		return this.tmpDir.delete();
	}
	public void cleanup() {
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter 'ok' to delete temp folder.");
		String ok = reader.next();
		if (ok.equals("ok")) {
			if (this.deleteTempFolder())
				System.err.println("Deleted "+this.tmpDir);
			else
				System.err.println("Failed to delete temp folder.");
		}
		reader.close();
	}

	/**
	 * pdftohtml "$SINGLE_PDF" -xml -i no images -c complex docs -q quiet -s into single file, name as arg tmp/"$SINGLE_PDF".html
	 * @param pdf
	 * @return
	 */
	public File pdfToXml(File pdf) throws IOException {
		String xmlFileName = pdf.getName().replace(".pdf", ".xml");
		File xmlFile = new File (this.tmpDir+"/"+xmlFileName);
		System.err.println("Will convert "+pdf.getAbsolutePath()+", writing to "+ xmlFile.getAbsolutePath());
		Runtime rt = Runtime.getRuntime();
		String shellCommand = "pdftohtml "+pdf.getAbsolutePath()+" -xml -i -c -q -s "+ xmlFile.getAbsolutePath();
		// TODO: This is way too much stuff, the error was somewhere else. You can probably simplify this.
		Process conversion = rt.exec(shellCommand);
		try {
			System.err.println("Waiting for conversion to finish..");
			boolean didFinish = conversion.waitFor(10, TimeUnit.SECONDS);
			if (didFinish) {
				System.err.println("Conversion done.");
			} else {
				System.err.println("Conversion not done..?");
			}
		} catch (InterruptedException e) {
			System.exit(1); // TODO: Check how to handle this properly
		}
		return xmlFile;
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
		System.err.println("Removing DTD in "+xml.getAbsolutePath());
		for(String line = ""; line!=null; line=bin.readLine()) {
			if (line.length() > 1 && !line.startsWith("<!DOCTYPE")) {
				bout.write(line+"\n");
			}
		}
		bout.flush();
		bout.close();

		boolean success = tmpFile.renameTo(xml);
		if (success)
			System.err.println("Done.");
		else
			System.err.println("Couldn't move file.");
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
		System.err.println("Removing DTD in "+xml.getAbsolutePath());
		for(String line = ""; line!=null; line=bin.readLine()) {
			if (line.startsWith("<!DOCTYPE")) {
				return true;
			}
		}
		return false;
	}
	public static void main(String[] argv) throws Exception {
		if (argv.length > 1) {
			PDF2XML pdf2XML = new PDF2XML(argv[0]);
			File pdf = pdf2XML.pdfToXml(new File(argv[1]));
			System.err.println("File has Doctype: "+ pdf2XML.hasDoctypeAnnotation(pdf));
			pdf2XML.removeDtdFromFile(pdf);
			System.err.println("File has Doctype: "+ pdf2XML.hasDoctypeAnnotation(pdf));

		}
	}

}
