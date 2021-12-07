package org.acoli.sc.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.acoli.sc.extract.Metadata;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.Key;
import org.jbibtex.KeyValue;

/**
 * Serialize metadata to bibtex format
 * @author demo
 *
 */
public class BibtexTools {
	
	public static void export(List<Metadata> records, File bibtexFile) {
		
		BibTeXDatabase db = new BibTeXDatabase();
		
		int dbID = 1;
		for (Metadata record : records) {
			
			// 1. create bibtex entry for record
			BibTeXEntry e = new BibTeXEntry(BibTeXEntry.TYPE_ARTICLE, new Key(Integer.toString(dbID++)));
			if (record.titleIsActive())
				e.addField(BibTeXEntry.KEY_TITLE, valueFor(record.getTitle()));
			if (record.authorsIsActive())
				e.addField(BibTeXEntry.KEY_AUTHOR, valueFor(String.join("", record.getAuthors())));
			if (record.beginPageIsActive() && record.endPageIsActive())
				e.addField(BibTeXEntry.KEY_PAGES, valueFor(record.getPages()));
			if (record.urlIsActive())
				e.addField(BibTeXEntry.KEY_URL, valueFor(record.getUrl()));
			if (record.journalTitleIsActive())
				e.addField(BibTeXEntry.KEY_JOURNAL, valueFor(record.getJournalTitle()));
			if (record.yearIsActive())
				e.addField(BibTeXEntry.KEY_YEAR, valueFor(Integer.toString(record.getYear())));

			
			// 2. add entry to database
			db.addObject(e);
		}
		
		// 3. write database contents
		try {
		  Writer writer = new FileWriter(bibtexFile);
		  BibTeXFormatter bibtexFormatter = new BibTeXFormatter();
		  bibtexFormatter.format(db, writer);
		} catch (IOException e) {
		  e.printStackTrace();
		}
		 
	}
	
	public static KeyValue valueFor(String string) {
		return new KeyValue("{"+string+"}");
	}

}
