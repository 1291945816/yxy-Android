package kilig.ink.yxy.entity;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 用于设置间隔的
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
     private int mSpace;
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }

    }

    public  SpacesItemDecoration (int space) {
        this.mSpace = space;
    }
}
