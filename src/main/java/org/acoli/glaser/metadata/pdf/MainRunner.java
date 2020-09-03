package org.acoli.glaser.metadata.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Name and reconceptualize this
 * As of now, this is the main entry point that reads configs, parses them, creates a pool of the extractors etc.
 * Probably this should be less monolithic
 */
public class MainRunner {

    List<Source> sources;
    FileHandler fh;
    PageSpider ps; // TODO: does this need to be object var??
    public MainRunner(String pathToSeedCSV) {
        sources = readURLSeed(pathToSeedCSV);
        fh = new FileHandler();
        ps = new PageSpider();
    }

    BufferedReader openURLSeedFile(String path) {
        try {
            return new BufferedReader(new FileReader(new File(path)));
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't open URL seed file.");
        }
        return null;
    }

    List<Source> readURLSeed(String pathToSeedCSV) {
        List<Source> sourcesList = new ArrayList<>();
        BufferedReader in = openURLSeedFile(pathToSeedCSV);
        try {
            for(String line = in.readLine(); line!=null; line=in.readLine()) {
                Source s = Source.sourceFromCSVRow(line);
                if (s.urlAsString.startsWith("http")) {
                    sourcesList.add(s);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed reading urlSeed at source "+sourcesList.size());
        }
        return sourcesList;
    }

    public void run() {
        for (Source source : this.sources) {
            // TODO: Probably this can be done in an asyc way. Parse the urls and have a callback that adds them to a parser pool
            // or sth like that
            System.err.println("Starting "+source+"..");
            Document mainPage = null;
            try {
                mainPage = Jsoup.connect(source.urlAsString).get();
            } catch (IOException e) {
                System.err.println("Couldn't connect to "+source);
            }
            if (mainPage != null) {
                List<URL> hrefs = ps.findHrefsByCSSQuery(mainPage, ".paper_papers > a");
                List<MetadataSourceHandler> initialSources = new ArrayList<>();
                for (URL href : hrefs) {
                    if (href.toString().endsWith("pdf")) {
                        MetadataFromPDF mfp = new MetadataFromPDF(href, true); // TODO: PARAMETERIZE THIS
                        initialSources.add(mfp);
                    } else {
                        MetadataFromHTML mfh = new MetadataFromHTML(href, new FileHandler());
                        initialSources.add(mfh);
                    }
                }
                PageHandler ph = new PageHandler(initialSources);
                ph.run();
            }
        }
    }
}
