package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Response;

public class AddRoomActivity extends AppCompatActivity {

    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        Intent i = getIntent();
        token = i.getStringExtra("token");

        loadpictures();
    }

    public void loadpictures()
    {

        Context that = this;
        AndroidNetworking.get("https://myhouse.enovations.fr/pictures")
                .addQueryParameter("type","room")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pics = response.getJSONArray("pictures");

                            ArrayList<Picture> picList = new ArrayList<>();

                            for(int iPicture = 0; iPicture < pics.length(); ++iPicture)
                            {

                                final JSONObject pic = pics.getJSONObject(iPicture);

                                picList.add(new Picture(
                                        pic.getInt("id"),
                                        pic.getString("url")
                                ));
                            }

                            /*PictureAdapter adapter = new PictureAdapter(
                                    that,
                                    R.layout.activity_add_room_layout,
                                    picList
                            );*/

                            /*ArrayAdapter<Picture> adapter = new ArrayAdapter<>(
                                    that,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    picList
                            );

                            Spinner spinner = findViewById(R.id.spin_room);
                            // Mise ne place de l’adaptateur dans le spinner
                            spinner.setAdapter(adapter);*/
                            PictureAdapter adapter = new PictureAdapter(
                                    that,
                                    R.layout.activity_add_room_layout,
                                    R.id.picture_dropdown_text,
                                    picList
                            );
                            Spinner spinner = findViewById(R.id.spin_room);
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
    public void add(View view){

        Spinner id_room = findViewById(R.id.spin_room);
        EditText name = findViewById(R.id.txt_room_name);

        Intent i = getIntent();
        String token = i.getStringExtra("token");

        Context that = this;

        String selectedName= name.getText().toString();
        Picture selectedPicture = (Picture)id_room.getSelectedItem();

        AndroidNetworking.post("https://myhouse.enovations.fr/room-create")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("name",selectedName)
                .addBodyParameter("idPicture","" + selectedPicture.getId())
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        String message = "";
                        switch(response.code()){
                            case 200:
                                message = " requête correcte :-)";
                                Intent i = new Intent(that, piecesActivity.class);
                                i.putExtra("token",token);
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