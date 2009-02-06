package org.codegen.util;

public class FileTemplateUtils {
	
	File file
	boolean debug = false
	List<ChangeSpec> changeSpecs = new ArrayList<ChangeSpec>()	

	public process () {
		List<String> lines = file.readLines()
		if (debug) println "Read in lines ${lines}"
		if (debug) println "There were ${lines.size()} in ${file}"
		file.withWriter{BufferedWriter writer ->
			for (String line in lines) {
				if (debug) println "Processing line ${line}"
				//if (changeSpecs[0].processLine(line, writer)) {
				if (proceed(line, writer)) {	
					if (debug) println "Writing line ${line}"
					writer.writeLine(line)
				}
			}
		}
	}
	
	public boolean proceed(String line, BufferedWriter writer) {
		int dontProceedCount = 0
		for (ChangeSpec changeSpec : changeSpecs) {
			if (!changeSpec.processLine(line, writer)) {
				dontProceedCount++
			}
		}
		if (dontProceedCount > 0) {
			return false
		} else {
			return true
		}
	}
}


class ChangeSpec {
	String startLocationMarker = "startLocationMarker"
	String stopLocationMarker = "stopLocationMarker"
	String replacementText
	boolean inTextThatNeedsToBeReplaced
	boolean debug = false
	boolean processLine(String line, BufferedWriter writer) {
		if (!inTextThatNeedsToBeReplaced && line.contains(startLocationMarker)){
			if (debug) println "Found text that needs to be replaced"
			inTextThatNeedsToBeReplaced = true
			writer.write(replacementText)
			return false
		}
		if (inTextThatNeedsToBeReplaced && line.contains(stopLocationMarker)) {
			if (debug) println "Exiting the text that needs to be replaced"
			inTextThatNeedsToBeReplaced = false
			return false
		}
		if (inTextThatNeedsToBeReplaced) {
			if (debug) println "Still in the text that need to be replaced"
			return false
		}
		return true
	}
}
