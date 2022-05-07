package com.ex.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private EditText editText1, editText2;
    private Button button1, button2;
    private TextView textView;
    public static final  int Connection_timeout = 20000;
    public static final int Read_timeout = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.editTextTextPersonName);
        editText2 = (EditText) findViewById(R.id.editTextTextPassword);
        textView = (TextView) findViewById(R.id.textView4);
        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = editText1.getText().toString();
                String passwod = editText2.getText().toString();
                new Login().execute(login, passwod);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }

    private class Login extends AsyncTask<String, String, String>{
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
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
                url = new URL("http://10.4.253.91/ProjetParking/login.php");
            }
            catch (MalformedURLException e){
                e.printStackTrace();
                return "exception1";
            }


            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(Read_timeout);
                conn.setConnectTimeout(Connection_timeout);
                conn.setRequestMethod("POST");
                //send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //append parameters url
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("login", params[0])
                        .appendQueryParameter("password", params[1]);
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
                Intent intent = new Intent(MainActivity.this, Connection_base.class);
                startActivity(intent);
                finish();
            }
            else if (result.equalsIgnoreCase("false")){
                Toast.makeText(MainActivity.this, "Invalide Login et password", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception1")){
                Toast.makeText(MainActivity.this, "Problème d'url", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception2")){
                Toast.makeText(MainActivity.this, "Problème de connexion au serveur", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception3") || result.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(MainActivity.this, "Aucune réponse du serveur", Toast.LENGTH_LONG).show();
            }
        }
    }
}