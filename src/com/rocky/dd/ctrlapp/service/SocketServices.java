package com.rocky.dd.ctrlapp.service;

import com.rocky.dd.ctrlapp.db.DBTools;
import com.rocky.dd.ctrlapp.view.RFrame;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/2/24.
 */
public class SocketServices {

    private final int INFO_NUMBER = 6;

    private ServerSocket server = null;
    private LinkedList<SocketClient> sockets;
    private RFrame frame;

    public SocketServices(RFrame frame) {
        this.frame = frame;
        try {
            sockets = new LinkedList<SocketClient>();
            //创建一个ServerSocket在端口4700监听客户请求
            server = new ServerSocket(8083);
        } catch (Exception e) {
            //出错，打印出错信息
            e.printStackTrace();
        }
    }

    public void accept() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (server != null) {
                            //使用accept()阻塞等待客户请求，有客户
                            Socket socket = server.accept();
//                            frame.showText("有一个Socket连接进来...");
                            SocketClient client = new SocketClient(socket, frame);
                            sockets.addLast(client);
                            client.read();
                        }
                    } catch (Exception e) {
                        //出错，打印出错信息
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 每30秒清除一次socket
     */
    public void clean() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Iterator<SocketClient> it = sockets.iterator();
                    while (it.hasNext()) {
                        SocketClient socketClient = it.next();
                        Socket socket = socketClient.getSocket();
                        if (socket == null || socket.isClosed() || !socket.isConnected()) {
                            System.out.println("清除已断开的Socket Client : " + socketClient.getDevice_no());
                            sockets.remove(socketClient);
                            socketClient.close();
                        }
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void write(String msg) {
        System.out.println("生成的CMD:" + msg);
        Iterator<SocketClient> it = sockets.iterator();
        while (it.hasNext()) {
            SocketClient socketClient = it.next();
            Socket socket = socketClient.getSocket();
            if (socket != null && !socket.isClosed() && socket.isConnected()) {
                socketClient.write(msg);
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void get10DevicesInfo() {
        System.out.println("get10DevicesInfo");
        ResultSet rs = null;
        try {
            DBTools dbTools = DBTools.getInstance();
            LinkedList<String> ids = new LinkedList<>();
            for (SocketClient sc : sockets) {
                if (sc != null) {
                    System.out.println("添加Socket NO:" + sc.getDevice_no());
                    ids.add(sc.getDevice_no());
                }
            }
            System.out.println("添加Socket成功:" + ids.size());
            // 选择sname这列数据             name = rs.getString("sname
            // 输出结果             System.out.println(rs.getString("sno") + "\t" + name);n
            for (String deviceinfo_id : ids) {
                ResultSet rs2 = dbTools.executeQuery("select count(id) from deviceinfos where deviceinfo_id='" + deviceinfo_id + "'");
                int idNumber;
                if(rs2 != null) {
                    rs2.next();
                    BigDecimal bd = rs2.getBigDecimal(1);
                     idNumber = INFO_NUMBER - bd.intValue();
                } else {
                    idNumber = INFO_NUMBER;
                }
                System.out.println(deviceinfo_id + " 数目为" + idNumber);
                if (idNumber > 0) {
                    String sql = "select id from deviceinfos where deviceinfo_id is null limit " + idNumber;
                    rs = dbTools.executeQuery(sql);
                    StringBuilder sb = new StringBuilder("update deviceinfos set deviceinfo_id='");
                    sb.append(deviceinfo_id);
                    sb.append("' where id in (");
                    while (rs.next()) {

                        sb.append(rs.getBigDecimal(1));
                        sb.append(",");
                    }
                    sb.replace(sb.length() - 1, sb.length(), "");
                    sb.append(")");
                    System.out.println(sb.toString());
                    dbTools.executeUpdate(sb.toString());
                }

            }

            for (SocketClient sc : sockets) {
                 rs =dbTools.executeQuery("select * from deviceinfos where deviceinfo_id='" + sc.getDevice_no() + "'");
                StringBuilder sb = new StringBuilder("[devicesinfo];");
                while(rs.next()) {
                    sb.append("REPLACE INTO device_info (`android_id`, `imei`, `mac_address`, `wifi_name`, `serial`, `bluetooth_mac`, `googlead_id`, `qq_number`, `qq_pwd`) VALUES(");
                    sb.append("'");
                    sb.append(rs.getString("android_id"));
                    sb.append("','");
                    sb.append(rs.getString("imei"));
                    sb.append("','");
                    sb.append(rs.getString("mac_address"));
                    sb.append("','");
                    sb.append(rs.getString("wifi_name"));
                    sb.append("','");
                    sb.append(rs.getString("serial"));
                    sb.append("','");
                    sb.append(rs.getString("bluetooth_mac"));
                    sb.append("','");
                    sb.append(rs.getString("googlead_id"));
                    sb.append("','");
                    sb.append(rs.getString("qq_number"));
                    sb.append("','");
                    sb.append(rs.getString("qq_pwd"));
                    sb.append("');");
                }
                sc.write(sb.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void destory() {
        try {
            Iterator<SocketClient> it = sockets.iterator();
            while (it.hasNext()) {
                SocketClient socketClient = it.next();
                socketClient.close();
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
