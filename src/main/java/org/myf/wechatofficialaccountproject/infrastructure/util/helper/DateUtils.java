package org.myf.wechatofficialaccountproject.infrastructure.util.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author myf
 */
public class DateUtils {

    public static String dateToString(Date date, String pattern) {

        if (Objects.isNull(date)) {
            date = new Date();
        }
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);

    }

    public static Date stringToDate(String date, String pattern) {

        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (StringUtils.isBlank(date)) {
            return new Date();
        }
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
