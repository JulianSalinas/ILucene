package UI;

import javax.swing.*;

import static javax.swing.UIManager.setLookAndFeel;

public class Window extends JFrame {
  
  private static Window window;
  private JLabel wallpaper;
  private Chooser chooser;
  private Browser browser;
  private Indexer indexer;
  
  private Window() {
    setProperties();
    setTheme();
    setPanels();
    setWallpaper();
  }
  
  public static Window Instance() {
    if (window == null) window = new Window();
    return window;
  }
  
  private void setProperties() {
    setTitle("ILucene");
    setSize(1005, 550);
    setResizable(false);
    setLocationRelativeTo(null);
    getContentPane().setLayout(null);
  }
  
  private void setTheme() {
    try {
      setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception e) {
      showMessage(e.getMessage());
    }
  }
  
  private void setPanels() {
    chooser = new Chooser();
    browser = new Browser();
    indexer = new Indexer();
    add(chooser);
    add(browser);
    add(indexer);
    browser.setVisible(false);
  }
  
  private void setWallpaper() {
    wallpaper = new JLabel();
    ImageIcon image = new ImageIcon(getClass().getResource("/Customizables/Totoro.jpg"));
    wallpaper.setIcon(image);
    wallpaper.setSize(getSize());
    getContentPane().add(wallpaper);
  }
  
  public void changePanel(int panel) {
    if (panel == 0) {
      indexer.setVisible(true);
      browser.setVisible(false);
    } else {
      indexer.setVisible(false);
      browser.setVisible(true);
    }
  }
  
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
  }
  
}
