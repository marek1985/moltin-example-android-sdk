package com.applidium.shutterbug;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.applidium.shutterbug.utils.ShutterbugManager;
import com.applidium.shutterbug.utils.ShutterbugManager.ShutterbugManagerListener;

public class FetchableImageView extends ImageView implements ShutterbugManagerListener {
    public interface FetchableImageViewListener {
        void onImageFetched(Bitmap bitmap, String url);

        void onImageFailure(String url);
    }

    private FetchableImageViewListener mListener;

    public FetchableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FetchableImageView(Context context) {
        super(context);
    }

    public FetchableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public FetchableImageViewListener getListener() {
        return mListener;
    }

    public void setListener(FetchableImageViewListener listener) {
        mListener = listener;
    }

    public void setImage(String url) {
        setImage(url, new ColorDrawable(getContext().getResources().getColor(R.color.transparent)));
    }

    public void setImage(String url, int desiredHeight, int desiredWidth) {
        setImage(url, new ColorDrawable(getContext().getResources().getColor(R.color.transparent)), desiredHeight, desiredWidth);
    }

    public void setImage(String url, int placeholderDrawableId) {
        setImage(url, getContext().getResources().getDrawable(placeholderDrawableId));
    }

    public void setImage(String url, Drawable placeholderDrawable) {
        setImage(url, placeholderDrawable, -1, -1);
    }

    public void setImage(String url, Drawable placeholderDrawable, int desiredHeight, int desiredWidth) {
        final ShutterbugManager manager = ShutterbugManager.getSharedImageManager(getContext());
        manager.cancel((ShutterbugManagerListener) this);
        setImageDrawable(placeholderDrawable);
        if (url != null) {
            manager.download(url, (ShutterbugManagerListener) this, desiredHeight, desiredWidth);
        }
    }

    public void cancelCurrentImageLoad() {
        ShutterbugManager.getSharedImageManager(getContext()).cancel((ShutterbugManagerListener) this);
    }

    @Override
    public void onImageSuccess(ShutterbugManager imageManager, Bitmap bitmap, String url) {
        setImageBitmap(bitmap);
        requestLayout();
        if (mListener != null) {
            mListener.onImageFetched(bitmap, url);
        }
    }

    @Override
    public void onImageFailure(ShutterbugManager imageManager, String url) {
        if (mListener != null) {
            mListener.onImageFailure(url);
        }
    }

}
