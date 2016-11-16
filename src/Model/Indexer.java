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
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Indexer {

    private static PerFieldAnalyzerWrapper analyzer;
    private static ArrayList<String> indexedFiles;
    private IndexWriter writer;
    private String indexPath;

    private Indexer(String indexPath) throws Exception {
        this.indexPath = indexPath;
        analyzer = createPerFieldAnalyzer();
        writer = createIndexWriter();
        indexedFiles = findXMLFile(indexPath+"\\indexedFiles.xml");
    }

    private PerFieldAnalyzerWrapper createPerFieldAnalyzer(){
        Map<String, Analyzer> map = new HashMap<>();
        map.put("body", new StandardAnalyzer());
        map.put("title", new StandardAnalyzer());
        map.put("author", new WhitespaceAnalyzer());
        return new PerFieldAnalyzerWrapper(new KeywordAnalyzer(), map);
    }

    private IndexWriter createIndexWriter() throws Exception{
        Path indexPath = Paths.get(this.indexPath);
        Directory dir = FSDirectory.open(indexPath);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(dir, iwc);
    }

    public static void indexFiles(ArrayList<File> files, String indexPath) throws Exception{
        Indexer indexer = new Indexer(indexPath);
        for(File file : files ) {
            if(!indexedFiles.contains(file.getAbsolutePath())) {
                indexFile(file, indexer);
                indexedFiles.add(file.getAbsolutePath());
            }
        }
        writeFiles(indexPath);
        indexer.writer.close();
    }

    private static void indexFile(File file, Indexer indexer) throws Exception{
        ArrayList<Document> documents = indexer.createDocuments(file);
        indexer.writer.updateDocuments(new Term("path",file.getPath()),documents);
        //indexer.writer.addDocuments(documents);
    }

    private ArrayList<Document> createDocuments(File file) throws Exception {
        ArrayList<Article> articles = Article.findFromFile(file);
        ArrayList<Document> documents = new ArrayList<>();
        for (Article article : articles) documents.add(createDocument(article));
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
        for(String topic : article.getTopics())
            document.add(new TextField("topics", topic, Field.Store.YES));
        for(String place : article.getPlaces())
            document.add(new TextField("places", place, Field.Store.YES));
        for(String org : article.getOrgs())
            document.add(new TextField("orgs", org, Field.Store.YES));
        for(String exchange : article.getExchanges())
            document.add(new TextField("exchanges", exchange, Field.Store.YES));
        return document;
    }

    public static ArrayList<String> findXMLFile(String indexPath) throws Exception{
        File xmlFile = new File(indexPath);
        if(xmlFile.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document document = dBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
            ArrayList<String> paths = new ArrayList<>();
            NodeList files = document.getElementsByTagName("FILE");
            for (int i = 0; i < files.getLength(); i++)
                paths.add(files.item(i).getTextContent());
            return paths;
        }
        else return new ArrayList<>();
    }

    private static void writeFiles(String indexPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        org.w3c.dom.Document document = implementation.createDocument(null, "INDEXED_FILES", null);
        document.setXmlVersion("1.0");
        Element root = document.getDocumentElement();
        for(String file : indexedFiles ) {
            Element itemNode = document.createElement("FILE");
            itemNode.setTextContent(file);
            root.appendChild(itemNode);
        }
        Source source = new DOMSource(document);
        javax.xml.transform.stream.StreamResult result =
                new StreamResult(new java.io.File(indexPath+"\\indexedFiles.xml"));
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }

}
