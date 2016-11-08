package Collection;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class Collection {

    private String path;
    private File[] files;
    private CollectionDocument [] documents;

    public String getPath(){ return path; }
    public File [] getFiles(){ return files; }

    public Collection(String path){
        this.path = path;
        initialize();
    }

    private void initialize(){
        try {
            files = searchFiles();
            searchDocuments();
        }
        catch (Exception e){ System.out.print(e.getMessage()); }
    }

    private File[] searchFiles () throws Exception {
        File file = new File(path);
        if(file.exists()){ return file.listFiles(); }
        else{ throw new Exception("El directorio no existe"); }
    }

    private void searchDocuments() throws  Exception {
        for (File file : files){
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList reuters = doc.getElementsByTagName("REUTERS");
            System.out.println("Archivo : " + file.getName());
            for (int i = 0; i < reuters.getLength(); i++) {
                Element element = (Element) reuters.item(i);

                /*Fecha del documento*/
                /*Date date = new SimpleDateFormat("dd MMM YYYY hh mm ss aa", Locale.US)
                .parse(element.getElementsByTagName("DATE").item(0).getTextContent());
                System.out.println(date.toString());*/
                /*
                //Categorias del documento (todas estan en el mismo string separadas por ",")
                Element topicsSection = (Element) element.getElementsByTagName("TOPICS").item(0);
                NodeList categories = topicsSection.getElementsByTagName("D");
                String topics = "";
                for(int topic = 0; topic < categories.getLength(); topic++)
                    topics += categories.item(topic).getTextContent() + " ";

                //Lugares geograficos, (igual que el anterior estan todos en un string)
                Element placesSection = (Element) element.getElementsByTagName("PLACES").item(0);
                NodeList placeElements = placesSection.getElementsByTagName("D");
                String places = "";
                for(int place = 0; place < placeElements.getLength(); place++)
                    places += placeElements.item(place).getTextContent() + " ";

                Element text = (Element) element.getElementsByTagName("TEXT").item(0);
                Element title = (Element) text.getElementsByTagName("TITLE").item(0);
                */
            }
        }
    }


}

