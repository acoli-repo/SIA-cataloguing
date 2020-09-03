package org.acoli.glaser.metadata.pdf;

import org.jbibtex.*;

import java.io.StringReader;
import java.util.*;

public class MetadataFromBibtex extends MetadataSourceHandler {
    String bibtex;
    boolean hasFailed = false;
    boolean doneParsing = false;
    List<Metadata> partialMetadata = new ArrayList<>();

    public MetadataFromBibtex(String bibtex){
        this.bibtex = bibtex;
    }
    @Override
    public void run() {

        BibTeXParser bibtexParser = null;
        try {
            bibtexParser = new BibTeXParser();
        } catch (ParseException e) {
            hasFailed = true;
            this.doneParsing = true;
            return;
        }

        org.jbibtex.BibTeXDatabase database = null;
        try {
            database = bibtexParser.parse(new StringReader(bibtex));
        } catch (ParseException e) {
            hasFailed = true;
            this.doneParsing = true;
            return;
        }

        Map<Key, BibTeXEntry> entryMap = database.getEntries();

        Collection<BibTeXEntry> entries = entryMap.values();
        for(BibTeXEntry entry : entries) {
            Metadata md = new Metadata();
            md.title = entry.getField(BibTeXEntry.KEY_TITLE).toUserString();
            md.authors = new ArrayList<>(Arrays.asList(entry.getField(BibTeXEntry.KEY_AUTHOR).toUserString().split(","))); // TODO: properly parse this
            if (entry.getField(BibTeXEntry.KEY_ADDRESS) != null) {
                md.location = entry.getField(BibTeXEntry.KEY_ADDRESS).toUserString();
            }
            if (entry.getField(BibTeXEntry.KEY_BOOKTITLE) != null) {
                md.booktitle = entry.getField(BibTeXEntry.KEY_BOOKTITLE).toUserString();
            }
            if (entry.getField(BibTeXEntry.KEY_PAGES) != null){
                String[] pages = entry.getField(BibTeXEntry.KEY_PAGES).toUserString().split("-"); // TODO: --?
                md.beginPage = Integer.parseInt(pages[0]);
                md.endPage = Integer.parseInt(pages[1]); // TODO: Take care of nulls
            }
            if (entry.getField(BibTeXEntry.KEY_JOURNAL) != null)
                md.journalTitle = entry.getField(BibTeXEntry.KEY_JOURNAL).toUserString();
            if (entry.getField(BibTeXEntry.KEY_YEAR) != null)
                md.year = Integer.parseInt(entry.getField(BibTeXEntry.KEY_YEAR).toUserString());
            System.err.println("Created "+md);
            this.partialMetadata.add(md);
        }
        this.doneParsing = true;
    }

    @Override
    public List<Metadata> getMetadata() {
        return this.partialMetadata;
    }

    @Override
    public boolean finished() {
        return this.doneParsing;
    }

    @Override
    public boolean success() {
        return this.doneParsing && ! this.hasFailed;
    }

    @Override
    public boolean foundOtherSourcesThatRequireHandling() {
        return false;
    }

    @Override
    public List<MetadataSourceHandler> getHandlersForOtherSources() {
        return null;
    }
}
