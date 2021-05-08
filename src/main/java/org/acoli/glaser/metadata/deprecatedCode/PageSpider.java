package org.acoli.glaser.metadata.deprecatedCode;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PageSpider {


    static public List<URL> findHrefsByCSSQuery(Document doc, String cssQuery) {
        Elements es = doc.select(cssQuery);
        List<URL> hrefs = new ArrayList<>();
        for (Element e : es ) {
            String href = e.absUrl("href");
            try {
                hrefs.add(new URL(href));
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }
        }
        return hrefs;
    }
}
