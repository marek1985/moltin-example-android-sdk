package moltin.example_moltin.activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import moltin.android_sdk.Moltin;
import moltin.android_sdk.utilities.Constants;
import moltin.example_moltin.R;
import moltin.example_moltin.data.ModifierItem;
import moltin.example_moltin.data.VariationItem;

public class DetailActivity extends Activity implements NumberPicker.OnValueChangeListener {

    private String itemId;
    private String itemTitle;
    private String itemDescription;
    private String itemPictureUrl;
    private String itemBrand;
    private String itemPrice;
    private String itemModifier;
    private String itemCollection;

    private String[][][] modifiers;

    private String[] urls;

    private Moltin moltin;

    private int quantity=1;

    private int[] modifierIndex=null;
    ArrayList<ModifierItem> modItems=null;
    ArrayList<VariationItem> varItems=null;
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
        itemModifier = getIntent().getExtras().getString("MODIFIER");
        itemCollection = getIntent().getExtras().getString("COLLECTION");

        urls = itemPictureUrl.split("\"");

        try {
            ((TextView)findViewById(R.id.txtDetailTitle)).setText(itemTitle);
            ((TextView)findViewById(R.id.txtDetailDescription)).setText(itemDescription);
            ((TextView)findViewById(R.id.txtDetailBrand)).setText(itemBrand);
            ((TextView)findViewById(R.id.txtDetailPrice)).setText(itemPrice);
            ((TextView)findViewById(R.id.txtDetailCollection)).setText(itemCollection);

            if(urls!=null && urls.length>0 && urls[0].length()>3)
            {
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                        // You can pass your own memory cache implementation
                        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        .build();

                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.init(config);
                imageLoader.displayImage(urls[0].replace("|",""), (ImageView)findViewById(R.id.imgDetailPhoto));
                //new DownloadImageTask(((ImageView)findViewById(R.id.imgDetailPhoto))).execute(urls[0].replace("|",""));
            }
            else
                ((ImageView)findViewById(R.id.imgDetailPhoto)).setImageResource(android.R.color.transparent);
        } catch (Exception e) {
            e.printStackTrace();
        }



