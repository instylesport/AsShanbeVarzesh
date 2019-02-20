package ir.amjadrad.asshanbevarzesh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import ir.amjadrad.asshanbevarzesh.app.App;

public class SMSReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

//        String strMessage = "";

        if (extras != null) {
            Object[] smsextras = (Object[]) extras.get("pdus");

            for (int i = 0; i < smsextras.length; i++) {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[]) smsextras[i]);

                String strMsgBody = smsmsg.getMessageBody().toString();
                String strMsgSrc = smsmsg.getOriginatingAddress();

//                strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;

                if (strMsgSrc.equals("+9821000550")) {
                    try {
                        String number = getNumber(strMsgBody);
                        App.confirm_code = number+"";
                        Log.i(">>>>>>>>>>>", number+"");
                    } catch (Exception e) {

                    }
                }

//                Log.i(TAG, strMessage);
            }

        }

    }

    private String getNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i)==':'){
                return str.substring(i+1 , i+6);
            }
        }

        return "";
    }

}