package org.acoli.sc.extract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;


/**
 * unchanged
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
	
	/*
	 * NEW @tobias
	 * @return true if successfull
	 */
	public static int extractXML(List<String> pdfFileNames, File dataFolder) throws Exception{
		
		List<String> failed = new ArrayList<String>();
		
		// Skip xml conversion if files are present in result folder
		File tmp = new File(dataFolder, "resultData");
		if (tmp.exists() && tmp.list().length > 0) {
			System.out.println("XML data exists - nothing to do !");
			return getFailedPdfConversions(dataFolder);
		}
		
		String base = dataFolder.getAbsolutePath();
		
        for (String pdf : pdfFileNames) {
        	
        	if (!new File(dataFolder,pdf).exists()) continue; // skip files listed in the info file that do not exist
        	
            String name = pdf.replace(".pdf", "");
            System.out.println(name);
            Runtime rt = Runtime.getRuntime();
            Process conversion = null;
            
            //String base = "/home/demo/Schreibtisch/ide/github-master/SIA-cataloguing/documentation/samples/input-examples/047006471";
            
            System.out.println("pdftohtml -xml -i -c -q -s "+base+"/"+pdf+" "+base+"/resultData/"+name+".xml");
            
            if(SystemUtils.IS_OS_LINUX){ //Operating System (OS)- Erkennung um den Shellcommand entsprechend anzupassen
                conversion = rt.exec("pdftohtml -xml -i -c -q -s "+base+"/"+pdf+" "+base+"/resultData/"+name+".xml");
            }else{
                conversion = rt.exec("wsl \n pdftohtml -xml -i -c -q -s "+base+"/"+pdf+" "+base+"/resultData/"+name+".xml");
                //conversion = rt.exec("wsl \n  pdftohtml -xml -i -c -q -s documentation/samples/input-examples/047006471/" + pdf + " resultData/"+name+".xml");
            }
            try {
                System.out.println("Waiting for conversion to finish..");
                boolean didFinish = conversion.waitFor(10, TimeUnit.SECONDS);
                if (didFinish) {
                    System.out.println("Conversion done.");
                } else {
                    System.out.println("Conversion not done..?");
                    failed.add(name);
                    
                    // Stop process !
                    conversion.destroy();
                    
                    File xmlFile = new File(base+"/resultData/"+name+".xml");
                    if (xmlFile.exists()) {
                	FileUtils.moveFileToDirectory(
                			xmlFile, new File(base+"/resultData/failed"), true);
                    }
                    
                    // delete xml file because it contains invalid XML
                	//File deleteFile = new File(base+"/resultData/"+name+".xml");
                	//deleteFile.delete();
                }
            } catch (InterruptedException e) {
            	e.printStackTrace();
                System.exit(1); // TODO: Check how to handle this properly
            }
        }
        
        System.out.println("failed xml conversions :"+failed.size());
        return getFailedPdfConversions(dataFolder);
    }
	

	private static int getFailedPdfConversions(File dataFolder) {
	
		File failedFolder = new File (new File(dataFolder,"resultData"), "failed");
		if (!failedFolder.exists()) return 0;
		else {
			return failedFolder.listFiles().length;
		}
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
		//String shellCommand = "pdftohtml "+pdf.getAbsolutePath()+" -xml -i -c -q -s "+ xml.getAbsolutePath();
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
