package com.cafewill.ads;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;

import java.util.ArrayList;
import java.util.List;

public class Rotator
{
    public final static String ADMOB = "admob";
    public final static String GOOGLE = "google";
    public final static String FACEBOOK = "facebook";

    private Listener listener;

    public void setListener (Listener listener) { this.listener = listener; }
    public interface Listener { public void onLoaded (); public void onFailed (); public void onClosed (); }

    private String echo = "cube";

    private boolean admobTestStatus = false;
    private String admobTestDeviceHash = "";

    private boolean facebookTestStatus = false;
    private String facebookTestDeviceHash = "";

    private void Log (String message) { Log.i (echo, message); }

    public void setEcho (String echo) { this.echo = echo; }

    public boolean isAdmobTestStatus () { return admobTestStatus; }
    public void setAdmobTestStatus (boolean admobTestStatus) { this.admobTestStatus = admobTestStatus; }
    public String getAdmobTestDeviceHash () { return admobTestDeviceHash; }
    public void setAdmobTestDeviceHash (String admobTestDeviceHash) { this.admobTestDeviceHash = admobTestDeviceHash; }

    public boolean isFacebookTestStatus () { return facebookTestStatus; }
    public void setFacebookTestStatus (boolean facebookTestStatus) { this.facebookTestStatus = facebookTestStatus; }
    public String getFacebookTestDeviceHash () { return facebookTestDeviceHash; }
    public void setFacebookTestDeviceHash (String facebookTestDeviceHash) { this.facebookTestDeviceHash = facebookTestDeviceHash; }

    public View getBanner (final Context context, final String type, final String adid, final String unit)
    {
        Log ("getBanner () @" + getClass ());

        View banner = null;

        try
        {
            if (ADMOB.equals (type)) banner = getGoogleBanner (context, adid, unit);
            if (GOOGLE.equals (type)) banner = getGoogleBanner (context, adid, unit);
            if (FACEBOOK.equals (type)) banner = getFacebookBanner (context, adid, unit);
        } catch (Exception e) { e.printStackTrace (); }

        return (banner);
    }

    private com.google.android.gms.ads.AdView getGoogleBanner (final Context context, final String adid, final String unit)
    {
        Log ("getGoogleBanner () @" + getClass ());

        com.google.android.gms.ads.AdView banner = new com.google.android.gms.ads.AdView (context);

        try
        {
            com.google.android.gms.ads.MobileAds.initialize (context, adid);
            banner.setAdSize (com.google.android.gms.ads.AdSize.BANNER);
            banner.setAdUnitId (unit);
            banner.setAdListener (new com.google.android.gms.ads.AdListener ()
            {
                @Override
                public void onAdLoaded ()
                {
                    Log ("onAdLoaded () @" + getClass ());

                    if (null != listener) listener.onLoaded ();
                }
                @Override
                public void onAdOpened ()
                {
                    Log ("onAdOpened () @" + getClass ());
                }
                @Override
                public void onAdImpression ()
                {
                    Log ("onAdImpression () @" + getClass ());
                }
                @Override
                public void onAdLeftApplication ()
                {
                    Log ("onAdLeftApplication () @" + getClass ());
                }
                @Override
                public void onAdClosed ()
                {
                    Log ("onAdClosed () @" + getClass ());
                }
                @Override
                public void onAdFailedToLoad (int errorCode)
                {
                    Log ("onAdFailedToLoad () [code : " + errorCode + "] @" + getClass ());
                    if (null != listener) listener.onFailed ();
                }
            });
            if (isAdmobTestStatus ())
            {
                banner.loadAd (new com.google.android.gms.ads.AdRequest.Builder ().addTestDevice (getAdmobTestDeviceHash ()).build ());
            }
            else
            {
                banner.loadAd (new com.google.android.gms.ads.AdRequest.Builder ().build ());
            }
        } catch (Exception e) { e.printStackTrace (); }

        return (banner);
    }

    private com.facebook.ads.AdView getFacebookBanner (final Context context, final String adid, final String unit)
    {
        Log ("getFacebookBanner () @" + getClass ());

        com.facebook.ads.AdView banner = new com.facebook.ads.AdView (context, adid, com.facebook.ads.AdSize.BANNER_HEIGHT_50);

        try
        {
            if (isFacebookTestStatus ())
            {
                List<String> testDevices = new ArrayList<>();
                testDevices.add (getFacebookTestDeviceHash ());
                com.facebook.ads.AdSettings.addTestDevices (testDevices);
            }

            banner.setAdListener (new com.facebook.ads.AdListener ()
            {
                @Override
                public void onAdLoaded (Ad ad)
                {
                    Log ("onAdLoaded @" + this.getClass ());
                    if (null != listener) listener.onLoaded ();
                }
                @Override
                public void onAdClicked (Ad ad)
                {
                    Log ("onAdClicked @" + this.getClass ());
                }
                @Override
                public void onLoggingImpression (Ad ad)
                {
                    Log ("onLoggingImpression @" + this.getClass ());
                }
                @Override
                public void onError (Ad ad, AdError adError)
                {
                    Log ("onError [" + adError.getErrorCode() + "][" + adError.getErrorMessage () + "] @" + this.getClass ());
                    if (null != listener) listener.onFailed ();
                }
            });
            banner.loadAd ();
        } catch (Exception e) { e.printStackTrace (); }

        return (banner);
    }

