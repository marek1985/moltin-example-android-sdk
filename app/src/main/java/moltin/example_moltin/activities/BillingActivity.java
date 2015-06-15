package moltin.example_moltin.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CountryItem;
import moltin.example_moltin.interfaces.CountryListAdapter;

public class BillingActivity extends Activity {

    private Moltin moltin;

    String email="";//"2myemail@email.com";
    String first_name="";//"joe";
    String last_name="";//"black";
    String address_1="";//"address 1";
    String address_2="";//"address 2";
    String country="";//"GB";
    String postcode="";//"1111";

    AlertDialog dialog;
    ArrayList<CountryItem> listCountry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        moltin = new Moltin(this);

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

                    if( ((TextView)findViewById(R.id.txtBillingEmail)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingEmail)).setError("Email is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        email=((TextView)findViewById(R.id.txtBillingEmail)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtBillingFirstName)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingFirstName)).setError("First name is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        first_name=((TextView)findViewById(R.id.txtBillingFirstName)).getText().toString();
                    }
                    if( ((TextView)findViewById(R.id.txtBillingLastName)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingLastName)).setError("Last name is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        last_name=((TextView)findViewById(R.id.txtBillingLastName)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtBillingAddress1)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingAddress1)).setError("Address is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_1=((TextView)findViewById(R.id.txtBillingAddress1)).getText().toString();
                    }

                    if( !((TextView)findViewById(R.id.txtBillingAddress1)).getText().toString().trim().equals(""))
                    {
                        address_2=((TextView)findViewById(R.id.txtBillingAddress2)).getText().toString();
                    }


                    if( ((TextView)findViewById(R.id.txtBillingCity)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingCity)).setError("City is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_2 += (address_2.length()>0 ? ", " : "") + ((TextView)findViewById(R.id.txtBillingCity)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtBillingZip)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingZip)).setError("Zip code is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        postcode=((TextView)findViewById(R.id.txtBillingZip)).getText().toString();
                    }

                    if( ((TextView)findViewById(R.id.txtBillingState)).getText().toString().trim().equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingState)).setError("State is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }
                    else
                    {
                        address_2 += (address_2.length()>0 ? ", " : "") + ((TextView)findViewById(R.id.txtBillingState)).getText().toString();
                    }

                    if(country.equals(""))
                    {
                        ((TextView)findViewById(R.id.txtBillingCountry)).setError("Country code is required!");
                        placeOrder=false;
                        if(oneErrorPerTry)return;
                    }


                    if(placeOrder)
                    {
                        if(((CheckBox)findViewById(R.id.cbSameAsShippingAddress)).isChecked())
                        {
                            Intent intent = new Intent(this, ShippingMethodActivity.class);
                            intent.putExtra("EMAIL",email);
                            intent.putExtra("B_FIRST_NAME",first_name);
                            intent.putExtra("B_LAST_NAME",last_name);
                            intent.putExtra("B_ADDRESS_1",address_1);
                            intent.putExtra("B_ADDRESS_2",address_2);
                            intent.putExtra("B_COUNTRY",country);
                            intent.putExtra("B_POSTCODE",postcode);
                            intent.putExtra("S_FIRST_NAME",first_name);
                            intent.putExtra("S_LAST_NAME",last_name);
                            intent.putExtra("S_ADDRESS_1",address_1);
                            intent.putExtra("S_ADDRESS_2",address_2);
                            intent.putExtra("S_COUNTRY",country);
                            intent.putExtra("S_POSTCODE",postcode);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(this, ShippingActivity.class);
                            intent.putExtra("EMAIL",email);
                            intent.putExtra("B_FIRST_NAME",first_name);
                            intent.putExtra("B_LAST_NAME",last_name);
                            intent.putExtra("B_ADDRESS_1",address_1);
                            intent.putExtra("B_ADDRESS_2",address_2);
                            intent.putExtra("B_COUNTRY",country);
                            intent.putExtra("B_POSTCODE",postcode);
                            startActivity(intent);
                        }
                    }
                    break;
                case R.id.btnBack:
                    finish();
                    break;
            }
        }
        catch (Exception e)
        {
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
                ((TextView)findViewById(R.id.txtBillingCountry)).setText(listCountry.get((int)view.getTag()).getItemTitle());
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
