package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.units.util.Metadata;

import java.util.*;

public class MetadataValidator {

	static boolean validate(Metadata md) {
		// Should probably be a bit cleverer.
		return true;
	}

	static boolean isFullyPopulated(Metadata md) {
		return md.endPage != 0
				&& md.beginPage != 0
				&& md.title != null && md.title.length() > 0
				&& md.authors != null && md.authors.size() > 0;
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

}
