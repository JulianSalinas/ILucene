package View;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import Controller.Application;
import Model.Beans.QueryBean;
import Model.Beans.ResultBean;
import View.Other.CustomTextField;

public class UIBrowser extends JPanel {

    private JButton buttonSearch;
    private JButton buttonOpenFile;
    private JLabel browserTitle;
    private CustomTextField textFieldQuery;
    private JList<ResultBean> listResults;
    private Application app;

    public UIBrowser(Application app) {
        this.app = app;
        setProperties();
        setLabels();
        setQueryTextBox();
        setButtons();
        setListResults();
        setFunctions();
    }
    private void setProperties() {
        setLayout(null);
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 144));
        setBounds(20, 75, 965, 425);
    }
    private void setLabels() {
        browserTitle = new JLabel("Buscador");
        browserTitle.setForeground(Color.WHITE);
        browserTitle.setBounds(25, 25, 800, 25);
        browserTitle.setFont(new Font("Jokerman", Font.PLAIN, 15));
        add(browserTitle);
    }
    private void setQueryTextBox(){
        textFieldQuery = new CustomTextField("Realice aquí la consulta");
        textFieldQuery.setBounds(25,70,700,35);
        add(textFieldQuery);
    }
    private void setButtons() {
        buttonSearch = new JButton("Consultar");
        buttonOpenFile = new JButton("Visualizar");
        buttonSearch.setBounds(740,70,200,50);
        buttonOpenFile.setBounds(740,120,200,50);
        add(buttonSearch);
        add(buttonOpenFile);
    }
    private void setListResults(){
        listResults = new JList<>();
        listResults.setModel(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25,115,700,285);
        scrollPane.setViewportView(listResults);
        add(scrollPane);
    }
    private void setFunctions() {
        buttonSearch.addActionListener(actionEvent -> { doSearch(); });
        buttonOpenFile.addActionListener(actionEvent -> { openSelectedFile(); });
    }

    private void openSelectedFile(){
        try {
            File file = new File(listResults.getSelectedValue().getPath());
            Desktop.getDesktop().open(file);
        }
        catch (NullPointerException e){ UIWindow.Instance().showMessage("No hay ningun archivo seleccionado"); }
        catch (Exception e ){ UIWindow.Instance().showMessage("No se ha podido abrir el archivo");}
    }
    private void doSearch() {
        if(app.getIndex() != null) {
            QueryBean query = new QueryBean();
            query.setPath(app.getIndex().getPath());
            query.setContent(textFieldQuery.getText());
            showResults(app.doSearch(query));
        }
    }
    private void showResults( ArrayList<ResultBean> results){
        results.sort(Comparator.comparing(ResultBean::getId));
        DefaultListModel<ResultBean> model = (DefaultListModel<ResultBean>) listResults.getModel();
        model.removeAllElements();
        for (ResultBean resultBean : results)
            model.addElement(resultBean);

    }

}
