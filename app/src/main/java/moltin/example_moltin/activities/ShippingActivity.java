package moltin.example_moltin.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;

public class ShippingActivity extends Activity {

    private Moltin moltin;

    String address="1004558173891199887";
    String customer="1004554501165679501";
    String email="myemail@email.com";
    String first_name="joe";
    String last_name="black";
    String address_1="address 1";
    String address_2="address 2";
    String country="GB";
    String postcode="1111";

    String gateway="stripe";
    static String method="purchase";
    String shipping="next-hour-delivery";
    String bill_to=address;
    String ship_to=address;

    String order="1004598531207463836";

    String number="4242424242424242";
    String expiry_month="12";
    String expiry_year="16";
    String cvv="123";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        moltin = new Moltin(this);

        //checkoutOrder();

        //cartOrder();

        payment();




        //createCustomer();

        //getCustomer();

        //findCustomer();

        //createAddressForCustomer();


    }

    private void cartOrder() {
        try {
            moltin.cart.order(new String[][]{
                    {"customer",customer},
                    {"shipping",shipping},
                    {"gateway",gateway},
                    {"bill_to",bill_to},
                    {"ship_to",ship_to}
            }, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    JSONObject json = (JSONObject) msg.obj;

                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            Log.i("Cart order",json.getJSONObject("result").getString("id"));
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
                    JSONObject json = (JSONObject) msg.obj;

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
                    JSONObject json = (JSONObject) msg.obj;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCustomer() {
        try {
            moltin.customer.get(customer,new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    JSONObject json=(JSONObject)msg.obj;

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
                    JSONObject json = (JSONObject) msg.obj;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkoutOrder() {
        try {
            moltin.cart.checkout(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    JSONObject json=(JSONObject)msg.obj;

                    if (msg.what == Constants.RESULT_OK) {
                        try {
                            JSONArray jsonArrayMethods = json.getJSONObject("result").getJSONObject("shipping").getJSONArray("methods");
                            for(int i=0;i<jsonArrayMethods.length();i++)
                            {
                                Log.i("methods", jsonArrayMethods.getJSONObject(i).toString());
                                Log.i("methods", jsonArrayMethods.getJSONObject(i).getString("slug"));
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

    private void createAddressForCustomer() {
        try {
            moltin.address.create(customer, new String[][]{
                    {"first_name",first_name},
                    {"last_name",last_name},
                    {"address_1",address_1},
                    {"address_2",address_2},
                    {"country",country},
                    {"postcode",postcode}
            },new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    JSONObject json=(JSONObject)msg.obj;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
