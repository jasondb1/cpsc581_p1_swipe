package com.example.cpsc581_p1_swipe;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.codetail.widget.RevealFrameLayout;
import io.uuddlrlrba.closepixelate.Pixelate;
import io.uuddlrlrba.closepixelate.PixelateLayer;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 250;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private static final String TAG = "SWIPE: ";                                // log debugging
    private String str;                                                         // for random image name
    private ImageView mImageView;                                               // single image on on screen
    private Button mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3,
            mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9,
            mDummyButton10;
    final Random random = new Random();                                         // used for randomness

    private String[] backText = {"TRY AGAIN", "NOPE", "SORRY", "WHO DIS"};
    private String[] backColour = {"#f2c25a", "#405ca3", "#fe8f2f", "#4c725e"};
    private String[] backTextColour = {"#775bd0", "#fade51", "#36aade", "#85231d"};
    private TextView mText;
    private RevealFrameLayout mBackground;

    private Bitmap currentBitmap = null;
    private Handler mImageHandler = new Handler();
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private boolean mVisible;

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "\nonCreate.\n");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        mVisible = true;

        // SYSTEM UI
        mContentView = findViewById(R.id.fullscreen_content);
        mBackground = findViewById(R.id.fullscreen_content_background);
        mText = findViewById(R.id.fullscreen_content);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // RANDOM BACKGROUND IMAGE
        mImageView = findViewById(R.id.imgRandom);


        // For permission to access phone and get random image from gallery //
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            setRandomImageFromGallery();
        }*/

        // For getting random image from resources //
        // IMPORTANT: Change according to number of images being used; name images as "img_#.jpg"
        //str = "img_" + random.nextInt(8);
        // From resource mipmap folder; instead of drawable due to size of photos
        //mImageView.setImageDrawable(getResources().getDrawable(getResourceID(str, "mipmap", getApplicationContext())));

        // Works to pixelate image from assets folder
        // https://github.com/bmaslakov/android-close-pixelate
        setRandomBackgroundImage(mImageView);

        // RANDOM BUTTON LOCATION
        mCorrectButton = findViewById(R.id.correct_button);
        mDummyButton1 = findViewById(R.id.dummy_button1);
        mDummyButton2 = findViewById(R.id.dummy_button2);
        mDummyButton3 = findViewById(R.id.dummy_button3);
        mDummyButton4 = findViewById(R.id.dummy_button4);
        mDummyButton5 = findViewById(R.id.dummy_button5);
        mDummyButton6 = findViewById(R.id.dummy_button6);
        mDummyButton7 = findViewById(R.id.dummy_button7);
        mDummyButton8 = findViewById(R.id.dummy_button8);
        mDummyButton9 = findViewById(R.id.dummy_button9);
        mDummyButton10 = findViewById(R.id.dummy_button10);

        randomButtonLocation(mCorrectButton);
        randomButtonLocation(mDummyButton1);
        randomButtonLocation(mDummyButton2);
        randomButtonLocation(mDummyButton3);
        randomButtonLocation(mDummyButton4);
        randomButtonLocation(mDummyButton5);
        randomButtonLocation(mDummyButton6);
        randomButtonLocation(mDummyButton7);
        randomButtonLocation(mDummyButton8);
        randomButtonLocation(mDummyButton9);
        randomButtonLocation(mDummyButton10);

        mCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText.setText("WELCOME");
                mText.setTextColor(getResources().getColor(android.R.color.black));
                mBackground.setBackgroundColor(getResources().getColor(android.R.color.white));

                final Animation anim_out = AnimationUtils.loadAnimation(FullscreenActivity.this, android.R.anim.fade_out);
                anim_out.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                      public void onAnimationStart(Animation animation) {}

                      @Override
                      public void onAnimationRepeat(Animation animation) {}

                      @Override
                      public void onAnimationEnd(Animation animation) {
                          moveTaskToBack(true);
                          android.os.Process.killProcess(android.os.Process.myPid());
                          System.exit(0);
                      }
                  });
                mCorrectButton.startAnimation(anim_out);
                mDummyButton1.startAnimation(anim_out);
                mDummyButton2.startAnimation(anim_out);
                mDummyButton3.startAnimation(anim_out);
                mDummyButton4.startAnimation(anim_out);
                mDummyButton5.startAnimation(anim_out);
                mDummyButton6.startAnimation(anim_out);
                mDummyButton7.startAnimation(anim_out);
                mDummyButton8.startAnimation(anim_out);
                mDummyButton9.startAnimation(anim_out);
                mDummyButton10.startAnimation(anim_out);
                mImageView.startAnimation(anim_out);
            }
        });

        mDummyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });

        mDummyButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFullscreenBack();
                imageViewAnimatedChange(FullscreenActivity.this, mImageView);
                setButtons(mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3, mDummyButton4, mDummyButton5, mDummyButton6, mDummyButton7, mDummyButton8, mDummyButton9, mDummyButton10);
            }
        });
    }


    // IMAGE STUFF //

    private void imageViewAnimatedChange(Context c, final ImageView v) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                setRandomBackgroundImage(v);

                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }


    private void setRandomBackgroundImage(ImageView mImageView) {
        str = "img_" + random.nextInt(9);
        Log.d(TAG, str);
        try {
            mImageView.setImageBitmap(Pixelate.fromAsset(
                    getAssets(), str+".jpg",
                    new PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                            .setResolution(24)
                            .setSize(36)
                            .build(),
                    new PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                            .setResolution(24)
                            .setOffset(12)
                            .build(),
                    new PixelateLayer.Builder(PixelateLayer.Shape.Square)
                            .setResolution(24)
                            .setAlpha(0.6f)
                            .build()));
        } catch (IOException e) {
            Log.d(TAG, "Pixelating image IOException: "+e);
        }

    }

    // BUTTON STUFF //

    private void setButtons(Button mCorrectButton, Button mDummyButton1, Button mDummyButton2, Button mDummyButton3, Button mDummyButton4, Button mDummyButton5, Button mDummyButton6, Button mDummyButton7, Button mDummyButton8, Button mDummyButton9, Button mDummyButton10) {
        buttonAnimatedChange(mCorrectButton);
        buttonAnimatedChange(mDummyButton1);
        buttonAnimatedChange(mDummyButton2);
        buttonAnimatedChange(mDummyButton3);
        buttonAnimatedChange(mDummyButton4);
        buttonAnimatedChange(mDummyButton5);
        buttonAnimatedChange(mDummyButton6);
        buttonAnimatedChange(mDummyButton7);
        buttonAnimatedChange(mDummyButton8);
        buttonAnimatedChange(mDummyButton9);
        buttonAnimatedChange(mDummyButton10);
    }

    private void randomButtonLocation(Button button) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        params.leftMargin = button.getWidth() + random.nextInt((metrics.widthPixels - 100));
        params.topMargin = button.getHeight() + random.nextInt((metrics.heightPixels - 300));
        button.setLayoutParams(params);
    }

    private void buttonAnimatedChange(final Button b) {
        final Animation anim_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                randomButtonLocation(b);

                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                b.startAnimation(anim_in);
            }
        });
        b.startAnimation(anim_out);
    }

    // FULL SCREEN STUFF //

    private void changeFullscreenBack() {
        int colourPosition = random.nextInt(4);
        int textPosition = random.nextInt(4);
        Log.d(TAG, "Color: "+colourPosition);
        Log.d(TAG, "Text: "+textPosition);
        mText.setText(backText[textPosition]);
        mText.setTextColor(Color.parseColor(backTextColour[colourPosition]));
        mBackground.setBackgroundColor(Color.parseColor(backColour[colourPosition]));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(50);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    //////////////////////////////////
    // EVERYTHING BELOW IS NOT USED //
    //////////////////////////////////


    // FOR RANDOM IMAGE FROM RESOURCE MIPMAP FOLDER

    protected final static int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID = ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);

        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        } else {
            return ResourceID;
        }
    }

    // FOR RANDOM IMAGE FROM PHONE GALLERY;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setRandomImageFromGallery();
            } else {
                // Permission Denied
                Toast.makeText(FullscreenActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void setRandomImageFromGallery() {
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA,
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = managedQuery(images, projection, "", null, "");

        final ArrayList<String> imagesPath = new ArrayList<String>();
        if (cur.moveToFirst()) {
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);

            do {
                imagesPath.add(cur.getString(dataColumn));
            } while (cur.moveToNext());
        }

        cur.close();

        final int count = imagesPath.size();

        mImageHandler.post(new Runnable() {
            @Override
            public void run() {
                int number = random.nextInt(count);
                String path = imagesPath.get(number);

                if (currentBitmap != null) {
                    currentBitmap.recycle();
                }

                currentBitmap = BitmapFactory.decodeFile(path);
                mImageView.setImageBitmap(currentBitmap);
            }
        });
    }

}
