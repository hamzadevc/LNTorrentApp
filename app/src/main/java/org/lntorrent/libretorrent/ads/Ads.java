package org.lntorrent.libretorrent.ads;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.lntorrent.libretorrent.R;

public class Ads {
    private static Ads INSTANCE;
    public static AdRequest adRequest;
    public static InterstitialAd interstitialAd;
    String TAG = "Hamza";
    Activity activity;

    public static Ads getInstance(Activity activity2) {
        if (INSTANCE == null) {
            INSTANCE = new Ads(activity2);
        }
        return INSTANCE;
    }

    private Ads(Activity activity2) {
        this.activity = activity2;
    }

    private void consentTypeSelectionRequest() {
        if (ConsentInformation.getInstance(this.activity).getConsentStatus().toString().equals("NON_PERSONALIZED")) {
            ConsentInformation.getInstance(this.activity).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
            adRequest = new AdRequest.Builder().addTestDevice("0551DBD4B44D74DE178CC64B834A2FD6").addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build();
            return;
        }
        ConsentInformation.getInstance(this.activity).setConsentStatus(ConsentStatus.PERSONALIZED);
        adRequest = new AdRequest.Builder().addTestDevice("0551DBD4B44D74DE178CC64B834A2FD6").build();
    }

    public void showBannerAd(AdView adView) {
        consentTypeSelectionRequest();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.Ads.AnonymousClass1 */

            @Override // com.google.android.gms.ads.AdListener
            public void onAdClosed() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdFailedToLoad(int i) {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLeftApplication() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
            }

            @Override // com.google.android.gms.ads.AdListener
            public void onAdOpened() {
            }
        });
    }

    public void initialInterstitial() {
        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(this.activity);
            interstitialAd.setAdUnitId(this.activity.getString(R.string.ad_interstitial));
        }
    }

    public void loadInterstitial() {
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            consentTypeSelectionRequest();
            interstitialAd.loadAd(adRequest);
        }
    }

    public void showInterstitial() {
        InterstitialAd interstitialAd2 = interstitialAd;
        if (interstitialAd2 == null || !interstitialAd2.isLoaded()) {
            Log.v("Hamza", "Ad did not load");
            loadInterstitial();
            return;
        }
        interstitialAd.show();
    }

    public static InterstitialAd getAd() {
        return interstitialAd;
    }

    public static Bundle getNonPersonalizedAdsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("npa", "1");
        return bundle;
    }
}
