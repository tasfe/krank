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
		StringBuffer buffer = new StringBuffer(fieldName.length() * 2);
		char[] chars = fieldName.toCharArray();

		for (int index = 0; index < chars.length; index++) {
			char cchar = chars[index];

			if (Character.isUpperCase(cchar)) {
				buffer.append(' ');
				buffer.append(cchar);

				continue;
			}

			if (index == 0) {
				cchar = Character.toUpperCase(cchar);
				buffer.append(cchar);

				continue;
			}

			buffer.append(cchar);
		}

		return buffer.toString();
	}

}
