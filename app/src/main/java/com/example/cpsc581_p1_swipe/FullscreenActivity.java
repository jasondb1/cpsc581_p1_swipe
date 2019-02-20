package com.example.cpsc581_p1_swipe;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

    private static final String TAG = "SWIPE: "; // used for debugging
    private ImageView mImageView; // single image on on screen
    private Button mCorrectButton, mDummyButton1, mDummyButton2, mDummyButton3;
    final Random random = new Random(); // used for randomness

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

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // RANDOM BACKGROUND IMAGE
        mImageView = findViewById(R.id.imgRandom);


        // For permission to access phone and get random image from gallery
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            setRandomImageFromGallery();
        }*/


        // For getting random image from resources
        // IMPORTANT: Change according to number of images being used; name images as "img_#.jpg"
        final String str = "img_" + random.nextInt(8);

        Log.d(TAG, str);
        // From resource mipmap folder; instead of drawable due to size of photos
        //mImageView.setImageDrawable(getResources().getDrawable(getResourceID(str, "mipmap", getApplicationContext())));

        // Works to pixelate image from assets folder
        // https://github.com/bmaslakov/android-close-pixelate
        try {
            mImageView.setImageBitmap(Pixelate.fromAsset(
                    getAssets(), str+".jpg",
                    new PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                            .setResolution(48)
                            .setSize(50)
                            .build(),
                    new PixelateLayer.Builder(PixelateLayer.Shape.Diamond)
                            .setResolution(48)
                            .setOffset(24)
                            .build(),
                    new PixelateLayer.Builder(PixelateLayer.Shape.Circle)
                            .setResolution(8)
                            .setSize(6)
                            .build()));
        } catch (IOException e) {
            Log.d(TAG, "Pixelating image IOException: "+e);
        }


        // RANDOM BUTTON LOCATION
        mCorrectButton = findViewById(R.id.correct_button);
        mDummyButton1 = findViewById(R.id.dummy_button1);
        mDummyButton2 = findViewById(R.id.dummy_button2);
        mDummyButton3 = findViewById(R.id.dummy_button3);

        randomButtonLocation(mCorrectButton);
        randomButtonLocation(mDummyButton1);
        randomButtonLocation(mDummyButton2);
        randomButtonLocation(mDummyButton3);

        mCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        mDummyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        mDummyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        mDummyButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        /*Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(restartIntent);*/

    }

    private void randomButtonLocation(Button button) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)button.getLayoutParams();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        params.leftMargin = button.getWidth()
                + new Random().nextInt(metrics.widthPixels - 2*button.getWidth());
        params.topMargin = button.getHeight()
                + new Random().nextInt(metrics.heightPixels - 3*button.getHeight());
        button.setLayoutParams(params);

        button.setLayoutParams(params);
    }


    /**
     * Get random image from resources folder
     *
     * @param resName
     * @param resType
     * @param ctx
     * @return
     */
    protected final static int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID = ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);

        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        } else {
            return ResourceID;
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
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


    // FOR RANDOM IMAGE FROM PHONE GALLERY;

    /**
     * For getting permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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


    /**
     * Get random image from phones gallery
     */
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
