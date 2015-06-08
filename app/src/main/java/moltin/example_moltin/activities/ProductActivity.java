package moltin.example_moltin.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.ProductItem;
import moltin.example_moltin.fragments.MenuFragment;
import moltin.example_moltin.fragments.ProductFragment;


public class ProductActivity extends SlidingFragmentActivity implements ProductFragment.OnFragmentInteractionListener,MenuFragment.OnFragmentInteractionListener{
    private Moltin moltin;
    private Context context;

    private ActionBar actionBar;
    private SlidingMenu menu;
    private Fragment mContent;
    private MenuFragment menuFragment;

    private String itemId="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_collection);

        moltin = new Moltin(this);
        //moltin.resetAuthenticationData();

        itemId = getIntent().getExtras().getString("ID");

        try
        {
            moltin.product.search(new String[][]{{"collection", itemId}}, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    showPopup(msg.obj.toString());
                    if (msg.what == Constants.RESULT_OK) {

                        ArrayList<ProductItem> items = new ArrayList<ProductItem>();
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if (json.has("status") && json.getBoolean("status") && json.has("result") && json.getJSONArray("result").length() > 0) {
                                for (int i = 0; i < json.getJSONArray("result").length(); i++) {
                                    items.add(new ProductItem(json.getJSONArray("result").getJSONObject(i)));
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "No products available in this collection.", Toast.LENGTH_LONG).show();
                            }

                            Fragment fragment = ProductFragment.newInstance(items, "PRODUCTS", 0);
                            mContent = fragment;
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.product_content_frame, fragment)
                                    .commit();
                            menu.showContent();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        menu = getSlidingMenu();
        menu.setShadowWidth(0);
        menu.setBehindWidth(200);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setMode(SlidingMenu.LEFT);
        menu.setFadeEnabled(false);
        menu.setBehindScrollScale(0.5f);
        setSlidingActionBarEnabled(true);

        context = this;

        ArrayList<ProductItem> items = new ArrayList<ProductItem>();

        if (savedInstanceState != null)
            mContent = getFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null) {
            mContent = ProductFragment.newInstance(items, "", 0);
        }

        setContentView(R.layout.product_content_frame);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.product_content_frame, mContent)
                .commit();

        setBehindContentView(R.layout.menu_frame);
        menuFragment = MenuFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, menuFragment)
                .commit();

        /*((LinearLayout)findViewById(R.id.btnCollections)).setBackgroundColor(getResources().getColor(R.color.BLACK));
        ((LinearLayout)findViewById(R.id.btnCart)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((LinearLayout)findViewById(R.id.btnPayment)).setBackgroundColor(getResources().getColor(android.R.color.transparent));*/
    }

    @Override
    public void onFragmentInteractionForProductItem(ProductItem item) {

        try {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("ID",item.getItemId());
            intent.putExtra("TITLE",item.getItemName());
            intent.putExtra("DESCRIPTION",item.getItemDescription());
            intent.putExtra("PICTURE",item.getItemPictureUrl());
            intent.putExtra("BRAND",item.getItemBrand());
            intent.putExtra("PRICE",item.getItemPrice());
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId()) {
                case R.id.btnCollections:
                    finish();
                    break;
                case R.id.btnCart:
                    Intent intent2 = new Intent(this, CartActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.btnPayment:
                    Intent intent3 = new Intent(this, PaymentActivity.class);
                    startActivity(intent3);
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showPopup(String text)
    {
        Log.i("POPUP","response " + text);
    }

    @Override
    public void onFragmentInteraction(String title) {

    }
}
