package com.aepronunciation.ipa;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Base activity that provides consistent edge-to-edge support and window insets handling
 * across the entire application for modern Android versions (including Android 15+).
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initEdgeToEdge();
        super.onCreate(savedInstanceState);
    }

    protected void initEdgeToEdge() {
        if (useDarkStatusBarIcons()) {
            EdgeToEdge.enable(this,
                    SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
                    SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT));
        } else {
            EdgeToEdge.enable(this,
                    SystemBarStyle.dark(Color.TRANSPARENT),
                    SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT));
        }
    }

    /**
     * Returns true if status bar icons should adapt to system light/dark theme.
     * By default, activities use a Toolbar with primaryColor (blue) and white text,
     * so status bar icons should be light (return false).
     * Subclasses without a Toolbar (like TestActivity) can override this to return true.
     */
    protected boolean useDarkStatusBarIcons() {
        return false;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupEdgeToEdgeInsets();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setupEdgeToEdgeInsets();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setupEdgeToEdgeInsets();
    }

    protected void setupEdgeToEdgeInsets() {
        ViewGroup content = findViewById(android.R.id.content);
        if (content == null || content.getChildCount() == 0) {
            return;
        }
        View rootView = content.getChildAt(0);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);

        final int initialRootLeft = rootView.getPaddingLeft();
        final int initialRootTop = rootView.getPaddingTop();
        final int initialRootRight = rootView.getPaddingRight();
        final int initialRootBottom = rootView.getPaddingBottom();

        if (toolbar != null) {
            final int initialToolbarLeft = toolbar.getPaddingLeft();
            final int initialToolbarTop = toolbar.getPaddingTop();
            final int initialToolbarRight = toolbar.getPaddingRight();
            final int initialToolbarBottom = toolbar.getPaddingBottom();

            int baseActionBarSize = 0;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(androidx.appcompat.R.attr.actionBarSize, tv, true)) {
                baseActionBarSize = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            if (baseActionBarSize == 0) {
                baseActionBarSize = toolbar.getMinimumHeight();
            }
            final int actionBarSize = baseActionBarSize;

            final View tabLayout = rootView.findViewById(R.id.tab_layout);
            final View viewPager = rootView.findViewById(R.id.pager);

            final int initialTabLeft = tabLayout != null ? tabLayout.getPaddingLeft() : 0;
            final int initialTabRight = tabLayout != null ? tabLayout.getPaddingRight() : 0;

            final int initialPagerLeft = viewPager != null ? viewPager.getPaddingLeft() : 0;
            final int initialPagerRight = viewPager != null ? viewPager.getPaddingRight() : 0;

            final List<View> contentChildren = new ArrayList<>();
            final List<int[]> contentChildrenInitialPadding = new ArrayList<>();
            if (rootView instanceof ViewGroup && tabLayout == null && viewPager == null) {
                ViewGroup vg = (ViewGroup) rootView;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    if (child != toolbar) {
                        contentChildren.add(child);
                        contentChildrenInitialPadding.add(new int[]{
                                child.getPaddingLeft(),
                                child.getPaddingTop(),
                                child.getPaddingRight(),
                                child.getPaddingBottom()
                        });
                    }
                }
            }

            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, windowInsets) -> {
                Insets systemBars = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
                );
                Insets ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
                int bottomInset = Math.max(systemBars.bottom, ime.bottom);

                // 1. Toolbar: draw background behind status bar, pad contents below status bar
                toolbar.setPadding(
                        initialToolbarLeft + systemBars.left,
                        initialToolbarTop + systemBars.top,
                        initialToolbarRight + systemBars.right,
                        initialToolbarBottom
                );
                // Keep minimum height as actionBarSize (56dp) so navigation icon alignmentHeight
                // is correctly 56dp and not inflated by statusBars.top
                if (actionBarSize > 0) {
                    toolbar.setMinimumHeight(actionBarSize);
                    ViewGroup.LayoutParams lp = toolbar.getLayoutParams();
                    if (lp != null) {
                        lp.height = actionBarSize + systemBars.top;
                        toolbar.setLayoutParams(lp);
                    }
                }

                // 2. Root view: pad bottom so bottom buttons/keyboard are not covered by navigation bar / IME
                rootView.setPadding(
                        initialRootLeft,
                        initialRootTop,
                        initialRootRight,
                        initialRootBottom + bottomInset
                );

                // 3. Horizontal insets for content below toolbar
                if (tabLayout != null) {
                    tabLayout.setPadding(
                            initialTabLeft + systemBars.left,
                            tabLayout.getPaddingTop(),
                            initialTabRight + systemBars.right,
                            tabLayout.getPaddingBottom()
                    );
                }
                if (viewPager != null) {
                    viewPager.setPadding(
                            initialPagerLeft + systemBars.left,
                            viewPager.getPaddingTop(),
                            initialPagerRight + systemBars.right,
                            viewPager.getPaddingBottom()
                    );
                }
                for (int i = 0; i < contentChildren.size(); i++) {
                    View child = contentChildren.get(i);
                    int[] initialPadding = contentChildrenInitialPadding.get(i);
                    child.setPadding(
                            initialPadding[0] + systemBars.left,
                            initialPadding[1],
                            initialPadding[2] + systemBars.right,
                            initialPadding[3]
                    );
                }

                return windowInsets;
            });
        } else {
            // No toolbar (e.g. TestActivity): pad root view on all sides
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, windowInsets) -> {
                Insets systemBars = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
                );
                Insets ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
                int bottomInset = Math.max(systemBars.bottom, ime.bottom);

                rootView.setPadding(
                        initialRootLeft + systemBars.left,
                        initialRootTop + systemBars.top,
                        initialRootRight + systemBars.right,
                        initialRootBottom + bottomInset
                );

                return windowInsets;
            });
        }

        ViewCompat.requestApplyInsets(rootView);
    }
}
