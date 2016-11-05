package com.nike.hacklikeagirl.ui;


import com.nike.hacklikeagirl.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class FullScreenThreadVideoActivity extends AppCompatActivity {

    private static final String ARGS_VIDEO_URL = "videoUrl";
    private static final String ARGS_LOOP_FLAG = "loopFlag";
    private final Handler mLeanBackHandler = new Handler();
    private final Runnable mEnterLeanback = new Runnable() {
        @Override
        public void run() {
            enableFullScreen(true);
        }
    };
    private String mVideoUrl;
    private int mLastSystemUIVisibility;

    /**
     * Navigates to this activity and starts playing the video requested
     *
     * @param activity used to build the intent and start the activity
     * @param videoUrl to be played
     * @param loopFlag will loop the video if set to true
     */
    public static void navigate(Activity activity, String videoUrl, boolean loopFlag) {
        Intent intent = new Intent(activity, FullScreenThreadVideoActivity.class);
        intent.putExtra(ARGS_VIDEO_URL, videoUrl);
        intent.putExtra(ARGS_LOOP_FLAG, loopFlag);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableFullScreen(true);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((mLastSystemUIVisibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0
                                && (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                            resetHideTimer();
                        }
                        mLastSystemUIVisibility = visibility;
                    }
                });
        setContentView(R.layout.thread_video_activity);
        findViewById(R.id.fl_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the `mainView` receives a click event then reset the leanback-mode clock
                resetHideTimer();
            }
        });

        Bundle b = getIntent() != null ? getIntent().getExtras() : null;
        if (b != null) {
            mVideoUrl = b.getString(ARGS_VIDEO_URL);
        }

            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, ThreadVideoFragment.newInstance(mVideoUrl))
                    .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableFullScreen(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            enableFullScreen(true);
        }
    }

    private void enableFullScreen(boolean enabled) {
        int newVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if (enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        // Set the visibility
        getWindow().getDecorView().setSystemUiVisibility(newVisibility);
    }

    private void resetHideTimer() {
        // First cancel any queued events - i.e. resetting the countdown clock
        mLeanBackHandler.removeCallbacks(mEnterLeanback);
        // And fire the event in 3s time
        mLeanBackHandler.postDelayed(mEnterLeanback, 2000);
    }
}
