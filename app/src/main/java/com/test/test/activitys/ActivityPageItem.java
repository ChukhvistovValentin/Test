package com.test.test.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.test.test.loaders.LoadJsonTask;
import com.test.test.R;
import com.test.test.adapters.PageViewAdapter;
import com.test.test.models.ItemModel;

import java.util.ArrayList;

import static com.test.test.MainActivity.testUrl;

/**
 * активити отображает слайды (влево-вправо)
 */
public class ActivityPageItem extends AppCompatActivity {
    private PageViewAdapter adapter = new PageViewAdapter(); // адаптер для слайдов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_item);

        int selectId = -1;
        Intent params = getIntent();
        if (params != null) { // элемент на который нажали
            selectId = params.getIntExtra("ID", -1);
        }
        init(selectId); // загружаем данные
    }

    /**
     * инициализация
     * @param id идентификатор элемента
     */
    private void init(final int id) {
        final ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // загружаем
        final LoadJsonTask loadTask = new LoadJsonTask(testUrl, new LoadJsonTask.JsonModelsListener() {
            @Override
            public void onStart() {
                // здесь можно прогрес какой-то отобразить..
            }

            @Override
            public void onComplete(ArrayList<ItemModel> models) {
                if (models != null) { // данные получили
                    adapter.update(models); // обновляем
                    // ищем позицию нажатого элемента
                    for (int i = 0; i < models.size(); i++) {
                        if (models.get(i).getId() == id) { // элемент найден
                            pager.setCurrentItem(i, false); // переходим на эту позицию
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                // вывести ошибку ???
            }
        });
        loadTask.execute(); // старт получения данных
    }
}
