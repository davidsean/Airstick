package com.example.airstick;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView bluetoothStatus_value = findViewById(R.id.sensorStatus_value);
        final TextView sensorStatus_value = findViewById(R.id.sensorStatus_value);


        sensorStreamer ss = new sensorStreamer((SensorManager) getSystemService(SENSOR_SERVICE));
        ss.setInstance(ss);

        ss.set_bluetoothAdapter(BluetoothAdapter.getDefaultAdapter());
        if (ss.get_bluetoothStatus() == 1) {
            bluetoothStatus_value.setText(R.string.online);
            ss.set_pairedDevices(ss.get_bluetoothAdapter().getBondedDevices());
        }

        if (ss.get_sensorStatus()==1){
            sensorStatus_value.setText(R.string.online);
        }

        Button toSensor = findViewById(R.id.sensor_button);
        toSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), sensor.class);
                startActivity(intent);
            }
        });

        Button toBluetooth = findViewById(R.id.bluetooth_button);
        toBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bluetooth.class);
                startActivity(intent);
            }
        });
    }

}


class sensorStreamer {

    private static sensorStreamer m_sensorStreamer;
    private final Sensor rotationVectorSensor;
    private float[] m_orientations;
    private BluetoothAdapter bluetoothAdapter;
    private Set <BluetoothDevice> pairedDevices;

    sensorStreamer(SensorManager sm){

        rotationVectorSensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SensorEventListener rotationVectorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, sensorEvent.values);

                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);
                m_orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, m_orientations);
                //SensorManager.getOrientation(rotationMatrix, m_orientations);

                for (int i = 0; i < 3; i++) {
                    m_orientations[i] = (float) (Math.toDegrees(m_orientations[i]));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sm.registerListener(rotationVectorEventListener,
                rotationVectorSensor, 1000 );
    }

    public static sensorStreamer getInstance() {
            return m_sensorStreamer;
    }
    public void setInstance(sensorStreamer ss) {
        m_sensorStreamer = ss;
    }

    public void get_orientations(float[] orientations){
        System.arraycopy(m_orientations, 0, orientations, 0, 3);
    }

    public int get_sensorStatus(){
        if (rotationVectorSensor == null) {
            return 0;
        } else {
            return 1;
        }
    }

    public void set_bluetoothAdapter(BluetoothAdapter bAdapter){
        bluetoothAdapter = bAdapter;
    }

    public BluetoothAdapter get_bluetoothAdapter(){
        return bluetoothAdapter;
    }

    public void set_pairedDevices(Set<BluetoothDevice> pd){
        pairedDevices=pd;
    }

    public Set<BluetoothDevice> get_pairedDevices(){
        return pairedDevices;
    }

    public int get_bluetoothStatus(){
        if (bluetoothAdapter ==null) {
            return 0;
        }
        else {
            return 1;
        }
    }


}

