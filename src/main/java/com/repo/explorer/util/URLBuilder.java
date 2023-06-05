package com.repo.explorer.util;

import java.net.URI;

public interface URLBuilder {
    URI buildUrl(String... pathSegments);
}
