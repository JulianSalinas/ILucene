package Controller;

import Model.Beans.QueryBean;
import Model.Beans.ResultBean;
import Model.Browser;
import Model.Index;
import Model.Registry;
import View.UIWindow;

import java.io.File;
import java.util.ArrayList;


public class Application {

    private Browser browser;
    private Index index;
    private Registry registry;
    private UIWindow window;

    public Index getIndex (){ return index; }
    public Browser getBrowser () { return browser; }

    public void run(){
        this.window = UIWindow.Instance(this);
        this.window.setVisible(true);
    }

    public ArrayList<File> openRegistry(String path){
        try {
            this.registry = new Registry(path);
            this.index = new Index(registry);
            this.browser = new Browser(index);
            return registry.getFiles();
        }
        catch (Exception e){
            UIWindow.Instance().showMessage("No se ha podido cargar el directorio");
            return new ArrayList<>();
        }
    }

    public void indexFiles(ArrayList<File>files){
        try {
            index.indexFiles(files);
        }
        catch (Exception e){
            UIWindow.Instance().showMessage("Error al indizar");
        }
    }

    public ArrayList<ResultBean> doSearch(QueryBean info) {
        try {
            if (index == null) {
                UIWindow.Instance().showMessage("No has cargado el directorio para realizar la consulta");
                return new ArrayList<>();
            }
            else return browser.doSearch(info);
        }
        catch (Exception e){
            UIWindow.Instance().showMessage("Error al realizar la búsqueda");
            return new ArrayList<>();
        }
    }

}
