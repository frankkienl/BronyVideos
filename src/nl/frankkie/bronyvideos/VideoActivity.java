package nl.frankkie.bronyvideos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoActivity extends Activity {

    String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoId = getIntent().getStringExtra("videoId");
        initUI();
        //Get Video!
        HttpGet request = new HttpGet("https://api.dailymotion.com/video/" + videoId + "?fields=title,allow_embed,description,embed_url,filmstrip_small_url,geoblocking,thumbnail_480_url,url,id");
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
        setTitle(R.string.video);
        setContentView(R.layout.activity_video);
    }


    protected void fillUI(JSONObject json) {
        if (json == null) {
            Util.showAlertDialog(this, "Communication Error", "Cannot load video... try again later.");
            return;
        }

        try {
            ((TextView) findViewById(R.id.video_title)).setText(json.getString("title"));
            String description = json.getString("description");
            ((TextView) findViewById(R.id.video_description)).setText(Html.fromHtml(description));
            //final String embedUrl = json.getString("embed_url");
            final String id = json.getString("id");
            ((Button) findViewById(R.id.video_view_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickedVideo(id,true);
                }
            });
            ImageView imageView = (ImageView) findViewById(R.id.video_image);
            UrlImageViewHelper.setUrlDrawable(imageView, json.getString("thumbnail_480_url"), R.drawable.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
            Util.showAlertDialog(this, "Communication Error", "Cannot load video... try again later.");
            return;
        }
    }

    public void clickedVideo(String id, boolean iframe){
        String url = "http://frankkie.nl/android/bronyvideos/viewDailymotion.php?id=" + id;
        if (!iframe){
            url = "http://www.dailymotion.com/embed/video/" + id;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video, menu);
        return true;
    }

}