    public static void showInterstitial (Object interstitial)
    {
        if (null != interstitial)
        {
            if (com.facebook.ads.InterstitialAd.class.isInstance (interstitial)) ((com.facebook.ads.InterstitialAd) interstitial).show ();
            if (com.google.android.gms.ads.InterstitialAd.class.isInstance (interstitial)) ((com.google.android.gms.ads.InterstitialAd) interstitial).show ();
        }
    }

    public Object getInterstitial (final Context context, final String type, final String adid, final String unit)
    {
        Log ("getInterstitial () @" + getClass ());

        Object interstitial = null;

        try
        {
            if (ADMOB.equals (type)) interstitial = getGoogleInterstitial (context, adid, unit);
            if (GOOGLE.equals (type)) interstitial = getGoogleInterstitial (context, adid, unit);
            if (FACEBOOK.equals (type)) interstitial = getFacebookInterstitial (context, adid, unit);
        } catch (Exception e) { e.printStackTrace (); }

        return (interstitial);
    }

    private com.google.android.gms.ads.InterstitialAd getGoogleInterstitial (final Context context, final String adid, final String unit)
    {
        Log ("getGoogleInterstitial () @" + getClass ());

        com.google.android.gms.ads.InterstitialAd interstitial = new com.google.android.gms.ads.InterstitialAd (context);

        try
        {
            com.google.android.gms.ads.MobileAds.initialize (context, adid);
            interstitial.setAdUnitId (unit);
            interstitial.setAdListener (new com.google.android.gms.ads.AdListener ()
            {
                @Override
                public void onAdLoaded ()
                {
                    Log ("onAdLoaded () @" + getClass ());

                    if (null != listener) listener.onLoaded ();
                }
                @Override
                public void onAdOpened ()
                {
                    Log ("onAdOpened () @" + getClass ());
                }
                @Override
                public void onAdImpression ()
                {
                    Log ("onAdImpression () @" + getClass ());
                }
                @Override
                public void onAdLeftApplication ()
                {
                    Log ("onAdLeftApplication () @" + getClass ());
                }
                @Override
                public void onAdClosed ()
                {
                    Log ("onAdClosed () @" + getClass ());

                    if (null != listener) listener.onClosed ();
                }
                @Override
                public void onAdFailedToLoad (int errorCode)
                {
                    Log ("onAdFailedToLoad () [code : " + errorCode + "] @" + getClass ());
                    if (null != listener) listener.onFailed ();
                }
            });
            if (isAdmobTestStatus ())
            {
                interstitial.loadAd (new com.google.android.gms.ads.AdRequest.Builder ().addTestDevice (getAdmobTestDeviceHash ()).build ());
            }
            else
            {
                interstitial.loadAd (new com.google.android.gms.ads.AdRequest.Builder ().build ());
            }
        } catch (Exception e) { e.printStackTrace (); }

        return (interstitial);
    }

    private com.facebook.ads.InterstitialAd getFacebookInterstitial (final Context context, final String adid, final String unit)
    {
        Log ("getFacebookInterstitial () @" + getClass ());

        com.facebook.ads.InterstitialAd interstitial = new com.facebook.ads.InterstitialAd (context, adid);

        try
        {
            if (isFacebookTestStatus ())
            {
                List<String> testDevices = new ArrayList<>();
                testDevices.add (getFacebookTestDeviceHash ());
                com.facebook.ads.AdSettings.addTestDevices (testDevices);
            }

            interstitial.setAdListener (new InterstitialAdListener ()
            {
                @Override
                public void onAdLoaded (Ad ad)
                {
                    Log ("onAdLoaded @" + this.getClass ());
                    if (null != listener) listener.onLoaded ();
                }
                @Override
                public void onAdClicked (Ad ad)
                {
                    Log ("onAdClicked @" + this.getClass ());
                }
                @Override
                public void onLoggingImpression (Ad ad)
                {
                    Log ("onLoggingImpression @" + this.getClass ());
                }
                @Override
                public void onInterstitialDisplayed (Ad ad)
                {
                    Log ("onInterstitialDisplayed @" + this.getClass ());
                }
                @Override
                public void onInterstitialDismissed (Ad ad)
                {
                    Log ("onInterstitialDismissed @" + this.getClass ());

                    if (null != listener) listener.onClosed ();
                }
                @Override
                public void onError (Ad ad, AdError adError)
                {
                    Log ("onError [" + adError.getErrorCode() + "][" + adError.getErrorMessage() + "] @" + this.getClass ());
                    if (null != listener) listener.onFailed ();
                }
            });
            interstitial.loadAd ();
        } catch (Exception e) { e.printStackTrace (); }

        return (interstitial);
    }
}
