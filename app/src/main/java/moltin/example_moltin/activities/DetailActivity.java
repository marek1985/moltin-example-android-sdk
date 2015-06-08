package moltin.example_moltin.activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;

public class DetailActivity extends Activity implements NumberPicker.OnValueChangeListener {

    private String itemId;
    private String itemTitle;
    private String itemDescription;
    private String itemPictureUrl;
    private String itemBrand;
    private String itemPrice;

    private Moltin moltin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        itemId = getIntent().getExtras().getString("ID");
        itemTitle = getIntent().getExtras().getString("TITLE");
        itemDescription = getIntent().getExtras().getString("DESCRIPTION");
        itemPictureUrl = getIntent().getExtras().getString("PICTURE");
        itemBrand = getIntent().getExtras().getString("BRAND");
        itemPrice = getIntent().getExtras().getString("PRICE");

        try {
            ((TextView)findViewById(R.id.txtDetailTitle)).setText(itemTitle);
            ((TextView)findViewById(R.id.txtDetailDescription)).setText(itemDescription);
            ((TextView)findViewById(R.id.txtDetailBrand)).setText(itemBrand);
            ((TextView)findViewById(R.id.txtDetailPrice)).setText(itemPrice);

            if(itemPictureUrl!=null && itemPictureUrl.length()>3)new DownloadImageTask(((ImageView)findViewById(R.id.imgDetailPhoto))).execute(itemPictureUrl);
            else ((ImageView)findViewById(R.id.imgDetailPhoto)).setImageResource(android.R.color.transparent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        moltin= new Moltin(this);
        try {
            moltin.cart.inCart(itemId, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {
                        JSONObject jsonObject=(JSONObject)msg.obj;
                        try {
                            Log.i("are in cart",jsonObject.toString());
                            if(jsonObject.has("status") && jsonObject.getBoolean("status"))
                                ((LinearLayout) findViewById(R.id.layPutIntoCart)).setVisibility(View.GONE);
                            else
                                ((LinearLayout)findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId()) {
                case R.id.btnPutIntoCart:
                    show();
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void show()
    {

        final Dialog d = new Dialog(this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ((Button)findViewById(R.id.btnPutIntoCart)).setEnabled(false);
                try {
                    moltin.cart.insert(itemId, np.getValue(), new JSONObject(), new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            if (msg.what == Constants.RESULT_OK) {
                                JSONObject jsonObject = (JSONObject) msg.obj;
                                Toast.makeText(getApplicationContext(), "Product added to cart.", Toast.LENGTH_LONG).show();
                                try {
                                    Log.i("add to cart",jsonObject.toString());
                                    if(jsonObject.has("status") && jsonObject.getBoolean("status"))
                                        ((LinearLayout) findViewById(R.id.layPutIntoCart)).setVisibility(View.GONE);
                                    else
                                        ((LinearLayout)findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            } else {
                                Toast.makeText(getApplicationContext(), "Error while adding to cart. Please try again.", Toast.LENGTH_LONG).show();
                                ((Button)findViewById(R.id.btnPutIntoCart)).setEnabled(true);
                                return false;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}