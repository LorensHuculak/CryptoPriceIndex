package com.example.lorens.cryptocurrencypriceindex;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;

import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String BPI_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";
    public static final String ETH_ENDPOINT = "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=EUR,USD,GBP";
    public static final String NEO_ENDPOINT = "https://min-api.cryptocompare.com/data/price?fsym=NEO&tsyms=EUR,USD,GBP";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ProgressDialog progressDialog;

    private TextView txt;
    private ImageView img;
    private String name;
    private String price;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_bitcoin:
                    loadBTC();
                    name = "Bitcoin";
                    return true;
                case R.id.navigation_ethereum:
                    loadETH();
                    name = "Ethereum";
                    return true;
                case R.id.navigation_neo:
                    loadNEO();
                    name = "NEO";
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (TextView) findViewById(R.id.txt);
        img = (ImageView) findViewById(R.id.logo);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("BPI Loading");
        progressDialog.setMessage("Wait ...");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "The " + name + " price has reached €" + price +"! #CryptoPriceIndex";
            String shareSub = "Cryptocurrency Price Snapshot";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
            myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(myIntent,"Share using"));
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadBTC() {
        Request request = new Request.Builder()
                .url(BPI_ENDPOINT)
                .build();

        progressDialog.show();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "Error during BPI loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        parseBpiResponse(body);
                    }
                });
            }
        });

    }

    private void parseBpiResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();

            JSONObject jsonObject = new JSONObject(body);
          /**  JSONObject timeObject = jsonObject.getJSONObject("time");
            builder.append(timeObject.getString("updated")).append("\n\n"); */

            JSONObject bpiObject = jsonObject.getJSONObject("bpi");

            JSONObject euroObject = bpiObject.getJSONObject("EUR");
            builder.append("€ ").append(euroObject.getString("rate")).append("\n");

            JSONObject usdObject = bpiObject.getJSONObject("USD");
            builder.append("$ ").append(usdObject.getString("rate")).append("\n");

            JSONObject gbpObject = bpiObject.getJSONObject("GBP");
            builder.append("£ ").append(gbpObject.getString("rate")).append("\n");



            txt.setText(builder.toString());
            img.setImageResource(R.drawable.bitcoin);
            price = euroObject.getString("rate");

        } catch (Exception e) {

        }
    }

    private void loadETH() {
        Request request = new Request.Builder()
                .url(ETH_ENDPOINT)
                .build();

        progressDialog.show();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "Error during ETH loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        parseEthResponse(body);
                    }
                });
            }
        });

    }

    private void parseEthResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();

            JSONObject jsonObject = new JSONObject(body);


            builder.append("€ ").append(jsonObject.getString("EUR")).append("\n");

            builder.append("$ ").append(jsonObject.getString("USD")).append("\n");


            builder.append("£ ").append(jsonObject.getString("GBP")).append(" ").append("\n");



            txt.setText(builder.toString());
            img.setImageResource(R.drawable.ethereum);
            price = jsonObject.getString("EUR");

        } catch (Exception e) {

        }
    }


    private void loadNEO() {
        Request request = new Request.Builder()
                .url(NEO_ENDPOINT)
                .build();

        progressDialog.show();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "Error during NEO loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        parseNeoResponse(body);
                    }
                });
            }
        });

    }

    private void parseNeoResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();

            JSONObject jsonObject = new JSONObject(body);

            builder.append("€ ").append(jsonObject.getString("EUR")).append("\n");

            builder.append("$ ").append(jsonObject.getString("USD")).append("\n");


            builder.append("£ ").append(jsonObject.getString("GBP")).append(" ").append("\n");


            txt.setText(builder.toString());
            img.setImageResource(R.drawable.neo);
            price = jsonObject.getString("EUR");

        } catch (Exception e) {

        }
    }
}
