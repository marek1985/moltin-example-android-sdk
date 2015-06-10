package moltin.example_moltin.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CartItem;
import moltin.example_moltin.data.CollectionItem;
import moltin.example_moltin.data.TotalCartItem;
import moltin.example_moltin.fragments.CartFragment;
import moltin.example_moltin.fragments.CollectionFragment;


public class CollectionActivity extends SlidingFragmentActivity implements CartFragment.OnFragmentChangeListener, CartFragment.OnFragmentInteractionListener, CollectionFragment.OnCollectionFragmentInteractionListener {
    private Moltin moltin;
    private Context context;
    private ArrayList<CollectionItem> items;
    private ArrayList<CartItem> itemsForCart;
    private TotalCartItem cart;
    public static CollectionActivity instance = null;

    private ActionBar actionBar;
    private SlidingMenu menu;
    private android.app.Fragment mContent;
    private CartFragment menuFragment;

    private Point screenSize;
    private int position=0;

    private LinearLayout layIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=this;

        instance = this;

        moltin = new Moltin(this);
        try
        {
            moltin.authenticate("umRG34nxZVGIuCSPfYf8biBSvtABgTR8GMUtflyE", new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {

                        try {
                            getCollections();
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
        menu.setShadowWidth(20);
        menu.setBehindWidth(getListviewWidth()-50);
        //menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setFadeEnabled(false);
        menu.setBehindScrollScale(0.5f);
        setSlidingActionBarEnabled(true);

        items=new ArrayList<CollectionItem>();

        if (savedInstanceState != null)
            mContent = getFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null) {
            mContent = CollectionFragment.newInstance(items,getListviewWidth());
        }

        setContentView(R.layout.activity_collection);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mContent)
                .commit();

        itemsForCart=new ArrayList<CartItem>();
        cart=new TotalCartItem(new JSONObject());
        cart.setItems(itemsForCart);

        setBehindContentView(R.layout.cart_content_frame);
        menuFragment = CartFragment.newInstance(cart, getApplicationContext());
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.cart_content_frame, menuFragment)
                .commit();

        /*try
        {
            moltin.cart.contents(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == Constants.RESULT_OK) {

                        ArrayList<CartItem> items = new ArrayList<CartItem>();
                        try {
                            JSONObject json = (JSONObject) msg.obj;
                            if (json.has("status") && json.getBoolean("status") && json.has("result") && !json.isNull("result") && json.getJSONObject("result").has("contents") && !json.getJSONObject("result").isNull("contents")) {
                                JSONObject jsonContent=json.getJSONObject("result").getJSONObject("contents");

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

                            menuFragment = CartFragment.newInstance(itemsForCart);
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.cart_content_frame, menuFragment)
                                    .commit();
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
*/
        ((TextView)findViewById(R.id.txtActivityTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        ((TextView)findViewById(R.id.txtActivityTitleCart)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
    }

    public void setInitialPosition()
    {
        layIndex = (LinearLayout)findViewById(R.id.layIndex);

        if(((LinearLayout) layIndex).getChildCount() > 0)
            ((LinearLayout) layIndex).removeAllViews();

        for(int i=0;i<items.size();i++)
        {
            ImageView img=new ImageView(this);
            if(position==i)
                img.setImageDrawable(getResources().getDrawable(R.drawable.circle_active));
            else
                img.setImageDrawable(getResources().getDrawable(R.drawable.circle_inactive));

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    30,
                    30);
            params.leftMargin = 5;
            params.rightMargin = 5;
            params.topMargin = 5;

            img.setLayoutParams(params);
            layIndex.addView(img);
        }
    }

    public void setPosition(int newPosition)
    {
        if(newPosition!=position)
        {
            if(((LinearLayout) layIndex).getChildCount() > 0)
            {
                ((ImageView)layIndex.getChildAt(position)).setImageDrawable(getResources().getDrawable(R.drawable.circle_inactive));
                ((ImageView)layIndex.getChildAt(newPosition)).setImageDrawable(getResources().getDrawable(R.drawable.circle_active));
                position=newPosition;
            }
        }
    }

    private int getListviewWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

        return screenSize.x;
    }

    public void onItemClickHandler(View view) {

        try
        {
            Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra("ID",view.getTag().toString());
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
            switch (view.getId())
            {
                case R.id.btnPlus:
                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.update(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(),new String[][]{{"quantity",""+(menuFragment.cart.getItems().get((int)view.getTag()).getItemQuantity()+1)}}, new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnMinus:
                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.update(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(),new String[][]{{"quantity",""+(menuFragment.cart.getItems().get((int)view.getTag()).getItemQuantity()-1)}}, new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnDelete:
                    ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.VISIBLE);
                    moltin.cart.remove(menuFragment.cart.getItems().get((int)view.getTag()).getItemIdentifier(), new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            ((LinearLayout)findViewById(R.id.layLoading)).setVisibility(View.GONE);
                            menuFragment.refresh();
                            if (msg.what == Constants.RESULT_OK) {
                                try {
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCheckout:
                    Intent intent = new Intent(this, ShippingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnMenu:
                    onHomeClicked();
                    break;
                case R.id.btnCart:
                    onHomeClicked();
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onHomeClicked() {
        toggle();
    }

    private void getCollections() throws Exception {
        moltin.collection.listing((JSONObject) null,new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == Constants.RESULT_OK) {

                    items=new ArrayList<CollectionItem>();
                    try {
                        JSONObject json=(JSONObject)msg.obj;
                        if(json.has("status") && json.getBoolean("status") && json.has("result") && json.getJSONArray("result").length()>0)
                        {
                            for(int i=0;i<json.getJSONArray("result").length();i++)
                            {
                                items.add(new CollectionItem(json.getJSONArray("result").getJSONObject(i)));
                            }
                        }

                        Fragment fragment= CollectionFragment.newInstance(items,getListviewWidth());
                        mContent = fragment;
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, mContent)
                                .commit();
                        menu.showContent();

                        setInitialPosition();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onFragmentInteractionForCollectionItem(String itemId) {

    }

    @Override
    protected void onDestroy() {
        try
        {
            instance=null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        try
        {
            menuFragment.refresh();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onFragmentInteractionForCartItem(CartItem item) {

    }

    @Override
    public void onFragmentChangeForCartItem(TotalCartItem cart) {
        ((TextView)findViewById(R.id.txtTotalPrice)).setText(cart.getItemTotalPrice());
    }
}
/*public class CollectionActivity extends SlidingFragmentActivity implements CollectionFragment.OnFragmentInteractionListener,MenuFragment.OnFragmentInteractionListener{
    private Moltin moltin;
    private Context context;

    private ActionBar actionBar;
    private SlidingMenu menu;
    private android.app.Fragment mContent;
    private MenuFragment menuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_collection);

        moltin = new Moltin(this);
        //moltin.resetAuthenticationData();



        try
        {
            moltin.authenticate("umRG34nxZVGIuCSPfYf8biBSvtABgTR8GMUtflyE", new Handler.Callback() {//"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    showPopup(msg.obj.toString());
                    if (msg.what == Constants.RESULT_OK) {

                        try {
                            getCollections();
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

        ArrayList<CollectionItem> items = new ArrayList<CollectionItem>();

        if (savedInstanceState != null)
            mContent = getFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null) {
            mContent = CollectionFragment.newInstance(items, "", 0);
        }

        setContentView(R.layout.collection_content_frame);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.collection_content_frame, mContent)
                .commit();

        setBehindContentView(R.layout.menu_frame);
        menuFragment = MenuFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, menuFragment)
                .commit();

    }

    private void getCollections() throws Exception {
        moltin.collection.listing((JSONObject) null,new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                showPopup(msg.obj.toString());
                if (msg.what == Constants.RESULT_OK) {

                    ArrayList<CollectionItem> items=new ArrayList<CollectionItem>();
                    try {
                        JSONObject json=(JSONObject)msg.obj;
                        if(json.has("status") && json.getBoolean("status") && json.has("result") && json.getJSONArray("result").length()>0)
                        {
                            for(int i=0;i<json.getJSONArray("result").length();i++)
                            {
                                items.add(new CollectionItem(json.getJSONArray("result").getJSONObject(i)));
                            }
                        }

                        Fragment fragment= CollectionFragment.newInstance(items, "COLLECTIONS", 0);
                        mContent = fragment;
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.collection_content_frame, fragment)
                                .commit();
                        menu.showContent();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onFragmentInteractionForCollectionItem(String itemId) {

        try {
            Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra("ID",itemId);
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
            switch (view.getId())
            {
                case R.id.btnCollections:
                    getCollections();
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
}*/
