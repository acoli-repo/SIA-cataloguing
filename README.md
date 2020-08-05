# SIA-cataloguing
Scientific Information Analysis: Cataloguing

"Formalerschließung"

## Rough Idea (as of now)

* configure crawler with link to a starting page plus a bunch of css queries
 to find places where there is metadata contained and how to partition it
* Get all of it and create metadata objects that contain some or all of the
metadata for a given publication.
  * Metadata will be contained in different places, e.g. in pdfs, in bibtex exports
  or in the raw html
* afterwards merge them together, probably title and pages are fairly useful 
* (Hopefully) adding new sources should be limited to adding new entries in the config/urlseed
