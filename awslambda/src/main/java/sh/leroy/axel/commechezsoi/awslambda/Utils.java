package sh.leroy.axel.commechezsoi.awslambda;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Pattern numericPattern = Pattern.compile("(\\d+)");

    public static String getNumericId(String input) {
        Matcher matcher = numericPattern.matcher(input);
        return (matcher.find()) ? matcher.group() : "";
    }
}
