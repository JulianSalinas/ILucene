package Model;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class Registry {

    private ArrayList<String> files;
    private String path;

    public String getPath() { return path; }

    public Registry (String path) throws Exception{
        this.path = path;
        this.files = loadFiles();
    }

    public Registry () throws Exception{
        this.path = System.getProperty("user.home") + "\\Desktop";
        this.files = loadFiles();
    }

    public ArrayList<File> getFiles() {
        ArrayList<File> oFiles = new ArrayList<>();
        for (String path : files)
            oFiles.add(new File(path));
        return oFiles;
    }

    public void save () throws Exception {
        Document document = buildDocument();
        Element root = document.getDocumentElement();
        for (String file : files) {
            Element itemNode = document.createElement("FILE");
            itemNode.setTextContent(file);
            root.appendChild(itemNode);
        }
        saveDocument(document);
    }

    public void add(File file){
        files.add(file.getAbsolutePath());
    }

    public boolean has(File file) {
        return files.contains(file.getAbsolutePath());
    }

    private ArrayList<String> loadFiles() throws Exception{
        File xmlFile = new File(path+"\\indexedFiles.xml");
        if(xmlFile.exists()) return readDocument(buildDocument(xmlFile));
        else return new ArrayList<>();
    }

    private Document buildDocument(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    private ArrayList<String> readDocument(Document document){
        NodeList paths = document.getElementsByTagName("FILE");
        ArrayList<String>files = new ArrayList<>();
        for (int i = 0; i < paths.getLength(); i++)
            files.add(paths.item(i).getTextContent());
        return files;
    }

    private Document buildDocument() throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        Document document = implementation.createDocument(null, "INDEXED_FILES", null);
        document.setXmlVersion("1.0");
        return document;
    }

    private void saveDocument(Document document) throws Exception{
        Source source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(path+"\\indexedFiles.xml"));
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }

}
