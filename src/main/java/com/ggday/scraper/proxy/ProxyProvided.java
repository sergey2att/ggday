package com.ggday.scraper.proxy;

import java.util.Collection;

public interface ProxyProvided {
    Collection<HttpProxy> provide();
}
