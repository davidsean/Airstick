package com.example.airstick;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class bluetooth extends AppCompatActivity {

    //Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    //startActivityForResult(turnOn,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);
        sensorStreamer ss = sensorStreamer.getInstance();
        TextView bluetoothStatus_value = findViewById(R.id.sensorStatus_value);
        TextView tv;
        if (ss.get_bluetoothStatus()==0) {
            bluetoothStatus_value.setText(R.string.bluetooth_status_fail);
            LinearLayout ll = findViewById((R.id.linearLayout));
            tv = new TextView(this);
            tv.setText(R.string.no_bt_device);
            ll.addView(tv);
        } else {
            bluetoothStatus_value.setText(R.string.online);
            LinearLayout ll = findViewById((R.id.linearLayout));
            for (BluetoothDevice bt : ss.get_pairedDevices()) {
                tv = new TextView(this);
                tv.setText(bt.getName());
                ll.addView(tv);
            }
        }
    }
}
