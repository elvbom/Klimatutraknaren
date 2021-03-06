package isisochbast.se.klimatutraknaren;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetDistance extends AsyncTask<String, Void, String> {

    private TextView mAvstandTextView;
    private TextView mTagTextView;
    private TextView mFlygTextView;
    private TextView mBilTextView;
    Context mContext;

    public GetDistance(Context mContext, TextView avsTV, TextView tTV, TextView fTV, TextView bTV) {
        this.mContext = mContext;
        this.mAvstandTextView = avsTV;
        this.mTagTextView = tTV;
        this.mFlygTextView = fTV;
        this.mBilTextView = bTV;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String aDouble) {
        super.onPostExecute(aDouble);
        if (aDouble != null) {
            setDouble(aDouble);
        } else
            Toast.makeText(mContext, "Något gick fel! Prova att fylla i igen", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode = con.getResponseCode();
            if (statuscode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                String json = sb.toString();
                Log.d("JSON", json);
                JSONObject root = new JSONObject(json);
                JSONArray array_rows = root.getJSONArray("rows");
                Log.d("JSON", "array_rows:" + array_rows);
                JSONObject object_rows = array_rows.getJSONObject(0);
                Log.d("JSON", "object_rows:" + object_rows);
                JSONArray array_elements = object_rows.getJSONArray("elements");
                Log.d("JSON", "array_elements:" + array_elements);
                JSONObject object_elements = array_elements.getJSONObject(0);
                Log.d("JSON", "object_elements:" + object_elements);
                // JSONObject object_duration=object_elements.getJSONObject("duration");
                JSONObject object_distance = object_elements.getJSONObject("distance");
                //  Log.d("JSON","object_duration:"+object_duration);
                return object_distance.getString("value");
            }
        } catch (MalformedURLException e) {
            Log.d("error", "error1");
        } catch (IOException e) {
            Log.d("error", "error2");
        } catch (JSONException e) {
            Log.d("error", "error3");
        }

        return null;
    }


    public void setDouble(String result) {
        String res[] = result.split(",");
        double dist = Integer.parseInt(res[0])/1000;

        Typeface open_sans = Typeface.createFromAsset(mContext.getAssets(), "fonts/Open_Sans/OpenSans-Regular.ttf");
        mAvstandTextView.setTypeface(open_sans);
        mTagTextView.setTypeface(open_sans);
        mFlygTextView.setTypeface(open_sans);
        mBilTextView.setTypeface(open_sans);

        mAvstandTextView.setText(String.format("Utsläpp per resetyp för en person"));
        //för ett halvfullt SJ 2000
        mTagTextView.setText(String.format("Tåg: %.5f kg CO²", dist*0.0000021));
        //för ett flyg fullt till 65%
        mFlygTextView.setText(String.format("Flyg: %.2f kg CO²", dist*0.16));
        //för bil som drivs av bensin m 2 pers i
        mBilTextView.setText(String.format("Bil: %.2f kg CO²", dist*0.096));

        //  mBussTextView.setTypeface(open_sans);
        //  mBussTextView.setText(String.format("Buss %s", dist * 0.089));
    }

}
