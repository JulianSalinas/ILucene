package UI;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.util.Properties;

import javax.swing.*;

import Customizables.JCTextField;

public class Browser extends JPanel {
  
  private JButton buttonSearch;
  private JLabel browserTitle;
  private JDatePickerImpl beginPicker;
  private JDatePickerImpl endPicker;
  private JPanel jpanel;
  
  private JCTextField body;
  private JCTextField title;
  private JCTextField author;
  private JCTextField topics;
  private JCTextField places;
  private JCTextField orgs;
  private JCTextField exchanges;
  
  private JCheckBox checkBoxBody;
  private JCheckBox checkBoxTitle;
  private JCheckBox checkBoxAuthor;
  private JCheckBox checkBoxDate;
  private JCheckBox checkBoxTopics;
  private JCheckBox checkBoxPlaces;
  private JCheckBox checkBoxOrgs;
  private JCheckBox checkBoxExchanges;
  
  public Browser() {
    setProperties();
    setLabels();
    setDatePickers();
    setTextboxAndButtons();
    setCheckboxes();
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
  
  private void setDatePickers() {
    UtilDateModel modelbegin = new UtilDateModel();
    Properties calendarProperties = new Properties();
    calendarProperties.put("text.today", "Hoy");
    calendarProperties.put("text.month", "Mes");
    calendarProperties.put("text.year", "Año");
    
    
    
    modelbegin.setDate(1997, 2, 1);
    JDatePanelImpl datePanel1 = new JDatePanelImpl(modelbegin, calendarProperties);
    datePanel1.setOpaque(true);
    datePanel1.setBackground(new Color(0, 0, 0, 144));
    
    UtilDateModel modelend = new UtilDateModel();
    
    
    modelend.setDate(1988, 1, 1);
    JDatePanelImpl datePanel2 = new JDatePanelImpl(modelend, calendarProperties);
    datePanel2.setOpaque(true);
    datePanel2.setBackground(new Color(0, 0, 0, 144));
    
    beginPicker = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
    endPicker = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
    
    beginPicker.setBounds(25, getHeight() - 50, 450, 30);
    endPicker.setBounds(490, getHeight() - 50, 450, 30);
    
    beginPicker.setTextEditable(true);
    endPicker.setTextEditable(true);
    
    add(beginPicker);
    add(endPicker);
  }
  
  private void setCheckboxes() {
    
    int size = (jpanel.getHeight() / 7) - 5;
    
    checkBoxBody = new JCheckBox("");
    checkBoxTitle = new JCheckBox("");
    checkBoxAuthor = new JCheckBox("");
    checkBoxDate = new JCheckBox("Consultar usando fechas");
    checkBoxTopics = new JCheckBox("");
    checkBoxPlaces = new JCheckBox("");
    checkBoxOrgs = new JCheckBox("");
    checkBoxExchanges = new JCheckBox("");
    
    checkBoxBody.setBounds(890, 5, 25, size);
    checkBoxTitle.setBounds(890, size + 5, 25, size);
    checkBoxAuthor.setBounds(890, size * 2 + 5, 25, size);
    checkBoxTopics.setBounds(890, size * 3 + 5, 25, size);
    checkBoxPlaces.setBounds(890, size * 4 + 5, 25, size);
    checkBoxOrgs.setBounds(890, size * 5 + 5, 25, size);
    checkBoxExchanges.setBounds(890, size * 6 + 5, 25, size);
    
    JPanel jpanelD = new JPanel();
    jpanelD.setBounds(25, getHeight() - 80, 915, 60);
    jpanelD.add(checkBoxDate);
    add(jpanelD);
    jpanel.add(checkBoxBody);
    jpanel.add(checkBoxTitle);
    jpanel.add(checkBoxAuthor);
    jpanel.add(checkBoxTopics);
    jpanel.add(checkBoxPlaces);
    jpanel.add(checkBoxOrgs);
    jpanel.add(checkBoxExchanges);
  }
  
  private void setTextboxAndButtons() {
    jpanel = new JPanel();
    jpanel.setLayout(null);
    jpanel.setBounds(25, 65, 915, 265);
    add(jpanel);
    
    int size = (jpanel.getHeight() / 7) - 5;
    
    body = new JCTextField("Body");
    title = new JCTextField("Title");
    author = new JCTextField("Authon");
    topics = new JCTextField("Topics");
    places = new JCTextField("Places");
    orgs = new JCTextField("Orgs");
    exchanges = new JCTextField("Exchanges");
    buttonSearch = new JButton("Consultar");
    
    body.setBounds(0, 5, 890, size);
    title.setBounds(0, size + 5, 890, size);
    author.setBounds(0, size * 2 + 5, 890, size);
    topics.setBounds(0, size * 3 + 5, 890, size);
    places.setBounds(0, size * 4 + 5, 890, size);
    orgs.setBounds(0, size * 5 + 5, 890, size);
    exchanges.setBounds(0, size * 6 + 5, 890, size);
    buttonSearch.setBounds(0, size * 7 + 5, 890, size);
    
    jpanel.add(body);
    jpanel.add(title);
    jpanel.add(author);
    jpanel.add(topics);
    jpanel.add(places);
    jpanel.add(orgs);
    jpanel.add(exchanges);
    jpanel.add(buttonSearch);
    
  }
  
  private void setFunctions() {
    buttonSearch.addActionListener(actionEvent -> {
      consult();
    });
  }
  
  private void consult() {
    
    /**
     * Aqui se extraerian todos los campos de texto para generar la(s) consultas
     */
    
  }
  
  
}
