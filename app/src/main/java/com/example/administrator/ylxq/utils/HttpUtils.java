package com.example.administrator.ylxq.utils;

import android.util.Base64;

import com.example.administrator.ylxq.entity.JsonEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Android Studio.
 * User: ShawnXiaoLai
 * Date: 2019/7/5
 * Time: 17:17
 */
public class HttpUtils {

    private static String path = "http://39.106.226.254/";
//    private static String path = "http://172.20.10.3:8888/";
    //获得注册码
    private static String zhucema = "getAuthCode";
    //注册请求接口
    private static String zhuce = "registerFinish";
    //登录接口
    private static String login = "loginAuth";
    //上传链元素
    private static String updateCJLYS = "dialMoneyOrder";
    //上传权重
    private static String updateCJQZ = "dialWeightOrder";

    private static String choujiang = "getDialset";
    //验证抽奖码
    private static String choujiangma = "activeCodeAuth";

    private static String getGameNumber = "getGameRound";

    private static String updateLYSNumber = "rechargeMoneyOrder";
    //获得修改密码的验证码
    private static String getUpdatePswYzm = "passwordGetAuthCode";

    private static String updatePsw = "modifierPassword";

    //获得全网总权重和总链元素数量接口
    private static String getAll = "getTotalMoenyAndWeight";
    //获得用户权重上限，兑换比例等信息
    private static String getWeigth = "getWeightConvers";
    //链元素兑换权重接口
    private static String duihuan = "moneyToWeight";
    //抽奖记录
    private static String userCJJL = "getUserDialRecode";

    private static String getWeigthList = "getUserWeightRecode";

    private static String getUserInfo = "getUserinfoByPhone";

    private static String userOrder =  "getUserDealOrder";

    public static final String GET_CODE = path + zhucema;

    public static final String ZHUCE = path + zhuce;

    public static final String LOGIN = path + login;

    public static final String CHOUJIANG = path + choujiang;

    public static final String CHOUJIANGMA = path + choujiangma;

    public static final String GET_ALL_INFO = path + getAll;

    public static final String GET_WEIGTH_INFO = path + getWeigth;

    public static final String DUIHUAN_QUANZHONG = path + duihuan;

    public static final String GET_GAME_NUMBER = path + getGameNumber;

    public static final String UPDATE_LYS_NUMBER = path + updateLYSNumber;

    public static final String GET_UPDATE_PSW_YZM = path+getUpdatePswYzm;

    public static final String UPDATE_PASSWORD = path + updatePsw;

    public static final String CHOUJIANG_JILU = path + userCJJL;

    public static final String UPDATE_CJ_LYS = path + updateCJLYS;

    public static final String UPDATE_CJ_QZ = path + updateCJQZ;

    public static final String GET_WEIGTH_List = path + getWeigthList;

    public static final String GET_USER_INFO = path + getUserInfo;

    public static final String GET_USER_MONEY_LIST = path + userOrder;
    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */

    public static final List<JsonEntity> mList = new ArrayList<>();
    /**
     * 当值为1时 说明修改密码 重新登录 为0时 说明是游客模式登录
     */
    public static String getLocalMacAddressFromIp() {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

        }

        return strMacAddr;
    }


    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }


    public static void savePicture(StringBuffer base64Code)throws Exception{
        byte[] b = Base64.decode(base64Code.toString().getBytes() , Base64.DEFAULT);
        //生成jpeg图片
        File file = new File("/storage/emulated/0");
        if (!file.exists()) {//判断文件目录是否存在，如不存在则新建
            file.mkdirs();
            file.createNewFile();
        }
//        fileNames[0] = fileName;
//        fileNames[1] = imgFilePath.replaceAll("/", "@");
//        fileNames[2] = fictitiousPath;
        OutputStream out = new FileOutputStream(file.getName());
        out.write(b);
        out.flush();
        out.close();
    }
}
