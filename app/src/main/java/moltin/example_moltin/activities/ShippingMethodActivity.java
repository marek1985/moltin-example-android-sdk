package moltin.example_moltin.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.ShippingItem;

public class ShippingMethodActivity extends Activity {

    private Moltin moltin;

    String email="";//"2myemail@email.com";

    String s_first_name="";//"joe";
    String s_last_name="";//"black";
    String s_address_1="";//"address 1";
    String s_address_2="";//"address 2";
    String s_country="";//"GB";
    String s_postcode="";//"1111";

    String b_first_name="";//"joe";
    String b_last_name="";//"black";
    String b_address_1="";//"address 1";
    String b_address_2="";//"address 2";
    String b_country="";//"GB";
    String b_postcode="";//"1111";

    String shipping="";//"next-hour-delivery";

    private ArrayList<ShippingItem> shippingArray;
    private int lastShippingIndex=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_method);

        moltin = new Moltin(this);
        shippingArray = new ArrayList<ShippingItem>();

        email = getIntent().getExtras().getString("EMAIL");

        s_first_name = getIntent().getExtras().getString("S_FIRST_NAME");
        s_last_name = getIntent().getExtras().getString("S_LAST_NAME");
        s_address_1 = getIntent().getExtras().getString("S_ADDRESS_1");
        s_address_2 = getIntent().getExtras().getString("S_ADDRESS_2");
        s_country = getIntent().getExtras().getString("S_COUNTRY");
        s_postcode = getIntent().getExtras().getString("S_POSTCODE");

        b_first_name = getIntent().getExtras().getString("B_FIRST_NAME");
        b_last_name = getIntent().getExtras().getString("B_LAST_NAME");
        b_address_1 = getIntent().getExtras().getString("B_ADDRESS_1");
        b_address_2 = getIntent().getExtras().getString("B_ADDRESS_2");
        b_country = getIntent().getExtras().getString("B_COUNTRY");
        b_postcode = getIntent().getExtras().getString("B_POSTCODE");

        checkoutOrder();

        changeFonts((RelativeLayout) findViewById(R.id.layMain));
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnPlaceOrder:

                    shipping=shippingArray.get(lastShippingIndex).getItemSlug();

                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("SHIPPING",shipping);
                    intent.putExtra("EMAIL",email);
                    intent.putExtra("B_FIRST_NAME",b_first_name);
                    intent.putExtra("B_LAST_NAME",b_last_name);
                    intent.putExtra("B_ADDRESS_1",b_address_1);
                    intent.putExtra("B_ADDRESS_2",b_address_2);
                    intent.putExtra("B_COUNTRY",b_country);
                    intent.putExtra("B_POSTCODE",b_postcode);
                    intent.putExtra("S_FIRST_NAME",s_first_name);
                    intent.putExtra("S_LAST_NAME",s_last_name);
                    intent.putExtra("S_ADDRESS_1",s_address_1);
                    intent.putExtra("S_ADDRESS_2",s_address_2);
                    intent.putExtra("S_COUNTRY",s_country);
                    intent.putExtra("S_POSTCODE",s_postcode);
                    startActivity(intent);

                    break;
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.btnShipmentInactive:
                    refreshShippings((int) view.getTag());
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void refreshShippings(int x)
    {
        LinearLayout layShippings=(LinearLayout)findViewById(R.id.layShippings);

        for(int i=0;i<layShippings.getChildCount();i++)
        {
            View view=layShippings.getChildAt(i);
            int tag=(int)view.getTag();
            if(tag==x)
            {
                layShippings.removeViewAt(x);

                LayoutInflater factory = LayoutInflater.from(getApplicationContext());

                View myView;
                myView = factory.inflate(R.layout.shipping_item_active, null);
                ((TextView)findViewById(R.id.txtTotalPrice)).setText(shippingArray.get(x).getItemTotalPrice());

                TextView txtTitle=(TextView) myView.findViewById(R.id.txtShippingTitle);
                TextView txtPrice=(TextView) myView.findViewById(R.id.txtShippingPrice);

                txtTitle.setText(shippingArray.get(x).getItemTitle());
                txtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
                txtPrice.setText(shippingArray.get(x).getItemPrice());
                txtPrice.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));

                myView.setTag(tag);

                myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshShippings((int)view.getTag());
                    }
                });

                layShippings.addView(myView, tag);
            }
            else if(tag==lastShippingIndex)
            {
                layShippings.removeViewAt(lastShippingIndex);

                LayoutInflater factory = LayoutInflater.from(getApplicationContext());

                View myView;
                myView = factory.inflate(R.layout.shipping_item_inactive, null);

                TextView txtTitle=(TextView) myView.findViewById(R.id.txtShippingTitle);
                TextView txtPrice=(TextView) myView.findViewById(R.id.txtShippingPrice);

                txtTitle.setText(shippingArray.get(lastShippingIndex).getItemTitle());
                txtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
                txtPrice.setText(shippingArray.get(lastShippingIndex).getItemPrice());
                txtPrice.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));

                myView.setTag(tag);

                myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshShippings((int)view.getTag());
                    }
                });

                layShippings.addView(myView, tag);
            }
        }
        lastShippingIndex=x;
    }

    private void setShippings(LinearLayout layShippings, int i, String shipTitle, String shipPrice)
    {
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());

        View myView;

        if(i==0)
        {
            myView = factory.inflate(R.layout.shipping_item_active, null);
            ((TextView)findViewById(R.id.txtTotalPrice)).setText(shippingArray.get(0).getItemTotalPrice());

            TextView txtTitle=(TextView) myView.findViewById(R.id.txtShippingTitle);
            TextView txtPrice=(TextView) myView.findViewById(R.id.txtShippingPrice);

            txtTitle.setText(shipTitle);
            txtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
            txtPrice.setText(shipPrice);
            txtPrice.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        }
        else
        {
            myView = factory.inflate(R.layout.shipping_item_inactive, null);

            TextView txtTitle=(TextView) myView.findViewById(R.id.txtShippingTitle);
            TextView txtPrice=(TextView) myView.findViewById(R.id.txtShippingPrice);

            txtTitle.setText(shipTitle);
            txtTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
            txtPrice.setText(shipPrice);
            txtPrice.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        }

        myView.setTag(i);

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshShippings((int)view.getTag());
            }
        });

        layShippings.addView(myView);
    }

    private void checkoutOrder() {
        try {

            ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
            moltin.cart.checkout(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                    JSONObject json=(JSONObject)msg.obj;

                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONArray jsonArrayMethods = json.getJSONObject("result").getJSONObject("shipping").getJSONArray("methods");

                            LinearLayout layShippings=(LinearLayout)findViewById(R.id.layShippings);

                            for(int i=0;i<jsonArrayMethods.length();i++)
                            {
                                Log.i("methods", jsonArrayMethods.getJSONObject(i).toString());
                                Log.i("methods", jsonArrayMethods.getJSONObject(i).getString("slug"));

                                String shipTitle=jsonArrayMethods.getJSONObject(i).getString("title");
                                String shipPrice=jsonArrayMethods.getJSONObject(i).getJSONObject("price").getJSONObject("data").getJSONObject("formatted").getString("with_tax");

                                shippingArray.add(new ShippingItem(jsonArrayMethods.getJSONObject(i).getString("slug"),shipTitle,shipPrice,jsonArrayMethods.getJSONObject(i).getJSONObject("totals").getJSONObject("post_discount").getJSONObject("formatted").getString("with_tax")));

                                setShippings(layShippings, i, shipTitle, shipPrice);

                            }

                            JSONObject jsonGateways = json.getJSONObject("result").getJSONObject("gateways");
                            {
                                Iterator i = jsonGateways.keys();

                                while (i.hasNext()) {
                                    String key = (String) i.next();
                                    Log.i("gateways", jsonGateways.getJSONObject(key).toString());
                                    Log.i("gateways", jsonGateways.getJSONObject(key).getString("slug"));
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void changeFonts(ViewGroup root) {
        try
        {
            for(int i = 0; i <root.getChildCount(); i++) {
                View v = root.getChildAt(i);
                if(v instanceof Button) {
                    ((Button)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
                } else if(v instanceof TextView) {
                    ((TextView)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
                } else if(v instanceof EditText) {
                    ((EditText)v).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
                } else if(v instanceof ViewGroup) {
                    changeFonts((ViewGroup) v);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
