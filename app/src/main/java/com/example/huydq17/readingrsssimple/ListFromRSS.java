package com.example.huydq17.readingrsssimple;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by HuyDQ17 on 10/6/2015.
 */
public class ListFromRSS extends AppCompatActivity {

    private ListView listRSS;
    private List<Item> rssList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_list_layout);
        listRSS = (ListView) findViewById(R.id.listRSS);
        ArrayAdapter<Item> adapter = new CustomAdapter(this, R.layout.rss_item, R.id.description, getDataWebPage());
        listRSS.setAdapter(adapter);
    }


    private List<Item> getDataWebPage() {
        try {
            File rssFile = new File(Environment.getExternalStorageDirectory(), "information.rss");
            InputStream in = new FileInputStream(rssFile);
            rssList = new RSSUtilities().getRssList(in);
            insertDB();
            for (Item i : rssList) {
                Log.i(">>>>>>>>", i.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rssList;
    }

    private void insertDB() {
        ContentValues contentValues = new ContentValues();
        for (Item i : rssList) {
            contentValues.put(RSSContentProvider.RSSSqliteHelper.RSS_TITLE, i.getTitle());
            contentValues.put(RSSContentProvider.RSSSqliteHelper.RSS_LINK, i.getLink());
            contentValues.put(RSSContentProvider.RSSSqliteHelper.RSS_CATEGORY, i.getCategory());
            contentValues.put(RSSContentProvider.RSSSqliteHelper.RSS_DESCRIPTION, i.getDescription());
            Uri uri = getContentResolver().insert(RSSContentProvider.CONTENT_URI, contentValues);
            Log.i(">>>>>>>>>>>>>>", uri.toString());
        }
    }


    private class CustomAdapter extends ArrayAdapter<Item> {

        private Context context;
        List<Item> list;

        public CustomAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
            super(context, resource, textViewResourceId, objects);
            this.context = context;
            this.list = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.rss_item, null);
                //    view = android.view.View.inflate(context, R.layout.rss_item, parent);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) view.findViewById(R.id.title);
                viewHolder.link = (TextView) view.findViewById(R.id.link);
                viewHolder.category = (TextView) view.findViewById(R.id.category);
                viewHolder.description = (TextView) view.findViewById(R.id.description);
                viewHolder.icon = (ImageView) view.findViewById(R.id.imIcon);
                viewHolder.detail = (ImageView) view.findViewById(R.id.btnDetail);

                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.link.setText("");
            viewHolder.category.setText("");
            viewHolder.description.setText(list.get(position).getDescription());

            String url = list.get(position).getDetailSrc();
            viewHolder.icon.setImageBitmap(list.get(position).getImageView());

            // viewHolder.detail.setImageResource(R.drawable.next);

            return view;
        }

        private class ViewHolder {

            TextView title;
            TextView link;
            TextView category;
            TextView description;
            ImageView icon;
            ImageView detail;

        }
    }
}
