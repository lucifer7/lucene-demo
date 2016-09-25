package beginner;

import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;

/**
 * Usage: <b> </b>
 *
 * @author lucifer
 *         Date 2016-9-24
 *         Device Aurora R5
 */
@Log4j
public class Indexer {
    private IndexWriter writer;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    String.format("Usage: java %s <index dir> <data dir>.", Indexer.class.getName()));
        }

        String indexDir = args[0];
        String dataDir = args[1];

        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed;
        try {
            numIndexed = indexer.index(dataDir, new TextFilesFilter());
        } finally {
            indexer.close();
        }
        long end = System.currentTimeMillis();
        log.info(String.format("Indexing %d files took %d ms.", numIndexed, end - start));

    }

    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir));
        writer = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_30),
                true, IndexWriter.MaxFieldLength.UNLIMITED);
    }

    private void close() throws IOException {
        writer.close();
    }

    public int index(String dataDir, FileFilter filter) throws IOException {
        File[] files = new File(dataDir).listFiles();

        for (File f :
                files) {
            if (!f.isDirectory() && !f.isHidden()
                    && f.exists() && f.canRead()
                    && (filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }

        return writer.numDocs();
    }

    private void indexFile(File f) throws IOException {
        log.info("Indexing " + f.getCanonicalPath());
        Document doc = getDocument(f);
        writer.addDocument(doc);
    }

    private Document getDocument(File file) throws IOException {
        Document doc = new Document();
        doc.add(new Field("contents", new FileReader(file)));
        doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("fullpath", file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        return doc;
    }

    private static class TextFilesFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(".txt");
        }
    }
}
