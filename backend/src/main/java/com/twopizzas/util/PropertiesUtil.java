package com.twopizzas.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtil {

    private final EnvironmentUtil environmentUtil;

    public PropertiesUtil(EnvironmentUtil environmentUtil) {
        this.environmentUtil = environmentUtil;
    }

    public String resolveWithEnv(String value) {
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
}
