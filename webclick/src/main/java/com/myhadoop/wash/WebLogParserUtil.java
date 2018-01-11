package com.myhadoop.wash;

import com.myhadoop.entity.WebLogBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebLogParserUtil {

    public static WebLogBean parserLog(String line) {

        String[] fields = line.split(" ");
        WebLogBean webLogBean = new WebLogBean();
        if (fields.length > 11) {

        }


        return webLogBean;
    }

    public static String parserDate(String dateSrc) {
        String result = "";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date datesS = sdf1.parse(dateSrc);
             result = sdf2.format(datesS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
