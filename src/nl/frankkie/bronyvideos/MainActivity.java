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

public class MainActivity extends Activity {

    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        //Get Playlists!
        HttpGet request = new HttpGet("https://api.dailymotion.com/user/x19emeo/playlists");
        Communication.MyCommunicationResponseListener listener = new Communication.MyCommunicationResponseListener() {
            @Override
            public void onResponse(JSONObject json) {
                fillUI(json);
            }
        };
        Communication.MyCommunicationAsyncTask task = new Communication.MyCommunicationAsyncTask(this,listener,request);
        task.execute("");
    }

    protected void initUI(){
        setTitle(R.string.playlists);
        setContentView(R.layout.activity_main);
        container = (LinearLayout) findViewById(R.id.playlist_container);
        container.removeAllViews();
    }
    protected void fillUI(JSONObject json){
        if (json == null){
            Util.showAlertDialog(this,"Communication Error","Cannot load playlists... try again later.");
            return;
        }
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        try {
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++){
                final JSONObject jsonObject = list.getJSONObject(i);
                Button itemLayout = new Button(this);
                itemLayout.setText(jsonObject.getString("name"));
                //LinearLayout itemLayout = (LinearLayout) layoutInflater.inflate(R.layout.playlist_item, container, false);
                //TextView itemTv = (TextView) itemLayout.findViewById(R.id.playlist_item_text);
                //itemTv.setText(jsonObject.getString("name"));
                final String playlistId = jsonObject.getString("id");
                itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedPlaylist(playlistId);
                    }
                });
                container.addView(itemLayout);
                boolean useSeparator = true;
                if (useSeparator){ //not at the bottom
                    LinearLayout separator = (LinearLayout) layoutInflater.inflate(R.layout.item_separator, container, false);
                    container.addView(separator);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Util.showAlertDialog(this,"Communication Error","Cannot load playlists... try again later.");
            return;
        }
    }

    public void clickedPlaylist(String id){
        Intent intent = new Intent();
        intent.setClass(this,PlaylistActivity.class);
        intent.putExtra("playlistId", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
   {
    "page": 1,
    "limit": 10,
    "explicit": false,
    "total": 3,
    "has_more": false,
    "list": [
        {
            "id": "x2emfj",
            "name": "MLP - Season 3",
            "owner": "x19emeo"
        },
        {
            "id": "x2emfe",
            "name": "MLP - Season 2",
            "owner": "x19emeo"
        },
        {
            "id": "x2emfc",
            "name": "MLP - Season 1",
            "owner": "x19emeo"
        }
    ]
}
     */
    
}
