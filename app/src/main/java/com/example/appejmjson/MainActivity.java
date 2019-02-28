package com.example.appejmjson;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appejmjson.model.City;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_Json;
    private Button btn_Leer, btn_Escribir, btn_serialize, btn_unserialize, btn_leerAssets;
    private static City city = new City("ES", "Madrid", 40.5, -3.666667);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarUI();

        iniciarEventos();

    }

    private void iniciarEventos() {

        btn_Leer.setOnClickListener(this);
        btn_Escribir.setOnClickListener(this);
        btn_serialize.setOnClickListener(this);
        btn_unserialize.setOnClickListener(this);
        btn_leerAssets.setOnClickListener(this);

    }

    private void inicializarUI() {
        btn_Leer = findViewById(R.id.btn_Leer);
        btn_Escribir = findViewById(R.id.btn_Escribir);
        btn_serialize = findViewById(R.id.btn_serialize);
        btn_unserialize = findViewById(R.id.btn_unserialize);
        btn_leerAssets = findViewById(R.id.btn_leerAssets);
        txt_Json = findViewById(R.id.txt_Json);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_Escribir:
                writeJson();
                break;

            case R.id.btn_Leer:

                break;

            case R.id.btn_serialize:
                serializeClassGSON();
                break;

            case R.id.btn_unserialize:
                unserializeClassGSON();
                break;

            case R.id.btn_leerAssets:
                readFromAssets();
                        break;
        }

    }

    public void serializeClassGSON(){
        Gson gson = new Gson();
        String jsonString = gson.toJson(city);
        IOHelper.writeToFile(this,"cityJsonObj.txt", jsonString);
    }

    public void unserializeClassGSON(){
        Gson gson = new Gson();
        try{
            FileInputStream is = openFileInput("cityJsonObj.txt");
            String result = IOHelper.stringFromStream(is);

            City city = gson.fromJson(result, City.class);
            txt_Json.setText("Country : " + city.getCountry() + "\n" +
                    "Name : " + city.getName() + city.getCountry() + "\n" +
                    "Latitude,Longitud :" + city.getLatitude() + ", " + city.getLongitude());
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeJson(){
        IOHelper.writeToFile(this, "cityJsonObj.txt", city.toJsonString());
    }

    public void readJson(){
        String jsonString = IOHelper.stringFromAsset(this, "cities.json");

        try{
            JSONArray cities = new JSONArray(jsonString);

            String result = "";
            for(int i=0; i<cities.length(); i++){
                JSONObject city = cities.getJSONObject(i);

                result += "Country : " + city.get("country") + "\n" +
                        "Name : " + city.getString("name") + "\n" +
                        "Latitude,Longitude :" + city.getDouble("lat") + ", " + city.getString("lng");
            }
            txt_Json.setText(result);
        } catch (Exception e){

        }
    }


    public void readFromAssets(){
        AssetManager am = getAssets();

        try{
            InputStream is = am.open("cities.json");
            String result = IOHelper.stringFromStream(is);
            txt_Json.setText(result);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
