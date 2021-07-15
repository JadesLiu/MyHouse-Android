package com.alisa.projet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeviceAdapter extends ArrayAdapter<Device> {
    //the list values in the List of type hero
    ArrayList<Device> devices;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public DeviceAdapter(Context context, int resource, int textViewId, ArrayList<Device> devices) {
        super(context, resource, textViewId, devices);
        this.context = context;
        this.resource = resource;
        this.devices = devices;
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
        ImageView imageView = view.findViewById(R.id.picture_device_order);
        TextView txtname = view.findViewById(R.id.txt_device_order);
        TextView txttype = view.findViewById(R.id.txt_device_type);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch status = view.findViewById(R.id.switch_device);

        //getting the hero of the specified position
        Device device = devices.get(position);


        //adding values to the list item
        loadDevice(device, imageView);
        txtname.setText(device.getName());
        txttype.setText(device.getType());
        status.setChecked(device.getStatus() == 1);
        //txtstatus.setText(device.getStatus());
        //imageView.setImageDrawable(context.getResources().getDrawable(contact.getPicture()));


        //finally returning the view
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }

    public void loadDevice(Device device, ImageView imageView)
    {
        if(device.getBmp() != null)
        {
            imageView.setImageBitmap(device.getBmp());
        }
        else {
            Executor loader = Executors.newSingleThreadExecutor();
            loader.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String url = "https://myhouse.enovations.fr/" + device.getUrl();
                        final URL pictureUrl = new URL(url);
                        device.setBmp(BitmapFactory.decodeStream(pictureUrl.openConnection().getInputStream()));

                        ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(device.getBmp());
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

