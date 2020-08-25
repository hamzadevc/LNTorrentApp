package org.lntorrent.libretorrent.ads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.lntorrent.libretorrent.R;

import java.net.MalformedURLException;
import java.net.URL;

public class ConsentSDK {
    private static String DUMMY_BANNER = "ca-app-pub-3940256099942544/6300978111";
    private static boolean NON_PERSONALIZED = false;
    private static boolean PERSONALIZED = true;
    private static String ads_preference = "ads_preference";
    private static String preferences_name = "com.ayoubfletcher.consentsdk";
    private static String user_status = "user_status";
    public boolean DEBUG = false;
    public String DEVICE_ID = "";
    public String LOG_TAG = "ID_LOG";
    public ConsentSDK consentSDK;
    private Context context;
    public ConsentForm form;
    private String privacyURL;
    private String publisherId;
    private SharedPreferences settings;

    public static abstract class ConsentCallback {
        public abstract void onResult(boolean z);
    }

    public static abstract class ConsentInformationCallback {
        public abstract void onFailed(ConsentInformation consentInformation, String str);

        public abstract void onResult(ConsentInformation consentInformation, ConsentStatus consentStatus);
    }

    public static abstract class ConsentStatusCallback {
        public abstract void onResult(boolean z, int i);
    }

    public static abstract class LocationIsEeaOrUnknownCallback {
        public abstract void onResult(boolean z);
    }

    public static class Builder {
        private boolean DEBUG = false;
        private String DEVICE_ID = "";
        private String LOG_TAG = "ID_LOG";
        private Context context;
        private String privacyURL;
        private String publisherId;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder addTestDeviceId(String str) {
            this.DEVICE_ID = str;
            this.DEBUG = true;
            return this;
        }

        public Builder addPrivacyPolicy(String str) {
            this.privacyURL = str;
            return this;
        }

        public Builder addPublisherId(String str) {
            this.publisherId = str;
            return this;
        }

        public Builder addCustomLogTag(String str) {
            this.LOG_TAG = str;
            return this;
        }

        public ConsentSDK build() {
            if (!this.DEBUG) {
                return new ConsentSDK(this.context, this.publisherId, this.privacyURL);
            }
            ConsentSDK consentSDK = new ConsentSDK(this.context, this.publisherId, this.privacyURL, true);
            String unused = consentSDK.LOG_TAG = this.LOG_TAG;
            String unused2 = consentSDK.DEVICE_ID = this.DEVICE_ID;
            return consentSDK;
        }
    }

    public static void initDummyBanner(Context context2) {
        AdView adView = new AdView(context2);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(DUMMY_BANNER);
        adView.loadAd(new AdRequest.Builder().build());
    }

    public ConsentSDK(Context context2, String str, String str2, boolean z) {
        this.context = context2;
        this.settings = initPreferences(context2);
        this.publisherId = str;
        this.privacyURL = str2;
        this.DEBUG = z;
        this.consentSDK = this;
    }

    private static SharedPreferences initPreferences(Context context2) {
        return context2.getSharedPreferences(preferences_name, 0);
    }

    public ConsentSDK(Context context2, String str, String str2) {
        this.context = context2;
        this.settings = context2.getSharedPreferences(preferences_name, 0);
        this.publisherId = str;
        this.privacyURL = str2;
        this.consentSDK = this;
    }

    public static boolean isConsentPersonalized(Context context2) {
        return initPreferences(context2).getBoolean(ads_preference, PERSONALIZED);
    }

    public void consentIsPersonalized() {
        this.settings.edit().putBoolean(ads_preference, PERSONALIZED).apply();
    }

    public void consentIsNonPersonalized() {
        this.settings.edit().putBoolean(ads_preference, NON_PERSONALIZED).apply();
    }

    public void updateUserStatus(boolean z) {
        this.settings.edit().putBoolean(user_status, z).apply();
    }

    public static AdRequest getAdRequest(Context context) {
        if (isConsentPersonalized(context)) {
            return new AdRequest.Builder().build();
        }
        return new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build();
    }

