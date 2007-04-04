package org.crank.validation.validators;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;


/**
 *
 * <p>
 * <small>
 * Regex validator.
 * </small>
 * </p>
 * @author Rick Hightower
 */
public class RegexValidator extends AbstractValidator {

    private String match;
    private boolean negate;
    private static Map<String, Pattern> compiledRegexCache = new HashMap<String, Pattern> ();

    public boolean isNegate() {
        return this.negate;
    }
    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    /** The match.
     * @return the regular expression
     */
    protected String getMatch() {
        return this.match;
    }
    public void setMatch(String regex) {
        this.match = regex;
    }

    public ValidatorMessageHolder validate(Object object, String fieldLabel) {
        ValidatorMessage message = new ValidatorMessage();
        if (object == null) {
        	return message;
        }
        String string = object.toString();
        Pattern pattern = compileRegex();
        boolean valid;
        if (negate) {
            valid = !pattern.matcher(string).matches();
        } else {
            valid = pattern.matcher(string).matches();
        }

        if (!valid) {
            populateMessage(message, fieldLabel);
            return message;
        }

        return message;
    }

    /**
     * Compiles a match.
     * @return the resulting pattern object
     */
    private Pattern compileRegex() {

        Pattern pattern = compiledRegexCache.get(getMatch());
        if (pattern == null) {
            pattern = Pattern.compile(getMatch());
            compiledRegexCache.put(getMatch(), pattern);
        }
        return pattern;
    }

}
