package com.repo.explorer.util.interfaces;

import java.net.URI;

public interface URLBuilder {
    URI buildUrl(String... pathSegments);
}
