package UI;

import javax.swing.*;

import static javax.swing.UIManager.setLookAndFeel;

public class UIWindow extends JFrame{

    private static UIWindow window;
    private JLabel wallpaper;
    private UIChooser UIChooser;
    private UIBrowser UIBrowser;
    private UIIndexer indexer;

    public JTextField getTextFieldIndexFile() { return indexer.getTextFieldIndexFile() ;}

    public static UIWindow Instance(){
        if(window == null) window = new UIWindow();
        return window;
    }

    private UIWindow(){
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
        UIChooser = new UIChooser();
        UIBrowser = new UIBrowser();
        indexer = new UIIndexer();
        add(UIChooser);
        add(UIBrowser);
        add(indexer);
        UIBrowser.setVisible(false);
    }

    private void setWallpaper(){
        wallpaper = new JLabel();
        ImageIcon image = new ImageIcon(getClass().getResource("/Customizables/Totoro.jpg"));
        wallpaper.setIcon(image);
        wallpaper.setSize(getSize());
        getContentPane().add(wallpaper);
    }

    public void changePanel(int panel){
        if(panel == 0) { indexer.setVisible(true); UIBrowser.setVisible(false); }
        else{ indexer.setVisible(false); UIBrowser.setVisible(true); }
    }

    public void showMessage (String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

}
