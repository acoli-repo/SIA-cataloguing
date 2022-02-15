package org.acoli.sc.mods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acoli.sc.extract.Metadata;
import org.acoli.sc.mods.classes.Mods;
import org.acoli.sc.mods.classes.ModsCollection;
import org.apache.commons.io.FileUtils;

public class MetadataDB {
	
	public static enum dbOperation {GET_NEW, GET_CHANGED, GET_MISSING, GET_EQUAL, GET_STATS, COMMIT_NEW, COMMIT_CHANGED, COMMIT_ALL, EXPORT_DB}


	private File metadataDB;
	private File modsExport;
	private final String dbFilename = "metaDB.ser";
	private final String modsexportFilename = "modsExport.mods.xml";
	
	
	public MetadataDB (File metadataDB) {
		this.metadataDB = new File (metadataDB, dbFilename);
		this.modsExport = new File (this.metadataDB.getParent(), modsexportFilename);
	}
	
	/**
	 * Run an operation on the metadata database
	 * @param code What to do
	 * @param mdList Extracted metadata from PDF files
	 * @param allMods 
	 * @return List with metadata depending on the operation
	 */
	public HashMap<String, List<Metadata>> runMetadataDBOperation (
						dbOperation code,
						HashMap<String, List<Metadata>> mdList,
						HashMap<String, Mods> allMods) {
		
		HashMap<String, List<Metadata>> database = readMetadataDB();

		
		switch (code) {
		
			case GET_NEW :
			case GET_CHANGED :
			case GET_MISSING :
			case GET_EQUAL :
			
				// read the database and compare it with the metadata in mdList
				// return only metadata records in mdList that do not exist in the database
				return compareMetadataWithDB(mdList, database, code);
		
			case COMMIT_NEW :
				
				// write only new metadata to the database
				HashMap<String, List<Metadata>> newMetadata = compareMetadataWithDB(mdList, database, dbOperation.GET_NEW);
				if (!newMetadata.isEmpty()) {
					database.putAll(newMetadata);
					writeMetadataDB(database, "COMMIT_NEW : Comitting "+newMetadata.size()+" newly detected metadata objects to the database !");
				} else {
					System.out.println("COMMIT_NEW : No new metadata objects detected - database unchanged !");
				}
				break;
				
			case COMMIT_CHANGED :
				
				// update changed metadata in the database
				HashMap<String, List<Metadata>> changedMetadata = compareMetadataWithDB(mdList, database, dbOperation.GET_CHANGED);
				if (!changedMetadata.isEmpty()) {
					database.putAll(changedMetadata);
					writeMetadataDB(database, "COMMIT_CHANGED : Comitting "+changedMetadata.size()+" altered metadata objects to the database !");
				} else {
					System.out.println("COMMIT_CHANGED : No altered metadata objects detected - database unchanged !");
				}
				break;
				
			case COMMIT_ALL :
				
				// write metadata in mdList to the database, thereby overwriting already existing records
				if (!mdList.isEmpty()) {
					database.putAll(mdList);
					writeMetadataDB(database, "COMMIT_ALL : Comitting all processed metadata objects ("+mdList.size()+") to the database !");
				} else {
					System.out.println("COMMIT_ALL : No metadata objects to write - database unchanged !");
				}
				break;
				
			case EXPORT_DB :
				
				// reads the database and exports the metadata to MODS xml
				ModsCollection modsCollection = new ModsCollection();
				modsCollection.getMods().addAll(allMods.values());
				// modsExport.getAbsolutePath()
				ModsTools.exportMods(modsCollection, database, modsExport.getAbsolutePath());
				break;
				
			case GET_STATS :
				
				if (mdList.isEmpty()) {
					System.out.println("*****************************************************************");
					System.out.println("Input list is empty !");
					System.out.println("Showing database statistic :");
					System.out.println("MODS records in database : "+database.keySet().size());
					int mdObjects = 0;
					for (String key : database.keySet()) {
						mdObjects+=database.get(key).size();
					}
					System.out.println("Metadata objects in database : "+mdObjects);
					return null;
				}
				
				HashSet<String> notInInput = new HashSet<String>();
				HashSet<String> notInDB = new HashSet<String>();
				
				
				
				
				HashMap<String, List<Metadata>> newMD = compareMetadataWithDB(mdList, database, dbOperation.GET_NEW);
				HashMap<String, List<Metadata>> equalMD = compareMetadataWithDB(mdList, database, dbOperation.GET_EQUAL);
				HashMap<String, List<Metadata>> changedMD = compareMetadataWithDB(mdList, database, dbOperation.GET_CHANGED);
				HashMap<String, List<Metadata>> missingMD = compareMetadataWithDB(mdList, database, dbOperation.GET_MISSING);
				
				int newMDObjects = 0;
				for (String key : newMD.keySet()) {
					newMDObjects+=newMD.get(key).size();
				}
				int equalMDObjects = 0;
				for (String key : equalMD.keySet()) {
					equalMDObjects+=equalMD.get(key).size();
				}
				int changedMDObjects = 0;
				for (String key : changedMD.keySet()) {
					changedMDObjects+=changedMD.get(key).size();
				}
				int missingMDObjects = 0;
				for (String key : missingMD.keySet()) {
					missingMDObjects+=missingMD.get(key).size();
				}
				int inputMDObjects = 0;
				for (String key : mdList.keySet()) {
					inputMDObjects+=mdList.get(key).size();
				}

				System.out.println("\n\n*****************************************************************");
				System.out.println("Processing results for input:");
				System.out.println("MODS IDs:");
				System.out.println(mdList.keySet().size());
				System.out.println("Metadata objects:");
				System.out.println(inputMDObjects);
				System.out.println("new IDs:");
				System.out.println(newMD.keySet().size());
				System.out.println("new objects:");
				System.out.println(newMDObjects);
				System.out.println("equal IDs:");
				System.out.println(equalMD.keySet().size());
				System.out.println("equal objects:");
				System.out.println(equalMDObjects);
				System.out.println("changed IDs:");
				System.out.println(changedMD.keySet().size());
				System.out.println("changed objects:");
				System.out.println(changedMDObjects);
				System.out.println("missing IDs:");
				System.out.println(missingMD.keySet().size());
				System.out.println("missing objects:");
				System.out.println(missingMDObjects);
				System.out.println();
				
				HashSet<String> allKeys = new HashSet<String>(database.keySet());
				HashSet<String> inputKeys = new HashSet<String>(mdList.keySet());
				allKeys.addAll(inputKeys);
				for (String key : allKeys) {
					if (!mdList.keySet().contains(key)) {
						notInInput.add(key);
					}
					if (!database.keySet().contains(key)) {
						notInDB.add(key);
					}
				}
				
				if (notInInput.isEmpty() && notInDB.isEmpty()) {
					System.out.println("Input and database contain the same MODS record IDs");
				} else {
					
					if (database.keySet() != mdList.keySet()) {
						System.out.println("Different record IDs : database-> "+database.size()+", input->"+mdList.size());
					}
					if(!notInDB.isEmpty()) {
						System.out.println("The following "+notInDB.size()+" record IDs are new (not in db)");
						for (String key : notInDB) {
							System.out.print(key+";");
						}
					}
					if(!notInInput.isEmpty()) {
						System.out.println("The following "+notInInput.size()+" record IDs are missing (not in input)");
						for (String key : notInInput) {
							System.out.print(key+";");
						}
					}
				}
				
				break;
				
			default:
				break;
		}
		
		return null;
	}
	
	
	public void writeMetadataDB(HashMap <String, List<Metadata>> map, String message) {
		
		try {
			 
	         FileOutputStream fileOut = new FileOutputStream(metadataDB);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(map);
	         out.close();
	         fileOut.close();
	         System.out.println(message);
	         System.out.println("Serialized metadata is saved in "+metadataDB);

		} catch (FileNotFoundException e) {
              e.printStackTrace();
		} catch (IOException e) {
              e.printStackTrace();
       	}
	}
	

