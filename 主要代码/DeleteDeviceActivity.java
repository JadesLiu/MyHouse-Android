package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
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

public class DeleteDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_device);

        loadright();
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

                            DeleteDeviceAdapter adapter = new DeleteDeviceAdapter(
                                    that,
                                    R.layout.activity_room,
                                    R.id.picture_lst_text_room,
                                    deviceList
                            );
                            //ListView listView = findViewById(R.id.lstDevices);
                            //listView.setAdapter(adapter);
                            Spinner spinner = findViewById(R.id.spin_device_delete);
                            spinner.setAdapter(adapter);

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
    public void delete(View view){

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_id = i.getStringExtra("idRoom");
        String room_name = i.getStringExtra("nameRoom");

        Spinner id_device = findViewById(R.id.spin_device_delete);

        Context that = this;

        Device selectedDevice = (Device)id_device.getSelectedItem();

        AndroidNetworking.post("https://myhouse.enovations.fr/device-delete")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("idDevice","" + selectedDevice.getId())
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        String message = "";
                        switch(response.code()){
                            case 200:
                                message = " requête correcte :-)";
                                Intent i = new Intent(that, intheroomActivity.class);
                                i.putExtra("token",token);
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

                        Toast toast = Toast.makeText(that,message,Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });

    }
}