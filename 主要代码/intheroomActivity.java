package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class intheroomActivity extends AppCompatActivity {

    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intheroom);

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_name = i.getStringExtra("nameRoom");
        String room_id = i.getStringExtra("idRoom");

        TextView textView= findViewById(R.id.txt_roomName);;
        textView.setText(room_name);

        loadleft();
        loadright();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadleft();
        loadright();
    }

    public  void  loadleft(){

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_id = i.getStringExtra("idRoom");

        Context that = this;

        AndroidNetworking.get("https://myhouse.enovations.fr/sensors")
                .addHeaders("Authorization", "Bearer " + token)
                .addQueryParameter("idRoom","" + room_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray sensors = response.getJSONArray("sensors");

                            ArrayList<Sensor> sensorList = new ArrayList<>();

                            for(int iSensor = 0; iSensor < sensors.length(); ++iSensor)
                            {

                                final JSONObject sensor = sensors.getJSONObject(iSensor);

                                sensorList.add(new Sensor(
                                        sensor.getInt("id"),
                                        sensor.getString("name"),
                                        sensor.getString("type"),
                                        sensor.getString("picture")
                                ));
                            }

                            SensorAdapter adapter = new SensorAdapter(
                                    that,
                                    R.layout.activity_sensor_layout,
                                    R.id.picture_lst_text_sensor,
                                    sensorList
                            );
                            ListView listView = findViewById(R.id.lstCapteurs);
                            listView.setAdapter(adapter);



                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    Intent i = getIntent();
                                    String token = i.getStringExtra("token");
                                    String room_id = i.getStringExtra("idRoom");

                                    Sensor sensor_info = (Sensor)listView.getItemAtPosition(position);
                                    String sensor_name=sensor_info.getName();
                                    int sensor_id =sensor_info.getId();

                                    AndroidNetworking.get("https://myhouse.enovations.fr/sensor-value")
                                            .addHeaders("Authorization", "Bearer " + token)
                                            .addQueryParameter("idSensor","" + sensor_id)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {

                                                        String value = response.getString("value");
                                                        String unit = response.getString("unit");

                                                        //TextView textView1 = listView.getAdapter().getView(position,null, listView).findViewById(R.id.txt_sensor_value);
                                                        TextView textView1 = listView.getChildAt(position).findViewById(R.id.txt_sensor_value);
                                                        TextView textView2 = listView.getChildAt(position).findViewById(R.id.txt_sensor_unit);
                                                        textView1.setText(value);
                                                        textView2.setText(unit);


                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                @Override
                                                public void onError(ANError anError) {
                                                    Toast toast = Toast.makeText(that,anError.getMessage(),Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            });

                                }

                            });


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(that,anError.getMessage(),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

    }


    class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String str = "";
            if (b == true) {
                str = "Open";
            } else {
                str = "Close";
            }
            Toast.makeText(intheroomActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    }



    public  void  loadright(){

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_id = i.getStringExtra("idRoom");

        Context that = this;

        AndroidNetworking.get("https://myhouse.enovations.fr/devices")
                .addHeaders("Authorization", "Bearer " + token)
                .addQueryParameter("idRoom","" + room_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray devices = response.getJSONArray("devices");

                            ArrayList<Device> deviceList = new ArrayList<>();

                            for(int iDevice = 0; iDevice < devices.length(); ++iDevice)
                            {

                                final JSONObject device = devices.getJSONObject(iDevice);

                                deviceList.add(new Device(
                                        device.getInt("id"),
                                        device.getString("name"),
                                        device.getString("type"),
                                        device.getString("picture"),
                                        device.getInt("status")
                                ));
                            }

                            DeviceAdapter adapter = new DeviceAdapter(
                                    that,
                                    R.layout.activity_device_layout,
                                    R.id.picture_lst_text_device,
                                    deviceList
                            );
                            ListView listView = findViewById(R.id.lstDevices);
                            listView.setAdapter(adapter);







                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    Intent i = getIntent();
                                    String token = i.getStringExtra("token");
                                    String room_id = i.getStringExtra("idRoom");
                                    String room_name = i.getStringExtra("nameRoom");

                                    Device device_info = (Device)listView.getItemAtPosition(position);
                                    String device_name=device_info.getName();
                                    int device_id =device_info.getId();


                                    @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switched = listView.getChildAt(position).findViewById(R.id.switch_device);
                                    if(!switched.isChecked())
                                    {
                                        switched.setChecked(true);
                                       device_info.setStatus(1);
                                        Toast.makeText(intheroomActivity.this, "Turn on", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        switched.setChecked(false);
                                        Toast.makeText(intheroomActivity.this, "Turn off", Toast.LENGTH_SHORT).show();
                                        device_info.setStatus(0);
                                    }


                                    int device_status = device_info.getStatus();
                                    AndroidNetworking.post("https://myhouse.enovations.fr/device-status")
                                            .addHeaders("Authorization", "Bearer " + token)
                                            .addBodyParameter("idDevice", "" + device_id)
                                            .addBodyParameter("status", "" + device_status)
                                            .build()
                                            .getAsOkHttpResponse(new OkHttpResponseListener() {
                                                @Override
                                                public void onResponse(Response response) {
                                                    String message = "";
                                                    switch (response.code()) {
                                                        case 200:
                                                            message = " requête correcte :-)";
                                                            Intent i = new Intent(that, intheroomActivity.class);
                                                            i.putExtra("token", token);
                                                            i.putExtra("nameRoom",room_name);
                                                            i.putExtra("idRoom",room_id);
                                                            startActivity(i);
                                                            break;
                                                        case 400:
                                                            message = "requête incorrecte";
                                                            break;
                                                        case 401:
                                                            message = " Accès non autorisé";
                                                            break;
                                                        default:
                                                            try {
                                                                throw new IllegalAccessException("Unexpected value: " + response.code());
                                                            } catch (IllegalAccessException e) {
                                                                e.printStackTrace();
                                                            }
                                                    }

                                                    //Toast toast = Toast.makeText(that, message, Toast.LENGTH_SHORT);
                                                    //toast.show();
                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                }
                                            });

/*
                                    AndroidNetworking.get("https://myhouse.enovations.fr/sensor-value")
                                            .addHeaders("Authorization", "Bearer " + token)
                                            .addQueryParameter("idDevice","" + device_id)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {

                                                        String value = response.getString("value");
                                                        String unit = response.getString("unit");

                                                        //TextView textView1 = listView.getAdapter().getView(position,null, listView).findViewById(R.id.txt_sensor_value);
                                                        TextView textView1 = listView.getChildAt(position).findViewById(R.id.txt_sensor_value);
                                                        TextView textView2 = listView.getChildAt(position).findViewById(R.id.txt_sensor_unit);
                                                        textView1.setText(value);
                                                        textView2.setText(unit);


                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                @Override
                                                public void onError(ANError anError) {
                                                    Toast toast = Toast.makeText(that,anError.getMessage(),Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            });

 */

                                }

                            });






                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(that,anError.getMessage(),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

    }

    public void add_capteur(View view){

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_name = i.getStringExtra("nameRoom");
        String room_id = i.getStringExtra("idRoom");
        Intent intent = new Intent(this,AddCapteurActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("nameRoom",room_name);
        intent.putExtra("idRoom",room_id);
        startActivity(intent);

    }
    public void add_device(View view) {

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_name = i.getStringExtra("nameRoom");
        String room_id = i.getStringExtra("idRoom");
        Intent intent = new Intent(this,AddDeviceActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("nameRoom",room_name);
        intent.putExtra("idRoom",room_id);
        startActivity(intent);
    }
    public void del_capteur(View view) {

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_name = i.getStringExtra("nameRoom");
        String room_id = i.getStringExtra("idRoom");
        Intent intent = new Intent(this,DeleteCapteurActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("nameRoom",room_name);
        intent.putExtra("idRoom",room_id);
        startActivity(intent);
    }
    public void del_device(View view) {

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_name = i.getStringExtra("nameRoom");
        String room_id = i.getStringExtra("idRoom");
        Intent intent = new Intent(this,DeleteDeviceActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("nameRoom",room_name);
        intent.putExtra("idRoom",room_id);
        startActivity(intent);
    }

}