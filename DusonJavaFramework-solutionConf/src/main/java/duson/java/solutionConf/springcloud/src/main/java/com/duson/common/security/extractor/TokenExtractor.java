package com.facewnd.ad.common.security.extractor;

public interface TokenExtractor {
    public String extract(String payload);
}
