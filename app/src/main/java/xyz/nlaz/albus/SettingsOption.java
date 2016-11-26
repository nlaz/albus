package xyz.nlaz.albus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import static xyz.nlaz.albus.R.id.toggleButton;

/**
 * Created by Nick on 11/25/2016.
 */

public class SettingsOption extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsoption_layout);
        ToggleButton toggle = (ToggleButton)findViewById(toggleButton);
        SharedPreferences prefers = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (prefers.getBoolean("togglebuttonState",true)){
           toggle.setChecked(true);
        }
        else{
            toggle.setChecked(false);
        }
        initNotificationState(toggle);
        }
    public void changeNotificationState(View view) {
        boolean checked = ((ToggleButton)view).isChecked();
        if(checked)
        {
            // Daily Notificavations
            Calendar calender = Calendar.getInstance();
            calender.set(Calendar.HOUR_OF_DAY,07);
            calender.set(Calendar.MINUTE,30);
            Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            SharedPreferences prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("togglebuttonState",true);
            editor.apply();
            Toast.makeText(this, "Notifications are On", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Daily Notificavations
            Calendar calender = Calendar.getInstance();
            calender.set(Calendar.HOUR_OF_DAY,00);
            calender.set(Calendar.MINUTE,00);
            SharedPreferences prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("togglebuttonState",false);
            editor.apply();
            Toast.makeText(this, "Notifications are Off", Toast.LENGTH_SHORT).show();
        }
    }
    public void initNotificationState(ToggleButton toggle) {
        boolean checked = toggle.isChecked();
        if(checked)
        {
            // Daily Notificavations
            Calendar calender = Calendar.getInstance();
            calender.set(Calendar.HOUR_OF_DAY,07);
            calender.set(Calendar.MINUTE,30);
            Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        else
        {
            // Daily Notificavations
            Calendar calender = Calendar.getInstance();
            calender.set(Calendar.HOUR_OF_DAY,00);
            calender.set(Calendar.MINUTE,00);
        }
    }
}
