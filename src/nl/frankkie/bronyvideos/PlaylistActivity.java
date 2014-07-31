package nl.frankkie.bronyvideos;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistActivity extends Activity {

    String playlistId;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistId = getIntent().getStringExtra("playlistId");
        initUI();
        //Get Videos!
        HttpGet request = new HttpGet("https://api.dailymotion.com/playlist/" + playlistId + "/videos?limit=100");
        Communication.MyCommunicationResponseListener listener = new Communication.MyCommunicationResponseListener() {
            @Override
            public void onResponse(JSONObject json) {
                fillUI(json);
            }
        };
        Communication.MyCommunicationAsyncTask task = new Communication.MyCommunicationAsyncTask(this, listener, request);
        task.execute("");
    }

    private void initUI() {
        setTitle(R.string.playlist);
        setContentView(R.layout.activity_main);
        container = (LinearLayout) findViewById(R.id.playlist_container);
        container.removeAllViews();
    }


    protected void fillUI(JSONObject json) {
        if (json == null) {
            Util.showAlertDialog(this, "Communication Error", "Cannot load videos... try again later.");
            return;
        }
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        try {
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                final JSONObject jsonObject = list.getJSONObject(i);
                Button itemLayout = new Button(this);
                itemLayout.setText(jsonObject.getString("title"));
                //LinearLayout itemLayout = (LinearLayout) layoutInflater.inflate(R.layout.playlist_item, container, false);
                //itemLayout.setFocusable(true);
                //TextView itemTv = (TextView) itemLayout.findViewById(R.id.playlist_item_text);
                //itemTv.setText(jsonObject.getString("title"));
                final String playlistId = jsonObject.getString("id");
                itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedVideo(playlistId);
                    }
                });
                container.addView(itemLayout);
                boolean useSeparator = true;
                if (useSeparator) { //not at the bottom
                    LinearLayout separator = (LinearLayout) layoutInflater.inflate(R.layout.item_separator, container, false);
                    container.addView(separator);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Util.showAlertDialog(this, "Communication Error", "Cannot load videos... try again later.");
            return;
        }
    }

    public void clickedVideo(String id) {
        Intent intent = new Intent();
        intent.setClass(this, VideoActivity.class);
        intent.putExtra("videoId", id);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playlist, menu);
        return true;
    }

}
