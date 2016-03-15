package com.rocky.dd.ctrlapp.xml;

import com.rocky.dd.ctrlapp.view.RFrame;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Administrator on 2016/2/26.
 */
public class XMLRead extends DefaultHandler {

    private int startIndex;
    private String name;
    private String cmd;
    private RFrame frame;

    public XMLRead(RFrame frame) {
        this.frame = frame;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "Button":
                startIndex = 1;
                break;
            case "name":
                startIndex = 2;
                break;
            case "cmd":
                startIndex = 3;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        startIndex = 0;
        if("Button".equals(qName)) {
            System.out.println(name + "," + cmd);
            JButton button = new JButton(name);
            button.addActionListener(new ButtonCMDActionListener(cmd));
            frame.addButtons(button);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value =  new String(ch,start,length).trim();
            switch (startIndex) {
                case 2:
                    name = value;
                    System.out.println("Name is : " + name);
                    break;
                case 3:
                    cmd = value;
                    System.out.println("Cmd is : " + cmd);
                    break;
            }
        }

 class ButtonCMDActionListener implements ActionListener{

     private String commond;

     public ButtonCMDActionListener(String cmd) {
         this.commond = cmd;
     }

     @Override
     public void actionPerformed(ActionEvent e) {
         frame.sendCommands(commond);
     }
 }
}
