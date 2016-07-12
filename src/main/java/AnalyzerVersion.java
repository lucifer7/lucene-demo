import lombok.extern.log4j.Log4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: Jingyi.Yang
 * Date: 2016/7/12
 * Time: 13:38
 **/
@Log4j
public class AnalyzerVersion {
    public static void main(String[] args) throws IOException {
        Version matchVersion = Version.LUCENE_6_1_0;  //5.3.1 fake, is 6.1.0
        //Analyzer analyzer = new StandardAnalyzer(matchVersion);
        Analyzer analyzer = new StandardAnalyzer();
        TokenStream ts = analyzer.tokenStream("myfield", new StringReader("sample text doubi 北京天安门"));
        OffsetAttribute offsetAttr = ts.addAttribute(OffsetAttribute.class);

        try {
            ts.reset();
            while (ts.incrementToken()) {
                log.info("token: " + ts.reflectAsString(true));
                log.info("token start offset: " + offsetAttr.startOffset());
                log.info("token end offset: " + offsetAttr.endOffset());
            }
            ts.end();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ts.close();
        }
    }
}
