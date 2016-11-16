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
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
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
  // TODO Probar algoritmo.
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
  
  
  // TODO Probar algoritmo.
  public ArrayList<Document> doSearch(java.util.Dictionary<String, String> userQuery)
      throws Exception {
    try {
      // Abre el archivo de índice de Lucene en modo lectura y genera el buscador.
      IndexSearcher searcher = new IndexSearcher(openIndexReader());
      
      BooleanQuery parsedQuery = prepareSearchQuery(userQuery);
  
      // Se muestran los 100 mejores resultados:
      TopDocs searchResults = searcher.search(parsedQuery, 100);
  
      // Los resultados se almacenan en un array de hits.
      ScoreDoc[] hits = searchResults.scoreDocs;
  
      // ArrayList que contendrá los documentos encontrados.
      ArrayList<Document> resultDocuments = new ArrayList<>();
      
      // Obtener documentos de array de hits.
      for (int hitIndex = 0; hitIndex < hits.length; hitIndex++){
        Document document = searcher.doc(hits[hitIndex].doc);
        resultDocuments.add(document);
      }
      
      return resultDocuments;
      
    }
    catch (Exception e){
      throw e;
    }
  }
  
  /**
   * Parsea la búsqueda del usuario para generar una consulta de Lucene.
   * @param userQueries Diccionario con la búsqueda ingresada por el usuario.
   * @throws Exception
   */
  private BooleanQuery prepareSearchQuery(java.util.Dictionary<String, String> userQueries)
      throws Exception {
  
    // Constructor de consulta para múltiples campos.
    BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
    
    if (userQueries.get("title") != null){
      QueryParser qpTitle = new QueryParser("title", IncomingReutersFile.getFieldsAnalyzers());
      Query qTitle = qpTitle.parse(userQueries.get("title"));
      queryBuilder.add(qTitle, BooleanClause.Occur.MUST);
    }
    if (userQueries.get("body") != null){
      QueryParser qpBody = new QueryParser("body", IncomingReutersFile.getFieldsAnalyzers());
      Query qBody = qpBody.parse(userQueries.get("body"));
      queryBuilder.add(qBody, BooleanClause.Occur.MUST);
    }
    if (userQueries.get("author") != null){
      QueryParser qpAuthor = new QueryParser("author", IncomingReutersFile.getFieldsAnalyzers());
      Query qAuthor = qpAuthor.parse(userQueries.get("author"));
      queryBuilder.add(qAuthor, BooleanClause.Occur.MUST);
    }
    if (userQueries.get("topics") != null){
      QueryParser qpTopics = new QueryParser("topics", IncomingReutersFile.getFieldsAnalyzers());
      Query qTopics = qpTopics.parse(userQueries.get("topics"));
      queryBuilder.add(qTopics, BooleanClause.Occur.MUST);
    }
    if (userQueries.get("places") != null){
      QueryParser qpPlaces = new QueryParser("places", IncomingReutersFile.getFieldsAnalyzers());
      Query qPlaces = qpPlaces.parse(userQueries.get("places"));
      queryBuilder.add(qPlaces, BooleanClause.Occur.MUST);
    }
    if (userQueries.get("people") != null){
      QueryParser qpPeople = new QueryParser("people", IncomingReutersFile.getFieldsAnalyzers());
      Query qPeople = qpPeople.parse(userQueries.get("people"));
      queryBuilder.add(qPeople, BooleanClause.Occur.MUST);
    }
    if (userQueries.get("orgs") != null){
      QueryParser qpOrgs = new QueryParser("orgs", IncomingReutersFile.getFieldsAnalyzers());
      Query qOrgs = qpOrgs.parse(userQueries.get("orgs"));
      queryBuilder.add(qOrgs, BooleanClause.Occur.MUST);
    }
    if (userQueries.get("exchanges") != null){
      QueryParser qpExchanges = new QueryParser
          ("exchanges", IncomingReutersFile.getFieldsAnalyzers());
      Query qExchanges = qpExchanges.parse(userQueries.get("exchanges"));
      queryBuilder.add(qExchanges, BooleanClause.Occur.MUST);
    }
    
    // TODO Incluir consulta en rango de fechas.
    if (userQueries.get("start_date") != null){
      if (userQueries.get("end_date") != null){
        // Hay fecha de inicio y fin.
      }
      else {
        // Sólo hay fecha de inicio.
      }
    }
    
    return queryBuilder.build();
  }
  
}
