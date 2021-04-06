package org.acoli.glaser.metadata.pdf.extract;

import org.acoli.glaser.metadata.pdf.util.Metadata;

import java.net.URL;
import java.util.List;

/**
 * A MetadataSourceHandler should define how to extract metadata from a given type of source.
 * As an example, if the spider returns a html as candidate for containing relevant metadata,
 * we pass it on to a MetadataSourceHandler that is concerned with handling pdfs.
 * TODO: Maybe think about changing this to an interface? Only reason its abstract class is the failed thing and the source attribute
 */
abstract public class MetadataSourceHandler {
    URL source;

    /**
     * Start the execution of what ever needs to be done in order to extract the metadata.
     */
    public abstract void run();

    /**
     * Should return a list of found Metadata if it has finished and succeeded. If nothing was found,
     * return an empty list. May only return null if not finished yet.
     */
    public abstract List<Metadata> getMetadata();

    /**
     * Indicate whether the extraction has finished. Finishing does not imply being successful.
     * @return
     */
    public abstract boolean finished();

    public abstract boolean success();

    public boolean failed() {
        return !success();
    }

    /**
     * In some cases, you may find stuff that requires handling by already existing metadata
     * handlers. Indicate this here.
     * @return
     */
    public abstract boolean foundOtherSourcesThatRequireHandling();

    public abstract List<MetadataSourceHandler> getHandlersForOtherSources();
}
