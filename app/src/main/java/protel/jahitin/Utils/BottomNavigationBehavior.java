package protel.jahitin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView>{
    public BottomNavigationBehavior() {
        super();
    }

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        boolean dependsOn = dependency instanceof FrameLayout;
        return dependsOn;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if(dyConsumed > 0){
            hideBottomNavigationView(child);
        }else if (dyConsumed < 0){
            showBottomNavigationView(child);
        }
    }

    private void showBottomNavigationView(BottomNavigationView view){
        view.clearAnimation();
        view.animate().translationY(0);
    }

    private void hideBottomNavigationView(BottomNavigationView view){
        view.clearAnimation();
        view.animate().translationY(view.getHeight());
    }
}
