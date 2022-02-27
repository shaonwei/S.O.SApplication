package com.example.sosapplication.nouse;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.SmsManager;

public class MyWhatsAppService {
/*
    private void handleActionWHATSAPP(String message,String count, String []mobile_number){
        try {
            PackageManager packageManager=getApplicationContext().getPackegemanager();
            if (mobile_number.length!=0){
                for (int j = 0; j < mobile_number.length; j++) {
                    for (int i = 0; i < Integer.parseInt(count.toString()); i++) {
                        String number=mobile_number[j];
                        String url = "https://api.whatsapp.com/send?phone=" + number + "&text="+message;
                        Intent intent= new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.whatsapp");
                        intent.setData(Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (intent.resolveActivity(packageManager)!=null){
                          getClass()
                        }
                        /*SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(mobile_number[j], null,message,null,null);
                        sendBroadcastMessage("Result:"+(i+1)+" "+mobile_number[j]);
                    }
                }
            }
        }
        catch (Exception e){

        }
    }*/
}
