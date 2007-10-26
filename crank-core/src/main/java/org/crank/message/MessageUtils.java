package org.crank.message;

import java.util.*;

/**
 * Functions to aid developing JSF applications.
 */
public final class MessageUtils {
	/**
	 * Stops creation of a new MessageUtils object.
	 */
	private MessageUtils() {
	}

	public static String createLabelNoPlural(String fieldName) {
		if (fieldName.endsWith("s")) {
			fieldName = fieldName.substring(0, fieldName.length()-1);
		} else if (fieldName.endsWith("es")) {
			fieldName = fieldName.substring(0, fieldName.length()-2);
		}
		return generateLabelValue(fieldName);
	}
	
	
		

	
	/**
	 * Get the field label.
	 *
	 * @param fieldName
	 *            fieldName
	 * @param messageSource
	 *            messageSource
	 *
	 * @return Label from the Message Source.
	 */
	public static String getLabel(final String fieldName,
			final ResourceBundle bundle) {

		String label;

		/** Look for fieldName, e.g., firstName. */
		try {
			label = bundle.getString(fieldName);
		} catch (MissingResourceException mre) {
			label = generateLabelValue(fieldName);
		}

		return label;
	}

	/**
	 * Generate the field. Transforms firstName into First Name. This allows
	 * reasonable defaults for labels.
	 *
	 * @param fieldName
	 *            fieldName
	 *
	 * @return generated label name.
	 */
	public static String generateLabelValue(final String fieldName) {
		
		final StringBuffer buffer = new StringBuffer(fieldName.length() * 2);
		
        
        class GenerationCommand {
            boolean capNextChar = false;
            boolean lastCharWasUpperCase = false;
            boolean lastCharWasNumber = false;
            boolean lastCharWasSpecial = false;
            boolean shouldContinue = true;
            char[] chars = fieldName.toCharArray();
            void processFieldName() {

            	for (int index = 0; index < chars.length; index++) {
        			char cchar = chars[index];
        			shouldContinue = true;

        			processCharWasNumber(buffer, index, cchar);
        			if (!shouldContinue) {
        				continue;
        			}

        			processCharWasUpperCase(buffer, index, cchar);
        			if (!shouldContinue) {
        				continue;
        			}
                    
        			processSpecialChars(buffer, cchar);

        			if (!shouldContinue) {
        				continue;
        			}

                    cchar = processCapitalizeCommand(cchar);

                    cchar = processFirstCharacterCheck(buffer, index, cchar);
        			
                    if (!shouldContinue) {
        				continue;
        			}

        			buffer.append(cchar);
        		}
            	
            }
			private void processCharWasNumber(StringBuffer buffer,
					int index, char cchar) {
				if (lastCharWasSpecial) {
					return;
				}
				
				if (Character.isDigit(cchar)) {
				    
				    if (index!=0 && !lastCharWasNumber) {
				        buffer.append(' ');
				    }
				    
				    lastCharWasNumber = true;                
					buffer.append(cchar);

					this.shouldContinue = false;
				} else {
					lastCharWasNumber = false;
				}
			}
			private char processFirstCharacterCheck(final StringBuffer buffer,
					int index, char cchar) {
				/* Always capitalize the first character. */
				if (index == 0) {
					cchar = Character.toUpperCase(cchar);
					buffer.append(cchar);
					this.shouldContinue = false;        				
				}
				return cchar;
			}
			private char processCapitalizeCommand(char cchar) {
				/* Capitalize the character. */
				if (capNextChar) {
				    capNextChar = false;
				    cchar = Character.toUpperCase(cchar);
				}
				return cchar;
			}
			private void processSpecialChars(final StringBuffer buffer,
					char cchar) {
				lastCharWasSpecial = false;
				/* If the character is '.' or '_' then append a space and mark
				 * the next iteration to capitalize.
				 */
				if (cchar == '.' || cchar == '_') {
				    buffer.append(' ');
				    capNextChar = true;
					lastCharWasSpecial = false;				    
				    this.shouldContinue = false;
				}
				
				
			}
			private void processCharWasUpperCase(final StringBuffer buffer,
					int index, char cchar) {
				/* If the character is uppercase, append a space and keep track
				 * that the last character was uppercase for the next iteration.
				 */
				if (Character.isUpperCase(cchar)) {
				    
				    if (index!=0 && !lastCharWasUpperCase) {
				        buffer.append(' ');
				    }
				    
				    lastCharWasUpperCase = true;                
					buffer.append(cchar);

					this.shouldContinue = false;
				} else {
				    lastCharWasUpperCase = false;
				}
			}
        }

        GenerationCommand gc = new GenerationCommand();
        gc.processFieldName();
        
        /* This is a hack to get address.line_1 to work. */
		return buffer.toString().replace("  ", " ");
	}

}
