package com.example.showtime.app.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.showtime.app.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadMoviePoster extends AsyncTask<Void, Void, Bitmap>
{
    private String url = "";
    private ImageView imageView = null;

    public LoadMoviePoster(String url, ImageView imageView)
    {
        this.url = "https://image.tmdb.org/t/p/w500" + url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params)
    {
        try
        {
            URL posterUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)posterUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(input);

            return imageBitmap;
        }
        catch (Exception ex)
        {

        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }
}
