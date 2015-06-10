package moltin.example_moltin.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import moltin.example_moltin.activities.CartActivity;
import moltin.example_moltin.data.CartItem;
import moltin.example_moltin.interfaces.CartItemArrayAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends ListFragment implements AbsListView.OnScrollListener {

    private ArrayList<CartItem> items;
    private String titleEng;
    private CartItemArrayAdapter itemAdapter;
    private CartActivity cartActivity;
    private int lastPosition=0;

    private OnFragmentInteractionListener mListener;

    private boolean loading=false;

    public static CartFragment newInstance(ArrayList<CartItem> posts, String title, int lastPos) {
        CartFragment fragment = new CartFragment();
        fragment.setArgs(posts, title, lastPos);
        return fragment;
    }

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try
        {
            cartActivity = ((CartActivity)getActivity());

            itemAdapter = new CartItemArrayAdapter(getActivity(), items, titleEng);
            setListAdapter(itemAdapter);
            getListView().setOnScrollListener(this);/*new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    customLoadMoreDataFromApi(page);
                    // or customLoadMoreDataFromApi(totalItemsCount);
                    Log.i("END SCROLL","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                }
            });*/
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mListener.onFragmentInteractionForCartItem(items.get(i));
                }
            });
            //((MainActivity)getActivity()).setButtonsForChannel(titleEng);

            try
            {
                getListView().setSelection(lastPosition);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    final static int SCROLL_STATE_IDLE=0;
    int currentFirstVisibleItem=0;
    int currentVisibleItemCount=0;
    int currentScrollState=SCROLL_STATE_IDLE;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    private void isScrollCompleted() {

        lastPosition = currentFirstVisibleItem;

        if (!loading && this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {

            final ListView lv=getListView();
            if (lv.getLastVisiblePosition() == lv.getAdapter().getCount() -1 &&
                    lv.getChildAt(lv.getChildCount() - 1).getBottom() <= lv.getHeight())
            {
                final int position = lv.getLastVisiblePosition();
                CartItem item = (CartItem)lv.getAdapter().getItem(lv.getChildCount() - 1);

                loading=true;

                final String channelName=titleEng;

            }
            else if (lv.getFirstVisiblePosition() == 0 &&
                    lv.getChildAt(0).getTop() >= 0)
            {
                if(lv  != null && lv.getAdapter() != null && lv.getAdapter().getCount()<=1)
                    return;

                CartItem item = (CartItem)lv.getAdapter().getItem(1);

                loading=true;
                final String channelName=titleEng;

            }
        }
    }

    public void setArgs(ArrayList<CartItem> posts, String title, int lasPos) {
        this.items = posts;
        this.titleEng = title;
        this.lastPosition = lasPos;
    }

    public String getTitleEng() {
        return titleEng;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteractionForCartItem(CartItem item);
    }
}
