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
    JSONArray android = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // init CardView
        mCardView = (CardUI) findViewById(R.id.cardsview);
        mCardView.setSwipeable(true);

//		CardStack stack2 = new CardStack();
//		stack2.setTitle("REGULAR CARDS");
//		mCardView.addStack(stack2);

//		// add AndroidViews Cards
//		mCardView.addCard(new MyCard("Get the CardsUI view"));
//		mCardView.addCardToLastStack(new MyCard("for Android at"));
//		MyCard androidViewsCard = new MyCard("www.androidviews.net");
//		androidViewsCard.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				intent.setData(Uri.parse("http://www.androidviews.net/"));
//				startActivity(intent);
//
//			}
//		});
//		androidViewsCard.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				Toast.makeText(v.getContext(), "This is a long click", Toast.LENGTH_SHORT).show();
//				return true;
//			}
//
//		});
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setData(Uri.parse("http://www.androidviews.net/"));
//
//		mCardView.addCardToLastStack(androidViewsCard);

        // Google Play Cards

//		CardStack stackPlay = new CardStack();
//		stackPlay.setTitle("GOOGLE PLAY CARDS");
//		mCardView.addStack(stackPlay);
//
//		// add one card, and then add another one to the last stack.
//		mCardView.addCard(new MyCard("Google Play Cards"));
//		mCardView.addCardToLastStack(new MyCard("By Androguide & GadgetCheck"));
//
//		mCardView.addCardToLastStack(new MyPlayCard("Google Play",
//				"This card mimics the new Google play cards look", "#33b6ea",
//				"#33b6ea", true, false));
//
//		mCardView
//				.addCardToLastStack(new MyPlayCard(
//						"Menu Overflow",
//						"The PlayCards allow you to easily set a menu overflow on your card.\nYou can also declare the left stripe's color in a String, like \"#33B5E5\" for the holo blue color, same for the title color.",
//						"#e00707", "#e00707", false, true));
//
//		// add one card
//		mCardView
//				.addCard(new MyPlayCard(
//						"Different Colors for Title & Stripe",
//						"You can set any color for the title and any other color for the left stripe",
//						"#f2a400", "#9d36d0", false, false));
//
//		mCardView
//				.addCardToLastStack(new MyPlayCard(
//						"Set Clickable or Not",
//						"You can easily implement an onClickListener on any card, but the last boolean parameter of the PlayCards allow you to toggle the clickable background.",
//						"#4ac925", "#222222", true, true));

        // Image Card

//        CardStack stackImage = new CardStack();
//        stackImage.setTitle("IMAGE CARDS");
//        mCardView.addStack(stackImage);
//
//        mCardView.addCard(new MyImageCard("Image 1", R.drawable.url1));
//        mCardView.addCardToLastStack(new MyImageCard("Image 2", R.drawable.url2));
//        mCardView.addCardToLastStack(new MyImageCard("Image 3", R.drawable.url3));

        // draw cards
//        mCardView.refresh();

        new JSONParse().execute();
//        showNewsCards();
    }
    public static String urlforClick;
    private void showNewsCards(ArrayList<HashMap<String, String>> newslist) {
        // Google Play Cards
        String[] color = {"#e00707","#33b6ea","#f2a400","#4ac925"};
        int i = 0;
        CardStack stackPlay = new CardStack();
        stackPlay.setTitle("NEWS CARDS");
        mCardView.addStack(stackPlay);
        for (HashMap<String, String> news_map : newslist) {
            urlforClick = news_map.get(TAG_URL);
            MyPlayCard cardUrl =  new MyPlayCard(
                    news_map.get(TAG_TOPIC),
                    "Mark: " + news_map.get(TAG_MARK) + " Published on " + news_map.get(TAG_PUBLISED_ON),
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
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_MARK, mark);
                    map.put(TAG_TOPIC, topic);
                    map.put(TAG_URL, url);
                    map.put(TAG_PUBLISED_ON, publised_on);
                    map.put(TAG_LAST_UPDATE, last_update);
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
