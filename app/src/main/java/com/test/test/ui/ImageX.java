package com.test.test.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.test.test.models.BmpModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * картинка которая догружает изображение
 */
public class ImageX extends AppCompatImageView {
    private BmpModel bmp; // модель хранит изображение
    private boolean isDownloaded; // флаг загрузки
    private boolean isDestroy;

    public ImageX(Context context) {
        super(context);
    }

    public ImageX(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * загружаем изображение
     *
     * @param url      ссылка изображения
     * @param bmpModel модель которая храних битмап
     */
    public void download(String url, BmpModel bmpModel) {
        bmp = bmpModel;
        // загрузка не начата и не скачано изображение
        if (!isDownloaded && bmpModel.getBitmap() == null) {
            new DownloadBmpTask(url).execute(); // начинаем закачку...
        } else {
            showBitmap(); // если есть изображение - отображаем
        }
    }

    /**
     * отображаем картинку
     */
    private void showBitmap() {
        if (!isDestroy) // картинка не отображается на екране - не отображаем!!!
            setImageBitmap(bmp.getBitmap()); // отображаем
    }

    /**
     * вью не отображается на екране
     */
    public void stopDownload() {
        isDestroy = true;
    }

    /**
     * загружаем картинку
     */
    @SuppressLint("StaticFieldLeak")
    private class DownloadBmpTask extends AsyncTask<Void, Void, Void> {
        String imgUrl; // ссылка

        DownloadBmpTask(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDownloaded = true; // старт загрузки
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (bmp.getBitmap() == null) // нет изображения - загружаем
                    bmp.setBitmap(BitmapFactory.decodeStream(new URL(imgUrl).openConnection().getInputStream()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid); // по завершению загрузки
            showBitmap(); // отображаем
            isDownloaded = false; // флаг о завершении загрузки
        }
    }

}
