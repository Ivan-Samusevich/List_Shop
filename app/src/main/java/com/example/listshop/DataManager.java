package com.example.listshop;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FILE_NAME = "shopping_lists.json";
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

    public boolean clearData() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.delete();
    }
}