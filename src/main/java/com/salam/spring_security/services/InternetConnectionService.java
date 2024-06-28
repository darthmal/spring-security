package com.salam.spring_security.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Service
public class InternetConnectionService {

    public boolean isInternetAvailable() {
//        try {
//            InetAddress address = InetAddress.getByName("google.com"); // Or any reliable host
//            return address.isReachable(5000); // Timeout set to 5 seconds

            try {
                final URL url = new URL("http://www.google.com");
                final URLConnection conn = url.openConnection();
                conn.connect();
                conn.getInputStream().close();
                return true;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                return false;
            }

    }
}