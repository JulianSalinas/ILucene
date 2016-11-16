package UI;

import Model.Article;
import Customizables.JCTextField;
import Model.Indexer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;


public class UIIndexer extends JPanel {

    private JFileChooser fileChooser;
    private JFileChooser directoryChooser;
    private JLabel labelAddFiles;
    private JList<File>files;
    private JCTextField textFieldIndexFile;
    private JButton buttonSearchIndex;
    private JButton buttonAddFile;
    private JButton buttonRemoveFile;
    private JButton buttonOpenFile;
    private JButton buttonIndexFiles;

    public JTextField getTextFieldIndexFile() { return textFieldIndexFile;}

    public UIIndexer() {
        setProperties();
        setLabels();
        setTextBox();
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
        labelAddFiles = new JLabel("Indizar nuevos archivos");
        labelAddFiles.setForeground(Color.WHITE);
        labelAddFiles.setBounds(25,25,800,25);
        labelAddFiles.setFont(new Font("Jokerman", Font.PLAIN, 15));
        add(labelAddFiles);
    }

    private void setTextBox(){
        textFieldIndexFile = new JCTextField("Seleccionar directorio");
        textFieldIndexFile.setBounds(25,70,640,35);
        textFieldIndexFile.setEditable(false);
        add(textFieldIndexFile);
    }

    private void setLists(){
        files = new JList<>();
        files.setModel(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25,115,700,285);
        scrollPane.setViewportView(files);
        add(scrollPane);
    }

    private void setButtons() {
        buttonAddFile = new JButton("Añadir");
        buttonRemoveFile = new JButton("Remover");
        buttonOpenFile = new JButton("Visualizar");
        buttonIndexFiles = new JButton("Procesar");
        buttonSearchIndex = new JButton("...");
        buttonAddFile.setBounds(740,70,200,50);
        buttonRemoveFile.setBounds(740,120,200,50);
        buttonOpenFile.setBounds(740,170,200,50);
        buttonIndexFiles.setBounds(740,220,200,50);
        buttonSearchIndex.setBounds(675,70,50,35);
        add(buttonAddFile);
        add(buttonRemoveFile);
        add(buttonOpenFile);
        add(buttonIndexFiles);
        add(buttonSearchIndex);
    }

    private void setFunctions() {
        buttonAddFile.addActionListener(actionEvent -> { addSelectedFiles(); });
        buttonRemoveFile.addActionListener(actionEvent -> { removeSelectedFiles(); });
        buttonIndexFiles.addActionListener(actionEvent -> { indexSelectedFiles(); });
        buttonOpenFile.addActionListener(actionEvent -> { openSelectedFile(); });
        buttonSearchIndex.addActionListener(actionEvent -> { openIndexFile() ;} );
    }

    private void setFileChooser(){
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir archivo");
        FileNameExtensionFilter filter;
        filter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.setFileFilter(filter);
        fileChooser.setMultiSelectionEnabled(true);
    }

    private void setDirectoryChooser(){
        directoryChooser = new JFileChooser();
        directoryChooser.setDialogTitle("Abrir carpeta");
        directoryChooser.setMultiSelectionEnabled(false);
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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

    private void indexSelectedFiles(){
        try {
            UIWindow.Instance().showMessage("Procesando archivos...");
            DefaultListModel<File> model =  (DefaultListModel<File>) files.getModel();
            ArrayList<File> files = new ArrayList<>();
            for(int i=0;i<model.getSize();i++) files.add(model.getElementAt(i));
            Indexer.indexFiles(files,textFieldIndexFile.getText());
            UIWindow.Instance().showMessage("Finalizado con éxito...");
        }
        catch (Exception e){
            UIWindow.Instance().showMessage(e.getMessage());}
    }

    private void openSelectedFile(){
        try {
            File file = files.getSelectedValue();
            Desktop.getDesktop().open(file);
        }
        catch (NullPointerException e){ UIWindow.Instance().showMessage("No hay ningun archivo seleccionado"); }
        catch (Exception e ){ UIWindow.Instance().showMessage("No se ha podido abrir el archivo");}
    }

    private void openIndexFile(){
        try{
            if(directoryChooser == null) setDirectoryChooser();
            int returnVal = directoryChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION)
                textFieldIndexFile.setText(directoryChooser.getSelectedFile().getPath());
        }
        catch (Exception e ){ UIWindow.Instance().showMessage("No se ha podido abrir el directorio");}
    }

}
