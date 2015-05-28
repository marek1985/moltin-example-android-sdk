package moltin.example_moltin;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;


public class MainActivity extends ActionBarActivity {
    private Moltin moltin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moltin = new Moltin(this);

        moltin.resetAuthenticationData();
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
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
                    moltin.address.find("0", null, new Handler.Callback() {
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
                    moltin.address.list("0", null, new Handler.Callback() {
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
                    JSONObject jsonCustomer = new JSONObject();
                    jsonCustomer.put("save_as","3");
                    jsonCustomer.put("first_name","Joe");
                    jsonCustomer.put("last_name","Black");
                    jsonCustomer.put("address_1","High Street");
                    jsonCustomer.put("address_2","Example Village");
                    jsonCustomer.put("postcode","1000");
                    jsonCustomer.put("country","GB");

                    moltin.address.create("0", jsonCustomer, new Handler.Callback() {
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
                    moltin.brand.find(null, new Handler.Callback() {
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
                    moltin.brand.list(null, new Handler.Callback() {
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
                    moltin.cart.insert("0", 2, "mods", new Handler.Callback() {
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
                    JSONObject jsonCart1 = new JSONObject();
                    jsonCart1.put("update", "");

                    moltin.cart.update("0",jsonCart1, new Handler.Callback() {
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
                    JSONObject jsonCart2 = new JSONObject();
                    jsonCart2.put("complete", "");

                    moltin.cart.complete(jsonCart2, new Handler.Callback() {
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
                    moltin.category.find(null, new Handler.Callback() {
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
                    moltin.category.list(null, new Handler.Callback() {
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
                    moltin.category.tree(null, new Handler.Callback() {
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
                    JSONObject jsonPayment = new JSONObject();
                    jsonPayment.put("payment", "");

                    moltin.checkout.payment("0", "1", jsonPayment, new Handler.Callback() {
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
                    moltin.collection.find(null, new Handler.Callback() {
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
                    moltin.collection.list(null, new Handler.Callback() {
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
                    moltin.currency.find(null, new Handler.Callback() {
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
                    moltin.currency.list(null, new Handler.Callback() {
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
                    moltin.entry.find("flow1", null, new Handler.Callback() {
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
                    moltin.entry.list("flow1", null, new Handler.Callback() {
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
                    moltin.gateway.list(null, new Handler.Callback() {
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
                    moltin.order.find(null, new Handler.Callback() {
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
                    moltin.order.list(null, new Handler.Callback() {
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
                    JSONObject jsonOrder = new JSONObject();
                    jsonOrder.put("order","3");

                    moltin.order.create(jsonOrder, new Handler.Callback() {
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
                    moltin.product.find(null, new Handler.Callback() {
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
                    moltin.product.list(null, new Handler.Callback() {
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
                    moltin.product.search(null, new Handler.Callback() {
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
                    moltin.product.fields("", new Handler.Callback() {
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
                    moltin.shipping.list(null, new Handler.Callback() {
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
                    moltin.tax.find(null, new Handler.Callback() {
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
                    moltin.tax.list(null, new Handler.Callback() {
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
        AlertDialog.Builder certBuilder = new AlertDialog.Builder(
                this);
        final View recipientsLayout = getLayoutInflater().inflate(R.layout.scroll_textview, null);
        final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.textPopup);
        recipientsTextView.setText(text);
        certBuilder.setView(recipientsLayout);
        certBuilder.show();
        // set rest of alertdialog attributes
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
