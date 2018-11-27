package com.cecgw.cq.util;

/**
 * Created by lifuyi on 2018/11/19.
 */
public class ESUtil {

    /**
     * 获取索引名
     * @param dateStr
     * @return
     */
    public static String getIndexName(String dateStr) {

        StringBuilder sb = new StringBuilder("cqct_");

        String[] arr = dateStr.split("-");
        sb.append(arr[0]);
        //检查月份
        String monthStr = null;
        switch (arr[1]) {
            case "01":
            case "02":
            case "03":
            case "04":
                monthStr = "0104";
                break;

            case "05":
            case "06":
            case "07":
            case "08":
                monthStr = "0508";
                break;

            case "09":
            case "10":
            case "11":
            case "12":
                monthStr = "0912";
                break;
        }
        sb.append(monthStr).append("_*");

        return sb.toString();
    }

}
