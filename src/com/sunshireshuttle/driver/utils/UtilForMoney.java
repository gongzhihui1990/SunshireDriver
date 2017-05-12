package com.sunshireshuttle.driver.utils;

import java.math.BigDecimal;

public class UtilForMoney {

    public static String fen2yuanStyle(String fen) {
        if (null == fen || fen.isEmpty()) {
            fen = "0";
        }
//		double double_fen = Double.parseDouble(String.valueOf(Long.parseLong(fen)));
        double double_fen = Double.parseDouble(String.valueOf((long) Double.parseDouble(fen)));
        BigDecimal bd = new BigDecimal(double_fen);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static String fen2yuan(String fen) {
        if (null == fen || fen.isEmpty()) {
            fen = "0";
        }
//		double double_fen = Double.parseDouble(String.valueOf(Long.parseLong(fen)));
        double double_fen = Double.parseDouble(String.valueOf((long) Double.parseDouble(fen)));
        BigDecimal bd = new BigDecimal(double_fen / 100.0);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static String fen2yuan(Double fen) {
        if (null == fen) {
            fen = (double) 0;
        }
//		double double_fen = Double.parseDouble(String.valueOf(Long.parseLong(fen)));
        double double_fen = fen;
        BigDecimal bd = new BigDecimal(double_fen / 100.0);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static String fen2yuan(Long fen) {
        if (null == fen) {
            fen = (long) 0;
        }
//		double double_fen = Double.parseDouble(String.valueOf(Long.parseLong(fen)));
        long double_fen = fen;
        BigDecimal bd = new BigDecimal(double_fen / 100.0);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static String yuan2fen(String yuan) {
        try {
            double double_yuan = Double.parseDouble(yuan);
            double double_fen = double_yuan * 100;
            BigDecimal bd = new BigDecimal(double_fen);
            bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
            return bd.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
            // TODO: handle exception
        }
    }
}
