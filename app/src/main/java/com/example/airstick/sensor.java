package com.example.airstick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.Locale.*;

public class sensor extends AppCompatActivity {
    private sensorStreamer ss;
    private float [] orientations = new float[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor);
        ss = sensorStreamer.getInstance();

        TextView sensorStatus_value=findViewById(R.id.sensorStatus_value);

        if (ss.get_sensorStatus()==0) {
            sensorStatus_value.setText(R.string.vectorsensor_fail);
        } else {
            sensorStatus_value.setText(R.string.online);
        }

        final TextView x_value = findViewById(R.id.x_value);
        final TextView y_value = findViewById(R.id.y_value);
        final TextView z_value = findViewById(R.id.z_value);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ss.get_orientations(orientations);
                        x_value.setText(String.format(CANADA,"%.2f", orientations[1]));
                        y_value.setText(String.format(CANADA,"%.2f", orientations[0]));
                        z_value.setText(String.format(CANADA,"%.2f", orientations[2]));
                    }
                });
            }
        }, 50, 50);




    }

}
