package Application;

import Model.QueryInfo;
import Model.Result;
import Model.Searcher;
import UI.UIWindow;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        UIWindow.Instance().setVisible(true);
        /*String[] fields = {"date", "id"};
        try {
            QueryInfo info = new QueryInfo();
            info.setContent("{19870226 TO 19870303} 235" );
            info.setFields(fields);
            info.setPath("C:\\Users\\Julian-PC\\Desktop\\Directory");
            info.setType("AND");
            ArrayList<Result>files = Searcher.performSearch(info);
            for(Result file : files){
                System.out.println(file);
            }
        } catch (Exception e) {
            e.printStackTrace();*/
    }
}