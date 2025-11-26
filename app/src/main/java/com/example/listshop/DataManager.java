package com.example.listshop;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import models.User;

public class DataManager {
    private static final String FILE_NAME = "shopping_lists.json";
    private static final String FILE_NAME_USER = "user.json";
    private static final String FILE_NAME_THEMES = "themes.json";
    private static final String FILE_NAME_SUB = "sub.json";

    private Context context;
    private Gson gson = new Gson();

    public DataManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void saveLists(List<ShoppingList> lists) {
        try {
            String json = gson.toJson(lists);
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ShoppingList> loadLists() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) return new ArrayList<>();

            FileInputStream fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            Type type = new TypeToken<List<ShoppingList>>(){}.getType();
            return gson.fromJson(sb.toString(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveThemes(String night) {
        try {
            String json = gson.toJson(night);
            FileOutputStream fos = context.openFileOutput(FILE_NAME_THEMES, Context.MODE_PRIVATE);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSub(String sub) {
        try {
            String json = gson.toJson(sub);
            FileOutputStream fos = context.openFileOutput(FILE_NAME_SUB, Context.MODE_PRIVATE);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadSub(){
        try {
            File file = new File(context.getFilesDir(), FILE_NAME_SUB);
            if (!file.exists()) return "no";

            FileInputStream fis = context.openFileInput(FILE_NAME_SUB);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            Type type = new TypeToken<String>(){}.getType();
            return gson.fromJson(sb.toString(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return "no";
        }
    }

    public String loadThemes(){
        try {
            File file = new File(context.getFilesDir(), FILE_NAME_THEMES);
            if (!file.exists()) return "light";

            FileInputStream fis = context.openFileInput(FILE_NAME_THEMES);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            fis.close();

            Type type = new TypeToken<String>(){}.getType();
            return gson.fromJson(sb.toString(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return "light";
        }
    }

    public boolean clearData() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.delete();
    }

    public User loadUser() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME_USER);
            if (!file.exists()) return null;

            FileInputStream fis = context.openFileInput(FILE_NAME_USER);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            isr.close();
            fis.close();

            return gson.fromJson(sb.toString(), User.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(User user) {
        try {
            FileOutputStream fos = context.openFileOutput("user.json", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(gson.toJson(user));
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

