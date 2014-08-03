package com.noveo.traineeship.network;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveo.traineeship.network.api.Api;
import com.noveo.traineeship.network.models.News;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class NetworkTests extends ApplicationTestCase<Application> {
    private static final String TAG = "network_tests";


    public NetworkTests() {
        super(Application.class);
    }

    public void testRetrofit() throws Exception{
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Api.END_POINT)
                .setClient(new OkClient())
                .build();

        Api api = adapter.create(Api.class);
        List<News> newsList = api.listNews();

        Log.d(TAG, "Retrofit : " + newsList.size());
        assertTrue(newsList.size() > 0);
    }

    public void testHttpClient() throws Exception {
        Gson gson = new Gson();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Api.END_POINT + Api.ALL_NEWS);

        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream stream = entity.getContent();

        String json = inputStreamToString(stream);
        List<News> newsList = gson.fromJson(json, new TypeToken<List<News>>(){}.getType());

        Log.d(TAG, "HttpClient : " + newsList.size());
        assertTrue(newsList.size() > 0);
    }

    public void testHttpUrlConnection() throws Exception {
        Gson gson = new Gson();
        URL url = new URL(Api.END_POINT + Api.ALL_NEWS);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

        String json = inputStreamToString(stream);

        urlConnection.disconnect();
        List<News> newsList = gson.fromJson(json, new TypeToken<List<News>>() {}.getType());

        Log.d(TAG, "HttpUrlConnection : " + newsList.size());
        assertTrue(newsList.size() > 0);
    }

    private static String inputStreamToString(final InputStream stream) {
        java.util.Scanner scanner = new java.util.Scanner(stream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}