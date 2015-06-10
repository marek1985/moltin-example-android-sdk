package moltin.example_moltin.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.utils.DownloadImageTask;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ((TextView)findViewById(R.id.txtActivityTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        ((TextView)findViewById(R.id.txtCollectionName)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        ((Button)findViewById(R.id.btnGo)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnGo:
                    Intent intent = new Intent(this, CollectionActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btnCart:
                    Intent intent2 = new Intent(this, CartActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
