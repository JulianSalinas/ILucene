package View;

import Controller.Application;
import View.Other.CustomTab;

import javax.swing.*;

import static javax.swing.UIManager.setLookAndFeel;

public class UIWindow extends JFrame{

    private static UIWindow window;
    private UIBrowser UIBrowser;
    private UIIndex UIIndex;

    private JLabel wallpaper;
    private CustomTab customTab;
    private Application ctrl;

    public static UIWindow Instance(Application ctrl){
        if(window == null) window = new UIWindow(ctrl);
        return window;
    }

    public static UIWindow Instance(){
        return window;
    }

    private UIWindow(Application ctrl){
        this.ctrl = ctrl;
        setProperties();
        setTheme();
        setPanels();
        setWallpaper();
    }

    private void setProperties(){
        setTitle("ILucene");
        setSize(1005,550);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
    }

    private void setTheme(){
        try{setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");}
        catch(Exception e){ showMessage(e.getMessage()); }
    }

    private void setPanels(){
        customTab = new CustomTab();
        UIBrowser = new UIBrowser(ctrl);
        UIIndex = new UIIndex(ctrl);
        add(customTab);
        add(UIBrowser);
        add(UIIndex);
        UIBrowser.setVisible(false);
    }

    private void setWallpaper(){
        wallpaper = new JLabel();
        ImageIcon image = new ImageIcon(getClass().getResource("/View/Other/Totoro.jpg"));
        wallpaper.setIcon(image);
        wallpaper.setSize(getSize());
        getContentPane().add(wallpaper);
    }

    public void changePanel(int panel){
        if(panel == 0) { UIIndex.setVisible(true); UIBrowser.setVisible(false); }
        else{ UIIndex.setVisible(false); UIBrowser.setVisible(true); }
    }

    public void showMessage (String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

}
