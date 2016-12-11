package com.ua.erent.module.core.presentation.mvp.view.util;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandlerFactory;

import dagger.internal.Preconditions;

/**
 * Created by Максим on 11/28/2016.
 */

public class MyURL implements Parcelable {

    private final URL url;

    public MyURL(@NotNull URL url) {
        this.url = Preconditions.checkNotNull(url);
    }

    public MyURL(@NotNull String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private MyURL(Parcel in) {
        this(in.readString());
    }

    public static final Creator<MyURL> CREATOR = new Creator<MyURL>() {
        @Override
        public MyURL createFromParcel(Parcel in) {
            return new MyURL(in);
        }

        @Override
        public MyURL[] newArray(int size) {
            return new MyURL[size];
        }
    };

    public String getQuery() {
        return url.getQuery();
    }

    public String getPath() {
        return url.getPath();
    }

    public int getDefaultPort() {
        return url.getDefaultPort();
    }

    public String getRef() {
        return url.getRef();
    }

    public boolean sameFile(URL other) {
        return url.sameFile(other);
    }

    public URI toURI() throws URISyntaxException {
        return url.toURI();
    }

    public URLConnection openConnection(Proxy proxy) throws IOException {
        return url.openConnection(proxy);
    }

    public static void setURLStreamHandlerFactory(URLStreamHandlerFactory fac) {
        URL.setURLStreamHandlerFactory(fac);
    }

    public String getAuthority() {
        return url.getAuthority();
    }

    public Object getContent(Class[] classes) throws IOException {
        return url.getContent(classes);
    }

    public String getProtocol() {
        return url.getProtocol();
    }

    public URLConnection openConnection() throws IOException {
        return url.openConnection();
    }

    public String getHost() {
        return url.getHost();
    }

    public String toExternalForm() {
        return url.toExternalForm();
    }

    public int getPort() {
        return url.getPort();
    }

    public String getFile() {
        return url.getFile();
    }

    public String getUserInfo() {
        return url.getUserInfo();
    }

    public InputStream openStream() throws IOException {
        return url.openStream();
    }

    public Object getContent() throws IOException {
        return url.getContent();
    }

    public URL toURL() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url.toExternalForm());
    }
}
