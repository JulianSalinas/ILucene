package Model;

import java.io.File;

public class Result{

    private String path;
    private String id;

    public Result(String path, String id){
        this.path = path;
        this.id = id;
    }

    public String getPath() { return path; }
    public String getId(){ return id; }

    @Override
    public String toString(){
        return "NEWID: " + id + "    " + "Ruta: " + path;
    }

}
