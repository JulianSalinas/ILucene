package Collection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Esteban on 15/11/2016.
 */
public class IncomingReutersFile {
  
  // Información básica del archivo de artículos
  private String fileName;
  private String filePath;
  
  private ArrayList<Article> articles;
  
  public IncomingReutersFile(String fileName, String filePath) {
    this.fileName = fileName;
    this.filePath = filePath;
  }
  
  public IncomingReutersFile(File file) throws Exception{
    fileName = file.getName();
    filePath = file.getAbsolutePath();
    try {
      parseFile(file);
    } catch (Exception e){
      throw new Exception("Cannot open the file.");
    }
    
  }
  
  public String getFileName() {
    return fileName;
  }
  
  public String getFilePath() {
    return filePath;
  }
  
  public ArrayList<Article> getArticles() {
    return articles;
  }
  
  private void parseFile(File file) throws Exception {
    Document fileData = DocumentParsing.buildDocument(file);
    extractArticles(fileData);
  }
  
  private void extractArticles(Document document) throws Exception {
    articles = new ArrayList<>();
  
    NodeList reutersNodes = document.getElementsByTagName("REUTERS");
    
    for (int i = 0; i < reutersNodes.getLength(); i++){
      Element articleNode = (Element) reutersNodes.item(i);
      articles.add(new Article(articleNode));
    }
  }
  
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    
    stringBuilder.append("File Name: ").append(fileName).append("\n");
    stringBuilder.append("File Path: ").append(filePath).append("\n");
    stringBuilder.append("File has ")
        .append(Integer.toString(articles.size()))
        .append(" registered article(s) from Reuters.\n");
    
    
    return stringBuilder.toString();
  }
}
