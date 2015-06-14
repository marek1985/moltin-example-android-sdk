package moltin.example_moltin.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CountryItem;
import moltin.example_moltin.data.ShippingItem;
import moltin.example_moltin.interfaces.CountryListAdapter;

public class ShippingActivity extends Activity {

    private Moltin moltin;

    String address="";//"1004558173891199887";
    String customer="";//"1004554501165679501";
    String email="";//"2myemail@email.com";
    String first_name="";//"joe";
    String last_name="";//"black";
    String address_1="";//"address 1";
    String address_2="";//"address 2";
    String country="";//"GB";
    String postcode="";//"1111";

    static String gateway="stripe";
    static String method="purchase";
    String shipping="";//"next-hour-delivery";
    String bill_to="";//address;
    String ship_to="";//address;

    String order="";//"1004598531207463836";

    String number="";//"4242424242424242";
    String expiry_month="";//"12";
    String expiry_year="";//"16";
    String cvv="";//"123";

    private ArrayList<ShippingItem> shippingArray;
    private int lastShippingIndex=0;

    AlertDialog dialog;
    ArrayList<CountryItem> listCountry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        moltin = new Moltin(this);
        shippingArray = new ArrayList<ShippingItem>();


        checkoutOrder();

        getCountryCodes();

        changeFonts((RelativeLayout) findViewById(R.id.layMain));
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnPlaceOrder:

                    boolean placeOrder=true;
                    boolean oneErrorPerTry=true;

                    if( ((TextView)findViewById(R.id.txtShippingEmail)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingEmail)).setError("Email is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        email=((TextView)findViewById(R.id.txtShippingEmail)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtShippingFirstName)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingFirstName)).setError("First name is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        first_name=((TextView)findViewById(R.id.txtShippingFirstName)).getText().toString();
                    }
                    if( ((TextView)findViewById(R.id.txtShippingLastName)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingLastName)).setError("Last name is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        last_name=((TextView)findViewById(R.id.txtShippingLastName)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtShippingAddress1)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingAddress1)).setError("Address is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_1=((TextView)findViewById(R.id.txtShippingAddress1)).getText().toString();
                    }

                    if( !((TextView)findViewById(R.id.txtShippingAddress1)).getText().toString().trim().equals(""))
                    {
                        address_2=((TextView)findViewById(R.id.txtShippingAddress2)).getText().toString();
                    }


