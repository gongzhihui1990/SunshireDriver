package net.iaf.framework.util;

import android.content.Context;
import android.util.Log;

import net.iaf.framework.http.HttpResult;
import net.iaf.framework.http.URLConnectionHelper;

import org.apache.http.HttpStatus;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class IpChecker {

    //获取本地局域网IP
    public static String getMyLocalIP(Context context) {
        //method 1
//		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifiManager.getConnectionInfo();
//        if(info!=null){
//        	return Formatter.formatIpAddress(info.getIpAddress());
//        }

        //method 2
//		try{
//            Socket socket = new Socket("www.baidu.com", 80);
//            return socket.getLocalAddress().toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //method3
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getMyRemoteIP() {
        try {
            HttpResult mHttpResult = URLConnectionHelper.executeGetRequest("http://ifconfig.me/ip", null);
            if (Integer.parseInt(mHttpResult.getStatusCode()) != HttpStatus.SC_OK) {
                return null;
            } else {
                Log.v("[getMyIP]", mHttpResult.getResponse());
                return mHttpResult.getResponse().replaceAll("\n", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param @param  ipBoundList 网段列表
     * @param @param  ip
     * @param @return
     * @return boolean
     * @throws Exception
     * @throws
     * @Title: isInArea
     * @Description: ip是否在特定的网络段内
     */
    public static boolean isInNetworkSegment(final ArrayList<String[]> ipBoundList, final String ip) throws Exception {
        if (ipBoundList == null || ipBoundList.size() == 0 || ip == null) {
            throw new Exception();
        }
        for (String[] ipBound : ipBoundList) {
            if (ipBound.length < 2) continue;
            if (IpChecker.ipInBound(ipBound[0], ipBound[1], ip)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param @param  ipbound “-”连接2个ip
     * @param @param  ip
     * @param @return
     * @return boolean
     * @throws
     * @Title: ipInBound
     * @Description: 检查IP是否在IP段内
     */
    public static boolean ipInBound(String ipBoundStart, String ipBoundEnd, String ip) {
        if (ipBoundStart == null || ipBoundEnd == null)
            throw new NullPointerException("ipbound is null");
        if (ip == null)
            throw new NullPointerException("ip is null");

        ipBoundStart = ipBoundStart.trim();
        ipBoundEnd = ipBoundEnd.trim();
        ip = ip.trim();

        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        if (!ipBoundStart.matches(REGX_IP) || !ipBoundEnd.matches(REGX_IP) || !ip.matches(REGX_IP))
            return false;

        String[] sips = ipBoundStart.split("\\.");
        String[] sipe = ipBoundEnd.split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }

        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }
}
