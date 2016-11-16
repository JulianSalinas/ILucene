package Collection;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by Esteban on 14/11/2016.
 */
public class LuceneIndexer {
  
  private static final String testFile = "test.lucene";   // Ruta (PRUEBA)
  private String indexFilePath;   // Ruta del archivo de índice.
  
  // --- //
  public LuceneIndexer() {
    this.indexFilePath = System.getProperty("user.dir") + testFile;
    System.out.println("Test index file = " + indexFilePath);
    
  }
  
  public LuceneIndexer(String indexFilePath) {
    this.indexFilePath = indexFilePath;
  }
  
  public String getIndexFilePath() {
    return indexFilePath;
  }
  
  public void setIndexFilePath(String indexFilePath) {
    this.indexFilePath = indexFilePath;
  }
  
  private boolean existsIndex() throws Exception {
    if (!indexFilePath.isEmpty()) {
      try {
        Directory indexFile = SimpleFSDirectory.open(Paths.get(indexFilePath));
        return DirectoryReader.indexExists(indexFile);
      } catch (IOException e) {
        System.out.println("IOException - Index does not exist.");
        return false;
      }
    }
    return false;
  }
  
  /**
   * Abre el archivo de índice en modo escritura para agregar documentos o modificar el índice.
   * Si el archivo no existe, es creado.
   */
  private IndexWriter openIndexWriter() throws Exception {
    if (!indexFilePath.isEmpty()) {
      try {
        Directory indexFile = SimpleFSDirectory.open(Paths.get(indexFilePath));
        
        IndexWriterConfig writerConfig =
            new IndexWriterConfig(IncomingReutersFile.getFieldsAnalyzers());
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(indexFile, writerConfig);
        
      } catch (IOException e) {
        throw new Exception("IOException - Index does not exist.");
        
      }
    }
    throw new Exception("File path not specified.");
  }
  
  /**
   *
   * Abre el archivo de índice en modo lectura para realizar búsquedas.
   */
  public IndexReader openIndexReader() throws Exception {
    if (!indexFilePath.isEmpty()) {
      try {
        Directory indexFile = SimpleFSDirectory.open(Paths.get(indexFilePath));
        return DirectoryReader.open(indexFile);
        
        
      } catch (IOException e) {
        throw new Exception("IOException - Index does not exist.");
      }
    }
    throw new Exception("File path not specified.");
  }
  
  
  /**
   * Agrega un documento de Reuters al índice de Lucene asociado a este objeto.
   * @param reutersFile
   * @throws Exception
   */
  public void addFileToIndex(IncomingReutersFile reutersFile) throws Exception {
    // Abre el archivo de Lucene para agregar nuevos documentos.
    IndexWriter indexWriter = openIndexWriter();
    
    // Genera un nuevo registro de Documento.
    Document newDocument = new Document();
    
    newDocument.add(new Field("file_name", reutersFile.getFileName(), StringField.TYPE_STORED));
    newDocument.add(new Field("file_path", reutersFile.getFilePath(), StringField.TYPE_STORED));
  
    // Indizado secuencial de los artículos dentro del documento.
    for (Article article : reutersFile.getArticles()) {
      if (!article.getTitle().isEmpty()) {
        newDocument.add(new Field("title", article.getTitle(), TextField.TYPE_STORED));
      }
      if (!article.getBody().isEmpty()) {
        newDocument.add(new Field("body", article.getBody(), TextField.TYPE_NOT_STORED));
      }
      if (!article.getAuthor().isEmpty()){
        newDocument.add(new Field("author", article.getAuthor(), StringField.TYPE_STORED));
      }
      if (!article.getDate().isEmpty()){
        Date dateObtained = DocumentParsing.parseDateString(article.getDate());
        newDocument.add(new LongPoint("date", dateObtained.getTime()));
      }
      for (String topic : article.getTopics()){
        newDocument.add(new Field("topics", topic, StringField.TYPE_STORED));
      }
    
      for (String place : article.getPlaces()){
        newDocument.add(new Field("places", place, StringField.TYPE_STORED));
      }
    
      for (String person : article.getPeople()){
        newDocument.add(new Field("people", person, StringField.TYPE_STORED));
      }
    
      for (String org : article.getOrgs()){
        newDocument.add(new Field("orgs", org, StringField.TYPE_STORED));
      }
    
      for (String exchange : article.getExchanges()){
        newDocument.add(new Field("exchanges", exchange, StringField.TYPE_STORED));
      }
    }
    indexWriter.addDocument(newDocument);
    System.out.println("Document " + reutersFile.getFileName() + " has been indexed.");
  }
  

  
  // TODO Implementar búsqueda.
  public void doSearch(java.util.Dictionary<String, String> userQueries) throws Exception {
    
  }
  
}
