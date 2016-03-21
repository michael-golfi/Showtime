package com.example.showtime.app.service;

import android.graphics.*;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.showtime.app.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadMoviePoster extends AsyncTask<Void, Void, Bitmap> {
    private String url = "";
    private ImageView imageView = null;

    public LoadMoviePoster(String url, ImageView imageView) {
        this.url = "https://image.tmdb.org/t/p/w500" + url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL posterUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) posterUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(input);

            return imageBitmap;
        } catch (Exception ex) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        if (result != null)
            imageView.setImageBitmap(result);
        else
            imageView.setImageBitmap(textAsBitmap("No Bitmap Found", 12.0f, Color.BLACK));
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
