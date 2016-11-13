package UI;

import javax.swing.*;

public class Chooser extends JPanel{

    private JToggleButton btnIndexer;
    private JToggleButton btnBrowser;

    public Chooser(){
        setLayout(null);
        setOpaque(true);
        setBounds(20,20,965, 50);
        setButtons();
        setFunctions();
    }

    private void setButtons(){
        btnIndexer = new JToggleButton("Colección");
        btnBrowser = new JToggleButton("Consultas");
        btnIndexer.setBounds(0, 0, getWidth()/2, getHeight());
        btnBrowser.setBounds(getWidth()/2, 0, getWidth()/2, getHeight());
        btnIndexer.setSelected(true);
        add(btnIndexer);
        add(btnBrowser);
    }

    private void setFunctions(){

        btnIndexer.addActionListener(actionEvent -> {
            if(btnIndexer.isSelected()) {
                Window.Instance().changePanel(0);
                btnBrowser.setSelected(false);
            }
            else btnIndexer.setSelected(true);
        });

        btnBrowser.addActionListener(actionEvent -> {
            if(btnBrowser.isSelected()) {
                Window.Instance().changePanel(1);
                btnIndexer.setSelected(false);
            }
            else btnBrowser.setSelected(true);
        });

    }

}
