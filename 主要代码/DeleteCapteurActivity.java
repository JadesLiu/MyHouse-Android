package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
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

public class DeleteCapteurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_capteur);

        loadleft();
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

                            DeleteCapteurAdapter adapter = new DeleteCapteurAdapter(
                                    that,
                                    R.layout.activity_room,
                                    R.id.picture_lst_text_room,
                                    sensorList
                            );
                            //ListView listView = findViewById(R.id.lstCapteurs);
                            //listView.setAdapter(adapter);
                            Spinner spinner = findViewById(R.id.spin_sensor_delete);
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

        Spinner id_sensor = findViewById(R.id.spin_sensor_delete);

        Context that = this;

        Sensor selectedSensor = (Sensor)id_sensor.getSelectedItem();

        AndroidNetworking.post("https://myhouse.enovations.fr/sensor-delete")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("idSensor","" + selectedSensor.getId())
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