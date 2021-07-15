package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class AddDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_name = i.getStringExtra("nameRoom");
        String room_id = i.getStringExtra("idRoom");

        loaddevices();
    }
    public void loaddevices()
    {

        Context that = this;
        AndroidNetworking.get("https://myhouse.enovations.fr/device-types")
                //.addQueryParameter("type","room")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray peris = response.getJSONArray("device-types");

                            ArrayList<Peri> periList = new ArrayList<>();

                            for(int iPeri = 0; iPeri < peris.length(); ++iPeri)
                            {

                                final JSONObject peri = peris.getJSONObject(iPeri);

                                periList.add(new Peri(
                                        peri.getInt("id"),
                                        peri.getString("name"),
                                        peri.getString("picture")
                                ));
                            }

                            PeriAdapter adapter = new PeriAdapter(
                                    that,
                                    R.layout.activity_room,
                                    R.id.picture_lst_text_room,
                                    periList
                            );
                            Spinner spinner = findViewById(R.id.spin_device);
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

    public void add_device(View view) {

        Spinner device_id_type = findViewById(R.id.spin_device);
        EditText name = findViewById(R.id.txt_device_name);

        Intent i = getIntent();
        String token = i.getStringExtra("token");
        String room_id = i.getStringExtra("idRoom");
        String room_name = i.getStringExtra("nameRoom");

        Context that = this;

        String selectedName = name.getText().toString();
        Peri selectedPicture = (Peri) device_id_type.getSelectedItem();

        AndroidNetworking.post("https://myhouse.enovations.fr/device-create")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("name", selectedName)
                .addBodyParameter("idPicture", "" + selectedPicture.getId())
                .addBodyParameter("idDeviceType", "" + selectedPicture.getId())
                .addBodyParameter("idRoom", "" + room_id)
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

                        Toast toast = Toast.makeText(that, message, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }
}