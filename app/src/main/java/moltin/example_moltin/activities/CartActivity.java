package moltin.example_moltin.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CartItem;
import moltin.example_moltin.data.TotalCartItem;
import moltin.example_moltin.fragments.CartFragment;
import moltin.example_moltin.fragments.MenuFragment;

public class CartActivity extends SlidingFragmentActivity implements CartFragment.OnFragmentInteractionListener,MenuFragment.OnFragmentInteractionListener{
    private Moltin moltin;
    private Context context;

    private ActionBar actionBar;
    private SlidingMenu menu;
    private Fragment mContent;
    private MenuFragment menuFragment;
    private ArrayList<CartItem> items;
    private TotalCartItem cart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_collection);

        moltin = new Moltin(this);
        //moltin.resetAuthenticationData();

        /*((LinearLayout)findViewById(R.id.btnCart)).setBackgroundColor(getResources().getColor(R.color.BLACK));
        ((LinearLayout)findViewById(R.id.btnCollections)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((LinearLayout)findViewById(R.id.btnPayment)).setBackgroundColor(getResources().getColor(android.R.color.transparent));*/

        try
        {
            moltin.cart.contents(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    showPopup(msg.obj.toString());
                    if (msg.what == Constants.RESULT_OK) {

                        ArrayList<CartItem> items = new ArrayList<CartItem>();
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if (json.has("status") && json.getBoolean("status") && json.has("result") && !json.isNull("result") && json.getJSONObject("result").has("contents") && !json.getJSONObject("result").isNull("contents")) {
                                JSONObject jsonContent=json.getJSONObject("result").getJSONObject("contents");/*.getJSONObject(moltin.cart.getIdentifier())*/

                                cart=new TotalCartItem(json.getJSONObject("result"));

                                Iterator i1 = jsonContent.keys();

                                while (i1.hasNext()) {
                                    String key1 = (String) i1.next();
                                    if (jsonContent.get(key1) instanceof JSONObject) {

                                        CartItem itemForArray=new CartItem(jsonContent.getJSONObject(key1));
                                        itemForArray.setItemIdentifier(key1);

                                        items.add(itemForArray);
                                    }
                                }

                                cart.setItems(items);

                                ((TextView)findViewById(R.id.txtTotalPrice)).setText(cart.getItemTotalPrice());
                            }

                            Fragment fragment = CartFragment.newInstance(items, "CARTS", 0);
                            mContent = fragment;
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.cart_content_frame, fragment)
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

        ArrayList<CartItem> items = new ArrayList<CartItem>();

        if (savedInstanceState != null)
            mContent = getFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null) {
            mContent = CartFragment.newInstance(items, "", 0);
        }

        setContentView(R.layout.cart_content_frame);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.cart_content_frame, mContent)
                .commit();

        setBehindContentView(R.layout.menu_frame);
        menuFragment = MenuFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, menuFragment)
                .commit();


        ((TextView)findViewById(R.id.txtTotalPrice)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        ((TextView)findViewById(R.id.txtTotal)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        ((Button)findViewById(R.id.btnCheckout)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
    }

    @Override
    public void onFragmentInteractionForCartItem(CartItem item) {

        try {
            /*Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("ID",item.getItemId());
            intent.putExtra("TITLE",item.getItemName());
            intent.putExtra("DESCRIPTION",item.getItemDescription());
            intent.putExtra("PICTURE",item.getItemPictureUrl());
            intent.putExtra("BRAND",item.getItemBrand());
            intent.putExtra("PRICE",item.getItemPrice());
            startActivity(intent);*/
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

    public void showPopup(String text)
    {
        Log.i("POPUP", "response " + text);
    }

    @Override
    public void onFragmentInteraction(String title) {

    }
}
