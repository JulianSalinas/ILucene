package Application;

import UI.Window;

public class Main {

    /**
     * Esto son solo pruebas, lo que hace es imprimir la informacion necesaria de cada articulos dentro de un archivo XML
     */
    public static void main(String[] args){
        /*try {
            ArrayList<Article> articles = Article.findFromFile("C:\\Users\\Julian-PC\\Dropbox\\IV Semestre 2016\\Recuperación de información textual\\Tarea programada 2\\Reuters\\reut2-000.xml");
            for(Article article : articles)
                System.out.println(article);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }*/
        Window.Instance().setVisible(true);
    }
}
