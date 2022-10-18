package dz.iyb.recoverydaycount;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
//interface with two methods, calculate days between two dates
//ge current date
public interface DateOperator {
    @RequiresApi(api = Build.VERSION_CODES.O)
    static String CalculateDays(String dateStart, String dateNow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date_start = LocalDate.parse(dateStart, formatter);
        LocalDate date_now = LocalDate.parse(dateNow, formatter);
        long daysBetween = ChronoUnit.DAYS.between(date_start, date_now);
        return String.valueOf(daysBetween);
    }

    static String getCurrentDate() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        return date;
    }
}
