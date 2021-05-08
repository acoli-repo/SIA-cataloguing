package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.unit.util.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * The PageHandler class is responsible for extracting one or more metadata sets from a page. Given a group of MetadataSourceHandlers,
 * it runs each, checks for additional sources wrapped in a MetadataSourceHandler and adds them to the pool.
 * Depending on configuration, the merging process happens after the entire run or after each Handler is unwind.
 */
public class PageHandler {
    List<Metadata> mds;
    List<MetadataSourceHandler> sources;
    private static Logger LOG = Logger.getLogger(PageHandler.class.getName());
    private boolean mergeAfterEach = true;

    public PageHandler(List<MetadataSourceHandler> initialSources) {
        this.sources = initialSources;
        mds = new ArrayList<>();
    }


    /**
     * TODO: We could potentially get into an infinite loop because we don't remember where we were already
     * @param handler
     */
    void unwindFinishedAndSuccessfulHandler(MetadataSourceHandler handler) {
        assert handler.finished();
        LOG.info("Unwinding "+handler+"..");
        if (handler.foundOtherSourcesThatRequireHandling()) {
            List<MetadataSourceHandler> newSources = handler.getHandlersForOtherSources();
            LOG.info(handler+" found "+newSources.size()+" new sources, adding them to existing pool.");
            sources.addAll(handler.getHandlersForOtherSources());
        }
        List<Metadata> foundPartialMetadata = handler.getMetadata();
        if (mergeAfterEach) {
            LOG.info("Merging ..");
            mds.addAll(MetadataMerger.mergeMetadata(foundPartialMetadata));
        } else {
            mds.addAll(foundPartialMetadata);
        }
    }

    /**
     * Main function. Runs through the initial pool of MetadataSourceHandlers, extracts metadata from them, sends
     * them to the unwind function. Terminates after the entire pool is empty.
     */
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
        List<Metadata> mergedMDs;
        if (!mergeAfterEach) {
            LOG.info("Merging metadata.. (" + mds.size() + " entries)");
            mergedMDs = MetadataMerger.mergeMetadata(mds);
            LOG.info("Done merging, "+mds.size()+" left.");
        } else {
            mergedMDs = this.mds;
        }
        for (Metadata md : mergedMDs) {
            System.err.println(md);

        }
    }
}
