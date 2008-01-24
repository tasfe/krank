package org.crank.core;

import org.testng.annotations.Test;

public class LogTest {

	@Test
	public void testLogMessages() {
		Log log = Log.getLog(LogTest.class);
		log.debug("this is a debug message");
		log.error("this is an error message");
		log.fatal("this is a fatal message");
		log.info("this is an info message");
	}

	@Test
	public void testLogHandling() {

		try {
			throw new RuntimeException("expected test condition");
		} catch (Exception e) {
			Log log = Log.getLog(LogTest.class);
			log.handleExceptionInfo("this is an info message", e);
			log.handleExceptionError("this is an error message", e);
			log.handleExceptionFatal("this is a fatal message", e);
			log.handleExceptionWarn("this is an warn message", e);
		}
	}

}
