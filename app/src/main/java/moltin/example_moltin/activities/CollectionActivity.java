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

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.CollectionItem;
import moltin.example_moltin.fragments.CollectionFragment;
import moltin.example_moltin.fragments.MenuFragment;


public class CollectionActivity extends SlidingFragmentActivity implements CollectionFragment.OnFragmentInteractionListener,MenuFragment.OnFragmentInteractionListener{
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
            moltin.authenticate("umRG34nxZVGIuCSPfYf8biBSvtABgTR8GMUtflyE"/*"wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4"*/, new Handler.Callback() {
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

        /*((LinearLayout)findViewById(R.id.btnCollections)).setBackgroundColor(getResources().getColor(R.color.BLACK));
        ((LinearLayout)findViewById(R.id.btnCart)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((LinearLayout)findViewById(R.id.btnPayment)).setBackgroundColor(getResources().getColor(android.R.color.transparent));*/
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



                /*
                case R.id.btnAuth:
                    moltin.authenticate("wf60kt82vtzkjIMslZ1FmDyV8WUWNQlLxUiRVLS4", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnAddressGet:
                    moltin.address.get("0", "0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnAddressFind:
                    moltin.address.find("0", (JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnAddressList:
                    moltin.address.listing("0", (JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnAddressFields:
                    moltin.address.fields("0", "3", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnAddressCreate:
                    moltin.address.create("0", new String[][]{
                            {"save_as","3"},
                            {"first_name","marko"},
                            {"last_name","mikolavcic"},
                            {"address_1","Example Village"},
                            {"postcode","1000"},
                            {"country","GB"}
                    }, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnBrandGet:
                    moltin.brand.get("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnBrandFind:
                    moltin.brand.find((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnBrandList:
                    moltin.brand.listing((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnBrandFields:
                    moltin.brand.fields("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartContents:
                    moltin.cart.contents(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartInsert:

                    moltin.cart.insert("1",2,new String[][]{{"modifier[1]","2"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartUpdate:

                    moltin.cart.update("1",new String[][]{{"price","10"},{"depth","10"},{"height","10"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartDelete:
                    moltin.cart.delete(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartRemove:
                    moltin.cart.remove("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartItem:
                    moltin.cart.item("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartInCart:
                    moltin.cart.inCart("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartCheckout:
                    moltin.cart.checkout(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCartComplete:
                    moltin.cart.order(new String[][]{{"shipping", "1"}, {"customer", "my@email.com"},{"gateway","2"},{"ship_to","3"},{"bill_to","3"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCategoryGet:
                    moltin.category.get("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCategoryFind:
                    moltin.category.find((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCategoryList:
                    moltin.category.listing((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCategoryTree:
                    moltin.category.tree((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCategoryFields:
                    moltin.category.fields("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCheckoutPayment:
                    String[][] data = new String[][]{
                            {"payment_data[cvv]","123"},
                            {"payment_data[expiry_month]","1"},
                            {"payment_data[expiry_year]","2016"},
                            {"payment_data[issue_number]","123123123123"},
                            {"payment_data[type]","visa"},
                            {"ip","123.123.123.123"},
                            {"cancel_url","www.molt.in/cancel.htm"},
                            {"return_url","www.molt.in/checkout.htm"},
                    };

                    moltin.checkout.payment("0", "1", data, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCollectionGet:
                    moltin.collection.get("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCollectionFind:
                    moltin.collection.find((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCollectionList:
                    moltin.collection.listing((JSONObject) null,new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCollectionFields:
                    moltin.collection.fields("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCurrencyGet:
                    moltin.currency.get("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCurrencyFind:
                    moltin.currency.find((JSONObject)null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCurrencyList:
                    moltin.currency.listing((JSONObject) null,new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnCurrencyFields:
                    moltin.currency.fields("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnEntryGet:
                    moltin.entry.get("flow1", "0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnEntryFind:
                    moltin.entry.find("flow1", (JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnEntryList:
                    moltin.entry.listing("flow1", (JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnGatewayGet:
                    moltin.gateway.get("slug1", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnGatewayList:
                    moltin.gateway.listing(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnOrderGet:

                    moltin.order.get("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnOrderFind:
                    moltin.order.find(new String[][]{{"ship_to","1"},{"status","1"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnOrderList:

                    moltin.order.listing((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnOrderCreate:
                    moltin.order.create(new String[][]{{"ship_to","1"},{"status","1"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnProductGet:
                    moltin.product.get("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnProductFind:
                    moltin.product.find((JSONObject)null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnProductList:
                    moltin.product.listing((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnProductSearch:

                    moltin.product.search(new String[][]{{"collection","999928496018948116"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnProductFields:
                    moltin.product.fields("1", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnProductModifiers:
                    moltin.product.modifiers("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnProductVariations:
                    moltin.product.variations("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnShippingGet:
                    moltin.shipping.get("1", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnShippingList:
                    moltin.shipping.listing(new String[][]{{"slug", "12"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnTaxGet:
                    moltin.tax.get("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnTaxFind:
                    moltin.tax.find(new String[][]{{"rate","20"}}, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnTaxList:
                    moltin.tax.listing((JSONObject) null, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;
                case R.id.btnTaxFields:
                    moltin.tax.fields("0", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            showPopup(msg.obj.toString());
                            if (msg.what == Constants.RESULT_OK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    break;*/
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
