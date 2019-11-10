package com.test.test.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.test.test.ui.ImageX;
import com.test.test.R;
import com.test.test.activitys.ActivityPageItem;
import com.test.test.models.ItemModel;

import java.util.ArrayList;

/**
 * адаптер списка
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private ArrayList<ItemModel> models = new ArrayList<>(); // список данных

    /**
     * обновляем список
     * @param models список с новыми данными
     */
    public void update(ArrayList<ItemModel> models) {
        this.models = models;
        notifyDataSetChanged(); // обновить
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ItemHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bind(models.get(position)); // отображаем данные
    }

    @Override
    public int getItemCount() {
        return models.size(); // к-во элементов списка
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private View container;
        private Context context;
        private ImageX imgItem;
        private AppCompatTextView txtName;
        private AppCompatTextView txtDateTime;

        ItemHolder(@NonNull View itemView, Context context) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txtName);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);

            this.context = context;
            container = itemView;
        }

        /**
         * отображаем данные
         * @param model данные
         */
        private void bind(final ItemModel model) {
            imgItem.download(model.getImageUrl(), model.getBmpModel()); // загружаем изображение
            txtName.setText(model.getName()); // имя
            txtDateTime.setText(model.getTime()); // время

            // переход для отображения доп.инфо
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityPageItem.class);
                    intent.putExtra("ID", model.getId()); // идентификатор элемента
                    context.startActivity(intent); // отображаем активити (слайдеры влево-вправо)
                }
            });
        }
    }
}
