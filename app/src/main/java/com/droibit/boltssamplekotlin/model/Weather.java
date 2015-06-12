package com.droibit.boltssamplekotlin.model;

/**
 * Created by kumagai on 2015/02/09.
 */
public class Weather {

    public String title;

    public Forecast[] forecasts;

    @Override
    public String toString() {
        if (forecasts != null && forecasts.length > 0) {
            return String.format("%s %s %s", title, forecasts[0].dateLabel, forecasts[0].telop);
        }
        return super.toString();
    }

    public static class Forecast {

        public String dateLabel;
        public String telop;

        @Override
        public String toString() {
            return dateLabel +  " / " + telop;
        }
    }
}