import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RandomClass {
    public String tryOut(String input) {
        Matcher matcher = Pattern.compile("").matcher(input);
//        ArrayList<String> matches = matcher.results().collect(Collectors.joining("/"));
        return input.toLowerCase();
    }
}
