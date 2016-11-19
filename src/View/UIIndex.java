package View;

import Controller.Application;
import View.Other.CustomTextField;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

public class UIIndex extends JPanel {

    private JFileChooser fileChooser;
    private JFileChooser directoryChooser;
    private JList<File>listFiles;
    private CustomTextField textFieldIndexFile;
    private JButton buttonSearchIndex;
    private JButton buttonAddFile;
    private JButton buttonRemoveFile;
    private JButton buttonOpenFile;
    private JButton buttonIndexFiles;
    private Application app;

    public UIIndex(Application app) {
        this.app = app;
        setProperties();
        setLabels();
        setTextBox();
        setLists();
        setFileChooser();
        setDirectoryChooser();
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
        JLabel labelAddFiles = new JLabel("Archivos indizados");
        labelAddFiles.setForeground(Color.WHITE);
        labelAddFiles.setBounds(25,25,800,25);
        labelAddFiles.setFont(new Font("Jokerman", Font.PLAIN, 15));
        add(labelAddFiles);
    }
    private void setTextBox(){
        textFieldIndexFile = new CustomTextField("Seleccionar directorio");
        textFieldIndexFile.setBounds(25,70,640,35);
        textFieldIndexFile.setEditable(false);
        add(textFieldIndexFile);
    }
    private void setLists(){
        listFiles = new JList<>();
        listFiles.setModel(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25,115,700,285);
        scrollPane.setViewportView(listFiles);
        add(scrollPane);
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
    private void setButtons() {
        buttonAddFile = new JButton("Añadir");
        buttonRemoveFile = new JButton("Remover");
        buttonOpenFile = new JButton("Visualizar");
        buttonIndexFiles = new JButton("Indizar nuevos");
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
        buttonAddFile.addActionListener(actionEvent -> { openFileChooser(); });
        buttonRemoveFile.addActionListener(actionEvent -> { removeFiles(); });
        buttonIndexFiles.addActionListener(actionEvent -> { indexFiles(); });
        buttonOpenFile.addActionListener(actionEvent -> { openSelectedFile(); });
        buttonSearchIndex.addActionListener(actionEvent -> { openRegistryFile() ;} );
    }

    private void openFileChooser(){
        if(app.getIndex() != null) {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION)
                addFiles(fileChooser.getSelectedFiles());
        }
        else UIWindow.Instance().showMessage("Primero debes seleccionar un directorio");
    }
    private void addFiles(File [] files){
        for (File file : files)
            ((DefaultListModel<File>) listFiles.getModel()).addElement(file);
    }
    private void addFiles(ArrayList<File> files){
        for (File file : files)
            ((DefaultListModel<File>) listFiles.getModel()).addElement(file);
    }
    private void removeFiles(){
        int [] selectedIndices = listFiles.getSelectedIndices();
        if(selectedIndices.length > 0)
            for(int i : selectedIndices)
                ((DefaultListModel<File>) listFiles.getModel()).remove(i);
    }
    private void openSelectedFile(){
        try {
            File file = listFiles.getSelectedValue();
            Desktop.getDesktop().open(file);
        }
        catch (NullPointerException e){ UIWindow.Instance().showMessage("No hay ningun archivo seleccionado"); }
        catch (Exception e ){ UIWindow.Instance().showMessage("No se ha podido abrir el archivo");}
    }

    private void openRegistryFile(){
        int returnVal = directoryChooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            String path = directoryChooser.getSelectedFile().getPath();
            ArrayList<File>files = app.openRegistry(path);
            addFiles(files);
            textFieldIndexFile.setText(path);
        }
    }
    private void indexFiles(){
        if(app.getIndex() != null) {
            ArrayList<File> files = new ArrayList<>();
            DefaultListModel<File> model = ((DefaultListModel<File>) listFiles.getModel());
            for (int i = 0; i < model.getSize(); i++) files.add(model.getElementAt(i));
            app.indexFiles(files);
        }
        else UIWindow.Instance().showMessage("Primero debes seleccionar un directorio");
    }

}
