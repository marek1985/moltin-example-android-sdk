package moltin.example_moltin.interfaces;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 09/06/15.
 */
public class CustomRecyclerView extends RecyclerView {
    private int screenWidth=0;

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setWidth(int width)
    {
        screenWidth=width;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        //return super.fling(velocityX,velocityY);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

        //these four variables identify the views you see on screen.
        int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
        int firstVisibleView = linearLayoutManager.findFirstVisibleItemPosition();
        View firstView = linearLayoutManager.findViewByPosition(firstVisibleView);
        View lastView = linearLayoutManager.findViewByPosition(lastVisibleView);

        //these variables get the distance you need to scroll in order to center your views.
        //my views have variable sizes, so I need to calculate side margins separately.
        //note the subtle difference in how right and left margins are calculated, as well as
        //the resulting scroll distances.
        if(lastView==null)
            return super.fling(velocityX, velocityY);

        int leftMargin = (screenWidth - lastView.getWidth()) / 2;
        int rightMargin = (screenWidth - firstView.getWidth()) / 2 + firstView.getWidth();
        int leftEdge = lastView.getLeft();
        int rightEdge = firstView.getRight();
        int scrollDistanceLeft = leftEdge - leftMargin;
        int scrollDistanceRight = rightMargin - rightEdge;

        //if(user swipes to the left)
        if(velocityX > 0)
        {
            if(leftEdge==0 && scrollDistanceLeft==0 && scrollDistanceRight==0)
                return super.fling(velocityX, velocityY);
            else
            {
                smoothScrollBy(scrollDistanceLeft, 0);
            }
        }
        else
        {
            if(leftEdge==0 && scrollDistanceLeft==0 && scrollDistanceRight==0)
                return super.fling(velocityX,velocityY);
            else
            {
                smoothScrollBy(-scrollDistanceRight, 0);
            }
        }

        return true;
    }
}