                    if( ((TextView)findViewById(R.id.txtShippingCity)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingCity)).setError("City is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_2 += (address_2.length()>0 ? ", " : "") + ((TextView)findViewById(R.id.txtShippingCity)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtShippingZip)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingZip)).setError("Zip code is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        postcode=((TextView)findViewById(R.id.txtShippingZip)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtShippingState)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingState)).setError("State is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_2 += (address_2.length()>0 ? ", " : "") + ((TextView)findViewById(R.id.txtShippingState)).getText().toString();
                    }

                    if(country.equals(""))
                    {
                        ((TextView)findViewById(R.id.txtShippingCountry)).setError("Country code is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentCardNumber)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentCardNumber)).setError("Credit card number is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        number=((TextView)findViewById(R.id.txtPaymentCardNumber)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentExpirationMonth)).setError("Credit card expiration month is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        expiry_month=((TextView)findViewById(R.id.txtPaymentExpirationMonth)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentExpirationYear)).setError("Credit card expiration year is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        expiry_year=((TextView)findViewById(R.id.txtPaymentExpirationYear)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtPaymentCVC)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtPaymentCVC)).setError("Credit card cvc/cvv is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        cvv=((TextView)findViewById(R.id.txtPaymentCVC)).getText().toString();
                    }

                    shipping=shippingArray.get(lastShippingIndex).getItemSlug();

                    if(placeOrder)
                    {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                        findCustomer();
                    }
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

    private void eraseCurrentCart()
    {
        moltin.cart.setIdentifier("");
    }

    private void cartOrder() {
        try {
            JSONObject address=new JSONObject();
            address.put("first_name",first_name);
            address.put("last_name",last_name);
            address.put("address_1",address_1);
            address.put("address_2",address_2);
            address.put("country",country);
            address.put("postcode",postcode);

            JSONObject orderData=new JSONObject();
            orderData.put("customer", customer);
            orderData.put("shipping", shipping);
            orderData.put("gateway", gateway);
            orderData.put("bill_to", address);
            orderData.put("ship_to", address);

            moltin.cart.order(orderData, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    JSONObject json = (JSONObject) msg.obj;

                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            if(json.getBoolean("status"))
                            {
                                Log.i("order", json.getJSONObject("result").getString("id"));
                                order = json.getJSONObject("result").getString("id");
                                payment();
                            }
                            else
                            {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                String errors="";

                                Iterator i = json.getJSONObject("errors").keys();

                                while (i.hasNext()) {
                                    String key = (String) i.next();

                                    JSONArray arrayError=json.getJSONObject("errors").getJSONArray(key);

                                    for(int j=0;j<arrayError.length();j++)
                                    {
                                        if(errors.length()!=0)
                                            errors+="\n";
                                        errors +=arrayError.getString(j);
                                    }
                                }

                                showAlert(false, errors);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                        try {
                            String errors="";

                            Iterator i = json.getJSONObject("errors").keys();

                            while (i.hasNext()) {
                                String key = (String) i.next();

                                JSONArray arrayError=json.getJSONObject("errors").getJSONArray(key);

                                for(int j=0;j<arrayError.length();j++)
                                {
                                    if(errors.length()!=0)
                                        errors+="\n";
                                    errors +=arrayError.getString(j);
                                }
                            }

                            showAlert(false,errors);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payment() {
        try {
            JSONObject jsonCard=new JSONObject();
            jsonCard.put("number", number);
            jsonCard.put("expiry_month", expiry_month);
            jsonCard.put("expiry_year", expiry_year);
            jsonCard.put("cvv", cvv);

            JSONObject jsonData=new JSONObject();
            jsonData.put("data", jsonCard);


            moltin.checkout.payment(method, order, jsonData, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                    if (msg.what == Constants.RESULT_OK) {
                        try {

                            JSONObject json = (JSONObject) msg.obj;
                            if(json.getBoolean("status"))
                            {
                                showAlert(true,json.getJSONObject("result").getString("message"));
                            }
                            else
                            {
                                showAlert(false,json.getString("error"));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        try {

                            JSONObject json = (JSONObject) msg.obj;

                            showAlert(false,json.getString("error"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCustomer() {
        try {
            moltin.customer.create(new String[][]{
                    {"first_name",first_name},
                    {"last_name",last_name},
                    {"email",email}
            }, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if(json.getBoolean("status"))
                            {
                                customer=json.getJSONObject("result").getString("id");
                                cartOrder();
                            }
                            else
                            {
                                ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                                String errors=json.getJSONArray("errors").getString(0);

                                showAlert(false,errors);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                        try {
                            JSONObject json = (JSONObject) msg.obj;

                            String errors=json.getJSONArray("errors").getString(0);

                            showAlert(false,errors);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findCustomer() {
        try {
            moltin.customer.find(new String[][]{
                    {"email", email}
            }, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if(json.getJSONArray("result").length()==0)
                            {
                                createCustomer();
                            }
                            else
                            {
                                customer=json.getJSONArray("result").getJSONObject(0).getString("id");
                                cartOrder();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                        try {
                            JSONObject json = (JSONObject) msg.obj;

                            customer=json.getJSONArray("result").getJSONObject(0).getString("id");
                            cartOrder();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void getCountryCodes() {
        try {

            moltin.address.fields("","",new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {

                    JSONObject json=(JSONObject)msg.obj;

                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONObject jsonCountries = json.getJSONObject("result").getJSONObject("country").getJSONObject("available");
                            {
                                Iterator i = jsonCountries.keys();
                                listCountry = new ArrayList<CountryItem>();
                                while (i.hasNext()) {
                                    String key = (String) i.next();
                                    listCountry.add(new CountryItem(key,jsonCountries.getString(key).toString()));
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

    private void showAlert(final boolean done,String message)
    {
        new AlertDialog.Builder(this)
                .setTitle((done ?"Payment":"Error"))
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(done)
                        {
                            eraseCurrentCart();
                            finish();
                        }
                    }
                })
                .setIcon((done ? android.R.drawable.ic_dialog_info : android.R.drawable.ic_dialog_alert))
                .show();
    }

    public void showCountries(View view)
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Country");
        ListView list=new ListView(this);
        list.setAdapter(new CountryListAdapter(listCountry, this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                country = listCountry.get((int)view.getTag()).getItemId();
                ((TextView)findViewById(R.id.txtShippingCountry)).setText(listCountry.get((int)view.getTag()).getItemTitle());
            }
        });
        builder.setView(list);
        dialog=builder.create();
        dialog.show();
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
