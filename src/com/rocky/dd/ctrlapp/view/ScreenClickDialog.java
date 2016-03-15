package com.rocky.dd.ctrlapp.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Administrator on 2016/3/15.
 */
public class ScreenClickDialog {

    private  JDialog jd;
    private RFrame rFrame;

    public ScreenClickDialog(RFrame rFrame) {
        System.out.println("初始化屏幕控制器...");
        jd = new JDialog(rFrame.getFrame(), "屏幕控制器", false);
        // 640 360
        jd.setSize(700, 500);
        jd.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jd.add(new ScreenPanel(rFrame));
        jd.setVisible(true);
    }

/*    public static JDialog getInstance(JFrame jFrame) {

        if(jd == null) {
            System.out.println("初始化屏幕控制器...");
            jd = new JDialog(jFrame, "屏幕控制器", true);
            jd.setSize(640, 360);
            jd.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jd.add(new ScreenPanel());
        }

        return jd;
    }*/

    class ScreenPanel extends JPanel
            implements MouseListener { //  MouseMotionListener

        int x1 = 29;
        int x2 = 671;
        int y1 = 29;
        int y2 = 391;
        private RFrame rFrame;

        public ScreenPanel(RFrame rFrame) {
            addMouseListener(this);
//            addMouseMotionListener(this);
            this.rFrame = rFrame;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawLine(x1,y1,x2,y1);
            g2d.drawLine(x1,y2,x2,y2);
            g2d.drawLine(x1,y1,x1,y2);
            g2d.drawLine(x2,y1,x2,y2);

            g2d.setColor(Color.lightGray);
            int Number=1;
            for(int i = 54;i < 390;i+=25) {
                g2d.drawString(Number*50 + "",5,i);
                g2d.drawLine(30,i,670,i);
                Number++;
            }
            Number=1;
            for(int i = 54;i < 679;i+=25) {
                g2d.drawString(Number*5 + "",i-5,25);
                g2d.drawLine(i,30,i,390);
                Number++;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = (e.getX() - 30) * 2;
            int y = (e.getY() - 30) * 2;
            if(x > 0 && x < 1280 && y > 0 && y < 720) {
                rFrame.sendCommands("input tap " + x + " " + y);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }
}
