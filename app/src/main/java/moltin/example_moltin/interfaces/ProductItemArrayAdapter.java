package moltin.example_moltin.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import moltin.example_moltin.R;
import moltin.example_moltin.data.ProductItem;

public class ProductItemArrayAdapter extends ArrayAdapter<ProductItem> {

    private Context mContext;
    private String imageUrl;
    private ProductItem postItem;
    private String titleEng;

    public ProductItemArrayAdapter(Context context, List<ProductItem> items, String title) {
        super(context, R.layout.product_list_item, items);
        mContext = context;
        titleEng = title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        {
            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.product_list_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
                viewHolder.description = (TextView) convertView.findViewById(R.id.txtDescription);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.imgItem);
                viewHolder.brand = (TextView) convertView.findViewById(R.id.txtBrand);
                viewHolder.price = (TextView) convertView.findViewById(R.id.txtPrice);
                convertView.setTag(viewHolder);
            } else {
                try
                {
                    viewHolder = (ViewHolder) convertView.getTag();

                    if (viewHolder == null)
                    {
                        convertView = inflater.inflate(R.layout.product_list_item, parent, false);

                        viewHolder = new ViewHolder();
                        viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
                        viewHolder.description = (TextView) convertView.findViewById(R.id.txtDescription);
                        viewHolder.image = (ImageView) convertView.findViewById(R.id.imgItem);
                        viewHolder.brand = (TextView) convertView.findViewById(R.id.txtBrand);
                        viewHolder.price = (TextView) convertView.findViewById(R.id.txtPrice);
                        convertView.setTag(viewHolder);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return convertView;
                }
            }
            postItem = getItem(position);

            imageUrl = postItem.getItemPictureUrl();

            try {
                viewHolder.title.setText(postItem.getItemName());
                viewHolder.description.setText(postItem.getShortItemDescription());
                viewHolder.brand.setText(postItem.getItemBrand());
                viewHolder.price.setText(postItem.getItemPrice());

                if(imageUrl!=null && imageUrl.length()>3)new DownloadImageTask(viewHolder.image).execute(imageUrl);
                else viewHolder.image.setImageResource(android.R.color.transparent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
        TextView brand;
        TextView price;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
