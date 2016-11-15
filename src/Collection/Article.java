package Collection;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Iterator;

import static Collection.DocumentParsing.findMultipleFields;
import static Collection.DocumentParsing.findSingleField;

/**
 * Usar solamente el metodo estatico findFromFiles(String path)
 */

public class Article {
  
  private String id;
  private String body;
  private String title;
  private String author;
  private String date;
  private ArrayList<String> topics;
  private ArrayList<String> places;
  private ArrayList<String> orgs;
  private ArrayList<String> exchanges;
  
  Article(Element element, Element text) {
    id = element.getAttribute("NEWID");
    body = findSingleField(text, "BODY");
    title = findSingleField(text, "TITLE");
    author = findSingleField(text, "AUTHOR");
    date = findSingleField(element, "DATE");
    topics = findMultipleFields(element, "TOPICS");
    places = findMultipleFields(element, "PLACES");
    orgs = findMultipleFields(element, "ORGS");
    exchanges = findMultipleFields(element, "EXCHANGES");
  }
  
  public Article(){
    id = "";
    body = "";
    title = "";
    author = "";
    date = "";
    topics = null;
    places = null;
    orgs = null;
    exchanges = null;
  }
  
  Article(Element articleNode){
    id = articleNode.getAttribute("NEWID");
    date = DocumentParsing.findSingleField(articleNode, "DATE");
    topics = DocumentParsing.findMultipleFields(articleNode, "TOPICS");
    places = DocumentParsing.findMultipleFields(articleNode, "PLACES");
    orgs = DocumentParsing.findMultipleFields(articleNode, "ORGS");
    exchanges = DocumentParsing.findMultipleFields(articleNode, "EXCHANGES");
    
    NodeList articleTextNodes = articleNode.getElementsByTagName("TEXT");
    
    if (articleTextNodes.getLength() > 0){
      // Datos extraídos de los sub-nodos TEXT de los artículos.
      Element articleTextNode = (Element) articleNode.getElementsByTagName("TEXT").item(0);
  
      body = DocumentParsing.findSingleField(articleTextNode, "BODY");
      title = DocumentParsing.findSingleField(articleTextNode, "TITLE");
      author = DocumentParsing.findSingleField(articleTextNode, "AUTHOR");
    }
    
    // Contingencia para evitar posible Exception.
    // Sólo ocurre si el documento no tiene sub-nodo TEXT.
    else {
      System.out.println("Incoming node has no TEXT child!");
      body = "";
      title = "";
      author = "";
    }
  }
    
  public String getId() {
    return id;
  }
  
  public String getBody() {
    return body;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getAuthor() {
    return author;
  }
  
  public String getDate() {
    return date;
  }
  
  public ArrayList<String> getTopics() {
    return topics;
  }
  
  public ArrayList<String> getPlaces() {
    return places;
  }
  
  public ArrayList<String> getOrgs() {
    return orgs;
  }
  
  public ArrayList<String> getExchanges() {
    return exchanges;
  }
  
  @Override
  public String toString() {
    
    String str =
        "ID: " + id + "\n" +
            "Title: " + title + "\n" +
            "Author: " + author + "\n" +
            "Date: " + date + "\n";
    
    StringBuilder stringBuilder = new StringBuilder(str);
      
    Iterator<String> topicsIter = topics.iterator();
    if (topicsIter.hasNext()){
      stringBuilder.append("Topics: ");
      while (topicsIter.hasNext()){
        stringBuilder.append(topicsIter.next());
        if (topicsIter.hasNext()){
          stringBuilder.append(", ");
        }
      }
      stringBuilder.append("\n");
    }
    
    Iterator<String> placesIter = places.iterator();
    if (placesIter.hasNext()){
      stringBuilder.append("Places: ");
      while (placesIter.hasNext()){
        stringBuilder.append(placesIter.next());
        if (placesIter.hasNext()){
          stringBuilder.append(", ");
        }
      }
      stringBuilder.append("\n");
    }
    
    Iterator<String> orgsIter = orgs.iterator();
    if (orgsIter.hasNext()){
      stringBuilder.append("Orgs: ");
      while (orgsIter.hasNext()){
        stringBuilder.append(placesIter.next());
        if (orgsIter.hasNext()){
          stringBuilder.append(", ");
        }
      }
      stringBuilder.append("\n");
    }
    
    Iterator<String> exchangesIter = exchanges.iterator();
    if (exchangesIter.hasNext()){
      stringBuilder.append("Exchanges: ");
      while (exchangesIter.hasNext()){
        stringBuilder.append(placesIter.next());
        if (exchangesIter.hasNext()){
          stringBuilder.append(", ");
        }
      }
      stringBuilder.append("\n");
    }
    stringBuilder.append("\n-----------------------------------");
    
    return stringBuilder.toString();
  }
  
}
