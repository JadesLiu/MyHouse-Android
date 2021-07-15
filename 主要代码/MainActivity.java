package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidNetworking.initialize(this);

    }

    public void connect(View view){

        EditText txtLogin = findViewById(R.id.txt_loginEmail);
        EditText txtPassword = findViewById(R.id.txt_loginPassword);

        Context that=this;

        AndroidNetworking.post("https://myhouse.enovations.fr/auth")
                .addBodyParameter("login",txtLogin.getText().toString())
                .addBodyParameter("password",txtPassword.getText().toString())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");
                            Intent i = new Intent(that,piecesActivity.class);
                            i.putExtra("token",token);
                            startActivity(i);

                            Toast toast = Toast.makeText(that, token, Toast.LENGTH_SHORT);
                            toast.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });

    }

    public void register(View view){
        Intent i = new Intent(this,registerActivity.class);
        startActivity(i);
    }
}