package Model.Beans;

public class QueryBean {

    private String content;
    private String path;

    public void setContent(String content) {this.content = content;}
    public void setPath(String path) {this.path = path;}

    public String getContent() {return content;}
    public String getPath() {return path;}

    @Override
    public String toString(){
        return content;
    }

}
