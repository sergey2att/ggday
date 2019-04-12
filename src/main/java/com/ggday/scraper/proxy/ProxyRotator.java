package com.ggday.scraper.proxy;

import com.google.common.collect.Iterables;

import java.util.*;

public class ProxyRotator {
    private Queue<HttpProxy> proxies = new ArrayDeque<>();
    private final RotationStrategy strategy;
    private final ProxyProvided provider;
    private HttpProxy current = null;
    private int counter = 1;
    private Iterator<HttpProxy> iterator;

    public ProxyRotator(ProxyProvided proxyProvider, RotationStrategy strategy) {
        this.provider = proxyProvider;
        this.proxies.addAll(provider.provide());
        this.strategy = strategy;
        this.iterator = Iterables.cycle(proxies).iterator();
    }

    public HttpProxy rotate() {
        switch (strategy) {
            case REFRESH: {
                if (counter >= proxies.size()) {
                    this.proxies.clear();
                    this.proxies.addAll(provider.provide());
                    this.iterator = Iterables.cycle(proxies).iterator();
                    counter = 1;
                }
                if (iterator.hasNext()) {
                    current = iterator.next();
                    counter++;
                    return current;
                }
            }
            case REPEAT: {
                if (iterator.hasNext()) {
                    current = iterator.next();
                    return current;
                }
            }
            default:
                throw new UnsupportedOperationException(String.format("Strategy '%s' does not supported", strategy.name()));
        }
    }

    public HttpProxy find(String host, int port) {
        return list().stream().filter(v -> v.getIp().equals(host) && v.getPort() == port).findFirst().orElse(null);
    }

    public List<HttpProxy> list() {
        return new ArrayList<>(proxies);
    }
}
