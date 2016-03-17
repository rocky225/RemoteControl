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
        panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.addPanels(panel);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case ComponentLabels.L01_PANEL:
                startIndex = START_INDEX_PANEL;

                JLabel label = new JLabel(attributes.getValue("name"));
                label.setBackground(Color.cyan);
                Font font = new Font("黑体", Font.BOLD, 18);
                label.setForeground(Color.WHITE);
                label.setFont(font);
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
                cmdBuffer = new StringBuffer();
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        startIndex = START_INDEX_NONE;
        switch (qName) {
            case ComponentLabels.L03_CMD:
                if(cmdBuffer.length() > 0) {
                    cmd = cmdBuffer.substring(0, cmdBuffer.length() - 1);
                } else {
                    cmd = "";
                }
                break;
            case ComponentLabels.L02_BUTTON:
                System.out.println("XML创建Button:" + name + ", 执行命令:" + cmd);
                JButton button = new JButton(name);
                button.addActionListener(new ButtonCMDActionListener(cmd));
                panel.add(button);
                break;
            case ComponentLabels.L02_SELECT:
                System.out.println("XML创建Select:" + name + ", 读取文件:" + cmd);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(cmd))));) {
                    String line = br.readLine();
                    LinkedList<String> list = new LinkedList<>();
                    ArrayList<String> cmds = new ArrayList<>();
                    while (line != null && !line.isEmpty()) {
                        if (!line.startsWith("#")) {
                            String[] s = line.split("::", 2);
                            list.add(s[0]);
                            cmds.add(s[1]);
                            System.out.println("Name : " + s[0] + ",CMD : " + s[1]);
                        }
                        line = br.readLine();
                    }
                    JComboBox comboBox = new JComboBox(list.toArray());
                    comboBox.getSelectedIndex();
                    comboBox.addActionListener(new SelectButtonCMDActionListener(comboBox, cmds));
//                JButton button = new JButton(name);
//                button.addActionListener(new SelectButtonCMDActionListener(comboBox,cmds));
                    panel.add(comboBox);
//                panel.add(button);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case ComponentLabels.L01_PANEL:
                JLabel label = new JLabel();
                label.setBackground(Color.cyan);
                label.setPreferredSize(new Dimension(100, 30));
                panel.add(label);
                break;

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        switch (startIndex) {
            case START_INDEX_NAME:
                name = value;
                break;
            case START_INDEX_CMD:
                addAvailableCommand(value);
                break;
        }
    }

    private StringBuffer cmdBuffer;
    private void addAvailableCommand(String cmd) {
        if(cmd != null && !cmd.isEmpty()) {
            String cmds[] = cmd.split("\n");
            for (int i = 0; i < cmds.length; i++) {
                cmd = cmds[i].trim();
                if (!cmd.isEmpty() && !cmd.startsWith("#")) {
                    cmdBuffer.append(cmd);
                    cmdBuffer.append(";");
                }
            }
        }
    }

    class ButtonCMDActionListener implements ActionListener {

        private String commond;

        public ButtonCMDActionListener(String cmd) {
            this.commond = cmd;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.sendCommands(commond);
        }
    }

    class SelectButtonCMDActionListener implements ActionListener {

        private JComboBox comboBox;
        private ArrayList<String> cmds;

        public SelectButtonCMDActionListener(JComboBox comboBox, ArrayList<String> cmds) {
            this.comboBox = comboBox;
            this.cmds = cmds;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int select = comboBox.getSelectedIndex();
            String commond = cmds.get(select);
            if (cmd != null && !cmd.isEmpty()) {
                frame.sendCommands(commond);
            }

        }
    }
}
