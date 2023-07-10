package com.example.clinic;

import android.telephony.SmsManager;

public class SMSManager {
    public static void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}

