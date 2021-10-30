package com.legyver.selexml.query.where.comparator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Support SQL 'like' wildcards '%' and '_'
 */
public class LikeComparator {
    private final Pattern pattern;

    /**
     * Construct a comparator that converts sql wild cards to
     * @param pattern the sql wildcard pattern
     * @param caseInSensitive true if the pattern should be made not case sensitive
     */
    public LikeComparator(String pattern, boolean caseInSensitive) {
        String regex = pattern;
        regex = regex.replaceAll("%", "(.)*");
        regex = regex.replaceAll("_", "(.)");
        if (caseInSensitive) {
            StringBuilder sb = new StringBuilder();
            for (char c : regex.toCharArray()) {
                if (c > 64 && c < 91) {
                    char toLower = (char) (c + 32);
                    sb.append("([").append(c).append(toLower).append("])");
                } else if (c > 96 && c < 123) {
                    char toUpper = (char) (c - 32);
                    sb.append("([").append(c).append(toUpper).append("])");
                } else {
                    sb.append(c);
                }
            }
            regex = sb.toString();
        }
        regex = "^" + regex;

        this.pattern = Pattern.compile(regex);
    }

    /**
     * Test if the actual value matches the specified pattern
     * @param actual the actual value
     * @return true if it matches
     */
    public boolean matches(String actual) {
        Matcher matcher = pattern.matcher(actual);
        return matcher.find();
    }
}
