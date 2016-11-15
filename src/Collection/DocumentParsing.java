package Collection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Esteban on 15/11/2016.
 */


/**
 * Clase para hacer búsqueda interna dentro de un nodo.
 */
class DocumentParsing {
  private DocumentParsing(){}
  
  static Document buildDocument(File file) throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document document = dBuilder.parse(file);
    document.getDocumentElement().normalize();
    return document;
  }
  
  static String findSingleField(Element nodeElement, String attr) {
    NodeList elements = nodeElement.getElementsByTagName(attr);
    if (elements.getLength() > 0) return elements.item(0).getTextContent();
    else return "";
  }
  
  static ArrayList<String> findMultipleFields(Element element, String attr) {
    ArrayList<String> fields = new ArrayList<>();
    
    NodeList nodeList = element.getElementsByTagName(attr);
    
    if (nodeList.getLength() > 0) {
      return findFieldNodes(nodeList);
    }
    // Si no existen subnodos, se retorna una lista vacía.
    return new ArrayList<>();
  }
  
  private static ArrayList<String> findFieldNodes(NodeList nodeList) {
    
    Element mainNode = (Element) nodeList.item(0);
    NodeList subNodeList = mainNode.getElementsByTagName("D");
    
    if (subNodeList.getLength() > 0){
      return addFieldNodes(subNodeList);
    }
    
    // Si no existen subnodos, se retorna una lista vacía.
    return new ArrayList<>();
  }
  
  private static ArrayList<String> addFieldNodes(NodeList subNodeList) {
    ArrayList<String> fields = new ArrayList<>();
    for (int i = 0; i < subNodeList.getLength(); i++)
      fields.add(subNodeList.item(i).getTextContent());
    return fields;
  }
}
