package com.example.zisakuziten;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;

public class DownloadBarBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private int defaultDependencyTop = -1;

    public DownloadBarBehavior(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout linearLayout, View dependency){
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,LinearLayout linearLayout,View dependency){
        if (defaultDependencyTop == -1){
            defaultDependencyTop = dependency.getTop();
        }
        ViewCompat.setTranslationY(linearLayout,-dependency.getTop() + defaultDependencyTop);
        return true;
    }
}
