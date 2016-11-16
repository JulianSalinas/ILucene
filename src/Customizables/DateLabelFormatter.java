package Customizables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;

/**
 * Created by Esteban on 14/11/2016.
 */
public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter{
  
  private String datePattern = "dd/MM/yyyy";
  private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
  
  @Override
  public Object stringToValue(String text) throws ParseException {
    return dateFormatter.parseObject(text);
  }
  
  @Override
  public String valueToString(Object value) throws ParseException {
    if (value != null){
      Calendar calendar = (Calendar) value;
      return dateFormatter.format(calendar.getTime());
    }
    return "";
  }
}
