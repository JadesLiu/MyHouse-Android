package com.alisa.projet;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PeriAdapter extends ArrayAdapter<Peri> {
    //the list values in the List of type hero
    ArrayList<Peri> peris;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public PeriAdapter(Context context, int resource, int textViewId, ArrayList<Peri> peris) {
        super(context, resource, textViewId, peris);
        this.context = context;
        this.resource = resource;
        this.peris = peris;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        ImageView imageView = view.findViewById(R.id.picture_room_order);
        TextView txtname = view.findViewById(R.id.txt_room_order);

        //getting the hero of the specified position
        Peri peri = peris.get(position);


        //adding values to the list item
        loadPeri(peri, imageView);
        txtname.setText(peri.getName());
        //imageView.setImageDrawable(context.getResources().getDrawable(contact.getPicture()));


        //finally returning the view
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }

    public void loadPeri(Peri peri, ImageView imageView)
    {
        if(peri.getBmp() != null)
        {
            imageView.setImageBitmap(peri.getBmp());
        }
        else {
            Executor loader = Executors.newSingleThreadExecutor();
            loader.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String url = "https://myhouse.enovations.fr/" + peri.getUrl();
                        final URL pictureUrl = new URL(url);
                        peri.setBmp(BitmapFactory.decodeStream(pictureUrl.openConnection().getInputStream()));

                        ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(peri.getBmp());
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

