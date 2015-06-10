package moltin.example_moltin.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import moltin.example_moltin.R;
import moltin.example_moltin.data.CartItem;

public class CartItemArrayAdapter extends ArrayAdapter<CartItem> {

    private Context context;
    private String imageUrl;
    private CartItem item;
    private String titleEng;

    public CartItemArrayAdapter(Context context, List<CartItem> items, String title) {
        super(context, R.layout.cart_list_item, items);
        this.context = context;
        titleEng = title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        {
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.cart_list_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
                viewHolder.price = (TextView) convertView.findViewById(R.id.txtPrice);
                viewHolder.quantity = (TextView) convertView.findViewById(R.id.txtQuantity);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.imgItem);
                viewHolder.btnPlus = (LinearLayout) convertView.findViewById(R.id.btnPlus);
                viewHolder.btnMinus = (LinearLayout) convertView.findViewById(R.id.btnMinus);
                viewHolder.btnDelete = (LinearLayout) convertView.findViewById(R.id.btnDelete);

                convertView.setTag(viewHolder);
            } else {
                try
                {
                    viewHolder = (ViewHolder) convertView.getTag();

                    if (viewHolder == null)
                    {
                        convertView = inflater.inflate(R.layout.cart_list_item, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
                        viewHolder.price = (TextView) convertView.findViewById(R.id.txtPrice);
                        viewHolder.quantity = (TextView) convertView.findViewById(R.id.txtQuantity);
                        viewHolder.image = (ImageView) convertView.findViewById(R.id.imgItem);
                        viewHolder.btnPlus = (LinearLayout) convertView.findViewById(R.id.btnPlus);
                        viewHolder.btnMinus = (LinearLayout) convertView.findViewById(R.id.btnMinus);
                        viewHolder.btnDelete = (LinearLayout) convertView.findViewById(R.id.btnDelete);


                        convertView.setTag(viewHolder);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return convertView;
                }
            }
            item = getItem(position);


            try {
                viewHolder.title.setText(item.getItemName());
                viewHolder.price.setText(item.getItemPrice());
                viewHolder.quantity.setText("" + item.getItemQuantity());

                viewHolder.btnPlus.setTag(position);
                viewHolder.btnMinus.setTag(position);
                viewHolder.btnDelete.setTag(position);

                if(item.getItemPictureUrl()!=null && item.getItemPictureUrl().length>0);
                {
                    try {
                        String imageUrl= item.getItemPictureUrl()[0];
                        if(imageUrl!=null && imageUrl.length()>3)
                        {
                            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                                    // You can pass your own memory cache implementation
                                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                                    .build();

                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.init(config);
                            imageLoader.displayImage(imageUrl, viewHolder.image);
                            //new DownloadImageTask(holder.image).execute(imageUrl);
                        }
                        else viewHolder.image.setImageResource(android.R.color.transparent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView price;
        TextView quantity;
        ImageView image;
        LinearLayout btnPlus;
        LinearLayout btnMinus;
        LinearLayout btnDelete;
    }
}
