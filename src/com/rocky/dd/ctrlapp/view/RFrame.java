package com.rocky.dd.ctrlapp.view;

import com.rocky.dd.ctrlapp.service.SocketServices;
import com.rocky.dd.ctrlapp.xml.XMLRead;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * Created by Administrator on 2016/2/26.
 */
public class RFrame {

    private JFrame frame;
    private JTextArea showArea;
    private SocketServices service;
    private JScrollBar jScrollBar;
    private JTextField commandField;
    private JPanel jPanel;

    public RFrame(String title) {

        init(title);
        startSocketServices();
    }

    public void init(String title) {
        frame = new JFrame(title);
        frame.setSize(1200, 650);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        service.destory();
                        System.exit(0);
                    }
                }
        );

        JButton button = new JButton("COMMAND SEND");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String line = commandField.getText();
                commandField.setText(null);
                sendCommands(line);
            }
        });

        jPanel = new JPanel();

        commandField = new JTextField();
        commandField.setColumns(30);
        jPanel.add(commandField);
        jPanel.add(button);
        button = new JButton("获取设备信息");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                service.get10DevicesInfo();
            }
        });
        jPanel.add(button);

        button = new JButton("唤醒");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                sendCommands("file script/wakeupscreen.script");
            }
        });
        jPanel.add(button);

        button = new JButton("清屏");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
            }
        });
        jPanel.add(button);

        frame.add(jPanel, BorderLayout.NORTH);


        readXML();

        frame.add(jPanel, BorderLayout.CENTER);

        showArea = new JTextArea();
        showArea.setAutoscrolls(true);
        showArea.setEditable(false);
        showArea.setBackground(Color.darkGray);
        showArea.setForeground(Color.white);
        showArea.setVisible(true);
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(showArea);
        jsp.setMinimumSize(new Dimension(300, 300));
        jsp.setPreferredSize(new Dimension(100,150));
        jScrollBar = jsp.getVerticalScrollBar();
        frame.add(jsp, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * 显示消息
     *
     * @param s
     */
    public void showText(String s) {
        showArea.append("\n\r");
        showArea.append(s);
        // 滚动到最下面
        jScrollBar.setValue(jScrollBar.getMaximum());
    }

    /**
     * 发送消息
     *
     * @param line
     */
    public void sendCommands(String line) {
        try {

            //在系统标准输出上打印读入的字符串
            showText("Server: " + line);
            if (line != null && !line.isEmpty()) {
                // 读取文件
                if (line.startsWith("file")) {
                    String cmds[] = line.split(" ", 2);
                    File file = new File(cmds[1]);
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    cmds = null;
                    String cmd = br.readLine();
                    StringBuilder sb = new StringBuilder();
                    while (cmd != null && !cmd.isEmpty()) {
//                            showText("cmd : " + cmd);
                        cmd = cmd.trim();
                        if (!cmd.startsWith("#")) {
                            sb.append(cmd);
                            sb.append(';');
                        }
                        cmd = br.readLine();
                    }
                    line = sb.substring(0, sb.length() - 1);
//                        showText("执行的File内容: " + line);
                    br.close();
                }
                service.write(line);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 开启Socket服务器端
     */
    private void startSocketServices() {
        showText("开启Socket服务器端...");
        service = new SocketServices(this);
        service.accept();
        service.clean();
    }

    private void readXML() {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();

            XMLReader xmlReader = saxParser.getXMLReader();
            XMLRead handler = new XMLRead(this);
            xmlReader.setContentHandler(handler);

            xmlReader.parse(new InputSource("script/index.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    public SocketServices getService() {
        return service;
    }

    public void addPanels(JPanel panel) {
        jPanel.add(panel);
    }

    public JFrame getFrame() {
        return frame;
    }
}
