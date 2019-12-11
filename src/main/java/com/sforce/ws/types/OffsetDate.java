package com.sforce.ws.types;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class OffsetDate {

    private final LocalDate date;
    private final ZoneOffset offset;

    public OffsetDate(LocalDate date, ZoneOffset offset) {
        this.date = date;
        this.offset = offset;
    }

    public LocalDate getDate() {
        return date;
    }

    public ZoneOffset getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return date.toString() + offset.toString();
    }
}
