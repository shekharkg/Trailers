package com.trailer.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


public class MainActivity extends Activity {

    String urlYouTube = "https://gdata.youtube.com/feeds/api/users/TheViralFeverVideos/uploads?v=2&alt=jsonc";
    String videoTitle, videoDescription, videoImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int listImages[] = new int[]{R.drawable.angry_1, R.drawable.angry_2,
                R.drawable.angry_3, R.drawable.angry_4, R.drawable.angry_5};

        ArrayList<Card> cards = new ArrayList<Card>();

        int x=0,y=0;
        for (int i = 0; i<5; i++) {
            // Create a Card
            IonCard card = new IonCard(this);
            card.setTitle("Title " + y);
            card.setSecondaryTitle("Description..." + y);
            y++;




            CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(listImages[i]);
            card.addCardThumbnail(thumb);

            switch (i % 5) {
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
            }

            cards.add(card);
            if(x<15 && i==4){
                i=-1;
                x++;
            }

        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

        CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

        AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
        animCardArrayAdapter.setAbsListView(listView);
        if (listView != null) {
            listView.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);
        }

        new HttpAsyncTask().execute(urlYouTube);

    }

    class HttpAsyncTask extends AsyncTask<String, Void, String> {
        GetJsonString getJsonString = new GetJsonString();


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            return getJsonString.GET(urlYouTube);
        }

        @Override
        protected void onPostExecute(String result) {

            String xxx = result;
            try {
                JSONObject json = new JSONObject(result);
                JSONObject object = json.getJSONObject("object");
                JSONObject data = object.getJSONObject("data");
                JSONArray items = data.getJSONArray("items");


                for (int i = 0; i < items.length(); i++) {
                    videoTitle = items.getJSONObject(i).getString("title");
                    videoDescription = items.getJSONObject(i).getString("description");
                    videoImage = items.getJSONObject(i).getJSONObject("thumbnail").getString("sqDefault");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}


