package moltin.example_moltin.interfaces;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import moltin.example_moltin.R;
import moltin.example_moltin.data.ProductItem;

public class ProductListAdapterHolder extends CustomRecyclerView.Adapter<ProductListAdapterHolder.ViewHolder> {

    private final FragmentActivity activity;
    private List<ProductItem> items = new ArrayList<ProductItem>();
    private int width;
    private OnItemClickListener itemClickListener;

    public ProductListAdapterHolder(FragmentActivity mActivity, List<ProductItem> items, int width) {
        this.activity = mActivity;
        this.items = items;
        this.width = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.product_list_item, parent, false);

        CustomRecyclerView.LayoutParams params = (CustomRecyclerView.LayoutParams)sView.getLayoutParams();
        params.width = width;
        sView.setLayoutParams(params);

        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder , int position) {
        holder.title.setText(items.get(position).getItemName());
        holder.description.setText(items.get(position).getItemPrice());
        holder.collection.setText(items.get(position).getItemCollection());
        holder.button.setTag(items.get(position).getItemId());

        ((Button)holder.button).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), "heuristica/Heuristica-Italic.otf"));
        ((TextView)holder.description).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), "heuristica/Heuristica-Italic.otf"));
        ((TextView)holder.collection).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));
        ((TextView)holder.title).setTypeface(Typeface.createFromAsset(activity.getResources().getAssets(), "montserrat/Montserrat-Bold.otf"));

        holder.image.setImageResource(android.R.color.transparent);
        if(items.get(position).getItemPictureUrl()!=null && items.get(position).getItemPictureUrl().length>0);
        {
            try {
                String imageUrl=items.get(position).getItemPictureUrl()[0];
                if(imageUrl!=null && imageUrl.length()>3)
                {
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity.getApplicationContext())
                            // You can pass your own memory cache implementation
                            .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                            .build();

                    /*final float scale = activity.getApplicationContext().getResources().getDisplayMetrics().density;

                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .displayer(new RoundedBitmapDisplayer((int)(10*scale + 0.5f))) //rounded corner bitmap
                            .cacheInMemory(true)
                            .cacheOnDisc(true)
                            .build();*/

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.init(config);
                    imageLoader.displayImage(imageUrl, holder.image/*, options*/);
                    //new DownloadImageTask(holder.image).execute(imageUrl);
                }
                else holder.image.setImageResource(android.R.color.transparent);

                if(items.get(position).getItemPictureUrl().length>1)
                {
                    holder.layoutImages.setVisibility(View.VISIBLE);
                    if(holder.layoutScrollImages.getChildCount() > 0)
                        holder.layoutScrollImages.removeAllViews();

                    for(int i=1;i<items.get(position).getItemPictureUrl().length;i++)
                    {
                        ImageView img=new ImageView(activity);
                        String imageUrlNext=items.get(position).getItemPictureUrl()[i];
                        if(img!=null && imageUrlNext.length()>3)
                        {
                            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity.getApplicationContext())
                                    // You can pass your own memory cache implementation
                                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                                    .build();

                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.init(config);
                            imageLoader.displayImage(imageUrlNext, img);
                            //new DownloadImageTask(img).execute(imageUrlNext);
                        }
                        else img.setImageResource(android.R.color.transparent);

                        final float scale = activity.getApplicationContext().getResources().getDisplayMetrics().density;
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                                (int)(80*scale + 0.5f),
                                (int)(80*scale + 0.5f));
                        img.setLayoutParams(params);
                        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        holder.layoutScrollImages.addView(img);
                    }
                }
                else
                {
                    holder.layoutImages.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends CustomRecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, description, collection;
        Button button;
        ImageView image;
        LinearLayout layoutImages;
        LinearLayout layoutScrollImages;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtTitle);
            description = (TextView) view.findViewById(R.id.txtDescription);
            collection = (TextView) view.findViewById(R.id.txtCollection);
            button = (Button) view.findViewById(R.id.btnInItem);
            image = (ImageView) view.findViewById(R.id.imgItem);
            layoutImages = (LinearLayout) view.findViewById(R.id.layImages);
            layoutScrollImages = (LinearLayout) view.findViewById(R.id.layScrollImages);
            view.setOnClickListener(this);

            /*button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    ProductActivity act=ProductActivity.instance;
                    if(act!=null)
                    {
                        act.showProduct(items.get(getPosition()));
                    }
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }
}
