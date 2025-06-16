package com.betafore.evoting.Util;

import java.text.DecimalFormat;
import java.util.Random;

public class OtpGenerateUtil {
    public static   String generateOTP() {
        return new DecimalFormat("000000")
            .format(new Random().nextInt(999999));
    }
}
