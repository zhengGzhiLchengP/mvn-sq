package com.feiqu.log.util;


/**
 * IP工具类
 *
 * @author chenlongfei
 * @version 2017-03-16
 * @see IpGetter
 */
public class IpGetter {

    /**
     * 获取本机的局域网ip地址，兼容Linux
     * @return String
     * @throws Exception
     */
    /*public static String getIp(){
        String localHostAddress = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while(allNetInterfaces.hasMoreElements()){
                NetworkInterface networkInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> address = networkInterface.getInetAddresses();
                while(address.hasMoreElements()){
                    InetAddress inetAddress = address.nextElement();
                    if(inetAddress != null && inetAddress instanceof Inet4Address){
                        if(!"127.0.0.1".equals(inetAddress.getHostAddress())) {
                            localHostAddress = inetAddress.getHostAddress();
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return localHostAddress;
    }*/
}