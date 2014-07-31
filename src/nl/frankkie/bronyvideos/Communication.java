package nl.frankkie.bronyvideos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by FrankkieNL on 25-6-13.
 */
public class Communication {

    public static interface MyCommunicationResponseListener {
        public void onResponse(JSONObject json);
    }

    public static class MyCommunicationAsyncTask extends AsyncTask<Object, Object, JSONObject> {

        Context context;
        Dialog dialog;
        boolean isCanceled = false;
        HttpUriRequest request;
        MyCommunicationResponseListener listener;


        public MyCommunicationAsyncTask(Context context,MyCommunicationResponseListener listener, HttpUriRequest request){
            this.context = context;
            this.listener = listener;
            this.request = request;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(context,context.getString(R.string.communication_title), context.getString(R.string.communication_message));
        }

        @Override
        protected JSONObject doInBackground(Object[] objects) {
            HttpClient client = new DefaultHttpClient();
            try {
                HttpResponse httpResponse = client.execute(request);
                String response = EntityUtils.toString(httpResponse.getEntity());
                JSONObject json = new JSONObject(response);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            isCanceled = true;
            Util.dismissDialog(dialog);
            listener.onResponse(null);
        }

        @Override
        protected void onPostExecute(JSONObject o) {
            Util.dismissDialog(dialog);
            listener.onResponse(o);
        }

    }
}
