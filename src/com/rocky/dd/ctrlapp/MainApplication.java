package com.rocky.dd.ctrlapp;

import com.rocky.dd.ctrlapp.service.SocketServices;
import com.rocky.dd.ctrlapp.view.RFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by Administrator on 2016/2/26.
 */
public class MainApplication {
    public static void main(String[] args)
    {

        MainApplication main = new MainApplication();
        main.init();

//        File file = new File("script/index.xml");
//        System.out.println(file.getAbsolutePath());

    }

    private RFrame frame;

    public void init() {
        frame = new RFrame("远程控制程序");
//        startSocketServices();
    }


}
