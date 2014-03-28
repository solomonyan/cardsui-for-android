package com.cardsui.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    private CardUI mCardView;
    ArrayList<HashMap<String, String>> newslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://data-hk.com/English/SCMP_News.json";
    //JSON Node Names
    private static final String TAG_NEWS = "News";
    private static final String TAG_MARK = "Mark";
    private static final String TAG_TOPIC = "Topic";
    private static final String TAG_URL = "Url";
    private static final String TAG_PUBLISED_ON = "Publised_on";
    private static final String TAG_LAST_UPDATE = "Last_update";
    private static final String TAG_HOTTEST_WORDS = "Hot";
    JSONArray android = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // init CardView
        mCardView = (CardUI) findViewById(R.id.cardsview);
        mCardView.setSwipeable(true);

        new JSONParse().execute();
//        showNewsCards();
    }
    public static String urlforClick;
    private void showNewsCards(ArrayList<HashMap<String, String>> newslist) {
        // Google Play Cards
        String[] color = {"#e00707","#33b6ea","#f2a400","#4ac925"};
        int i = 0;
        CardStack stackPlay = new CardStack();
        stackPlay.setTitle("DAILY RECOMMENDABLE NEWS");
        mCardView.addStack(stackPlay);
        for (HashMap<String, String> news_map : newslist) {
            urlforClick = news_map.get(TAG_URL);
            MyPlayCard cardUrl =  new MyPlayCard(
                    news_map.get(TAG_TOPIC),
                    news_map.get(TAG_LAST_UPDATE),
                    "Hottest word: " + news_map.get(TAG_HOTTEST_WORDS),
                    "Rank: " + (i+1),
                    color[i], color[i], false, true);

            cardUrl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlforClick));
                    startActivity(intent);
                }
            });
            mCardView.addCardToLastStack(cardUrl);
            i++;
        }


        mCardView.refresh();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_NEWS);
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String mark = c.getString(TAG_MARK);
                    String topic = c.getString(TAG_TOPIC);
                    String url = c.getString(TAG_URL);
                    String publised_on = c.getString(TAG_PUBLISED_ON);
                    String last_update = c.getString(TAG_LAST_UPDATE);
                    String hottest_words = c.getString(TAG_HOTTEST_WORDS);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_MARK, mark);
                    map.put(TAG_TOPIC, topic);
                    map.put(TAG_URL, url);
                    map.put(TAG_PUBLISED_ON, publised_on);
                    map.put(TAG_LAST_UPDATE, last_update);
                    map.put(TAG_HOTTEST_WORDS, hottest_words);
                    newslist.add(map);
                }
                showNewsCards(newslist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                mCardView.refresh();
                return true;
        }
    }
}
