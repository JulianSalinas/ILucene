package Collection;

import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Esteban on 14/11/2016.
 */
public class LuceneIndexer {
  
  private final String testFile = "test.lucene";
  private String indexFilePath;   // Ruta del archivo de índice.
  // Para utilizar analizadores para diferentes campos.
  private PerFieldAnalyzerWrapper analyzerWrapper;
  
  
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
  private IndexWriter getIndexWriter() throws Exception {
    if (!indexFilePath.isEmpty()) {
      try {
        Directory indexFile = SimpleFSDirectory.open(Paths.get(indexFilePath));
        
        IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
        writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(indexFile, writerConfig);
      } catch (IOException e) {
        throw new Exception("IOException - Index does not exist.");
        
      }
    }
    throw new Exception("File path not specified.");
  }
  
  /**
   * Abre el archivo de índice en modo lectura para realizar búsquedas.
   */
  private IndexReader getIndexReader() throws Exception {
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
  
  
  private void setFieldAnalyzers() {
    
  }
  
}
