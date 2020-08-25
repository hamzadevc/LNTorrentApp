package org.lntorrent.libretorrent.ads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;

import java.net.MalformedURLException;
import java.net.URL;

public class GdprHelper {
    private static final String MARKET_URL_PAID_VERSION = "";
    private static final String PRIVACY_URL = "";
    private static final String PUBLISHER_ID = "";
    public ConsentForm consentForm;
    public final Context context;

    public GdprHelper(Context context2) {
        this.context = context2;
    }

    public void initialise() {
        ConsentInformation instance = ConsentInformation.getInstance(this.context);
        ConsentInformation.getInstance(this.context).addTestDevice("");
        ConsentInformation.getInstance(this.context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        instance.requestConsentInfoUpdate(new String[]{PUBLISHER_ID}, new ConsentInfoUpdateListener() {
            /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.GdprHelper.AnonymousClass1 */

            @Override // com.google.ads.consent.ConsentInfoUpdateListener
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                if (consentStatus == ConsentStatus.UNKNOWN) {
                    GdprHelper.this.displayConsentForm();
                }
            }

            @Override // com.google.ads.consent.ConsentInfoUpdateListener
            public void onFailedToUpdateConsentInfo(String str) {
                if ("release".equals("debug")) {
                    Toast.makeText(GdprHelper.this.context, str, 1).show();
                }
            }
        });
    }

    public void resetConsent() {
        ConsentInformation.getInstance(this.context).reset();
    }

    public void displayConsentForm() {
        this.consentForm = new ConsentForm.Builder(this.context, getPrivacyUrl()).withListener(new ConsentFormListener() {
            /* class LNtorrent.torrent.bittorrent.torrents.torrentclient.ads.GdprHelper.AnonymousClass2 */

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormOpened() {
            }

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormLoaded() {
                GdprHelper.this.consentForm.show();
            }

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormClosed(ConsentStatus consentStatus, Boolean bool) {
                if (bool.booleanValue()) {
                    GdprHelper.this.redirectToPaidVersion();
                }
            }

            @Override // com.google.ads.consent.ConsentFormListener
            public void onConsentFormError(String str) {
                if ("release".equals("debug")) {
                    Toast.makeText(GdprHelper.this.context, str, 1).show();
                }
            }
        }).withPersonalizedAdsOption().withNonPersonalizedAdsOption().withAdFreeOption().build();
        this.consentForm.load();
    }

    private URL getPrivacyUrl() {
        try {
            return new URL(PRIVACY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void redirectToPaidVersion() {
        this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(MARKET_URL_PAID_VERSION)));
    }
}
