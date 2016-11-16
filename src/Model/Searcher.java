package Model;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;

public class Searcher {

    private static PerFieldAnalyzerWrapper analyzer;
    private static Searcher instance;
    private IndexReader reader;
    private IndexSearcher searcher;
    private String indexPath;

    private Searcher(String indexPath) throws Exception {
        this.indexPath = indexPath;
        analyzer = createPerFieldAnalyzer();
        reader = createIndexReader();
        searcher = new IndexSearcher(reader);
    }

    private PerFieldAnalyzerWrapper createPerFieldAnalyzer(){
        Map<String, Analyzer> map = new HashMap<>();
        map.put("body", new StandardAnalyzer());
        map.put("title", new StandardAnalyzer());
        map.put("author", new WhitespaceAnalyzer());
        return new PerFieldAnalyzerWrapper(new KeywordAnalyzer(), map);
    }

    private IndexReader createIndexReader() throws Exception{
        Path indexPath = Paths.get(this.indexPath);
        Directory dir = FSDirectory.open(indexPath);
        return DirectoryReader.open(dir);
    }

    private Query createQuery(String[] fields, String queryContent, String type) throws Exception {
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,analyzer);
        if(type == "AND") parser.setDefaultOperator(QueryParser.Operator.AND);
        else parser.setDefaultOperator(QueryParser.Operator.OR);
        return parser.parse(queryContent);
    }

    public static ArrayList<Result> performSearch(QueryInfo info) throws Exception{
        if(Searcher.instance == null) instance = new Searcher(info.getPath());
        Query query = instance.createQuery(info.getFields(),info.getContent(), info.getType());
        ScoreDoc[] hits = instance.searcher.search(query, 100).scoreDocs;
        ArrayList<Result> results = new ArrayList<>();
        for (int i = 0; i < hits.length; i++) {
            Document document = instance.searcher.doc(hits[i].doc);
            String path = document.getValues("path")[0];
            String id = document.getValues("id")[0];
            results.add(new Result(path,id));
        }
        return results;
    }

}

