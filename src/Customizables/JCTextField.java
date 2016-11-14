package Customizables;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class JCTextField extends JTextField{

    private Dimension d = new Dimension(200,32);
    private String placeholder = "";
    private Color phColor= new Color(72,117,210);
    private boolean band = true;

    public JCTextField(String placeholder)
    {
        super();
        this.placeholder = placeholder;
        setSize(d);
        setPreferredSize(d);
        setVisible(true);
        setMargin( new Insets(3,12,3,12));
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                band = (getText().length()>0) ? false:true ;
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