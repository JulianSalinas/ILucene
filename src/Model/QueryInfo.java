package Model;

public class QueryInfo {

    private String[] fields;
    private String content;
    private String path;
    private String type;

    public void setFields(String[] fields) {this.fields = fields;}
    public void setContent(String content) {this.content = content;}
    public void setPath(String path) {this.path = path;}
    public void setType(String type) {this.type = type; }

    public String[] getFields() {return fields;}
    public String getContent() {return content;}
    public String getPath() {return path;}
    public String getType() {return type;}

}
