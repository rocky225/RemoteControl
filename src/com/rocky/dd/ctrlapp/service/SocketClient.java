package com.rocky.dd.ctrlapp.service;

import com.rocky.dd.ctrlapp.view.RFrame;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/24.
 */
public class SocketClient  {

    private Socket socket;
    private BufferedReader is;
    private PrintWriter os;
    private boolean running = true;
    private RFrame frame;

    private String Android_id;
    private String device_no;
    private Date connect_date;

    public SocketClient(Socket socket,RFrame frame) {
        this.socket = socket;
        this.frame = frame;
        if (socket != null && socket.isConnected()) {
            try {
                is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                os = new PrintWriter(socket.getOutputStream());
                getAndroidID(is);
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }

        }
    }

    private void getAndroidID(BufferedReader is) {
        try {
            write("getAndroidId");
//            System.out.println("getAndroidId...");
            device_no = is.readLine();
            Android_id = is.readLine();
            connect_date = new Date();
            frame.showText("新设备连接: ANDROID设备ID = " + device_no + ",连接时间: " + connect_date + ",ANDROID_ID = " + Android_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        new Thread(new Read()).start();
    }

    public void write(String msg) {
        os.println(msg);
        //向客户端输出该字符串
        os.flush();
    }

    public void close() {
        try {
            frame.showText("清除过期设备: ANDROID设备ID = " + device_no + ",连接时间: " + connect_date + ",ANDROID_ID = " + Android_id);
            running = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
                socket = null;
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Read implements Runnable {

        @Override
        public void run() {
            while (running) {
                if (is != null) {
                    try {
                        String line = is.readLine();
                        if(line != null) {
                            frame.showText("客户端: " + line);
                        } else {
                            close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        close();
                    }
                }
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getIs() {
        return is;
    }

    public void setIs(BufferedReader is) {
        this.is = is;
    }

    public PrintWriter getOs() {
        return os;
    }

    public void setOs(PrintWriter os) {
        this.os = os;
    }

    public String getAndroid_id() {
        return Android_id;
    }

    public String getDevice_no() {
        return device_no;
    }

    public Date getConnect_date() {
        return connect_date;
    }
}
