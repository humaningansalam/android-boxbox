package com.example.BoxBox.set;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Setdate {

    private static final Date currentTime = Calendar.getInstance().getTime();

    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static int year = Integer.parseInt(yearFormat.format(currentTime));
    public static int month = Integer.parseInt(monthFormat.format(currentTime));
    public static int day = Integer.parseInt(dayFormat.format(currentTime));
    public static String date = dateFormat.format(currentTime);


    public static int getYear() {
        Log.d("TAG", String.valueOf(year));
        return year;
    }

    public static void setYear(int year) {
        Setdate.year = year;
    }

    public static int getMonth() {
        Log.d("TAG", String.valueOf(month));
        return month;
    }

    public static void setMonth(int month) {
        Setdate.month = month;
    }

    public static int getDay() {
        Log.d("TAG", String.valueOf(day));
        return day;
    }

    public static void setDay(int day) {
        Setdate.day = day;
    }

    public void addDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        calendar.add(Calendar.DAY_OF_MONTH, days);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date = dateFormat.format(calendar.getTime());
    }

    public String todayDate(){
        Log.d("TAG", String.valueOf(date));
        return date;
    }
    /*

    public void nextDate(){

        mDate = new Date ( mDate.getTime ( ) + (long) ( 1000 * 60 * 60 * 24 ) );
        String getTime = simpleDate.format(mDate);

        textdate.setText(getTime);
    }

    public void prevDate(){

        mDate = new Date ( mDate.getTime ( ) - (long) ( 1000 * 60 * 60 * 24 ) );
        String getTime = simpleDate.format(mDate);

        textdate.setText(getTime);
    }
 */

    public String matchDate(int year, int month, int dayOfMonth){

        String date;
                if((month < 10) && (dayOfMonth < 10)){
                    date = year + "-0" + month + "-0" + dayOfMonth;
                }
                else if(month < 10){
                    date = year + "-0" + month + "-" + dayOfMonth;
                }
                else if(dayOfMonth < 10){
                    date = year + "-" + month + "-0" + dayOfMonth;
                }
                else{
                    date = year + "-" + month + "-" + dayOfMonth;
                }

                return date;
    }


}
