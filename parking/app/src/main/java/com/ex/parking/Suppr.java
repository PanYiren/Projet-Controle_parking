package com.ex.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Suppr extends AppCompatActivity {

    private TextView textView, textView1, textView2, textView3;
    private Spinner spinner;
    private Button button, button1;
    private String nom_hab, prenom_hab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppr);

        //lister
        textView = (TextView) findViewById(R.id.textView5);
        //insérer
        textView1 = (TextView) findViewById(R.id.textView9);
        //Modifier
        textView3 = (TextView) findViewById(R.id.textView10);
        textView3.setText("Modifier");
        //information
        textView2 = (TextView) findViewById(R.id.textView12);
        //habiton
        spinner = (Spinner) findViewById(R.id.spinner2);
        //supprimer
        button = (Button) findViewById(R.id.button4);
        //quitter
        button1 = (Button) findViewById(R.id.button5);

        //acces page lister
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Suppr.this, Connection_base.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //access page insérer
        textView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Suppr.this, insert.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //page modifier
        textView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Suppr.this, Modifier.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
        //quitter
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        //Supprimet
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Supprimer().execute();
                finish();
                startActivity(getIntent());
            }
        });

        new Data().execute();
    }

    protected class Data extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            String str = "http://192.168.1.13/ProjetParking/nom.php";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                return new JSONObject(stringBuffer.toString());
            } catch (Exception ex) {
                Log.e("App", "Infos", ex);
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    Log.e("App", "Success : " + response.getString("Infos"));
                    JSONArray array = response.getJSONArray("Infos");
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        list.add(object.getString("idImma"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Suppr.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            new Infos().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
        }
    }


    private class Infos extends AsyncTask<String, String, JSONObject> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                url = new URL("http://192.168.1.13/ProjetParking/infos.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //append parameters url
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idImma", spinner.getSelectedItem().toString());
                String query = builder.build().getEncodedQuery();
                //open conncetion for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                int response_code = conn.getResponseCode();

                //check if successful connection
                if (response_code == HttpURLConnection.HTTP_OK) {
                    //read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return new JSONObject(result.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException je){
                je.printStackTrace();
            }
            finally {
                conn.disconnect();
            }
            return null;
        }

        protected void onPostExecute(JSONObject result){
            try {
                String status = null;
                if (result.getString("Resident").equals("1") && result.getString("Visiteur").equals("0")){
                    status = "Résident";
                }
                else if (result.getString("Resident").equals("0") && result.getString("Visiteur").equals("1")){
                    status = "Visiteur";
                }
                else if(result.getString("Resident").equals("0") && result.getString("Visiteur").equals("0")){
                    status = "Inconnu";
                }
                textView2.setText(
                        "Immatricumation : " + spinner.getSelectedItem().toString() + "\nStatus : " + status
                );
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class Supprimer extends AsyncTask<String, String, String>{
        ProgressDialog pdLoading = new ProgressDialog(Suppr.this);
        HttpURLConnection conn;
        URL url = null;

        protected void onPreExecute(){
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://192.168.1.13/ProjetParking/delete.php");
            }
            catch (MalformedURLException e){
                e.printStackTrace();
                return "exception1";
            }


            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                //send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //append parameters url
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("idImma", spinner.getSelectedItem().toString());
                String query = builder.build().getEncodedQuery();
                //open conncetion for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }
            catch (IOException e1){
                e1.printStackTrace();
                return "exception2";
            }

            try {
                int response_code = conn.getResponseCode();

                //check if successful connection
                if (response_code == HttpURLConnection.HTTP_OK){
                    //read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        result.append(line);
                    }
                    return(result.toString());
                }else {
                    return("unsuccessful");
                }
            }
            catch (IOException e){
                e.printStackTrace();
                return "exception3";
            }
            finally {
                conn.disconnect();
            }
        }

        protected void onPostExecute(String result){
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("true")){
                Toast.makeText(Suppr.this, "Suppression réussi", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("false")){
                Toast.makeText(Suppr.this, "Problème sur la base de données", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception1")){
                Toast.makeText(Suppr.this, "Problème d'url", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception2")){
                Toast.makeText(Suppr.this, "Problème de transfert de données", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception3") || result.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(Suppr.this, "Aucune réponse du serveur", Toast.LENGTH_LONG).show();
            }
        }
    }
}