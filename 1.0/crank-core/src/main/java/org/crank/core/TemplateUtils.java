package org.crank.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Scott Fauerbach, Rick Hightower
 *
 */
public class TemplateUtils {
	/**
	 * The default pattern start delimiter
	 */
	public static final String DEFAULT_START_DELIMITER = "${";

	/**
	 * The default pattern stop delimiter
	 */
	public static final String DEFAULT_STOP_DELIMITER = "}";

	/**
	 * Process a string and replace all patterns with an appropriate value
	 * from the dictionary using the default start and stop pattern delimiters.
	 * @param sSource the string to process for patterns to replace
	 * @param dictReplace the dictionary used to look up patterns to replace
	 */
	public static String replaceAll(String sSource, Map<?,?> dictReplace) {
		return replaceAll(sSource, dictReplace, DEFAULT_START_DELIMITER,
				DEFAULT_STOP_DELIMITER);
	}
	public static String newReplaceAll(String sSource, Map<?,?> dictReplace) {
		return newReplaceAll(sSource, dictReplace, DEFAULT_START_DELIMITER,
				DEFAULT_STOP_DELIMITER);
	}

	/**
	 * Process a string and replace all patterns with an appropriate value
	 * from the dictionary using the supplied start and stop pattern delimiters.
	 * @param sSource the string to process for patterns to replace
	 * @param dictReplace the dictionary used to look up patterns to replace
	 * @param sStartDelim the string delimiter which indicates the start of a pattern to replace
	 * @param sStopDelim the string delimiter which indicates the stop or end of a pattern to replace
	 */
	public static String newReplaceAll(String sSource, Map<?,?> dictReplace,
					String sStartDelim, String sStopDelim) {
		char [] inputArray = sSource.toCharArray();
		char [] startDelimArray = sStartDelim.toCharArray();
		char [] stopDelimArray = sStopDelim.toCharArray();
		StringBuilder builder = new StringBuilder(sSource.length());
		boolean inPossibleTokenMode = false;
		StringBuilder possibleToken = new StringBuilder(10);
		
		
		for (int index = 0; index < inputArray.length; index++) {
			char c = inputArray[index];
			
			if (c == ' ' || c == '\t' || c == '\n') {
				if (inPossibleTokenMode == true) {
					builder.append(sStartDelim + possibleToken.toString());
					possibleToken = null;
				}
				inPossibleTokenMode = false;
				builder.append(c);
				continue;
			} else {
				if (inPossibleTokenMode) {
					if (c==stopDelimArray[0]) {
						String token = possibleToken.toString();
						Object value = dictReplace.get(token);
						builder.append(value == null ? "error " + sStartDelim + token + sStopDelim + " not found!!!! "
								: value.toString());
						inPossibleTokenMode = false;
						possibleToken = null;
						
					} else {
						possibleToken.append(c);
					}
					continue;
				}
				
			}
			if (!inPossibleTokenMode) {
				if (startDelimArray.length + index > inputArray.length) {
					builder.append(c);
				} else {
					if (startDelimArray[0] == c) {
						int possibleIndex = index;
						int hits = 0;
						for (int delimIndex = 0; delimIndex < startDelimArray.length; delimIndex++, possibleIndex++) {
							if (startDelimArray[delimIndex] == inputArray[possibleIndex]) {
								hits ++;
							} else {
								break;
							}
						}
						if (hits==startDelimArray.length) {
							index = possibleIndex-1;
							inPossibleTokenMode = true;
							possibleToken = new StringBuilder(10);
							continue;
						}
					} else {
						builder.append(c);
					}
				}
			} else {
				
			}
		}
		return builder.toString();
	}

	public static String replaceAll(String sSource, Map<?,?> dictReplace,
			String sStartDelim, String sStopDelim) {
		StringBuilder sb = new StringBuilder(sSource.length());

		int startDelimAt = sSource.indexOf(sStartDelim);
		if (startDelimAt == -1)
			return sSource;

		String sKey;
		Object oValue;
		int startDelimLen = sStartDelim.length();
		int stopDelimLen = sStopDelim.length();
		int stopDelimAt = -stopDelimLen;

		while (startDelimAt != -1) {
			sb.append(sSource.substring(stopDelimAt + stopDelimLen,
					startDelimAt));

			stopDelimAt = sSource.indexOf(sStopDelim, startDelimAt
					+ startDelimLen);
			if (stopDelimAt == -1) {
				stopDelimAt = startDelimAt - stopDelimLen;
				break;
			} else {
				sKey = sSource.substring(startDelimAt + startDelimLen,
						stopDelimAt);
				oValue = dictReplace.get(sKey);

				if (oValue != null)
					sb.append(oValue.toString());

				startDelimAt = sSource.indexOf(sStartDelim, stopDelimAt
						+ stopDelimLen);
			}
		}

		sb.append(sSource.substring(stopDelimAt + stopDelimLen));

		return sb.toString();
	}

