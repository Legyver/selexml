package com.legyver.selexml.api.license;

import com.legyver.core.license.LicenseService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LicenseServiceImpl implements LicenseService {

    @Override
    public Properties loadLicenseProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = LicenseServiceImpl.class.getResourceAsStream("license.properties")) {
            properties.load(inputStream);
        }
        return properties;
    }
}
