package org.acoli.sc.util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class ClusterUtils {
	
	
	/**
	 * 
	 * @param dirPath
	 * @param minClusterNameLength
	 * @param clusterJoinMaxSuffixMissmatch
	 * @param recursions
	 * @param extension
	 * @return
	 */
	public static HashMap<String, List<String>> clusterFilesInDir(
			String dirPath,
			int minClusterNameLength,
			int clusterJoinMaxSuffixMissmatch,
			int recursions,
			String extension) {
		
		System.out.println("Used clustering parameter :");
		System.out.println("Directory : "+dirPath);
		System.out.println("File extension : "+extension);
		System.out.println("MinClusterNameLength : "+minClusterNameLength);
		System.out.println("ClusterJoinMaxSuffixMissmatch : "+clusterJoinMaxSuffixMissmatch);
		System.out.println("Recursions : "+recursions);

		
		List<String> fileNames = new ArrayList<String>();
		fileNames.clear();
		
		ArrayList<String> extensionList = new ArrayList<String>();
		extensionList.add(extension);
		String[] extensions = extensionList.toArray(new String[1]);
		Collection<File> files = FileUtils.listFiles(new File(dirPath), extensions , false);
		for (File x : files) {
			//System.out.println(x.getName());
			fileNames.add(x.getName());
		}
		
		return clusterFilenames(fileNames, minClusterNameLength, clusterJoinMaxSuffixMissmatch, recursions);
	}
	
	
	public static void moveFilesToClusters(HashMap<String, List<String>> clustering, String workingDir) {
		
		
		for (String clusterName : clustering.keySet()) {
			
			if (clusterName.trim().isEmpty() || clustering.get(clusterName).size() == 0) {
				System.out.println("Skipping empty cluster : '"+clusterName+"'");
				continue;
			}
			
			System.out.println("* Creating cluster folder : "+clusterName);
			
			// make subfolder clusterName
			try {
				File workingDirectory = new File(workingDir);
				File subFolder = new File(workingDirectory, clusterName);
				
			    // create subfolder and move files into subfolder
				System.out.println("Moving files:");
				for (String fileName : clustering.get(clusterName)) {
					File sourceFile = new File(workingDirectory, fileName);
					System.out.println(sourceFile.getAbsolutePath());
					FileUtils.moveFileToDirectory(sourceFile, subFolder, true);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		
	}
	
	
	/**
	 * Cluster similar strings, based on a maximal common prefix search
	 * @param fileNames List with filenames
	 * @param minClusterNameLength Minimal length of a maximal common prefix in order to form a cluster
	 * @param clusterJoinMaxSuffixMissmatch Join cluster names that differ at most in k chars at the end
	 * @param recursions Join clusters recursively, use -1 to recurse until cluster size doesn't change anymore
	 * (recursion=0 can yield cluster with size 1!)
	 * @return mapping of prefix->strings that have this prefix
	 */
	public static HashMap<String, List<String>> clusterFilenames (
			List<String> fileNames,
			int minClusterNameLength,
			int clusterJoinMaxSuffixMissmatch,
			int recursions) {

		System.out.println("input files : "+fileNames.size());

		// init cluster
		HashMap<String, List<String>> filenameClustering = cluster(fileNames);
		
		//		for (String y : filenameClustering.keySet()) {
		//			System.out.println("prefix: "+y);
		//			for (String z : filenameClustering.get(y)) {
		//				System.out.println(z);
		//			}
		//			System.out.println();
		//		}
		
		// Recursively group similar clusters (similarity given with function arguments)
		int r = 1;
		int clusterCount=-1;
		int loopCount = 0; // catch endless-loop error
		while(r < recursions+1 || recursions == -1) {
			
			HashMap<String, List<String>> clusternameClustering = cluster(new ArrayList<String>(filenameClustering.keySet()));
			
			for (String y : clusternameClustering.keySet()) {
			System.out.println("prefix "+r+" : "+y);
			for (String z : clusternameClustering.get(y)) {
				System.out.println(z);
			}
			System.out.println();
			}
			
			
			// build result from joined clusters
			for (String newClustername : clusternameClustering.keySet()) {
				
				// join clusters if the following three requirements are met
				// 1, minClusterNameLength
				if (newClustername.length() < minClusterNameLength) {
					//System.out.println("Skipping : "+newClustername );
					continue;
				}
				
				// 2, cluster size
				if (clusternameClustering.get(newClustername).size() < 2) continue;
				
				// 3, similarity of cluster names
				int maxDeviation=-1;
				for (String y : clusternameClustering.get(newClustername)) {
					int dev = y.length() - newClustername.length();
					if (dev > maxDeviation) maxDeviation = dev;
				}
				System.out.println("maxDeviation "+maxDeviation);
				if (maxDeviation > clusterJoinMaxSuffixMissmatch) continue;
				
				// join clusters
				List<String> joined = new ArrayList<String>();
				for (String z : clusternameClustering.get(newClustername)) {
					System.out.println("join "+ z);
					joined.addAll(filenameClustering.get(z));
					filenameClustering.remove(z); // delete filename cluster
				}
				filenameClustering.put(newClustername, joined); // add joined filename cluster
				
			}
			
			// finally join special clusters into one cluster
			// members are ::
			// clusters with name length < minClusterNameLength
			// clusters that have only one member
			List<String> noClusters = new ArrayList<String>();
	
			List<String> clusterNames = new ArrayList<String>(filenameClustering.keySet());
			for (String clusterName  : clusterNames) {
				if (clusterName.length() < minClusterNameLength ||
						filenameClustering.get(clusterName).size() < 2) {
					noClusters.addAll(filenameClustering.get(clusterName));
					filenameClustering.remove(clusterName);
				}
			}
			
			// add special cluster
			filenameClustering.put("", noClusters);
			
			if (clusterCount == filenameClustering.size() && recursions == -1) {
				break;
			}
			
			clusterCount=filenameClustering.size();
			r++;
			
			if (loopCount++ > 1000) break; // catch endless loop error
			
		}
		
		
		return filenameClustering;
	}
	
	
	/**
	 * Cluster files by name.
	 * @param fileNames
	 * @return Map with clusters of similar named files
	 */
	private static HashMap<String, List<String>> cluster (List<String> fileNames) {
		
		HashMap<String, List<String>> clusteredNames = new HashMap<String, List<String>>();
		HashMap<String, String> maxPrefix = new HashMap<String, String>();

		// init
		//System.out.println("names 2b clustered :");
		for (String name : fileNames) {
		//	System.out.println(name);
			maxPrefix.put(name, "");
		}
		//System.out.println();
		
		// Get max common prefix for each filename
		String lcp="";
		String maxLcp="";
		for (String name : fileNames) {
			
			maxLcp="";
			for (String clusterName : maxPrefix.keySet()) {
				
				// skip identity a=a
				if (clusterName.equals(name)) continue;
				
				// check
				lcp = longestCommonPrefix(clusterName, name);
				if (lcp.length() > maxLcp.length()) {
					maxLcp=lcp;
				}
				
			}
			
			maxPrefix.put(name, maxLcp); 
		}
		
				
		//Cluster filenames by max common prefix
		for (String name : maxPrefix.keySet()) {
				
			maxLcp = maxPrefix.get(name);
			
			if (!clusteredNames.containsKey(maxLcp)) {
				ArrayList<String> x = new ArrayList<String>();
				x.add(name);
				clusteredNames.put(maxLcp, x);
			} else {
				List<String> x = clusteredNames.get(maxLcp);
				x.add(name);
				clusteredNames.put(maxLcp, x);
			}
		}
		
		return clusteredNames;
	}
	
	
	/**
	 * Get longest common prefix
	 * @param a
	 * @param b
	 * @return
	 */
	public static String longestCommonPrefix(String a, String b) {
		
		String lcp = "";
		int al = a.length();
		int bl = b.length();
		int i = 0;
		while (i < al && i < bl) {
			if (!a.substring(i, i+1).endsWith(b.substring(i, i+1))) {
				break;
			} else {
				lcp+=a.substring(i, i+1);
			}
			i++;
		}
		
		return lcp;
	}
	
	
	public static void main(String[] args) {
		
		List<String> fileNames = new ArrayList<String>();
		fileNames.add("fel-2018-00-pdf.pdf");
		fileNames.add("ldd01-02-pdf.pdf");
		fileNames.add("lls-chapter-03-koch-obata-pdf.pdf");
		fileNames.add("fel-2018-10-pdf.pdf");
		fileNames.add("ldd01-03-pdf.pdf");
		fileNames.add("lls-chapter-15-koch-obata-pdf.pdf");
		fileNames.add("lls-chapter-05-koch-obata-pdf.pdf");
		fileNames.add("ll878");
		fileNames.add("ldd-chzuzu");
		fileNames.add("hzuzu");
		fileNames.add("qwli7ro");
		fileNames.add("9875eskyx");



//		HashMap<String, List<String>> result = cluster(fileNames);
//		System.out.println("clustering 1");
//
//		for (String y : result.keySet()) {
//			System.out.println("prefix: "+y);
//			for (String z : result.get(y)) {
//				System.out.println(z);
//			}
//			System.out.println();
//		}
//		
//		HashMap<String, List<String>> result2 = cluster(new ArrayList<String>(result.keySet()));
//		System.out.println("clustering 2");
//		for (String y : result2.keySet()) {
//			System.out.println("prefix: "+y);
//			for (String z : result2.get(y)) {
//				System.out.println(z);
//			}
//			System.out.println();
//		}
		
		String dir = "/run/media/demo/bb92bf31-b049-4d65-8e71-5a6a0cb44d49/fdata/048134759";
		//dir = "/run/media/demo/bb92bf31-b049-4d65-8e71-5a6a0cb44d49/fdata/047505494";
		//dir = "/run/media/demo/bb92bf31-b049-4d65-8e71-5a6a0cb44d49/fdata/047502614";
		
		HashMap<String, List<String>> yy = clusterFilesInDir(dir, 3, 6, -1, "pdf");
		
		

		fileNames.clear();
		String[] extensions = {"pdf"};
		Collection<File> files = FileUtils.listFiles(new File(dir), extensions , false);
		for (File x : files) {
			//System.out.println(x.getName());
			fileNames.add(x.getName());
		}
		
		
		
		//HashMap<String, List<String>> yy = clusterFilenames(fileNames, 3, 6, -1);
		int fileCount=0;
		for (String y : yy.keySet()) {
			System.out.println("cluster: "+y);
			fileCount+=yy.get(y).size();
			for (String z : yy.get(y)) {
				System.out.println(z);
			}
			System.out.println();
		}
		
		System.out.println("clusters : "+yy.keySet().size());
		System.out.println("files : "+fileCount);

	}


	public static void printClusters(HashMap<String, List<String>> map) {
		
		System.out.println("==========================================================================================");
		System.out.println();
		System.out.println("Clustering result :");
		
		int fileCount=0;
		int clusterCount=1;
		for (String y : map.keySet()) {
			System.out.println("cluster "+clusterCount+++" : "+y);
			fileCount+=map.get(y).size();
			for (String z : map.get(y)) {
				System.out.println(z);
			}
			System.out.println();
		}
		
		System.out.println("clusters : "+map.keySet().size());
		System.out.println("files : "+fileCount);
		
	}

}


