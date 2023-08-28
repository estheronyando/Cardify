package com.logicea.cardify.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Helper {
    private static Timestamp timestampToString;
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);


    public static Date getRequestedDateTime() {
        try {
            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(timestamp.toString());
            return parsedDate;

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }

    public static boolean isColorFormatValid(String color) {
        if (color == null || color.isEmpty()) {
            return true; // Allow null color
        }
        // Validate color format
        return Pattern.matches("^#[0-9a-fA-F]{6}$", color);
    }

}
