package com.bj58.daojia.qa.report;

/**
 * Report Exception Class
 * 
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class ReportNGException extends RuntimeException {
	public ReportNGException(String string) {
		super(string);
	}

	public ReportNGException(String string, Throwable throwable) {
		super(string, throwable);
	}
}