	/**
	 * Process a file and replace all patterns with an appropriate value
	 * from the dictionary using the default start and stop pattern delimiters.
	 * @param sInFile the name of a file to read from
	 * @param sOutFile the name of a file to write the processed output to
	 * @param dictReplace the dictionary used to look up patterns to replace
	 */
	public static void replaceAll(String sInFile, String sOutFile,
			Map<?,?> dictReplace) throws FileNotFoundException, IOException {
		replaceAll(sInFile, sOutFile, dictReplace, DEFAULT_START_DELIMITER,
				DEFAULT_STOP_DELIMITER);
	}

	/**
	 * Process a file and replace all patterns with an appropriate value
	 * from the dictionary using the supplied start and stop pattern delimiters.
	 * @param sInFile the name of a file to read from
	 * @param sOutFile the name of a file to write the processed output to
	 * @param dictReplace the dictionary used to look up patterns to replace
	 * @param sStartDelim the string delimiter which indicates the start of a pattern to replace
	 * @param sStopDelim the string delimiter which indicates the stop or end of a pattern to replace
	 */
	public static void replaceAll(String sInFile, String sOutFile,
			Map<?,?> dictReplace, String sStartDelim, String sStopDelim)
			throws FileNotFoundException, IOException {
		BufferedReader in = new BufferedReader(new FileReader(sInFile));
		try {
			PrintWriter out = new PrintWriter(new FileWriter(sOutFile));
			try {
				replaceAll(in, out, dictReplace, sStartDelim, sStopDelim);
			}
			finally {
				out.close();
			}
		}
		finally {
			in.close();
		}
	}

	/**
	 * Process a file and replace all patterns with an appropriate value
	 * from the dictionary using the default start and stop pattern delimiters.
	 * @param fIn the File to read from
	 * @param fOut the File to write the processed output to
	 * @param dictReplace the dictionary used to look up patterns to replace
	 */
	public static void replaceAll(File fIn, File fOut, Map<?,?> dictReplace)
			throws FileNotFoundException, IOException {
		replaceAll(fIn, fOut, dictReplace, DEFAULT_START_DELIMITER,
				DEFAULT_STOP_DELIMITER);
	}

	/**
	 * Process a file and replace all patterns with an appropriate value
	 * from the dictionary using the supplied start and stop pattern delimiters.
	 * @param fIn the File to read from
	 * @param fOut the File to write the processed output to
	 * @param dictReplace the dictionary used to look up patterns to replace
	 * @param sStartDelim the string delimiter which indicates the start of a pattern to replace
	 * @param sStopDelim the string delimiter which indicates the stop or end of a pattern to replace
	 */
	public static void replaceAll(File fIn, File fOut, Map<?,?> dictReplace,
			String sStartDelim, String sStopDelim)
			throws FileNotFoundException, IOException {
		BufferedReader in = new BufferedReader(new FileReader(fIn));
		try {
			PrintWriter out = new PrintWriter(new FileWriter(fOut));
			try {
				replaceAll(in, out, dictReplace, sStartDelim, sStopDelim);
			}
			finally {
				out.close();
			}
		}
		finally {
			in.close();
		}
	}

	/**
	 * Process the input and replace all patterns with an appropriate value
	 * from the dictionary using the default start and stop pattern delimiters.
	 * @param in where to read from
	 * @param out where to write the processed output to
	 * @param dictReplace the dictionary used to look up patterns to replace
	 */
	public static void replaceAll(BufferedReader in, PrintWriter out,
			Map<?,?> dictReplace) throws IOException {
		replaceAll(in, out, dictReplace, DEFAULT_START_DELIMITER,
				DEFAULT_STOP_DELIMITER);
	}

	/**
	 * Process the input and replace all patterns with an appropriate value
	 * from the dictionary using the supplied start and stop pattern delimiters.
	 * @param in where to read from
	 * @param out where to write the processed output to
	 * @param dictReplace the dictionary used to look up patterns to replace
	 * @param sStartDelim the string delimiter which indicates the start of a pattern to replace
	 * @param sStopDelim the string delimiter which indicates the stop or end of a pattern to replace
	 */
	public static void replaceAll(BufferedReader in, PrintWriter out,
			Map<?,?> dictReplace, String sStartDelim, String sStopDelim)
			throws IOException {
		String sLine = in.readLine();
		while (sLine != null) {
			out
					.println(replaceAll(sLine, dictReplace, sStartDelim,
							sStopDelim));
			sLine = in.readLine();
		}
	}

    public static List<String> loadTemplate(String sFile)
			throws FileNotFoundException, IOException {

		BufferedReader in = new BufferedReader(new FileReader(sFile));
		try {
			File f = new File(sFile);
			int approxSize = (int)Math.max(10, f.length()/80);
			List<String> l = new ArrayList<String>(approxSize);
			String sLine = in.readLine();
			while (sLine != null) {
				l.add(sLine);
				sLine = in.readLine();
			}
			return l;
		}
		finally {
			in.close();
		}
	}
}
