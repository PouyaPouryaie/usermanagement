package ir.bigz.springboot.userManagement.utils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface DateAndTimeTools {

    static Timestamp getTimestampNow() {

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateTimeString = dtf.format(localDateTime);
        Date dateTime = null;
        try {
            dateTime = sdf.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(dateTime.getTime());
    }
}
