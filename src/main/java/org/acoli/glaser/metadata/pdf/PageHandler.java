package org.acoli.glaser.metadata.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class PageHandler {
    @Deprecated
    List<MetadataFromHTML> htmlSources;
    @Deprecated
    List<MetadataFromPDF> pdfSources;
    List<Metadata> mds;
    List<MetadataSourceHandler> sources;
    private static Logger LOG = Logger.getLogger(PageHandler.class.getName());

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
        // TODO: Unwinding should include merging them?
        LOG.info("Unwinding "+handler+"..");
        if (handler.foundOtherSourcesThatRequireHandling()) {
            List<MetadataSourceHandler> newSources = handler.getHandlersForOtherSources();
            LOG.info(handler+" found "+newSources.size()+" new sources, adding them to existing pool.");
            sources.addAll(handler.getHandlersForOtherSources());
        }
        mds.addAll(MetadataMerger.mergeMetadata(handler.getMetadata()));
    }
    public void run() {
        while (!sources.isEmpty()) {
            LOG.info(sources.size()+" sources left to extract..");
            MetadataSourceHandler source = sources.remove(0);
            LOG.info("Handling "+source+" now..");
            source.run();
            if (source.finished()) {
                if (source.success())
                    unwindFinishedAndSuccessfulHandler(source);
                else
                    LOG.warning(source+" unsuccessful");
            }
            if (source.finished() && source.failed())
                LOG.warning(source+" failed during handling.");
        }
        LOG.info("Merging metadata.. ("+mds.size()+" entries)");
        List<Metadata> mergedmds = MetadataMerger.mergeMetadata(mds);
        LOG.info("Done merging, "+mds.size()+" left.");
        for (Metadata md : mergedmds) {
            System.err.println(md);

        }
    }
}
