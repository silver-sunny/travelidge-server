package com.studio.core.global.utils;

public class PhoneNumberUtil {



    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return "";
        }

        // Extract digits only
        String digits = phoneNumber.replaceAll("\\D", "");

        // Format if starting with 010 and having 8 digits after
        if (digits.matches("^010\\d{8}$")) {
            return digits.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }

        return phoneNumber;
    }
}
