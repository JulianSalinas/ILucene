package Model;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Index {

    private Registry registry;
    private IndexWriter writer;
    private PerFieldAnalyzerWrapper analyzer;

    public Analyzer getAnalizer() { return analyzer; }
    public String getPath () { return registry.getPath(); }

    public Index(Registry registry) throws Exception {
        this.registry = registry;
        this.analyzer = createPerFieldAnalyzer();
        //this.writer = createIndexWriter();
    }

    public PerFieldAnalyzerWrapper createPerFieldAnalyzer(){
        Map<String, Analyzer> map = new HashMap<>();
        map.put("id", new KeywordAnalyzer());
        map.put("path", new KeywordAnalyzer());
        map.put("body", new StandardAnalyzer());
        map.put("title", new StandardAnalyzer());
        return new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), map);
    }

    private IndexWriter createIndexWriter() throws Exception{
        Path path = Paths.get(registry.getPath());
        Directory dir = FSDirectory.open(path);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(dir, iwc);
    }

    public void indexFiles(ArrayList<File> files) throws Exception{
        writer = createIndexWriter();
        for(File file : files ) {
            if(!registry.has(file)) {
                writer.addDocuments(createDocuments(file));
                registry.add(file);
            }
        }
        registry.save();
        writer.close();
    }

    private ArrayList<Document> createDocuments(File file) throws Exception {
        ArrayList<Article> articles = Article.findFromFile(file);
        ArrayList<Document> documents = new ArrayList<>();
        for (Article article : articles)
            documents.add(createDocument(article));
        return documents;
    }

    private Document createDocument(Article article) {
        Document document = new Document();
        document.add(new StringField("path", article.getPath(), Field.Store.YES));
        document.add(new StringField("id", article.getId(), Field.Store.YES));
        document.add(new TextField("body", article.getBody(), Field.Store.YES));
        document.add(new TextField("title", article.getTitle(), Field.Store.YES));
        document.add(new TextField("author", article.getAuthor(), Field.Store.YES));
        document.add(new StringField("date", article.getDate(), Field.Store.YES));
        for(String topic : article.getTopics()) document.add(new TextField("topics", topic, Field.Store.YES));
        for(String place : article.getPlaces()) document.add(new TextField("places", place, Field.Store.YES));
        for(String org : article.getOrgs()) document.add(new TextField("orgs", org, Field.Store.YES));
        for(String exchange : article.getExchanges()) document.add(new TextField("exchanges", exchange, Field.Store.YES));
        return document;
    }

}
