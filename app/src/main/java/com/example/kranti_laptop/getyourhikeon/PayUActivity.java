package com.example.kranti_laptop.getyourhikeon;//package hike.me.webify.getyourhikeon;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//
//import com.payumoney.core.PayUmoneySdkInitializer;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
///**
// * Created by varun on 03-09-2017.
// */
//
//public class PayUActivity extends AppCompatActivity {
//    String key,txnid,amount,productinfo,firstname,email,udf1,udf2,udf3,udf4,udf5,salt;
//    String hashSequence = key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt;
//    String serverCalculatedHash= hashCal("SHA-512", hashSequence);
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
//                PayUmoneySdkInitializer.PaymentParam.Builder();
//        builder.setAmount(amount)                          // Payment amount
//                .setTxnId(txnId)                                             // Transaction ID
//                .setPhone(phone)                                           // User Phone number
//                .setProductName(productName)                   // Product Name or description
//                .setFirstName(firstName)                              // User First name
//                .setEmail(email)                                            // User Email ID
//                .setsUrl(appEnvironment.surl())                    // Success URL (surl)
//                .setfUrl(appEnvironment.furl())                     //Failure URL (furl)
//                .setUdf1(udf1)
//                .setUdf2(udf2)
//                .setUdf3(udf3)
//                .setUdf4(udf4)
//                .setUdf5(udf5)
//                .setUdf6(udf6)
//                .setUdf7(udf7)
//                .setUdf8(udf8)
//                .setUdf9(udf9)
//                .setUdf10(udf10)
//                .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
//                .setKey(“enter merchant key”)                        // Merchant key
//                .setMerchantId(“enter merchant ID”);             // Merchant ID
//    }
//
//    public static String hashCal(String type, String hashString) {
//        StringBuilder hash = new StringBuilder();
//        MessageDigest messageDigest = null;
//        try {
//            messageDigest = MessageDigest.getInstance(type);
//            messageDigest.update(hashString.getBytes());
//            byte[] mdbytes = messageDigest.digest();
//            for (byte hashByte : mdbytes) {
//                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
//            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return hash.toString();
//    }
//}
