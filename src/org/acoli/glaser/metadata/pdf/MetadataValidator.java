package org.acoli.glaser.metadata.pdf;

import java.util.*;

public class MetadataValidator {

	static boolean validate(Metadata md) {
		return true;
	}

	static boolean isFullyPopulated(Metadata md) {
		return md.endPage != 0
				&& md.beginPage != 0
				&& md.title != null && md.title.length() > 0
				&& md.authors != null && md.authors.size() > 0;
	}

	/**
	 * Will this even make sense?
	 *
	 * @param md
	 * @return
	 */
	static Metadata repair(Metadata md) {
		if (validate(md)) {
			return md;
		} else {
			return null;
		}
	}

	/**
	 * Finds page number gaps for a given set of Metadata. This can be used as a hint for either mistakes in the page
	 * numbers or for missing documents in a given data set.
	 * @param mds
	 * @return
	 */
	static List<Integer> findMissingPageNumbers(Set<Metadata> mds) {
		List<Integer> coveredPageNumbers = new ArrayList<>();
		for (Metadata md : mds) {
			for (int i = md.beginPage; i < md.endPage + 1; i++) {
				coveredPageNumbers.add(i);
			}
		}
		Collections.sort(coveredPageNumbers);
		List<Integer> missingPageNumbers = new ArrayList<>();

		for (int i = 0; i < coveredPageNumbers.size() - 1; i++) {
			if (coveredPageNumbers.get(i) < coveredPageNumbers.get(i + 1) - 1) {
				for (int j = coveredPageNumbers.get(i) + 1; j < coveredPageNumbers.get(i + 1); j++) {
					missingPageNumbers.add(j);
				}
			}
		}
		System.err.println("Found " + missingPageNumbers.size() + " missing pages.");
		return missingPageNumbers;
	}

	public static void main(String[] argv) throws Exception {
		Metadata md1 = new Metadata();
		md1.beginPage = 1;
		md1.endPage = 3;
		Metadata md2 = new Metadata();
		md2.beginPage = 6;
		md2.endPage = 7;
		Metadata md3 = new Metadata();
		md3.beginPage = 8;
		md3.endPage = 10;

		Set<Metadata> testSet = new HashSet<>();
		testSet.add(md1);
		testSet.add(md2);
		testSet.add(md3);

		System.err.println(findMissingPageNumbers(testSet));
	}
}
