package halim.beta.ta.arduino;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class BroadcastSms extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle pbBundle=intent.getExtras();
		if (pbBundle!=null) {
				Object[] pdus=(Object[])pbBundle.get("pdus");
				SmsMessage[] sms=new SmsMessage[pdus.length];	
				String NumSender="",IsiSms="";
				
		 for (int i = 0; i < sms.length; i++) {
	          sms[i]=android.telephony.SmsMessage.createFromPdu((byte[])pdus[i]);
			  NumSender=sms[i].getOriginatingAddress();
					
			  IsiSms=sms[i].getMessageBody().toString();					
			  String result="Peringatan";
		 }
					
						 
		 Intent broadcastIntent = new Intent();
         broadcastIntent.setAction("SMS_RECEIVED_ACTION");
         broadcastIntent.putExtra("sms", IsiSms);
         context.sendBroadcast(broadcastIntent);
				 
				  }
			
		
	}

}
