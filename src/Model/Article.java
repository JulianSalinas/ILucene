package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Article {

    private String path;
    private String id;
    private String body;
    private String title;
    private String author;
    private String date;
    private ArrayList<String> topics;
    private ArrayList<String> places;
    private ArrayList<String> orgs;
    private ArrayList<String> exchanges;

    public String getPath() { return path; }
    public String getId(){ return id; }
    public String getBody(){ return body; }
    public String getTitle(){ return title; }
    public String getAuthor(){ return author; }
    public String getDate(){ return date; }
    public ArrayList<String>getTopics(){ return topics; }
    public ArrayList<String>getPlaces(){ return places; }
    public ArrayList<String>getOrgs(){ return orgs; }
    public ArrayList<String>getExchanges(){ return exchanges; }

    private Article (Element element, Element text, String path){
        this.path = path;
        id = element.getAttribute("NEWID");
        body = findSingleField(text, "BODY");
        title = findSingleField(text, "TITLE");
        author = findSingleField(text, "AUTHOR");
        date =  traslateDate(findSingleField(element, "DATE"));
        topics = findMultipleFields(element,"TOPICS");
        places = findMultipleFields(element,"PLACES");
        orgs = findMultipleFields(element,"ORGS");
        exchanges = findMultipleFields(element,"EXCHANGES");
        //System.out.println(this);
    }

    public static ArrayList<Article> findFromFiles(ArrayList<File> files) throws Exception{
        ArrayList<Article>articles = new ArrayList<>();
        for(File file : files) articles.addAll(findFromFile(file));
        return articles;
    }

    public static ArrayList<Article> findFromFile(File file) throws Exception{
        Document document = buildDocument(file);
        ArrayList<Article> articles = new ArrayList<>();
        NodeList reuters = document.getElementsByTagName("REUTERS");
        for (int i = 0; i < reuters.getLength(); i++) {
            Element element = (Element) reuters.item(i);
            Element text = (Element) element.getElementsByTagName("TEXT").item(0);
            Article article = new Article(element, text, file.getAbsolutePath());
            articles.add(article);
        }
        return articles;
    }

    private static Document buildDocument(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    private ArrayList<String> findMultipleFields(Element element, String attr){
        ArrayList<String> fields = new ArrayList<>();
        NodeList nodeList = element.getElementsByTagName(attr);
        if(nodeList.getLength() <= 0 ) fields.add("NOT EXISTS");
        else fields = findFieldNodes(nodeList);
        return fields;
    }

    private String findSingleField(Element element, String attr){
        NodeList elements = element.getElementsByTagName(attr);
        if(elements.getLength() > 0) return elements.item(0).getTextContent();
        else return "NOT EXISTS";
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

    private static Pattern pattern = Pattern.compile("([0-9]+)-([A-Z][A-Z][A-Z])-([0-9]+)");
    private static String traslateDate (String date) {
        try {
            Matcher matcher = pattern.matcher(date);
            if (matcher.find()) {
                String day = traslateDay(matcher.group(1));
                String month = traslateMonth(matcher.group(2));
                String year = matcher.group(3);
                date = year + month + day;
                return date;
            } return "19970101";
        } catch (Exception e) { return "19970101";}
    }

    //Ej: "FEB" = "02"
    private static String traslateMonth(String month) throws Exception {
        Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String traslated = String.valueOf(cal.get(Calendar.MONTH) + 1);
        if(traslated.length() == 1) traslated = "0" + traslated;
        return traslated;
    }

    //Ej: "1" = "02"
    private static String traslateDay(String day) throws Exception {
        if(day.length() == 1) return "0" + day;
        else return day;
    }

    @Override
    public String toString(){

        String str =
                "ID: " + id + "\n" +
                        "Title: " + title + "\n" +
                        "Author: " + author + "\n" +
                        "Date: " + String.valueOf(date) + "\n";

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

        str += "Path: " + path + "\n";

        str += "\n----------------------------------------------------------------------";
        return str;
    }

}
