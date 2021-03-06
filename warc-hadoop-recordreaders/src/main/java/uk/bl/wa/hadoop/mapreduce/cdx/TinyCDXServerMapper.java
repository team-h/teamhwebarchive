package uk.bl.wa.hadoop.mapreduce.cdx;

/*
 * #%L
 * warc-hadoop-recordreaders
 * %%
 * Copyright (C) 2013 - 2018 The webarchive-discovery project contributors
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveReaderFactory;
import org.archive.io.arc.ARCReader;
import org.archive.io.warc.WARCReader;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.resourceindex.cdx.SearchResultToCDXFormatAdapter;
import org.archive.wayback.resourceindex.cdx.format.CDXFormat;
import org.archive.wayback.resourcestore.indexer.ArcIndexer;
import org.archive.wayback.resourcestore.indexer.WarcIndexer;

import uk.bl.wa.hadoop.mapreduce.lib.DereferencingArchiveToCDXRecordReader;

/**
 * 
 * This simple mapper takes the CDX lines generated by
 * 
 * @see uk.bl.wa.hadoop.mapreduce.lib.ArchiveToCDXFileInputFormat and POSTs them
 *      in chunks to an instance of the TinyCDXServer.
 * 
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class TinyCDXServerMapper extends Mapper<Text, Text, Text, Text> {

    private static final Log log = LogFactory.getLog(TinyCDXServerMapper.class);

    private TinyCDXSender tcs;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.hadoop.mapreduce.Mapper#setup(org.apache.hadoop.mapreduce.
     * Mapper.Context)
     */
    @Override
    protected void setup(Mapper<Text, Text, Text, Text>.Context context)
            throws IOException, InterruptedException {
        super.setup(context);

        String endpoint = context.getConfiguration().get("tinycdxserver.endpoint",
                "http://localhost:9090/test");
        log.warn("Sending to " + endpoint);
        int batch_size = context.getConfiguration()
                .getInt("tinycdxserver.batch_size", 10000);

        this.tcs = new TinyCDXSender(endpoint, batch_size);
        
        System.setProperty("http.proxyHost", "explorer-private");
        System.setProperty("http.proxyPort", "3127");

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.hadoop.mapreduce.Mapper#map(java.lang.Object,
     * java.lang.Object, org.apache.hadoop.mapreduce.Mapper.Context)
     */
    @Override
    protected void map(Text key, Text value,
            Mapper<Text, Text, Text, Text>.Context context)
                    throws IOException, InterruptedException {

        tcs.add(value);
        // Record progress:
        if (context != null) {
            context.setStatus("Seen " + tcs.getTotalRecords()
                    + " records, sent " + tcs.getTotalSentRecords() + "...");
            // Also pass to reducers for cross-checking.
            try {
                context.write(key, value);
            } catch (Exception e) {
                log.error("Write failed.", e);
            }
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.hadoop.mapreduce.Mapper#cleanup(org.apache.hadoop.mapreduce.
     * Mapper.Context)
     */
    @Override
    protected void cleanup(Mapper<Text, Text, Text, Text>.Context context)
            throws IOException, InterruptedException {
        super.cleanup(context);
        tcs.close();
        context.setStatus("Seen " + tcs.getTotalRecords() + " records...");
    }

    /**
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        File inputFile = new File(args[0]);
        ArchiveReader arcreader = ArchiveReaderFactory.get(inputFile);
        arcreader.setStrict(false);
        WarcIndexer warcIndexer = new WarcIndexer();
        ArcIndexer arcIndexer = new ArcIndexer();
        Iterator<CaptureSearchResult> archiveIterator;
        if (inputFile.getName().matches("^.+\\.warc(\\.gz)?$")) {
            archiveIterator = warcIndexer.iterator((WARCReader) arcreader);
        } else {
            archiveIterator = arcIndexer.iterator((ARCReader) arcreader);
        }
        Iterator<String> cdxlines = SearchResultToCDXFormatAdapter.adapt(
                archiveIterator,
                new CDXFormat(DereferencingArchiveToCDXRecordReader.CDX_11));

        // Test it:
        TinyCDXServerMapper mapper = new TinyCDXServerMapper();
        mapper.tcs = new TinyCDXSender("http://localhost:9090/t3", 20);
        Text cdxline = new Text();
        while (cdxlines.hasNext()) {
            cdxline.set(cdxlines.next());
            mapper.map(cdxline, cdxline, null);
        }
        mapper.tcs.close();

    }
}
