package com.twopizzas.configuration;

import com.twopizzas.util.EnvironmentUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationContextImpl implements ConfigurationContext {

    private static final String FILE_NAME = "application";
    private final EnvironmentUtil environmentUtil;
    private final String profile;
    private final String fileName;
    private Properties properties;

    public ConfigurationContextImpl(EnvironmentUtil environmentUtil, String profile) {
        this.environmentUtil = environmentUtil;
        this.profile = profile;
        this.fileName = getFileNameForProfile(profile);
    }

    // for testing
    String resolveWithEnv(String value) {
        if (value == null) {
            return null;
        }

        Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(value);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            Optional<String> envValue = environmentUtil.getEnv(matcher.group(1));
            matcher.appendReplacement(stringBuffer,
                    Matcher.quoteReplacement(envValue.orElse("")));
        }

        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    private String getFileNameForProfile(String profile) {
        StringBuilder fileName = new StringBuilder(FILE_NAME);
        if (profile != null && !profile.isEmpty()) {
            fileName.append("-");
            fileName.append(profile);
        }
        return fileName.append(".properties").toString();
    }

    @Override
    public void init() {
        try {
            properties = new Properties();
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);

            if (stream == null) {
                throw new ConfigurationContextException(String.format("unable to locate configuration file [%s]", fileName));
            }

            properties.load(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getConfigurationProperty(String name) {
        return resolveWithEnv(properties.getProperty(name));
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
