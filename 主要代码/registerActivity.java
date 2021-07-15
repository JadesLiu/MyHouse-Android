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
import com.androidnetworking.interfaces.OkHttpResponseListener;

import okhttp3.Response;

public class registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AndroidNetworking.initialize(this);
    }
    public void  register(View view){

        EditText txtName = findViewById(R.id.txt_registerName);
        EditText txtLogin = findViewById(R.id.txt_registerEmail);
        EditText txtPassword = findViewById(R.id.txt_registerPassword);

        Context that=this;

        AndroidNetworking.post("https://myhouse.enovations.fr/register")
                .addBodyParameter("name",txtName.getText().toString())
                .addBodyParameter("login",txtLogin.getText().toString())
                .addBodyParameter("password",txtPassword.getText().toString())
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        String message = "";
                        switch(response.code()){
                            case 200:
                                message = "Enregistrement réussie :-)";
                                Intent i = new Intent(that, MainActivity.class);
                                startActivity(i);
                                break;
                            case 500:
                                message = "Une erreur s'est produite sur le serveur";
                                break;
                            case 400:
                                message = "requete incorrect";
                                break;
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