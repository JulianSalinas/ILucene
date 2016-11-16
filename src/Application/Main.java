package Application;

import java.io.File;

import UI.Window;

public class Main {
  
  /**
   * Esto son solo pruebas, lo que hace es imprimir la informacion necesaria de cada articulos
   * dentro de un archivo XML
   */
  public static void main(String[] args) {
    System.out.println("Program root folder: " + System.getProperty("user.dir"));
  
    File file = new File(System.getProperty("user.dir") + "pruebas.luc");
    System.out.println("Indice de prueba: " + file.getAbsolutePath());
  
  
    Window.Instance().setVisible(true);
  }
}