        try
        {
            if(itemModifier!=null && itemModifier.length()>0 && !itemModifier.equals("null"))
            {
                JSONArray jsonModsArray=null;
                try {
                    jsonModsArray = new JSONArray(itemModifier);
                } catch (JSONException ex) {

                }

                JSONObject jsonMods=null;
                try {
                    jsonMods = new JSONObject(itemModifier);
                } catch (JSONException ex) {

                }
                varItems = new ArrayList<VariationItem>();
                modItems = new ArrayList<ModifierItem>();

                for(int x=0;(jsonMods!=null && x==0) || (jsonModsArray!=null && x<jsonModsArray.length());x++)
                {


                    if(jsonModsArray!=null)
                        jsonMods=jsonModsArray.getJSONObject(x);

                    Iterator i1 = jsonMods.keys();

                    String modifierId, modifierTitle, variationId, variationTitle, variationDiffernce;
                    while (i1.hasNext()) {
                        String key1 = (String) i1.next();
                        if (jsonMods.get(key1) instanceof JSONObject) {
                            varItems = new ArrayList<VariationItem>();

                            modifierId = jsonMods.getJSONObject(key1).getString("id");
                            modifierTitle = jsonMods.getJSONObject(key1).getString("title");

                            JSONObject jsonVars = jsonMods.getJSONObject(key1).getJSONObject("variations");
                            Iterator i2 = jsonVars.keys();

                            while (i2.hasNext()) {
                                String key2 = (String) i2.next();
                                if (jsonVars.get(key2) instanceof JSONObject) {
                                    variationId = jsonVars.getJSONObject(key2).getString("id");
                                    variationTitle = jsonVars.getJSONObject(key2).getString("title");
                                    variationDiffernce = (jsonVars.getJSONObject(key2).has("difference") ? jsonVars.getJSONObject(key2).getString("difference") : "");

                                    varItems.add(new VariationItem(variationId, variationTitle, variationDiffernce));
                                }
                            }

                            modItems.add(new ModifierItem(modifierId, modifierTitle, varItems));
                        }
                    }
                }
                LinearLayout layoutModifiers=(LinearLayout)findViewById(R.id.layModifiers);

                modifierIndex=new int[modItems.size()];

                for(int i=0;i<modItems.size();i++)
                {
                    modifierIndex[i]=0;
                    TextView textModifier=new TextView(this);
                    textModifier.setText("Select " + modItems.get(i).getItemTitle());
                    textModifier.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
                    layoutModifiers.addView(textModifier);

                    ArrayList<String> spinnerArray = new ArrayList<String>();

                    for(int j=0;j<modItems.get(i).getItemVariation().size();j++)
                    {
                        /*TextView textVariation=new TextView(this);
                        textVariation.setText(modItems.get(i).getItemVariation().get(j).getItemTitle() + "(" + modItems.get(i).getItemVariation().get(j).getItemDifference() + ")");
                        textVariation.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Light.otf"));
                        layoutModifiers.addView(textVariation);*/

                        spinnerArray.add(modItems.get(i).getItemVariation().get(j).getItemTitle() + " (" + modItems.get(i).getItemVariation().get(j).getItemDifference() + ")");
                    }

                    final int index=i;

                    Spinner spinner = new Spinner(this);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

                    spinner.setAdapter(spinnerArrayAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            modifierIndex[index]=i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    layoutModifiers.addView(spinner);

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        LinearLayout layoutImages=(LinearLayout)findViewById(R.id.layImages);
        LinearLayout layoutScrollImages=(LinearLayout)findViewById(R.id.layScrollImages);

        try
        {
            if(urls.length>1)
            {
                layoutImages.setVisibility(View.VISIBLE);
                if(layoutScrollImages.getChildCount() > 0)
                    layoutScrollImages.removeAllViews();

                for(int i=1;i<urls.length;i++)
                {
                    ImageView img=new ImageView(this);
                    String imageUrlNext=urls[i].replace("|","");
                    if(img!=null && imageUrlNext.length()>3)
                    {
                        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                                // You can pass your own memory cache implementation
                                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                                .build();

                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.init(config);
                        imageLoader.displayImage(imageUrlNext, img);
                        //new moltin.example_moltin.utils.DownloadImageTask(img).execute(imageUrlNext);
                    }
                    else
                    img.setImageResource(android.R.color.transparent);

                    final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                            (int)(80*scale + 0.5f),
                            (int)(80*scale + 0.5f));
                    img.setLayoutParams(params);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    layoutScrollImages.addView(img);
                }
            }
            else
            {
                layoutImages.setVisibility(View.GONE);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
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
                                ((LinearLayout) findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
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

        ((TextView)findViewById(R.id.txtActivityTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        ((TextView)findViewById(R.id.txtDetailTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        ((TextView)findViewById(R.id.txtDetailPrice)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        ((TextView)findViewById(R.id.txtDetailCollection)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Regular.otf"));
        ((TextView)findViewById(R.id.txtDetailBrand)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "heuristica/Heuristica-Italic.otf"));
        ((TextView)findViewById(R.id.txtDetailDescription)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "heuristica/Heuristica-Italic.otf"));
        ((Button)findViewById(R.id.btnPutIntoCart)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId()) {
                case R.id.btnPutIntoCart:
                    show();
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

    public void show()
    {

        final Dialog d = new Dialog(this);
        d.setTitle("Select quantity");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        b1.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_violet));
        Button b2 = (Button) d.findViewById(R.id.button2);
        b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_violet));
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

                    String[][] modsArray=new String[0][0];
                    if(modItems!=null)
                    {
                        modsArray=new String[modItems.size()][2];
                        for(int i=0;i<modItems.size();i++)
                        {
                            modsArray[i][0]=/*"modifier["+*/modItems.get(i).getItemId()/*+"]"*/;
                            modsArray[i][1]=modItems.get(i).getItemVariation().get(modifierIndex[i]).getItemId();
                        }
                    }

                    moltin.cart.insert(itemId, np.getValue(), modsArray, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            JSONObject jsonObject = (JSONObject) msg.obj;
                            Log.i("add to cart", jsonObject.toString());
                            if (msg.what == Constants.RESULT_OK) {

                                Toast.makeText(getApplicationContext(), "Product added to cart.", Toast.LENGTH_LONG).show();
                                try {
                                    Log.i("add to cart", jsonObject.toString());
                                    if (jsonObject.has("status") && jsonObject.getBoolean("status"))
                                        ((LinearLayout) findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                                    else
                                        ((LinearLayout) findViewById(R.id.layPutIntoCart)).setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ((Button) findViewById(R.id.btnPutIntoCart)).setEnabled(true);
                                return true;
                            } else {
                                Toast.makeText(getApplicationContext(), "Error while adding to cart. Please try again.", Toast.LENGTH_LONG).show();
                                ((Button) findViewById(R.id.btnPutIntoCart)).setEnabled(true);
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

        quantity = newVal;

    }
}