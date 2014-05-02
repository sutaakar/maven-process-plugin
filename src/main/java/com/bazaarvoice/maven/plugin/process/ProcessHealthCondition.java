package com.bazaarvoice.maven.plugin.process;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ProcessHealthCondition {
    private static final int SECONDS_BETWEEN_CHECKS = 1;
    private static final int SECONDS_TO_WAIT_WHEN_NO_HEALTHCHECK = 30;

    private ProcessHealthCondition() {}

    public static void waitSecondsUntilHealthy(String healthCheckUrl, int timeoutInSeconds) {
        if (healthCheckUrl == null) {
            // Wait for some arbitrary time to give the process to come up
            sleep(SECONDS_TO_WAIT_WHEN_NO_HEALTHCHECK);
            return;
        }
        final long start = System.currentTimeMillis();
        final URL url = url(healthCheckUrl);
        while ((System.currentTimeMillis() - start) / 1000 < timeoutInSeconds) {
            internalSleep();
            if (is200(url)) {
                return; // success!!!
            }
        }
        throw new RuntimeException("emo service was not healthy even after " + timeoutInSeconds + " seconds");
    }

    private static boolean is200(URL url) {
        try {
            final int code = getResponseCode(url);
            return 200 <= code && code < 300;
        } catch (Exception e) {
            return false;
        }
    }

    private static int getResponseCode(URL url) {
        OutputStream out = null;
        try {
            final HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            return http.getResponseCode();
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    private static URL url(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void internalSleep() {
        try {
            Thread.sleep(SECONDS_BETWEEN_CHECKS * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void closeQuietly(Closeable out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {/**/}
    }
}
