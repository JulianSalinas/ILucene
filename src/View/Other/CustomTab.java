package View.Other;

import View.UIWindow;

import javax.swing.*;

public class CustomTab extends JPanel{

    private JToggleButton btnIndexer;
    private JToggleButton btnBrowser;

    public CustomTab(){
        setProperties();
        setButtons();
        setFunctions();
    }

    private void setProperties(){
        setLayout(null);
        setOpaque(true);
        setBounds(20,20,965, 50);
    }

    private void setButtons(){
        btnIndexer = new JToggleButton("Colección");
        btnBrowser = new JToggleButton("Consultas");
        btnIndexer.setBorder(null);
        btnBrowser.setBorder(null);
        btnIndexer.setBounds(0, 0, getWidth()/2, getHeight());
        btnBrowser.setBounds(getWidth()/2, 0, getWidth()/2, getHeight());
        btnIndexer.setSelected(true);
        add(btnIndexer);
        add(btnBrowser);
    }

    private void setFunctions(){
        btnIndexer.addActionListener(actionEvent -> {changeToIndexerPanel();});
        btnBrowser.addActionListener(actionEvent -> {changeToBrowserPanel();});
    }

    private void changeToIndexerPanel(){
        if(btnIndexer.isSelected()) {
            UIWindow.Instance().changePanel(0);
            btnBrowser.setSelected(false);
        } else btnIndexer.setSelected(true);
    }

    private void changeToBrowserPanel(){
        if(btnBrowser.isSelected()) {
            UIWindow.Instance().changePanel(1);
            btnIndexer.setSelected(false);
        } else btnBrowser.setSelected(true);
    }

}
