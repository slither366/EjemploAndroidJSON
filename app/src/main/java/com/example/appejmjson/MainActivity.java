package com.example.appejmjson;

import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appejmjson.model.City;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_Json;
    private Button btn_Leer, btn_Escribir, btn_serialize, btn_unserialize, btn_leerAssets;
    private static City city = new City("ES", "Madrid", 40.5, -3.666667);
    private static City city2 = new City("EN", "Peru", 140.5, -103.666667);
    Button leer, escribir, borrar;
    private String archivo = "miarchivo";
    private String carpeta = "/archivos/";
    String contenido;
    File file;
    String file_path = "";
    private EditText texto;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarUI();

        iniciarEventoCrearCarpeta();

        iniciarEventoEjemplo();

        iniciarEventos();

    }

    private void iniciarEventoCrearCarpeta() {

        // Verifica si disco externo esta montado
        if(isExternalStorageWritable()){
            Log.d("dflores", "El almacenamiento externo esta disponible :)");
        }else{
            Log.e("dflores", "El almacenamiento externo no esta disponible :(");
        }

        if(isExternalStorageWritable()){
            String nombreDirectorioPublico = "MyFilePrueba";
            crearDirectorioPublico(nombreDirectorioPublico);
        }

    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void iniciarEventoEjemplo() {

        this.file_path = (Environment.getExternalStorageDirectory() + this.carpeta);
        File localFile = new File(this.file_path);
        Toast.makeText(this, "" + file_path, Toast.LENGTH_SHORT).show();
        Log.d("Log:", file_path);

        if(!localFile.exists()){
            localFile.mkdirs();
        }

        this.name = (this.archivo + ".txt");
        Toast.makeText(this,"Archivo: " + name, Toast.LENGTH_SHORT).show();
        this.file = new File(localFile, this.name);

        try{
            this.file.createNewFile();
            Toast.makeText(this, "Se creo archivo 2", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file.exists()){
            leer();
        }

        leer.setOnClickListener(this);
        escribir.setOnClickListener(this);
        borrar.setOnClickListener(this);

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
        escribir = findViewById(R.id.escribir);
        leer = findViewById(R.id.leer);
        borrar = findViewById(R.id.borrar);
        texto = findViewById(R.id.texto);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_Escribir:
                //writeJson();
                readFromAssets();
                break;

            case R.id.btn_Leer:
                readJson();
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

            case R.id.leer:
                leer();
                break;

            case R.id.escribir:
                escribir();
                break;

            case R.id.borrar:
                texto.setText("");
                break;
        }

    }

    public void serializeClassGSON(){
        Gson gson = new Gson();
        String jsonString = gson.toJson(city2);
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

                //City city2 = new Gson().fromJson(city.toString(), City.class);

                result += "Country : " + city.get("country") + "\n" +
                        "Name : " + city.getString("name") + "\n" +
                        "Latitude,Longitude :" + city.getDouble("lat") + ", " + city.getString("lng");
            }
            txt_Json.setText(result);
        } catch (Exception e){
            Log.d("ReadPlacesFeedTask", e.getLocalizedMessage());
        }
    }

    ArrayList<String> numberlist = new ArrayList<>();
    public void readFromAssets(){
        AssetManager am = getAssets();
        String json;

        try{
            InputStream is = am.open("cities.json");
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                if(obj.getString("name").equals("Madrid")){
                    numberlist.add(obj.getString("country"));
                }
            }

            Toast.makeText(getApplicationContext(),numberlist.toString(),Toast.LENGTH_LONG).show();

            /*
            String result = IOHelper.stringFromStream(is);
            txt_Json.setText(result);
            is.close();*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void escribir(){
        FileWriter fichero = null;
        PrintWriter pw = null;

        try{
            fichero = new FileWriter(file);
            pw = new PrintWriter(fichero);
            pw.println(texto.getText().toString());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(null != fichero)
                    fichero.close();
            } catch(Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void leer(){
        String contenido = "";
        FileInputStream fIn = null;

        try{
            fIn = new FileInputStream(file);
        } catch (FileNotFoundException e1){
            e1.printStackTrace();
        }

        InputStreamReader archivo = new InputStreamReader(fIn);
        BufferedReader br = new BufferedReader(archivo);
        int ascii;

        try{
            while ((ascii = br.read()) != -1){
                char carcater = (char) ascii;
                contenido += carcater;
            }
            texto.setText(contenido);
        }catch (IOException e){
            e.printStackTrace();
        }

        Log.e("leer", contenido);
    }



    public File crearDirectorioPublico(String nombreDirectorio) {
        //Crear directorio público en la carpeta Pictures.
        File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.mkdirs())
            Log.e("dflores", "Error: No se creo el directorio público");

        return directorio;
    }
}
