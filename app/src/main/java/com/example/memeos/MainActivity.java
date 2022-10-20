package com.example.memeos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView memeView;
    String currentStateUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        memeView = findViewById(R.id.memeView);

        this.loadMeme();

    }
    public void loadMeme(){
        RequestQueue requestQueue;
        requestQueue= Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentStateUrl = response.getString("url");
                            Picasso.get().load(currentStateUrl).placeholder(R.drawable.loading).into(memeView);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Error occurred please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error, pls check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        // first parameter is the file for icon and second one is menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // We are using switch case because multiple icons can be kept
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this cool meme i got from Reddit "+currentStateUrl);
        sharingIntent.setType("text/plain");

        startActivity(Intent.createChooser(sharingIntent, "Share this meme using..."));
        return super.onOptionsItemSelected(item);
    }

    public void nextMeme(View view) {
        this.loadMeme();
    }
}