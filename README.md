# SIA-cataloguing
Scientific Information Analysis: Cataloguing "Formalerschließung"

This module extracts metadata from known sources of scientific journals.

# How it works
* For each entry link, the package follows all links that return on the initial css query. Then, metadata contained in each source is 
extracted, right now we support bibtex, pdf and html. 
* Often, extracted metadata is not complete and we find multiple sets of partial metadata for a single publication. Thus, we merge
the metadata after extraction. 

Example:
The bibtex embedded on the LREC page contains (besides other fields) title, authors and the location, but is missing the page number. The PDF version of the entire
proceedings does not contain the location but (implicitly) the page numbers. 
Solution:
We get all sets of metadata from the pdf AND from the homepage. We merge them based on title. We intend to extend this to the future,
as they may be multiple papers with the same title (e.g. "introduction"), but we exclude this as of now.

# Overview of current state

| Requirement                                     | Implementation                                                                                               |
|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| Extract metadata from pdfs                      | class MetadataFromPDF, handled by MainRunner                                                                 |
| Extract metadata from entire proceedings in pdf | class MetadataFromPDF, handled by MainRunner, splitted into individual papers by class Splitter              |
| Extract metadata from html landing pages        | class MetadataFromHTML, handled by MainRunner                                                                |
| Produce high quality output                     | We avoid doublets and partial datasets with class MetadataMerger                                             |
| Validate/repair output                          | class MetadataValidator, rudimentary as of now and can't repair                                              |
| Make it easy to add new pdf sources             | PDF extraction is parameterized over XPaths. Configure them with the PDFExtractor object. See below for more |
| Make it easy to add new html sources            | HTML extraction is parameterized over css queries. Currently hard coded                                      |
|                                                 |                                                                                                              |

## Current Todos:
* In [some proceedings pdfs](http://lrec-conf.org/workshops/lrec2018/W30/pdf/book_of_proceedings.pdf) there are different fonts for titles. Make it possible to have multiple sets of pdf extractor configs per source
* Find a good way to represents the configs.

# Usage
* To add sources, edit the urlseed.csv file. Each row represents a starting point for the metadata extractor to find data.

## Parameterizing PDF extraction
Challenge: Different publications typeset their metadata differently, e.g. different fonts in the title.

Solution: The PDFMetadataExtractor class is configurable on font height and font type which sufficed until now.

How to get this information: Get one of the publications pdf and perform the following command (requires poppler):
`pdftohtml <PDF_FILE> -xml -i -c -q -s <OUTPUT>`
Open the output xml and search for the attribute values. 
```xml
<text top="111" left="192" width="514" height="19" font="0"><b>My awesome new paper </b></text>
<text top="159" left="446" width="5" height="16" font="1"> </text>
<text top="180" left="81" width="735" height="16" font="2"><b>Alice Premier &amp; Bob Second </b></text>
```
Results in the following configuration:
```java
PDFMetadataExtractor pme = ...
pme.authorHeight = 16;
pme.authorFont = 2;
pme.titleHeight = 19;
pme.titleFont = 0;
```
