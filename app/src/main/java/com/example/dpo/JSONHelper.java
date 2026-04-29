package com.example.dpo;

import android.content.Context;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONHelper {

    private static final String FILE_NAME = "users.json";

    public static boolean exportToJSON(Context context, List<User> users) {
        Gson gson = new Gson();
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.setUsers(users);
        String jsonString = gson.toJson(dataWrapper);

        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> importFromJSON(Context context) {
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis)) {

            Gson gson = new Gson();
            DataWrapper dataWrapper = gson.fromJson(isr, DataWrapper.class);
            return dataWrapper.getUsers();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Вспомогательный класс для обёртки списка
    private static class DataWrapper {
        private List<User> users = new ArrayList<>();

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }
}