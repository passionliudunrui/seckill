package com.xxxx.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    //        var str =salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);

    private static final String salt="1a2b3c4d";

    public static String inputPassToFromPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBPass(String formPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+ salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass,String salt){

        String fromPass=inputPassToFromPass(inputPass);
        String dbPass=fromPassToDBPass(fromPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {

        String s = inputPassToFromPass("123456");
        System.out.println(s);
        String str = fromPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9", "1a2b3c4d");
        System.out.println(str);


    }


}
