package Model.Beans;

public class ResultBean {

    private String id;
    private String title;
    private String path;

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPath(String path) { this.path = path; }

    public String getId(){ return id; }
    public String getTitle(){ return title; }
    public String getPath(){ return path; }

    @Override
    public String toString(){
        return "ID: " + id + "    " + "Title: " + title;
    }

}
