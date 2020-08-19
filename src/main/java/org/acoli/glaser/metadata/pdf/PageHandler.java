package org.acoli.glaser.metadata.pdf;

import java.util.ArrayList;
import java.util.List;


public class PageHandler {
    @Deprecated
    List<MetadataFromHTML> htmlSources;
    @Deprecated
    List<MetadataFromPDF> pdfSources;
    List<Metadata> mds;
    List<MetadataSourceHandler> sources;

    @Deprecated
    public PageHandler() {
        htmlSources = new ArrayList<>();
        pdfSources = new ArrayList<>();
        mds = new ArrayList<>();
        sources = new ArrayList<>();
    }
    public PageHandler(List<MetadataSourceHandler> initialSources) {
        this.sources = initialSources;
        mds = new ArrayList<>();
    }

    /**
     * TODO: We could potentially get into an infinite loop because we don't remember where we were already
     * @param handler
     */
    void unwindFinishedAndSuccessfulHandler(MetadataSourceHandler handler) {
        assert handler.finished(); // Maybe this is bad practice?
        System.err.println("Unwinding "+handler+"..");
        if (handler.foundOtherSourcesThatRequireHandling()) {
            List<MetadataSourceHandler> newSources = handler.getHandlersForOtherSources();
            System.err.println(handler+" found "+newSources.size()+" new sources, adding them.");
            sources.addAll(handler.getHandlersForOtherSources());
        }
        mds.addAll(handler.getMetadata());
    }
    public void run() {
        while (!sources.isEmpty()) {
            System.err.println(sources.size()+" sources left to extract..");
            MetadataSourceHandler source = sources.remove(0);
            System.err.println("Handling "+source+" now..");
            source.run();
            if (source.finished()) {
                if (source.success())
                    unwindFinishedAndSuccessfulHandler(source);
                else
                    System.err.println(source+" unsuccessful");
            }
            if (source.finished() && source.failed())
                System.err.println("FAILED "+source);
        }
        Metadata2TTL m2t = new Metadata2TTL();
        Metadata mergedmd = MetadataMerger.mergeMetadata(mds.get(0), mds.get(1));
        mds.clear();
        mds.add(mergedmd);
        for (Metadata md : mds) {
            System.err.println("Transforming..");
            System.err.println(m2t.metadataToTTL(md));

        }
    }
}
