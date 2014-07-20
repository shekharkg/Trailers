package com.trailer.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.trailer.dao.TrailerDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


public class MainActivity extends Activity {

  String urlYouTube = "https://gdata.youtube.com/feeds/api/users/tseries/uploads?v=2&alt=jsonc";
  CardListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    listView = (CardListView) this.findViewById(R.id.myList);

    new HttpAsyncTask().execute(urlYouTube);

  }

  class HttpAsyncTask extends AsyncTask<String, Void, String> {
    GetJsonString getJsonString = new GetJsonString();
    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
      // TODO Auto-generated method stub
      dialog = new ProgressDialog(MainActivity.this);
      dialog.setTitle("Loading...");
      dialog.show();
      super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... urls) {
      return getJsonString.GET(urlYouTube);
    }

    @Override
    protected void onPostExecute(String result) {
      dialog.dismiss();

      try {
        JSONObject object = new JSONObject(result);
        JSONObject data = object.getJSONObject("data");
        JSONArray items = data.getJSONArray("items");
        ArrayList<Card> cards = new ArrayList<Card>();


        for (int i = 0; i < items.length(); i++) {
          String title = items.getJSONObject(i).getString("title");
          String description = items.getJSONObject(i).getString("description");
          String thumbnail = items.getJSONObject(i).getJSONObject("thumbnail").getString("sqDefault");
          String url = items.getJSONObject(i).getString("id");
          final TrailerDao trailerDao = new TrailerDao(title, description, thumbnail, url);

            // Create a Card
            IonCard card = new IonCard(MainActivity.this);
            card.setTitle(trailerDao.getTitle());
            card.setSecondaryTitle(trailerDao.getDescription());
            card.setId(trailerDao.getUrl());

            CardThumbnail thumb = new CardThumbnail(MainActivity.this);
            thumb.setUrlResource(trailerDao.getThumbnailUrl());

            card.addCardThumbnail(thumb);
          card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
              //Toast.makeText(getContext(), card.getId(), Toast.LENGTH_SHORT).show();
              //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + trailerDao.getUrl()));
              Intent intent = new Intent(MainActivity.this, FullscreenDemoActivity.class);
              intent.putExtra("videoId", trailerDao.getUrl());
              startActivity(intent);
            }
          });

           /* switch (i % 5) {
              case 0:
                card.setBackgroundResourceId(R.drawable.demoextra_card_selector_color1);
                break;
              case 1:
                card.setBackgroundResourceId(R.drawable.demoextra_card_selector_color2);
                break;
              case 2:
                card.setBackgroundResourceId(R.drawable.demoextra_card_selector_color3);
                break;
              case 3:
                card.setBackgroundResourceId(R.drawable.demoextra_card_selector_color4);
                break;
              case 4:
                card.setBackgroundResourceId(R.drawable.demoextra_card_selector_color5);
                break;
            }*/
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(MainActivity.this, cards);


        if (listView != null) {
          listView.setAdapter(mCardArrayAdapter);
        }

        AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
        animCardArrayAdapter.setAbsListView(listView);
        if (listView != null) {
          listView.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }
}


