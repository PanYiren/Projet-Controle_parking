package com.ex.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.number.NumberRangeFormatter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class insert extends AppCompatActivity {

    private TextView textView, textView1, textView2;
    private EditText editText5;
    private RadioGroup radioGroup;
    private RadioButton radioButton, radioButton1;
    private Button button, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //lister
        textView = (TextView) findViewById(R.id.textView13);
        //Supprimer
        textView1 = (TextView) findViewById(R.id.textView15);
        //Modifier
        textView2 = (TextView) findViewById(R.id.textView14);
        textView2.setText("Modifier");
        //radiogroup
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        //resident
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        //visiteur
        radioButton1 = (RadioButton) findViewById(R.id.radioButton2);
        //immatriculation
        editText5 = (EditText) findViewById(R.id.editTextTextPersonName5);
        //ajouter
        button = (Button) findViewById(R.id.button6);
        //quitter
        button2 = (Button) findViewById(R.id.button7);

        //acces page lister
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(insert.this, Connection_base.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //acces page supprimer
        textView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(insert.this, Suppr.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //page modifier
        textView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(insert.this, Modifier.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //inserer
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imma = editText5.getText().toString();
                String isChecked = null;
                if (radioButton.isChecked()){
                    isChecked = radioButton.getText().toString();
                }
                if (radioButton1.isChecked()){
                    isChecked = radioButton1.getText().toString();
                }
                String profile = "";
                if (isChecked.equals("Résident")){
                    profile = "resident";
                }
                else if (isChecked.equals("Visiteur")){
                    profile = "visiteur";
                }
                new Insert().execute(imma, profile);
                finish();
                startActivity(getIntent());
            }
        });

        //quitter
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }

    private class Insert extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(insert.this);
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
                url = new URL("http://10.4.253.91/ProjetParking/insert.php");
            }
            catch (MalformedURLException e){
                e.printStackTrace();
                return "exception1";
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
                        .appendQueryParameter("idImma", params[0])
                        .appendQueryParameter("Profile", params[1]);
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
                Toast.makeText(insert.this, "Inserssion réussi", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("false")){
                Toast.makeText(insert.this, "Veillez remplir tous les champs", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception1")){
                Toast.makeText(insert.this, "Problème d'url", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception2")){
                Toast.makeText(insert.this, "Problème de transfert de données", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception3") || result.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(insert.this, "Aucune réponse du serveur", Toast.LENGTH_LONG).show();
            }
        }
    }

}