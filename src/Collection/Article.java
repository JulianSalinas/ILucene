package Collection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * Usar solamente el metodo estatico findFromFile(String path)
 */

public class Article {

    public String id;
    public String body;
    public String title;
    public String author;
    public String date;
    public ArrayList<String> topics;
    public ArrayList<String> places;
    public ArrayList<String> orgs;
    public ArrayList<String> exchanges;

    private Article (Element element, Element text){
        id = element.getAttribute("NEWID");
        body = findSingleField(text, "BODY");
        title = findSingleField(text, "TITLE");
        author = findSingleField(text, "AUTHOR");
        date =  findSingleField(element, "DATE");
        topics = findMultipleFields(element,"TOPICS");
        places = findMultipleFields(element,"PLACES");
        orgs = findMultipleFields(element,"ORGS");
        exchanges = findMultipleFields(element,"EXCHANGES");
    }

    public static ArrayList<Article> findFromFile(String path) throws Exception{
        File file = readFile(path);
        Document document = buildDocument(file);
        return findFromDocument(document);
    }

    private static ArrayList<Article> findFromDocument(Document document) throws Exception{
        ArrayList articles = new ArrayList();
        NodeList reuters = document.getElementsByTagName("REUTERS");
        for (int i = 0; i < reuters.getLength(); i++) {
            Element element = (Element) reuters.item(i);
            Element text = (Element) element.getElementsByTagName("TEXT").item(0);
            Article article = new Article(element, text);
            articles.add(article);
        }
        return articles;
    }

    private ArrayList<String> findMultipleFields(Element element, String attr){
        ArrayList<String> fields = new ArrayList<>();
        NodeList nodeList = element.getElementsByTagName(attr);
        if(nodeList.getLength() <= 0 ) fields.add("NOT EXISTS");
        else fields = findFieldNodes(nodeList);
        return fields;
    }

    private ArrayList<String> findFieldNodes(NodeList nodeList){
        ArrayList<String> fields = new ArrayList<>();
        Element mainNode = (Element) nodeList.item(0);
        NodeList subNodeList = mainNode.getElementsByTagName("D");
        if (subNodeList.getLength() <= 0) fields.add("EMPTY");
        else fields = addFieldNodes(subNodeList);
        return fields;
    }

    private ArrayList<String> addFieldNodes(NodeList subNodeList){
        ArrayList<String> fields = new ArrayList<>();
        for (int i = 0; i < subNodeList.getLength(); i++)
            fields.add(subNodeList.item(i).getTextContent());
        return fields;
    }

    private String findSingleField(Element element, String attr){
        NodeList elements = element.getElementsByTagName(attr);
        if(elements.getLength() > 0) return elements.item(0).getTextContent();
        else return "NOT EXISTS";
    }

    private static Document buildDocument(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    private static File readFile(String path) throws Exception {
        File file = new File(path);
        if(file.exists()){ return file; }
        else{ throw new Exception("El archivo no existe"); }
    }

    @Override
    public String toString(){

        String str =
                "ID: " + id + "\n" +
                "Title: " + title + "\n" +
                "Author: " + author + "\n" +
                "Date: " + date + "\n";

        String substr = "";
        for(String element : topics) substr += element + ", ";
        substr = "Topics: " + substr.substring(0, substr.length()-2) + "\n";
        str += substr;

        substr = "";
        for(String element : places) substr += element + ", ";
        substr = "Places: " + substr.substring(0, substr.length()-2) + "\n";
        str += substr;

        substr = "";
        for(String element : orgs) substr += element + ", ";
        substr = "Orgs: " + substr.substring(0, substr.length()-2) + "\n";
        str += substr;

        substr = "";
        for(String element : exchanges) substr += element + ", ";
        substr = "Exchanges: " + substr.substring(0, substr.length()-2) + "\n";
        str += substr;

        str += "\n----------------------------------------------------------------------";
        return str;
    }

}