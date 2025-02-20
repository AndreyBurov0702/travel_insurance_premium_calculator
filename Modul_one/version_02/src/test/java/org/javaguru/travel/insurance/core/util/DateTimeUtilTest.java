package org.javaguru.travel.insurance.core.util;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeUtilTest {
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();

    @Test
    public void shouldGetDaysBetweenSameDate() {
        Date date1 = createDate("01.01.2023");
        Date date2 = createDate("01.01.2023");
        assertEquals( dateTimeUtil.getDaysBetween(date1, date2), 0L);
    }
    @Test
    public void shouldGetDaysBetweenDifferentDates() {
        Date date1 = createDate("01.01.2023");
        Date date2 = createDate("10.01.2023");
        assertEquals(dateTimeUtil.getDaysBetween(date1, date2), 9L);
    }
    @Test
    public void shouldGetDaysBetweenNegativeDifference() {
        Date date1 = createDate("10.01.2023");
        Date date2 = createDate("01.01.2023");
        assertEquals(dateTimeUtil.getDaysBetween(date1, date2), -9L);
    }
    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
