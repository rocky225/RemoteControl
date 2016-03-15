package com.rocky.dd.ctrlapp;

import com.rocky.dd.ctrlapp.db.DBTools;
import com.rocky.dd.ctrlapp.info.*;

import java.io.*;

/**
 * Created by Administrator on 2016/3/12.
 */
public class CreateDevicesInfo {
    public static void  main(String[] args) {
//        deviceinfos
        try {
            DBTools dbTools = DBTools.getInstance();
            File file = new File("D:/qq.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = reader.readLine();
            while(line != null && !line.isEmpty()) {
                String[] qqs = line.split(" ");

//                System.out.println("qq:" + qqs[0] + ", pwd:" + qqs[1]);
                StringBuilder sb = new StringBuilder("INSERT INTO `deviceinfos` (`android_id`, `imei`, `mac_address`, `wifi_name`, `serial`, `bluetooth_mac`, `googlead_id`, `date`, `qq_number`, `qq_pwd`) VALUES (");
 //               INSERT INTO `deviceinfos` (`android_id`, `imei`, `mac_address`, `wifi_name`, `serial`, `bluetooth_mac`, `googlead_id`, `date`, `deviceinfo_id`, `qq_number`, `qq_pwd`)
 // //VALUES ('111', '111', '111', '111111', '111', '11', '11', '2016-03-12 16:08:40', '11', '11', '11')
                sb.append("'");
                sb.append(AndroidID.get());
                sb.append("','");
                sb.append(Imei.get());
                sb.append("','");
                sb.append(MAC.get());
                sb.append("','");
                sb.append(WifiName.get());
                sb.append("','");
                sb.append(SERIAL.get());
                sb.append("','");
                sb.append(MAC.get());
                sb.append("','unknown',now(),'");
                sb.append(qqs[0]);
                sb.append("','");
                sb.append(qqs[1]);
                sb.append("')");
//                System.out.println(sb.toString());
                dbTools.execute(sb.toString());
                line = reader.readLine();
            }
dbTools.destory();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