	public HashMap<String, List<Metadata>> readMetadataDB () {
		
		HashMap<String, List<Metadata>> mdList = new HashMap <String, List<Metadata>>();
		
		if(!metadataDB.exists()) {
			try {
				FileUtils.touch(metadataDB);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return mdList;
		}
		
		
		ObjectInputStream in = null;
	    try {
	            FileInputStream fileIn = new FileInputStream(metadataDB);
	            in = new ObjectInputStream(fileIn);
	            System.out.println("Serialized metadata is read from "+metadataDB);

	            mdList = (HashMap<String, List<Metadata>>) in.readObject();
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
	
	
	private HashMap<String, List<Metadata>> compareMetadataWithDB (
			HashMap<String, List<Metadata>> inputMap,
			HashMap<String, List<Metadata>> dbMap,
			dbOperation code) {
		
		
		switch (code) {
		
			case GET_NEW:
				
				HashMap<String, List<Metadata>> newMD = new HashMap <String, List<Metadata>>();
				for (String id : inputMap.keySet()) {
					if (!dbMap.containsKey(id)) {
						newMD.put(id, inputMap.get(id));
					}
				}
				System.out.println("Detected "+newMD.size()+" new metadata objects");
				return newMD;
			
			case GET_CHANGED:
				
				HashMap<String, List<Metadata>> changedMD = new HashMap <String, List<Metadata>>();
				for (String id : dbMap.keySet()) {
					if (inputMap.containsKey(id)) {
						if (!new HashSet<Metadata> (dbMap.get(id)).equals(new HashSet<Metadata>(inputMap.get(id)))) {
							changedMD.put(id, inputMap.get(id));
							printMetadataDiff(dbMap.get(id), inputMap.get(id), id);
						}
					}
				}
				System.out.println("Detected "+changedMD.size()+" altered metadata objects");
				return changedMD;
			
			case GET_MISSING:
				
				HashMap<String, List<Metadata>> missingMD = new HashMap <String, List<Metadata>>();
				for (String id : dbMap.keySet()) {
					if (!inputMap.containsKey(id)) {
						missingMD.put(id, dbMap.get(id));
					}
				}
				System.out.println("Detected "+missingMD.size()+" missing metadata objects");
				return missingMD;
				
			case GET_EQUAL:
				
				HashMap<String, List<Metadata>> equalMD = new HashMap <String, List<Metadata>>();
				for (String id : dbMap.keySet()) {
					if (inputMap.keySet().contains(id)) {
						if (new HashSet<Metadata> (dbMap.get(id)).equals(new HashSet<Metadata>(inputMap.get(id)))) {
							equalMD.put(id, inputMap.get(id));
						}
					}
				}
				return equalMD;
			
			default:
				break;
		}
		
		return new HashMap <String, List<Metadata>>();
	}
	

	public String getDbFilename() {
		return dbFilename;
	}
	
	
	public static void printMetadataDiff(List<Metadata> mdOld, List<Metadata> mdNew, String id) {
		
		System.out.println("old version: ("+mdOld.size()+")");
		int counter = 1;
		for (Metadata md : mdOld) {
			System.out.println("old "+counter+++")");
			System.out.println("ID : "+id);
			System.out.println(md);
		}
		
		System.out.println("new version: ("+mdNew.size()+")");
		counter = 1;
		for (Metadata md : mdNew) {
			System.out.println("new "+counter+++")");
			System.out.println("ID : "+id);
			System.out.println(md);
		}
			
		
	}


}
