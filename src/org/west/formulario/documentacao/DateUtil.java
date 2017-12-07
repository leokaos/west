package org.west.formulario.documentacao;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(field, amount);

        return calendar.getTime();
    }
}
