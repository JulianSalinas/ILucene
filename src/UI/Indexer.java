package UI;

import Collection.Article;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;


public class Indexer extends JPanel {

    private JFileChooser fileChooser;
    private JLabel labelAddFiles;
    private JList<File>files;
    private JButton buttonAddFile;
    private JButton buttonRemoveFile;
    private JButton buttonOpenFile;
    private JButton buttonIndexFiles;

    public Indexer(){
        setProperties();
        setLabels();
        setLists();
        setButtons();
        setFunctions();
    }

    private void setProperties(){
        setLayout(null);
        setOpaque(true);
        setBackground(new Color(0,0,0,144));
        setBounds(20,75,965, 425);
    }

    private void setLabels(){
        labelAddFiles = new JLabel("Añadir archivos");
        labelAddFiles.setForeground(Color.WHITE);
        labelAddFiles.setBounds(25,25,800,25);
        labelAddFiles.setFont(new Font("Jokerman", Font.PLAIN, 15));
        add(labelAddFiles);
    }

    private void setLists(){
        files = new JList<>();
        files.setModel(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25,70,700,330);
        scrollPane.setViewportView(files);
        add(scrollPane);
    }

    private void setButtons() {
        buttonAddFile = new JButton("Añadir");
        buttonRemoveFile = new JButton("Remover");
        buttonOpenFile = new JButton("Visualizar");
        buttonIndexFiles = new JButton("Indexar");
        buttonAddFile.setBounds(740,70,200,50);
        buttonRemoveFile.setBounds(740,120,200,50);
        buttonOpenFile.setBounds(740,170,200,50);
        buttonIndexFiles.setBounds(740,220,200,50);
        add(buttonAddFile);
        add(buttonRemoveFile);
        add(buttonOpenFile);
        add(buttonIndexFiles);
    }

    private void setFunctions() {
        buttonAddFile.addActionListener(actionEvent -> { addSelectedFiles(); });
        buttonRemoveFile.addActionListener(actionEvent -> { removeSelectedFiles(); });
        buttonIndexFiles.addActionListener(actionEvent -> { indexSelectedFiles(); });
        buttonOpenFile.addActionListener(actionEvent -> { openSelectedFile(); });
    }

    private void setFileChooser(){
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir archivo");
        FileNameExtensionFilter filter;
        filter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.setFileFilter(filter);
        fileChooser.setMultiSelectionEnabled(true);
    }

    private void addSelectedFiles(){
        if(fileChooser == null) setFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        for (File file : fileChooser.getSelectedFiles())
            ((DefaultListModel<File>) files.getModel()).addElement(file);
    }

    private void removeSelectedFiles(){
        int [] selectedIndices = files.getSelectedIndices();
        if(selectedIndices.length > 0)
            for(int i : selectedIndices)
                ((DefaultListModel<File>) files.getModel()).remove(i);
    }

    /**
     * Este no es fijo es solo para prober, aqui iria el metodos para indexar los archivos
     */
    private void indexSelectedFiles(){
        try {
            Window.Instance().showMessage("Procesando archivos...");
            DefaultListModel<File> model =  (DefaultListModel<File>) files.getModel();
            ArrayList<File> array = new ArrayList<>();
            for(int i=0;i<model.getSize();i++) array.add(model.getElementAt(i));
            ArrayList<Article> articles = Article.findFromFiles(array);

            /**
             * El arraylist articles es el que se tiene que meter en lucene
             */

            Article.printAll(articles);
        }
        catch (Exception e){Window.Instance().showMessage(e.getMessage());}
    }

    private void openSelectedFile(){
        try {
            File file = files.getSelectedValue();
            Desktop.getDesktop().open(file);
        }
        catch (NullPointerException e){ Window.Instance().showMessage("No hay ningun archio seleccionado"); }
        catch (Exception e ){ Window.Instance().showMessage("No se ha podido abrir el archivo");}
    }

}
