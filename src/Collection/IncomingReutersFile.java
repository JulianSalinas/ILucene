package Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
  
  /**
   * Realiza el parsing del archivo ingresado.
   * @param file
   * @throws Exception
   */
  private void parseFile(File file) throws Exception {
    Document fileData = DocumentParsing.buildDocument(file);
    extractArticles(fileData);
  }
  
  /**
   * Extrae los artículos del archivo.
   * @param document
   * @throws Exception
   */
  private void extractArticles(Document document) throws Exception {
    articles = new ArrayList<>();
  
    NodeList reutersNodes = document.getElementsByTagName("REUTERS");
    
    for (int i = 0; i < reutersNodes.getLength(); i++){
      Element articleNode = (Element) reutersNodes.item(i);
      articles.add(new Article(articleNode));
    }
  }
  
  
  /**
   * Obtener los analizadores a utilizar para cada campo del archivo que será indizado.
   * Se usa WhiteSpaceAnalyzer como analizador por defecto.
   */
  public static PerFieldAnalyzerWrapper getFieldsAnalyzers(){
    Map<String, Analyzer> specializedAnalyzers = new HashMap<>();
    specializedAnalyzers.put("file_name", new KeywordAnalyzer());
    specializedAnalyzers.put("file_path", new KeywordAnalyzer());
    specializedAnalyzers.put("body", new StandardAnalyzer());
    specializedAnalyzers.put("title", new StandardAnalyzer());
    
    return new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), specializedAnalyzers);
    
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
