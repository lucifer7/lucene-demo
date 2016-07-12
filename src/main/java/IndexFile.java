import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created with IntelliJ IDEA.
 * User: Jingyi.Yang
 * Date: 2016/7/12
 * Time: 13:32
 **/
@Log4j
public class IndexFile {
    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            log.error("Invalid arguments.");
            throw new InvalidParameterException();
        }

        String indexPath = args[0];
        IndexWriter writer;
        //writer = new IndexWriter(indexPath, new SimpleAnalyzer(), false);
    }
}
