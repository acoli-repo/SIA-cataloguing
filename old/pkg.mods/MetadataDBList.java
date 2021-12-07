package org.acoli.sc.mods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.acoli.sc.extract.Metadata;
import org.acoli.sc.mods.classes.ModsCollection;

public class MetadataDBList {
	
	public static enum dbOperation {GET_NEW, GET_CHANGED, GET_MISSING, COMMIT_NEW, COMMIT_CHANGED, COMMIT_ALL, EXPORT_DB}


	private File metadataDB;
	private final String dbFilename = "metaDB";
	
	
	public MetadataDBList (File metadataDB) {
		this.metadataDB = new File (metadataDB, dbFilename);
	}
	
	/**
	 * Run an operation on the metadata database
	 * @param code What to do
	 * @param mdList Extracted metadata from PDF files
	 * @return List with metadata depending on the operation
	 */
	public List<Metadata> runMetadataDBOperation (dbOperation code, List<Metadata> mdList) {
		
		List<Metadata> database = readMetadataDB();
		List<Metadata> resultMetadata = new ArrayList<Metadata>();		

		
		switch (code) {
		
			case GET_NEW :
			case GET_CHANGED :
			case GET_MISSING :
			
				// read the database and compare it with the metadata in mdList
				// return only metadata records in mdList that do not exist in the database
				return compareMetadataWithDB(mdList, database, code);
		
			case COMMIT_NEW :
				
				// write only new metadata to the database
				database.addAll(compareMetadataWithDB(mdList, database, dbOperation.GET_NEW));
				writeMetadataDB(database);
				break;
				
			case COMMIT_CHANGED :
				
				// update changed metadata in the database
				database.addAll(compareMetadataWithDB(mdList, database, dbOperation.GET_CHANGED));
				writeMetadataDB(database);
				break;
				
			case COMMIT_ALL :
				
				// write metadata in mdList to the database, thereby overwriting already existing records
				database.addAll(mdList);
				writeMetadataDB(database);
				break;
				
			case EXPORT_DB :
				
				// reads the database and exports the metadata to MODS xml
				ModsCollection modsCollection = null;
				Map<String, String> modsRecordIDs2PDFfilenames = null;
				String modsFilePath = null;
				//ModsTools.exportMods(modsCollection, database, modsRecordIDs2PDFfilenames, modsFilePath);
				break;
				
			default:
				break;
		}
		
		return resultMetadata;
	}
	
	
	public void writeMetadataDB(List<Metadata> mdList) {
		
		// remove duplicates from list
		HashMap<String, Metadata> map = new HashMap<String, Metadata>();
		for (Metadata md : mdList) {
			map.put(md.getID(), md);
		}
	 
		try {
			 
	         FileOutputStream fileOut = new FileOutputStream(metadataDB);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(map.values());
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized metadata is saved in "+metadataDB);

		} catch (FileNotFoundException e) {
              e.printStackTrace();
		} catch (IOException e) {
              e.printStackTrace();
       	}
	}
	

	public List<Metadata> readMetadataDB () {
		
		List<Metadata> mdList = new ArrayList<Metadata>();
		ObjectInputStream in = null;
	    try {
	            FileInputStream fileIn = new FileInputStream(metadataDB);
	            in = new ObjectInputStream(fileIn);
	            System.out.println("Serialized metadata is read from "+metadataDB);

	            mdList = (List<Metadata>) in.readObject();
	            System.out.println("Successfully read metadata DB with "+ mdList.size()+" records!");
	            in.close();
	            
	    } catch (FileNotFoundException e) {
	            e.printStackTrace();
	    } catch (IOException e) {
	           e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	           e.printStackTrace();
	    }
	    return mdList;
	}
	
	
	private List<Metadata> compareMetadataWithDB (List<Metadata> mdList, List<Metadata> dbList, dbOperation code) {
		
		HashMap<String, Metadata> inputMap = new HashMap<String, Metadata>();
		HashMap<String, Metadata> dbMap = new HashMap<String, Metadata>();
		
		for (Metadata md : mdList) {
			inputMap.put(md.getID(), md);
		}
		for (Metadata md : dbList) {
			dbMap.put(md.getID(), md);
		}
		
		switch (code) {
		
			case GET_NEW:
				List<Metadata> newMD = new ArrayList<Metadata>();
				for (String id : inputMap.keySet()) {
					if (!dbMap.containsKey(id)) {
						newMD.add(inputMap.get(id));
					}
				}
				return newMD;
			
			case GET_CHANGED:
				
				List<Metadata> changedMD = new ArrayList<Metadata>();
				for (String id : dbMap.keySet()) {
					if (inputMap.containsKey(id)) {
						if (dbMap.get(id).equals(inputMap.get(id))) {
							changedMD.add(inputMap.get(id));
						}
					}
				}
				return changedMD;
			
			case GET_MISSING:
				
				List<Metadata> missingMD = new ArrayList<Metadata>();
				for (String id : dbMap.keySet()) {
					if (!inputMap.containsKey(id)) {
						missingMD.add(dbMap.get(id));
					}
				}
				return missingMD;
			
			default:
				break;
		}
		
		return new ArrayList<Metadata>();
	}
	

	public String getDbFilename() {
		return dbFilename;
	}


}
