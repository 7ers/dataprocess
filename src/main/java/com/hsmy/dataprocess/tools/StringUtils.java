package com.hsmy.dataprocess.tools;

import org.springframework.lang.Nullable;

import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isValidRecord(String[] record) {
        if (record.length < 3)
            return false;

        if (record[0].contains(","))
            return false;
        
//        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
//                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
//        Pattern pattern = Pattern.compile(regex);
//        if (!pattern.matcher(record[0]).matches())
//            return false;

        return true;
    }
}
