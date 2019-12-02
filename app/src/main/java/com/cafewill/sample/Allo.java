package com.cafewill.sample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Allo
{
    public static final boolean DEBUG_ECHO = true;

    public static final int DEF_MONO = 1200;
    public static final int DEF_BACKPRESSED = 3000;

    //////////////////////////////////////////////////
    public static final boolean DEBUG_ADMOB_TEST_DEVICE = true;
    public static final String DEBUG_ADMOB_TEST_DEVICE_HASH = "https://developers.google.com/admob/android/test-ads";
    //////////////////////////////////////////////////
    public static final boolean DEBUG_FACEBOOK_TEST_DEVICE = true;
    public static final String DEBUG_FACEBOOK_TEST_DEVICE_HASH = "https://business.facebook.com/pub/testing";
    //////////////////////////////////////////////////

    public static final String ECHO = "cube";
    public static void i (final String message) { if (DEBUG_ECHO) Log.i (ECHO, message); }
    public static void t (final Context context, String message) { if (DEBUG_ECHO) Toast.makeText (context, message, Toast.LENGTH_SHORT).show (); }
}
