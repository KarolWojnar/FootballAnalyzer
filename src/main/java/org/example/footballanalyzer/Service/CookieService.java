package org.example.footballanalyzer.Service;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class CookieService {
    public Cookie generateCookie(String name, String value, int exp) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(exp);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
