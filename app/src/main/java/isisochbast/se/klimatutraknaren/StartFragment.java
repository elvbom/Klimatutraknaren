package isisochbast.se.klimatutraknaren;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class StartFragment extends Fragment {

    private EditText mStartEditText;
    private EditText mSlutEditText;
    private Button mBerakna;
    private String mStartDest;
    private String mSlutDest;
    private TextView mAvstandTextView;
    private TextView mTagTextView;
    private TextView mFlygTextView;
    private TextView mValkommen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        Typeface open_sans = Typeface.createFromAsset(getContext().getAssets(), "fonts/Open_Sans/OpenSans-Regular.ttf");
        Typeface playfair = Typeface.createFromAsset(getContext().getAssets(), "fonts/Playfair_Display/PlayfairDisplay-Bold.ttf");

        mStartEditText = (EditText) view.findViewById(R.id.start_editText);
        mSlutEditText = (EditText) view.findViewById(R.id.slut_editText);
        mBerakna = (Button) view.findViewById(R.id.berakna_button);

        mAvstandTextView = (TextView) view.findViewById(R.id.avstand);
        mTagTextView = (TextView) view.findViewById(R.id.tag);
        mFlygTextView = (TextView) view.findViewById(R.id.flyg);
        mValkommen = (TextView) view.findViewById(R.id.valkommen);

        mValkommen.setTypeface(playfair);
        mStartEditText.setTypeface(open_sans);
        mSlutEditText.setTypeface(open_sans);
        mBerakna.setTypeface(open_sans);
        mStartEditText.setTypeface(open_sans);
        mSlutEditText.setTypeface(open_sans);

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=YOUR_API_KEYy=AIzaSyCCFP1Zdu51zwsF1x7mRlkRwYbxTwuqvdo");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mBerakna.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mStartDest = mStartEditText.getText().toString();
                                            mSlutDest = mSlutEditText.getText().toString();

                                            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + mStartDest + "&destinations=" + mSlutDest + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyCCFP1Zdu51zwsF1x7mRlkRwYbxTwuqvdo";

                                            new GetDistance(StartFragment.this.getContext(), mAvstandTextView, mTagTextView, mFlygTextView).execute(url);
                                        }
                                    }
        );

        return view;
    }

}
