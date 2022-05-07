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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Connection_base extends AppCompatActivity {

    private TextView textView, textView2, textView3, textView4, textView5;
    private Button button;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private RadioButton radioButton, radioButton1;
    private String imm = "0", role = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_base);

        //habitant
        spinner = (Spinner) findViewById(R.id.spinner);
        //information
        textView = (TextView) findViewById(R.id.textView20);
        //Modifier
        textView5 = (TextView)findViewById(R.id.textView6);
        textView5.setText("Modifier");
        //Variable lister
        textView2 = (TextView) findViewById(R.id.textView6);
        //Variable insérer
        textView3 = (TextView) findViewById(R.id.textView7);
        //radiogroup
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupe2);
        //radiobutton imma
        radioButton = (RadioButton) findViewById(R.id.radioButton8);
        radioButton.setChecked(true);
        //radiobutton role
        radioButton1 = (RadioButton) findViewById(R.id.radioButton9);
        //Variable supprimer
        textView4 = (TextView) findViewById(R.id.textView8);
        //quitter
        button = (Button) findViewById(R.id.button3);


        new Data().execute();
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Data().execute();
            }
        });

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new ArrayList<>();
                list.add("Resident");
                list.add("Visiteur");
                list.add("Inconnu");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Connection_base.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String idRV = spinner.getSelectedItem().toString();
                        String profile = "";
                        if (idRV.equals("Resident")){
                            profile = "resident";
                        }
                        if (idRV.equals("Visiteur")){
                            profile = "visiteur";
                        }
                        new Lister().execute(profile);
                        textView.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });

        //accès page insérer
        textView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Connection_base.this, insert.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //accès page supprimer
        textView4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Connection_base.this, Suppr.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        //page modifier
        textView5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(Connection_base.this, Modifier.class);
                startActivity(intent);
                finish();
                return false;
            }
        });


        //quitter
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Connection_base.this, android.R.layout.simple_spinner_item, list);
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
                String status = null;
                if (result.getString("Profile").equals("resident")){
                    status = "Résident";
                }
                else if (result.getString("Profile").equals("visiteur")){
                    status = "Visiteur";
                }
                else{
                    status = "Inconnu";
                }
                textView.setText(
                        "Immatricumation : " + spinner.getSelectedItem().toString() + "\nStatus : " + status
                );
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    private class Lister extends AsyncTask<String, String, JSONObject> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                url = new URL("http://10.4.253.91/ProjetParking/lister.php");
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
                        .appendQueryParameter("Profile", params[0]);
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
                JSONArray jsonArray = result.getJSONArray("idImma");
                if (result.names().equals("status")){
                    textView.setText(result.getString("status"));
                }
                else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        textView.append("Immatriculation " + i + ": " + jsonObject.getString("idImma") + "\n");
                    }
                }

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
