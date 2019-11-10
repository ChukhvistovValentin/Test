package com.test.test.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;

import com.test.test.ui.ImageX;
import com.test.test.R;
import com.test.test.models.ItemModel;

import java.util.ArrayList;

/**
 * адаптер для вьэпейджер
 */
public class PageViewAdapter extends PagerAdapter {
    // список для отображения
    private ArrayList<ItemModel> models = new ArrayList<>();

    @Override
    public int getCount() {
        return models.size(); // к-во страниц
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        onDestroyView((View) object); // не отображаем изображение если оно скачивается
        container.removeView((View) object); // разрушаем страничку
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // инициализация страницы
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.item_page_view, container, false);

        // добавляем вью
        container.addView(view, 0);
        // отображаем содержимое
        onShow(view, position);
        return view; // вью с содержимим
    }

    /**
     * отображаем данные
     * @param v вью
     * @param pos позиция
     */
    private void onShow(View v, int pos) {
        ItemModel model = models.get(pos); // модель с данными

        // отображение данных...
        ((ImageX) v.findViewById(R.id.img)).download(model.getImageUrl(), model.getBmpModel());
        ((AppCompatTextView) v.findViewById(R.id.txtName)).setText(model.getName());
        ((AppCompatTextView) v.findViewById(R.id.txtDateTime)).setText(model.getTime());
        ((AppCompatTextView) v.findViewById(R.id.txtDescription)).setText(model.getDescription());
    }

    /**
     * остановить загрузку
     * @param v вью
     */
    private void onDestroyView(View v){
        ((ImageX) v.findViewById(R.id.img)).stopDownload(); // останавливаем
    }

    // получены данные - обновляем
    public void update(ArrayList<ItemModel> models) {
        this.models = models;
        notifyDataSetChanged(); // обновить вью
    }
}
