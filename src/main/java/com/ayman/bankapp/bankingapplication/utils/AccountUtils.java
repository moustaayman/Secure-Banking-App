package com.ayman.bankapp.bankingapplication.utils;

import java.util.Random;

public class AccountUtils {

    public static String generateAccountNumber() {
        //the account number will be random 10 digits
        return String.format("%010d", new Random().nextInt(1000000000));
    }
}
