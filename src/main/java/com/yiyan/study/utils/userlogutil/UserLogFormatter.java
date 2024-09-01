package com.yiyan.study.utils.userlogutil;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLogFormatter extends Formatter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateFormat.format(new Date(record.getMillis()))).append(" ");
        sb.append(record.getLevel().getLocalizedName()).append(": ");
        sb.append(record.getMessage()).append("\n");
        return sb.toString();
    }
}