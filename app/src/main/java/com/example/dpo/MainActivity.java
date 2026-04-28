package com.example.dpo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    String getURL = "https://jsonplaceholder.typicode.com/posts/1";
    String postURL = "https://jsonplaceholder.typicode.com/posts";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        textView = findViewById(R.id.textDate);
        Button buttonGet = findViewById(R.id.btnGet);
        Button buttonPost = findViewById(R.id.btnPost);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get();
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
    }

    public void get() {
        Request request = new Request.Builder()
                .url(getURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

                    }

            @Override
            public void onResponse(@NonNull Call call,@NonNull  Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(responseData);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("Ошибка сервера: " + response.code());
                        }
                    });
                }
            }
        });
    }

    public void post(){
        RequestBody requestBody = new FormBody.Builder()
                .add("key_name", "Demo value")
                .build();
        Request request = new Request.Builder().url(postURL).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call,@NonNull  Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(responseData);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("Ошибка сервера: " + response.code());
                        }
                    });
                }
            }
        });

    }

}