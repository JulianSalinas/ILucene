package View.Other;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomTextField extends JTextField{

    private String placeholder = "";
    private Color phColor= new Color(72,117,210);
    private boolean band = true;

    public CustomTextField(String placeholder) {
        super();
        this.placeholder = placeholder;
        setVisible(true);
        setMargin( new Insets(3,12,3,12));
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                band = getText().length() <= 0;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                band = false;
            }

            @Override
            public void changedUpdate(DocumentEvent de) {}

        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor( new Color(phColor.getRed(),phColor.getGreen(),phColor.getBlue(),90));
        g.drawString((band)?placeholder:"",
                getMargin().left,
                (getSize().height)/2 + getFont().getSize()/2 );
    }

}