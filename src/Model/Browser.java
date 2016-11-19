package Model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import Model.Beans.QueryBean;
import Model.Beans.ResultBean;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;

public class Browser {

    private IndexReader reader;
    private IndexSearcher searcher;
    private Index index;

    public Browser(Index index) throws Exception {
        this.index = index;
    }
    private IndexReader createIndexReader() throws Exception{
        Path path = Paths.get(index.getPath());
        Directory dir = FSDirectory.open(path);
        return DirectoryReader.open(dir);
    }

    public ArrayList<ResultBean> doSearch(QueryBean info) throws Exception{
        reader = createIndexReader();
        searcher = new IndexSearcher(reader);
        Query query = createQuery(info);
        ScoreDoc[] hits = searcher.search(query, 100).scoreDocs;
        ArrayList<ResultBean> results = new ArrayList<>();
        for (int i = 0; i < hits.length; i++) {
            Document document = searcher.doc(hits[i].doc);
            ResultBean result = new ResultBean();
            result.setPath(document.getValues("path")[0]);
            result.setId(document.getValues("id")[0]);
            result.setTitle(document.getValues("title")[0]);
            results.add(result);
        }
        reader.close();
        return results;
    }
    private Query createQuery(QueryBean info) throws Exception {
        QueryParser parser = new QueryParser(info.getContent(), index.getAnalizer());
        return parser.parse(info.getContent());
    }

}

