package edu.hitsz.bim.utils;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface BearerTokenResolver {
    String resolve(HttpServletRequest request);
}