    private static Bundle getNonPersonalizedAdsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("npa", "1");
        return bundle;
    }

    private void initConsentInformation(final ConsentInformationCallback consentInformationCallback) {
        final ConsentInformation instance = ConsentInformation.getInstance(this.context);
        if (this.DEBUG) {
            if (!this.DEVICE_ID.isEmpty()) {
                instance.addTestDevice(this.DEVICE_ID);
            }
            instance.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        }
        instance.requestConsentInfoUpdate(new String[]{this.publisherId}, new ConsentInfoUpdateListener() {
            /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.AnonymousClass1 */

            @Override // com.google.ads.consent.ConsentInfoUpdateListener
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
               // ConsentInformationCallback consentInformationCallback = consentInformationCallback;
                if (consentInformationCallback != null) {
                    consentInformationCallback.onResult(instance, consentStatus);
                }
            }

            @Override // com.google.ads.consent.ConsentInfoUpdateListener
            public void onFailedToUpdateConsentInfo(String str) {
                consentInformationCallback.onFailed(instance, str);
            }
        });
    }

    public void isRequestLocationIsEeaOrUnknown(final LocationIsEeaOrUnknownCallback locationIsEeaOrUnknownCallback) {
        initConsentInformation(new ConsentInformationCallback() {

            @Override

            public void onResult(ConsentInformation consentInformation, ConsentStatus consentStatus) {
                locationIsEeaOrUnknownCallback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
            }

            @Override

            public void onFailed(ConsentInformation consentInformation, String str) {
                locationIsEeaOrUnknownCallback.onResult(false);
            }
        });
    }

    public static boolean isUserLocationWithinEea(Context context2) {
        return initPreferences(context2).getBoolean(user_status, false);
    }

    public void checkConsent(final ConsentCallback consentCallback) {
        initConsentInformation(new ConsentInformationCallback() {


            @Override

            public void onResult(ConsentInformation consentInformation, ConsentStatus consentStatus) {
                switch (AnonymousClass6.$SwitchMap$com$google$ads$consent$ConsentStatus[consentStatus.ordinal()]) {
                    case 1:
                        if (ConsentSDK.this.DEBUG) {
                            Log.d(ConsentSDK.this.LOG_TAG, "Unknown Consent");
                            String access$000 = ConsentSDK.this.LOG_TAG;
                            Log.d(access$000, "User location within EEA: " + consentInformation.isRequestLocationInEeaOrUnknown());
                        }
                        if (!consentInformation.isRequestLocationInEeaOrUnknown()) {
                            ConsentSDK.this.consentIsPersonalized();
                            consentCallback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
                            break;
                        } else {
                            ConsentSDK.this.requestConsent(new ConsentStatusCallback() {
                                /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.AnonymousClass3.AnonymousClass1 */

                                @Override
                                // LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.ConsentStatusCallback
                                public void onResult(boolean z, int i) {
                                    consentCallback.onResult(z);
                                }
                            });
                            break;
                        }
                    case 2:
                        ConsentSDK.this.consentIsNonPersonalized();
                        consentCallback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
                        break;
                    default:
                        ConsentSDK.this.consentIsPersonalized();
                        consentCallback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
                        break;
                }
                ConsentSDK.this.updateUserStatus(consentInformation.isRequestLocationInEeaOrUnknown());
            }

            @Override

            public void onFailed(ConsentInformation consentInformation, String str) {
                if (ConsentSDK.this.DEBUG) {
                    Log.d(ConsentSDK.this.LOG_TAG, "Failed to update: $reason");
                }
                ConsentSDK.this.updateUserStatus(consentInformation.isRequestLocationInEeaOrUnknown());
                consentCallback.onResult(consentInformation.isRequestLocationInEeaOrUnknown());
            }
        });
    }

    /* renamed from: LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK$6  reason: invalid class name */
    static class AnonymousClass6 {
        static final int[] $SwitchMap$com$google$ads$consent$ConsentStatus = new int[ConsentStatus.values().length];

        static {
            $SwitchMap$com$google$ads$consent$ConsentStatus[ConsentStatus.UNKNOWN.ordinal()] = 1;
            $SwitchMap$com$google$ads$consent$ConsentStatus[ConsentStatus.NON_PERSONALIZED.ordinal()] = 2;
        }
    }

    public void requestConsent(final ConsentStatusCallback consentStatusCallback) {
        URL url;
        try {
            url = new URL(this.privacyURL);
        } catch (MalformedURLException unused) {
            url = null;
        }
        this.form = new ConsentForm.Builder(this.context, url).withListener(new ConsentFormListener() {
            /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.AnonymousClass4 */

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormLoaded() {
                if (ConsentSDK.this.DEBUG) {
                    Log.d(ConsentSDK.this.LOG_TAG, "Consent Form is loaded!");
                }
                ConsentSDK.this.form.show();
            }

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormError(String str) {
                if (ConsentSDK.this.DEBUG) {
                    Log.d(ConsentSDK.this.LOG_TAG, "Consent Form ERROR: $reason");
                }
                if (consentStatusCallback != null) {
                    ConsentSDK.this.consentSDK.isRequestLocationIsEeaOrUnknown(new LocationIsEeaOrUnknownCallback() {
                        /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.AnonymousClass4.AnonymousClass1 */

                        @Override
                        // LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.LocationIsEeaOrUnknownCallback
                        public void onResult(boolean z) {
                            consentStatusCallback.onResult(z, -1);
                        }
                    });
                }
            }

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormOpened() {
                if (ConsentSDK.this.DEBUG) {
                    Log.d(ConsentSDK.this.LOG_TAG, "Consent Form is opened!");
                }
            }

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormClosed(ConsentStatus consentStatus, Boolean bool) {
                final int i;
                if (ConsentSDK.this.DEBUG) {
                    Log.d(ConsentSDK.this.LOG_TAG, "Consent Form Closed!");
                }
                if (AnonymousClass6.$SwitchMap$com$google$ads$consent$ConsentStatus[consentStatus.ordinal()] != 2) {
                    ConsentSDK.this.consentIsPersonalized();
                    i = 1;
                } else {
                    ConsentSDK.this.consentIsNonPersonalized();
                    i = 0;
                }
                if (consentStatusCallback != null) {
                    ConsentSDK.this.consentSDK.isRequestLocationIsEeaOrUnknown(new LocationIsEeaOrUnknownCallback() {
                        /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.AnonymousClass4.AnonymousClass2 */

                        @Override
                        // LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.LocationIsEeaOrUnknownCallback
                        public void onResult(boolean z) {
                            consentStatusCallback.onResult(z, i);
                        }
                    });
                }
            }
        }).withPersonalizedAdsOption().withNonPersonalizedAdsOption().build();
        this.form.load();
    }

    public static InterstitialAd loadInterstitial(Activity activity) {
        final InterstitialAd interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdUnitId(activity.getString(R.string.ad_interstitial));
        interstitialAd.loadAd(getAdRequest(activity));
        interstitialAd.setAdListener(new AdListener() {
            /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.ConsentSDK.AnonymousClass5 */

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                interstitialAd.show();
                super.onAdLoaded();
            }
        });
        return interstitialAd;
    }
}
