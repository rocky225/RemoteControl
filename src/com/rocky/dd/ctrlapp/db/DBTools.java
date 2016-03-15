package com.rocky.dd.ctrlapp.db;

import java.sql.*;

/**
 * Created by Administrator on 2016/3/12.
 */
public class DBTools {
    private Connection conn = null;

    // 驱动程序名
    private String driver = "com.mysql.jdbc.Driver";
    // URL指向要访问的数据库名scutcs
    private String url = "jdbc:mysql://127.0.0.1:3306/sl";
    // MySQL配置时的用户名
    private String user = "root";
    // MySQL配置时的密码
    private String password = "";
    // statement用来执行SQL语句
    private Statement statement;

    private static DBTools dbTools;

    private DBTools() {
        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            conn = DriverManager.getConnection(url, user, password);
            if (!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");

            statement = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            destory();
        }
    }

    public static DBTools getInstance()  {
        if (dbTools == null) {
            dbTools = new DBTools();
        }
        return dbTools;
    }

    public ResultSet executeQuery(String sql)  {
        ResultSet rs = null;
        try {
             rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            destory();
        }finally {
            return rs;
        }
    }

    public void execute(String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            destory();
        }
    }

    public void executeUpdate(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            destory();
        }
    }

    public  void destory() {
        try {
            if(statement != null) {
                statement.close();
                statement = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
