package com.jive.twitter.oauth.engines;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;

import com.jive.twitter.external.PercentEscaper;
import com.jive.twitter.oauth.utils.NonceGeneratorFactory;
import com.jive.twitter.oauth.utils.TimeStampGenerator;

public abstract class MyOAuthEngine extends OAuthProvider {

    public static final String MAC_ALG = "HmacSHA1";

    SignInActivityCompletedReceiver m_receiver = new SignInActivityCompletedReceiver();

    public MyOAuthEngine(OAuthClient listener) {
        super(listener);
    }

    public String getAuthorizationHeader(String request) {
        String nonce = NonceGeneratorFactory.get().get();

        HeaderParams authHeaderParams = new HeaderParams();

        authHeaderParams.add(new HeaderParam(OAUTH_CONSUMER_KEY, getConsumerCredentials().first));
        authHeaderParams.add(new HeaderParam(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_ALG));
        authHeaderParams.add(new HeaderParam(OAUTH_TIMESTAMP, TimeStampGenerator.get().get()));

        authHeaderParams.add(new HeaderParam(OAUTH_NONCE, nonce));
        authHeaderParams.add(new HeaderParam(OAUTH_VERSION, OAUTH_VERSION_VALUE));
        if (!TextUtils.isEmpty(getCallbackUrl())) {
            authHeaderParams.add(new HeaderParam(OAUTH_CALLBACK, getCallbackUrl()));
        }
        OAuthToken oauthRequestToken = getOauthRequestToken();
        if (oauthRequestToken != null && !TextUtils.isEmpty(oauthRequestToken.oauth_token)) {
            authHeaderParams.add(new HeaderParam(OAUTH_TOKEN, oauthRequestToken.oauth_token));
        }

        String signature = generateOauthSignature(POST, request, authHeaderParams);

        authHeaderParams.add(new HeaderParam(OAUTH_SIGNATURE, signature));

        StringBuilder authHeader = new StringBuilder(OAUTH_HEADER_PREFIX);
        authHeader.append(authHeaderParams.toString());

        return authHeader.toString();

    }

    private String generateOauthSignature(String method, String request, HeaderParams requestParams) {

        Pair<String, String> consumerCreds = getConsumerCredentials();

        Mac mac;
        try {
            mac = Mac.getInstance(MAC_ALG);

            String oauthSignature = percentEncode(consumerCreds.second) + "&";
            SecretKey key = new SecretKeySpec(oauthSignature.getBytes(), MAC_ALG);

            mac.init(key);

            StringBuffer sbsBuffer = new StringBuffer(percentEncode(method)).append("&").append(percentEncode(normalizeRequestUrl(request)))
                    .append("&");
            sbsBuffer.append(normalizeRequestParameters(requestParams));
            String sbs = sbsBuffer.toString();

            byte[] text = sbs.getBytes();
            return Base64.encodeToString(mac.doFinal(text), Base64.DEFAULT).trim();
        } catch (NoSuchAlgorithmException e) {

        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    public String normalizeRequestUrl(String request) {
        try {
            URI uri = new URI(request);
            String scheme = uri.getScheme().toLowerCase(Locale.ENGLISH);
            String authority = uri.getAuthority().toLowerCase(Locale.ENGLISH);
            boolean dropPort = (scheme.equals(HTTP) && uri.getPort() == 80) || (scheme.equals(HTTPS) && uri.getPort() == 443);
            if (dropPort) {
                // find the last : in the authority
                int index = authority.lastIndexOf(":");
                if (index >= 0) {
                    authority = authority.substring(0, index);
                }
            }
            String path = uri.getRawPath();
            if (path == null || path.length() <= 0) {
                path = "/"; // conforms to RFC 2616 section 3.2.2
            }
            // we know that there is no query and no fragment here.
            return scheme + "://" + authority + path;
        } catch (URISyntaxException e) {

        }
        return "";
    }

    public String normalizeRequestParameters(HeaderParams requestParams) {
        if (requestParams == null) {
            return "";
        }

        HeaderParams sbsHeaderParams = new HeaderParams();
        for (HeaderParam hp : requestParams) {
            if (OAUTH_SIGNATURE.equals(hp.first) || REALM.equals(hp.first)) {
                continue;
            }
            sbsHeaderParams.add(hp);
        }

        Collections.sort(sbsHeaderParams, new Comparator<Pair<String, String>>() {

            @Override
            public int compare(Pair<String, String> lhs, Pair<String, String> rhs) {
                return lhs.first.compareTo(rhs.first);
            }
        });

        return sbsHeaderParams.getNormalized("&");
    }

    class HeaderParam extends Pair<String, String> {

        public HeaderParam(String key, String value) {
            super(percentEncode(key), percentEncode(value));
        }

        public String getNormalized() {
            StringBuilder sb = new StringBuilder();
            sb.append(first);
            sb.append(percentEncode("="));
            sb.append(second);
            return sb.toString();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(first);
            sb.append("=\"");
            sb.append(second);
            sb.append("\"");
            return sb.toString();
        }
    }

    @SuppressWarnings("serial")
    class HeaderParams extends ArrayList<HeaderParam> {

        public String getNormalized(String concatChar) {
            StringBuilder sb = new StringBuilder();

            int i = 0;
            for (HeaderParam hp : this) {
                sb.append(hp.getNormalized());
                i++;
                if (i != size())
                    sb.append(percentEncode(concatChar));
            }
            return sb.toString();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            int i = 0;
            for (HeaderParam hp : this) {
                sb.append(hp.toString());
                i++;
                if (i != size())
                    sb.append(",");
            }
            return sb.toString();
        }
    }

    private static final PercentEscaper percentEncoder = new PercentEscaper("-._~", false);

    public static String percentEncode(String s) {
        if (s == null) {
            return "";
        }
        return percentEncoder.escape(s);
    }

    public static String percentDecode(String s) {

        if (s == null) {
            return "";
        }
        return URLDecoder.decode(s);
        // This implements http://oauth.pbwiki.com/FlexibleDecoding

    }

   
}
