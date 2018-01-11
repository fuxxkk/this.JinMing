package com.myhadoop.wash;

import com.myhadoop.entity.WebLogBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class WebLogParserUtil {

    public static WebLogBean parserLog(String line) {

        String[] fields = line.split(" ");
        WebLogBean webLogBean = new WebLogBean();
        if (fields.length > 11) {
            webLogBean.setValid(true);
            webLogBean.setRemote_addr(fields[0]);
            webLogBean.setRemote_user(fields[1]);
            webLogBean.setTime_local(parserDate(fields[3].substring(1)));
            webLogBean.setRequest(fields[6]);
            webLogBean.setStatus(fields[8]);
            webLogBean.setBody_bytes_sent(fields[9]);
            webLogBean.setHttp_referer(fields[10]);

            if (fields.length>12) {
                StringBuilder sb = new StringBuilder();
                for(int i=11;i<fields.length;i++) {
                    sb.append(fields[i]);
                }
                webLogBean.setHttp_user_agent(sb.toString());
            }else {
                webLogBean.setHttp_user_agent(fields[11]);
            }
            if (Integer.valueOf(webLogBean.getStatus())>=400) {
                webLogBean.setValid(false);
            }
        }else {
            webLogBean.setValid(false);
        }


        return webLogBean;
    }

    public static String parserDate(String dateSrc) {
        String result = "";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date datesS = sdf1.parse(dateSrc);
             result = sdf2.format(datesS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //过滤静态资源
    public static WebLogBean filtStaticResource (WebLogBean webLogBean, Set<String> set) {

        if (!set.contains(webLogBean.getRequest())) {
            webLogBean.setValid(false);
        }

        return webLogBean;
    }
}
