package com.test.test.loaders;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.test.test.models.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoadJsonTask extends AsyncTask<String, Void, ArrayList<ItemModel>> {
    public interface JsonModelsListener {
        void onStart();

        void onComplete(ArrayList<ItemModel> models);

        void onError(Exception e);
    }

    private String url; // откуда берем данные
    private JsonModelsListener modelsListener; // слушатель калбэк

    public LoadJsonTask(String url, @NonNull JsonModelsListener modelsListener) {
        this.url = url;
        this.modelsListener = modelsListener;
    }

    @Override
    protected ArrayList<ItemModel> doInBackground(String... strings) {
        try {
            return getJsonResponse(); // пробуем загрузить / парсить данные
        } catch (JSONException e) { // ОШИБКИ...
            e.printStackTrace();
            modelsListener.onError(e);
        } catch (IOException e) {
            e.printStackTrace();
            modelsListener.onError(e);
        } catch (Exception e) {
            e.printStackTrace();
            modelsListener.onError(e);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        modelsListener.onStart(); // при старте
    }

    @Override
    protected void onPostExecute(ArrayList<ItemModel> models) {
        super.onPostExecute(models);
        modelsListener.onComplete(models); // по завершении
    }

    /**
     * соединяемся, парсим
     * @return список элементов с данными
     * @throws JSONException ошибка
     * @throws IOException ошибка
     */
    private ArrayList<ItemModel> getJsonResponse() throws JSONException, IOException {
        StringBuffer buffer;
        BufferedReader reader;

        URL url = new URL(this.url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) { //200 ответ - все гуд
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            buffer = new StringBuffer();

            // получаем строку джейсона
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            connection.disconnect();
            return toModel(buffer.toString()); // парсим..
        } else {
            return null;
        }
    }

    /**
     * парсинг джейсона
     * @param json строка джейсона
     * @return список элементов
     * @throws JSONException ошибка
     */
    private ArrayList<ItemModel> toModel(String json) throws JSONException {
        // {"itemId":"10056","name":"IronMan","image":"http://s8.hostingkartinok.com/uploads/images/2016/03/b70762d52599ffc44dc7539bf57baa1c.jpg",
        // "description":"heavy armor","time":"1457018867393"}
        JSONArray jsonArray = new JSONArray(json);
        ArrayList<ItemModel> models = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject response = jsonArray.getJSONObject(i); // разбираем...
            int id = response.getInt("itemId");
            String name = response.getString("name");
            String image = response.getString("image");
            String description = response.getString("description");
            String time = response.getString("time");

            models.add(new ItemModel(id, name, image, description, time)); // добавляем в список
        }
        return models; // список элементов
    }

}
