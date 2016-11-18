package View;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Properties;

import Controller.Application;
import Model.Beans.QueryBean;
import Model.Beans.ResultBean;
import Model.Browser;
import View.Other.CustomFormatter;
import View.Other.CustomTextField;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.*;

public class UIBrowser extends JPanel {

    /*********************************************************************
     * ************************** Parte visual ***************************
     *********************************************************************/

    private JButton buttonSearch;
    private JLabel browserTitle;
    private JDatePickerImpl beginPicker;
    private JDatePickerImpl endPicker;
    private JPanel panelDatePickers;
    private CustomTextField textFieldQuery;
    private JCheckBox checkBoxId;
    private JCheckBox checkBoxBody;
    private JCheckBox checkBoxTitle;
    private JCheckBox checkBoxAuthor;
    private JCheckBox checkBoxTopics;
    private JCheckBox checkBoxPlaces;
    private JCheckBox checkBoxOrgs;
    private JCheckBox checkBoxExchanges;
    private JCheckBox checkBoxDate;
    private JPanel panelCheckBoxes;
    private JList<ResultBean> listResults;
    private Application app;

    public UIBrowser(Application app) {
        this.app = app;
        setProperties();
        setLabels();
        setQueryTextBox();
        setDatePickers();
        setButtonSearch();
        setCheckboxes();
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
    private void setDatePickers() {
        panelDatePickers = new JPanel();
        checkBoxDate = new JCheckBox("Usar rango de fechas");
        checkBoxDate.setBounds(0,0,435,25);
        Properties calendarProperties = new Properties();
        calendarProperties.put("text.today", "Hoy");
        calendarProperties.put("text.month", "Mes");
        calendarProperties.put("text.year", "Año");
        UtilDateModel model = new UtilDateModel();
        model.setDate(1997, 1, 1);
        UtilDateModel model2 = new UtilDateModel();
        model2.setDate(1997, 12, 12);
        JDatePanelImpl datePanel1 = new JDatePanelImpl(model, calendarProperties);
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, calendarProperties);
        beginPicker = new JDatePickerImpl(datePanel1, new CustomFormatter());
        endPicker = new JDatePickerImpl(datePanel2, new CustomFormatter());
        beginPicker.setTextEditable(true);
        endPicker.setTextEditable(true);
        panelDatePickers.add(checkBoxDate);
        panelDatePickers.add(beginPicker);
        panelDatePickers.add(endPicker);
        panelDatePickers.setBounds(740,155,200,100);
        add(panelDatePickers);
    }
    private void setButtonSearch() {
        buttonSearch = new JButton("Consultar");
        buttonSearch.setBounds(740,70,200,50);
        add(buttonSearch);
    }
    private void setCheckboxes() {
        panelCheckBoxes = new JPanel();
        panelCheckBoxes.setBounds(25,115,700,30);
        JLabel jLabel = new JLabel("Buscar en: ");
        checkBoxId = new JCheckBox("ID");
        checkBoxBody = new JCheckBox("Body");
        checkBoxTitle = new JCheckBox("Title");
        checkBoxAuthor = new JCheckBox("Author");
        checkBoxTopics = new JCheckBox("Topics");
        checkBoxPlaces = new JCheckBox("Places");
        checkBoxOrgs = new JCheckBox("Orgs");
        checkBoxExchanges = new JCheckBox("Exchanges");
        panelCheckBoxes.add(jLabel);
        panelCheckBoxes.add(checkBoxId);
        panelCheckBoxes.add(checkBoxBody);
        panelCheckBoxes.add(checkBoxTitle);
        panelCheckBoxes.add(checkBoxAuthor);
        panelCheckBoxes.add(checkBoxTopics);
        panelCheckBoxes.add(checkBoxPlaces);
        panelCheckBoxes.add(checkBoxOrgs);
        panelCheckBoxes.add(checkBoxExchanges);
        add(panelCheckBoxes);
    }
    private void setListResults(){
        listResults = new JList<>();
        listResults.setModel(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25,155,700,245);
        scrollPane.setViewportView(listResults);
        add(scrollPane);
    }

    /*********************************************************************
     * **************************** Funciones ****************************
     *********************************************************************/

    private void setFunctions() {
        buttonSearch.addActionListener(actionEvent -> { doSearch(); });
    }

    private void doSearch() {
        try {
            QueryBean query = captureQueryInfo();
            showResults(app.doSearch(query));
        }
        catch (Exception e){ UIWindow.Instance().showMessage(e.getMessage()); }
    }

    private void showResults( ArrayList<ResultBean> results){
        DefaultListModel<ResultBean> model = (DefaultListModel<ResultBean>) listResults.getModel();
        model.removeAllElements();
        for (ResultBean resultBean : results) model.addElement(resultBean);
    }

    //Hacer cambios aqui
    private QueryBean captureQueryInfo(){
        QueryBean query = new QueryBean();
        query.setPath(app.getIndex().getPath());
        query.setType("AND");
        query.setContent(textFieldQuery.getText());
        query.setFields(getMarkedFields());
        return query;
    }

    private String[] getMarkedFields(){
        ArrayList<String> fields = new ArrayList<>();
        if(checkBoxId.isSelected()) fields.add("id");
        if(checkBoxBody.isSelected()) fields.add("body");
        if(checkBoxTitle.isSelected()) fields.add("title");
        if(checkBoxAuthor.isSelected()) fields.add("author");
        if(checkBoxDate.isSelected()) fields.add("date");
        if(checkBoxTopics.isSelected()) fields.add("topics");
        if(checkBoxPlaces.isSelected()) fields.add("places");
        if(checkBoxOrgs.isSelected()) fields.add("orgs");
        if(checkBoxExchanges.isSelected()) fields.add("exchanges");
        return fields.toArray(new String[fields.size()]);
    }

}
