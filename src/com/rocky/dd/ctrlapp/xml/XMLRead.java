package com.rocky.dd.ctrlapp.xml;

import com.rocky.dd.ctrlapp.view.RFrame;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/2/26.
 */
public class XMLRead extends DefaultHandler {

    private int startIndex;
    private String name;
    private String cmd;
    private RFrame frame;
    private JPanel panel;

    private final int START_INDEX_NONE = 0;
    private final int START_INDEX_PANEL = 1;
    private final int START_INDEX_BUTTON = 2;
    private final int START_INDEX_NAME = 3;
    private final int START_INDEX_CMD = 4;

    public XMLRead(RFrame frame) {
        this.frame = frame;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case ComponentLabels.L01_PANEL:
                startIndex = START_INDEX_PANEL;
                panel = new JPanel();
                panel.setBackground(Color.DARK_GRAY);
                panel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel label = new JLabel(attributes.getValue("name"));
                label.setBackground(Color.cyan);
                panel.add(label);
                break;
            case ComponentLabels.L02_BUTTON:
                startIndex = START_INDEX_BUTTON;
                break;
            case ComponentLabels.L03_NAME:
                startIndex = START_INDEX_NAME;
                break;
            case ComponentLabels.L03_CMD:
                startIndex = START_INDEX_CMD;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        startIndex = START_INDEX_NONE;
        if(ComponentLabels.L03_NAME.equals(qName)) {
            System.out.println("XML创建Button:"+name + ", 执行命令:" + cmd);
            JButton button = new JButton(name);
            button.addActionListener(new ButtonCMDActionListener(cmd));
            panel.add(button);
        } else if(ComponentLabels.L02_SELECT.equals(qName)) {
            System.out.println("XML创建Button:"+name + ", 读取文件:" + cmd);
            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(cmd))));) {
                String line  = br.readLine();
                LinkedList<String> list = new LinkedList<>();
                ArrayList<String> cmds = new ArrayList<>();
                while(line != null && !line.isEmpty()) {
                    if(!line.startsWith("#")) {

                    }
                }
                JComboBox comboBox = new JComboBox(list.toArray());
                comboBox.getSelectedIndex();
                JButton button = new JButton(name);
                button.addActionListener(new ButtonCMDActionListener(cmd));
                panel.add(button);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (ComponentLabels.L01_PANEL.equals(qName)) {
            frame.addPanels(panel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value =  new String(ch,start,length).trim();
            switch (startIndex) {
                case START_INDEX_NAME:
                    name = value;
                    break;
                case START_INDEX_CMD:
                    cmd = value;
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
