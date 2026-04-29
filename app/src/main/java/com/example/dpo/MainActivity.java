package com.example.dpo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    EditText editName;
    EditText editAge;
    private List<User> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        textView = findViewById(R.id.textDate);

        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);

        Button buttonGet = findViewById(R.id.btnGet);
        Button buttonPost = findViewById(R.id.btnPost);
        Button buttonSaveJson = findViewById(R.id.btnSaveJson);
        Button buttonLoadJson = findViewById(R.id.btnLoadJson);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        // КНОПКА ДОБАВЛЕНИЯ НОВОГО ПОЛЬЗОВАТЕЛЯ
        btnAddUser.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String ageStr = editAge.getText().toString().trim();

            if (!name.isEmpty() && !ageStr.isEmpty()) {
                int age = Integer.parseInt(ageStr);
                usersList.add(new User(name, age));
                textView.setText("Добавлен: " + name + ", " + age);
                editName.setText("");
                editAge.setText("");
            } else {
                textView.setText("Заполните имя и возраст");
            }
        });

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

        buttonSaveJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUsersToJson();  // ← БЕЗ ПАРАМЕТРОВ
            }
        });

        buttonLoadJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUsersFromJson();
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
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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

    public void post() {
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
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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

    private void saveUsersToJson() {
        if (usersList.isEmpty()) {
            textView.setText("Нет пользователей для сохранения! Сначала добавьте их через кнопку Добавить");
            return;
        }

        boolean result = JSONHelper.exportToJSON(this, usersList);
        if (result) {
            textView.setText("JSON сохранён! Пользователей: " + usersList.size());
        } else {
            textView.setText("Ошибка сохранения JSON");
        }
    }

    private void loadUsersFromJson() {
        List<User> loadedUsers = JSONHelper.importFromJSON(this);

        if (loadedUsers == null) {
            textView.setText("Файл JSON не найден");
            return;
        }

        if (loadedUsers.isEmpty()) {
            textView.setText("В JSON файле нет пользователей");
            return;
        }

        usersList.clear();
        usersList.addAll(loadedUsers);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < usersList.size(); i++) {
            User u = usersList.get(i);
            sb.append(i + 1).append(". ").append(u.toString()).append("\n");
        }
        textView.setText(sb.toString());
    }
}