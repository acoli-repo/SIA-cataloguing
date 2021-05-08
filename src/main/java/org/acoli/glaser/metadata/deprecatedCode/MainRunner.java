package org.acoli.glaser.metadata.deprecatedCode;

import com.google.gson.Gson;
import org.acoli.glaser.metadata.units.util.Config;
import org.acoli.glaser.metadata.units.extract.MetadataFromPDF;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * As of now, this is the main entry point that reads configs, parses them, creates a pool of the extractors etc.
 * Probably this should be less monolithic
 */
public class MainRunner {

    List<SourceDescriptions> sourceDescriptions;
    FileHandler fh;
    PageSpider ps;
    private static Logger LOG = Logger.getLogger(MainRunner.class.getName());
    public MainRunner(String pathToConfigJSON) {
        fh = new FileHandler();
        ps = new PageSpider();
        sourceDescriptions = readConfigs(pathToConfigJSON).sources;
    }


    static String readConfigJSONToString(String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        } catch (IOException e) {
            LOG.info("Couldn't read JSON config file.");
        }
        return null;
    }
    static public Config readConfigs(String pathToConfigFile) {
        Gson gson = new Gson();
        String json = readConfigJSONToString(pathToConfigFile);
        Config config = gson.fromJson(json, Config.class);
        return config;
    }

    public void run() {

        List<MetadataSourceHandler> initialSources = new ArrayList<>();
        for (SourceDescriptions source : this.sourceDescriptions) {
            // TODO: Probably this can be done in an asyc way. Parse the urls and have a callback that adds them to a parser pool
            LOG.info("Starting "+source+"..");
            if (source.type == SourceTypes.html) {
                Document mainPage = null;
                try {
                    mainPage = Jsoup.connect(source.url.toString()).get();
                } catch (IOException e) {
                    LOG.warning("Couldn't connect to " + source);
                }
                if (mainPage != null) {
                    List<URL> hrefs = ps.findHrefsByCSSQuery(mainPage, ".paper_papers > a");
                    for (URL href : hrefs) {
                        if (href.toString().endsWith("pdf")) {
                            MetadataFromPDF mfp = new MetadataFromPDF(href, source.split);
                            //initialSources.add(mfp);
                        } else {
                            MetadataFromHTML mfh = new MetadataFromHTML(href, new FileHandler());
                            initialSources.add(mfh);
                        }
                    }
                }
            } else if (source.type == SourceTypes.pdf) {
                MetadataFromPDF mfp = new MetadataFromPDF(source.url, source.split);
                //initialSources.add(mfp);
            }
        }
        PageHandler ph = new PageHandler(initialSources);
        ph.run();
    }

    public static void main(String[] args) {
        MainRunner mr = new MainRunner("configs.json");
        mr.run();
    }
}
