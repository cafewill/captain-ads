package com.cafewill.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cafewill.ads.Rotator;
import com.cafewill.sample.common.Const;

public class MainActivity extends AppCompatActivity
{
    private View contentBorder;
    private LinearLayout bannerLayout;
    private LinearLayout contentLayout;
    private RelativeLayout outlineLayout;

    private View banner;
    private Object interstitial;
    private int bannerMargin = 10;
    private int bannerDefaultHeight = 100;
    private int bannerAnimationDelay = 250;
    private boolean bannerStatus = false;
    private boolean interstitialStatus = false;

    private long backPressedInterval = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Allo.i ("onCreate () @" + getClass ());

        try
        {
            requestWindowFeature (Window.FEATURE_NO_TITLE);
            requestWindowFeature (Window.FEATURE_ACTION_BAR_OVERLAY);
            getWindow ().requestFeature (Window.FEATURE_NO_TITLE);
            getWindow ().requestFeature (Window.FEATURE_ACTION_BAR_OVERLAY);
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView (R.layout.activity_main);

            outlineLayout = (RelativeLayout) findViewById (R.id.layout_outline);

            bannerLayout = new LinearLayout (this);
            bannerLayout.setVisibility (View.INVISIBLE);
            bannerLayout.setBackgroundColor (ContextCompat.getColor (getApplicationContext (), R.color.banner_background));
            bannerLayout.setOrientation (LinearLayout.VERTICAL);
            bannerLayout.setPadding (0, bannerMargin, 0, bannerMargin);
            RelativeLayout.LayoutParams bannerParams = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            bannerParams.addRule (RelativeLayout.ALIGN_PARENT_BOTTOM);
            outlineLayout.addView (bannerLayout, bannerParams);
        } catch (Exception e) { e.printStackTrace (); }
    }

    @Override
    protected void onResume ()
    {
        super.onResume ();

        Allo.i ("onResume () @" + getClass ());

        try
        {
            if (bannerStatus && null != banner) showBanner ();
            if (!bannerStatus) rotateBanner (getApplicationContext ());
        } catch (Exception e) { e.printStackTrace (); }
    }

    @Override
    public void onPause ()
    {
        super.onPause ();

        Allo.i ("onPause () @" + getClass ());
    }

    @Override
    public void onStop ()
    {
        super.onStop ();

        Allo.i ("onStop () @" + this.getClass ());

        try
        {
            hideBanner ();
        } catch (Exception e) { e.printStackTrace (); }
    }

    @Override
    public void onDestroy ()
    {
        super.onDestroy ();

        Allo.i ("onDestroy () @" + getClass ());
    }

    @Override
    public void onBackPressed ()
    {
        Allo.i ("onBackPressed () @" + getClass ());

        try
        {
            if (System.currentTimeMillis () > backPressedInterval)
            {
                backPressedInterval = System.currentTimeMillis() + Allo.DEF_BACKPRESSED;
                Toast.makeText (this, R.string.message_quitback, Toast.LENGTH_SHORT).show ();
            }
            else if (System.currentTimeMillis() <= backPressedInterval)
            {
                super.onBackPressed ();
            }
        } catch (Exception e) { e.printStackTrace (); }
    }

    ///////////////////////
    // @CUSTOMIZE
    ///////////////////////

    public void actionRotateBanner (View view)
    {
        Allo.i ("actionRotateBanner () @" + getClass ());

        try
        {
            rotateBanner (getApplicationContext ());
        } catch (Exception e) { e.printStackTrace (); }
    }

    public void actionRotateInterstitial (View view)
    {
        Allo.i ("actionRotateInterstitial () @" + getClass ());

        try
        {
            rotateInterstitial (getApplicationContext ());
        } catch (Exception e) { e.printStackTrace (); }
    }

    public void actionShowInterstitial (View view)
    {
        Allo.i ("actionShowInterstitial () @" + getClass ());

        try
        {
            showInterstitial ();
        } catch (Exception e) { e.printStackTrace (); }
    }

    public void actionSettings (View view)
    {
        Allo.i ("actionSettings () @" + getClass ());

        try
        {
            Intent intent = new Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory (Intent.CATEGORY_DEFAULT);
            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags (Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags (Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.setData (Uri.parse (Const.PACKAGE + getApplicationContext ().getPackageName ()));
            startActivity (intent);
        } catch (Exception e) { e.printStackTrace (); }
    }

    private void rotateBanner (final Context context)
    {
        Allo.i ("rotateBanner () @" + getClass ());

        try
        {
            hideBanner ();
            bannerStatus = false;
            if (null != banner) bannerLayout.removeView (banner);

            Rotator rotator = new Rotator ();
            rotator.setAdmobTestStatus (Allo.DEBUG_ADMOB_TEST_DEVICE);
            rotator.setAdmobTestDeviceHash (Allo.DEBUG_ADMOB_TEST_DEVICE_HASH);
            rotator.setFacebookTestStatus (Allo.DEBUG_FACEBOOK_TEST_DEVICE);
            rotator.setFacebookTestDeviceHash (Allo.DEBUG_FACEBOOK_TEST_DEVICE_HASH);
            rotator.setListener (new Rotator.Listener ()
            {
                @Override
                public void onLoaded ()
                {
                    Allo.i ("onLoaded () @" + getClass ());

                    if (null == banner.getParent ()) bannerLayout.addView (banner); bannerStatus = true; showBanner ();
                }
                @Override
                public void onFailed ()
                {
                    Allo.i ("onFailed () @" + getClass ());

                    if (null != banner) bannerLayout.removeView (banner); bannerStatus = false; hideBanner ();
                }
                @Override
                public void onClosed ()
                {
                    Allo.i ("onClosed () @" + getClass ());
                }
            });

            /////////////

            String frontAdid = "";
            String frontUnit = "";
            String frontExtra = "";

            boolean frontStatus = true; String frontType = ""; String frontCode = "";
            String frontList = context.getString (R.string.list_of_banner);

            // 20191026 완벽한 체크 루틴 보단 대충 철저한 루틴으로 처리함
            frontExtra = frontList;
            if (frontList.contains (","))
            {
                String [] cases = frontList.split (",");
                int mix = (int) Math.floor (Math.random () * cases.length);
                frontExtra = cases [mix].trim ();
            }

            if (frontExtra.contains (":"))
            {
                String [] extras = frontExtra.split (":", 2);
                frontType = extras [0].trim (); frontCode = extras [1].trim ();
            }
            if ("".equals (frontType) || "".equals (frontCode)) { frontStatus = false; }
            try
            {
                // 페북 adid, 애드몹 unit 만 입력하는 경우를 고려함
                frontAdid = frontCode; frontUnit = frontCode;
                String [] codes = frontCode.split ("\\+");
                frontAdid = codes [0].trim (); frontUnit = codes [1].trim ();
            } catch (Exception e) { e.printStackTrace (); }

            /////////////

            Allo.i ("banner front status : [" + frontStatus + "][" + frontAdid + "][" + frontUnit + "][" + frontExtra + "][" + frontList + "]");

            if (frontStatus) { banner = rotator.getBanner (context, frontType, frontAdid, frontUnit); } else { hideBanner (); }
        } catch (Exception e) { e.printStackTrace (); }
    }

    private void showBanner ()
    {
        Allo.i ("showBanner () @" + getClass ());

        try
        {
            int bottomMargin = 0;
            int bannerHeight = banner.getHeight ();
            if (1 > bannerHeight) bannerHeight = bannerDefaultHeight;
            if (0 < bannerHeight) bottomMargin = bannerHeight + (bannerMargin * 2);

            contentBorder = (View) findViewById (R.id.layout_content_border);
            contentLayout = (LinearLayout) findViewById (R.id.layout_content);
            RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            contentParams.setMargins (0, 0, 0, bottomMargin);
            contentLayout.setLayoutParams (contentParams);
            contentBorder.setVisibility (View.VISIBLE);

            banner.setY (banner.getHeight () + (bannerDefaultHeight * 2));
            bannerLayout.setVisibility (View.VISIBLE);
            banner.animate ().translationY (banner.getHeight () + (bannerDefaultHeight * 2));
            (new Handler ()).postDelayed (new Runnable ()
            {
                @Override
                public void run ()
                {
                    banner.animate ()
                            .alpha (1.0f)
                            .translationY (0)
                            .setDuration (520)
                            .setListener(new AnimatorListenerAdapter ()
                            {
                                @Override
                                public void onAnimationEnd (Animator animation)
                                {
                                    super.onAnimationEnd (animation);
                                }
                            });
                }
            }, bannerAnimationDelay);
        } catch (Exception e) { e.printStackTrace (); }
    }

    private void hideBanner ()
    {
        Allo.i ("hideBanner () @" + getClass ());

        try
        {
            int bottomMargin = 0;
            contentBorder = (View) findViewById (R.id.layout_content_border);
            contentLayout = (LinearLayout) findViewById (R.id.layout_content);
            RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            contentParams.setMargins (0, 0, 0, bottomMargin);
            contentLayout.setLayoutParams (contentParams);

            bannerLayout.setVisibility (View.INVISIBLE);
            contentBorder.setVisibility (View.INVISIBLE);
            // if (null != banner) banner.setVisibility (View.GONE);
        } catch (Exception e) { e.printStackTrace (); }
    }

    private void rotateInterstitial (final Context context)
    {
        Allo.i ("rotateInterstitial () @" + getClass ());

        try
        {
            interstitialStatus = false;
            if (null != interstitial) interstitial = null;

            Rotator rotator = new Rotator ();
            rotator.setAdmobTestStatus (Allo.DEBUG_ADMOB_TEST_DEVICE);
            rotator.setAdmobTestDeviceHash (Allo.DEBUG_ADMOB_TEST_DEVICE_HASH);
            rotator.setFacebookTestStatus (Allo.DEBUG_FACEBOOK_TEST_DEVICE);
            rotator.setFacebookTestDeviceHash (Allo.DEBUG_FACEBOOK_TEST_DEVICE_HASH);
            rotator.setListener (new Rotator.Listener ()
            {
                @Override
                public void onLoaded ()
                {
                    Allo.i ("onLoaded () @" + getClass ());

                    interstitialStatus = true;
                    Toast.makeText (getApplicationContext (), R.string.message_interstitial_available, Toast.LENGTH_SHORT).show ();
                }
                @Override
                public void onFailed ()
                {
                    Allo.i ("onFailed () @" + getClass ());

                    interstitialStatus = false;
                }
                @Override
                public void onClosed ()
                {
                    Allo.i ("onClosed () @" + getClass ());

                    interstitial = null; interstitialStatus = false;
                }
            });

            /////////////

            String frontAdid = "";
            String frontUnit = "";
            String frontExtra = "";

            boolean frontStatus = true; String frontType = ""; String frontCode = "";
            String frontList = context.getString (R.string.list_of_interstitial);

            // 20191026 완벽한 체크 루틴 보단 대충 철저한 루틴으로 처리함
            frontExtra = frontList;
            if (frontList.contains (","))
            {
                String [] cases = frontList.split (",");
                int mix = (int) Math.floor (Math.random () * cases.length);
                frontExtra = cases [mix].trim ();
            }

            if (frontExtra.contains (":"))
            {
                String [] extras = frontExtra.split (":", 2);
                frontType = extras [0].trim (); frontCode = extras [1].trim ();
            }
            if ("".equals (frontType) || "".equals (frontCode)) { frontStatus = false; }
            try
            {
                // 페북 adid, 애드몹 unit 만 입력하는 경우를 고려함
                frontAdid = frontCode; frontUnit = frontCode;
                String [] codes = frontCode.split ("\\+");
                frontAdid = codes [0].trim (); frontUnit = codes [1].trim ();
            } catch (Exception e) { e.printStackTrace (); }

            /////////////

            Allo.i ("interstitial front status : [" + frontStatus + "][" + frontAdid + "][" + frontUnit + "][" + frontExtra + "][" + frontList + "]");

            if (frontStatus) { interstitial = rotator.getInterstitial (context, frontType, frontAdid, frontUnit); }
        } catch (Exception e) { e.printStackTrace (); }
    }

    private void showInterstitial ()
    {
        Allo.i ("showInterstitial () @" + getClass ());

        try
        {
            if (interstitialStatus && null != interstitial) Rotator.showInterstitial (interstitial);
            if (!interstitialStatus) Toast.makeText (this, R.string.message_interstitial_unavailable, Toast.LENGTH_SHORT).show ();
        } catch (Exception e) { e.printStackTrace (); }
    }
}
