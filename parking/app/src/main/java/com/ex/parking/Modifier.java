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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Modifier extends AppCompatActivity {

    private TextView textView, textView1, textView2;
    private EditText editText2;
    private Spinner spinner;
    private RadioButton radioButton, radioButton1;
    private RadioGroup radioGroup;
    private Button button, button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier);
        //Lister
        textView = (TextView) findViewById(R.id.textView19);
        //Insérer
        textView1 = (TextView) findViewById(R.id.textView23);
        //Supprimer
        textView2 = (TextView) findViewById(R.id.textView24);
        //spinner
        spinner = (Spinner) findViewById(R.id.spinner3);
        //immatriculation
        editText2 = (EditText) findViewById(R.id.editTextTextPersonName6);
        //radioGroup
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupe2);
        //résident
        radioButton = (RadioButton) findViewById(R.id.radioButton3);
        //visiteur
        radioButton1 = (RadioButton) findViewById(R.id.radioButton4);
        //modifier
        button = (Button) findViewById(R.id.button8);
        //quitter
        button1 = (Button) findViewById(R.id.button9);

        //page Connection_base
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Modifier.this, Connection_base.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //page insert
        textView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Modifier.this, insert.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //page suppr
        textView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Modifier.this, Suppr.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //clic modifier
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imma = editText2.getText().toString();
                String isChecked = null;
                if (radioButton.isChecked()){
                    isChecked = radioButton.getText().toString();
                }
                if (radioButton1.isChecked()){
                    isChecked = radioButton1.getText().toString();
                }
                String profile = "";
                if (isChecked.equals("Resident")){
                    profile = "resident";
                }
                else if (isChecked.equals("Visiteur")){
                    profile = "visiteur";
                }
                new Modifie().execute(imma, profile, spinner.getSelectedItem().toString());
                finish();
                startActivity(getIntent());
            }
        });

        //clic quitter
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        new Data().execute();
    }

    protected class Data extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            String str = "http://10.4.253.91/ProjetParking/nom.php";
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Modifier.this, android.R.layout.simple_spinner_item, list);
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
                url = new URL("http://10.4.253.91/ProjetParking/infos.php");
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
                if (result.getString("Profile").equals("resident")){
                    radioButton.setChecked(true);
                }
                else if (result.getString("Profile").equals("visiteur")){
                    radioButton1.setChecked(true);
                }
                else{
                    radioButton.setChecked(false);
                    radioButton1.setChecked(false);
                }
                editText2.setText(result.getString("idImma"));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class Modifie extends AsyncTask<String, String, String>{
        ProgressDialog pdLoading = new ProgressDialog(Modifier.this);
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
                url = new URL("http://10.4.253.91/ProjetParking/modifier.php");
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
                        .appendQueryParameter("idImma", params[0])
                        .appendQueryParameter("Profile", params[1])
                        .appendQueryParameter("Imma", params[2]);
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
                Toast.makeText(Modifier.this, "Modification réussi", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("false")){
                Toast.makeText(Modifier.this, "Problème sur la base de donnée", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception1")){
                Toast.makeText(Modifier.this, "Problème d'url", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception2")){
                Toast.makeText(Modifier.this, "Problème de transfert de données", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception3") || result.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(Modifier.this, "Aucune réponse du serveur", Toast.LENGTH_LONG).show();
            }
        }
    }

}