package com.example.huydq17.readingrsssimple;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuyDQ17 on 10/6/2015.
 */
public class RSSUtilities {

    private List<Item> rssList;
    private Item rssItem;
    private String text;
    private int i = 0;


    public RSSUtilities() {
        rssList = new ArrayList<>();
    }

    public List<Item> getRssList(InputStream in) {
        XmlPullParserFactory factory = null;
        XmlPullParser xmlPullParser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xmlPullParser = factory.newPullParser();

            xmlPullParser.setInput(in, null);
            int eventType = xmlPullParser.getEventType();

            while (eventType != xmlPullParser.END_DOCUMENT) {
                String tagName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if (tagName.equalsIgnoreCase("item")) {
                            rssItem = new Item();
                        }
                        break;
                    }
                    case XmlPullParser.TEXT: {
                        text = xmlPullParser.getText();
                        break;
                    }
                    case XmlPullParser.END_TAG: {

                        if (tagName.equalsIgnoreCase("item")) {
                            rssList.add(rssItem);
                        } else if (tagName.equalsIgnoreCase("title")) {
                            i++;
                            if (i > 3)
                                rssItem.setTitle(text);
                        } else if (tagName.equalsIgnoreCase("link")) {
                            i++;
                            if (i > 3)
                                rssItem.setLink(text);
                        } else if (tagName.equalsIgnoreCase("description")) {
                            i++;
                            if (i > 3)
                                rssItem.setDescription(text);
                        } else if (tagName.equalsIgnoreCase("category")) {
                            rssItem.setCategory(text);
                        } else {
                            // Nothing
                        }
                        break;
                    }
                }

                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rssList;
    }

    /*private Item addMoreData(Item item) {
        new RSSBacGround().execute(item);
        return rssItem;
    }

    private class RSSBacGround extends AsyncTask<Item, Bitmap, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Item... params) {
            URL imgUrl = null;
            Bitmap mIcon11;
            try {
                imgUrl = new URL(params[0].getDetailSrc());
                mIcon11 = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
                onProgressUpdate(mIcon11);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
            rssItem.setImageView(values[0]);
        }

    }*/

}
