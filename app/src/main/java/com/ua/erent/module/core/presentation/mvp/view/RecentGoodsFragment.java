package com.ua.erent.module.core.presentation.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ua.erent.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Максим on 10/30/2016.
 */

public final class RecentGoodsFragment extends Fragment {

    GridView gridView;

    Adapter adapter = new Adapter();

    public static class ItemModel {

        String title;
        String description;

        public ItemModel(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    private class Adapter extends BaseAdapter {

        private List<ItemModel> items;

        public Adapter() {
            String title = "Орлёнок";
            String descr = "Киев 1$, просмотрено 68 раз";
            items = Arrays.asList(
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr),
                    new ItemModel(title, descr)
            );
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public ItemModel getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.
                        from(parent.getContext()).inflate(R.layout.activity_main_good_item, parent, false);
            }

            ItemModel model = getItem(position);

            TextView title = (TextView) convertView.findViewById(R.id.good_title);
            TextView descr = (TextView) convertView.findViewById(R.id.good_details);

            title.setText(model.title);
            descr.setText(model.description);

            return convertView;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_goods, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (GridView) view.findViewById(R.id.recent_goods_container);
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setAdapter(adapter);
    }



}
