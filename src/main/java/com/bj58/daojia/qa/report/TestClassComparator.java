package com.bj58.daojia.qa.report;

import java.util.Comparator;
import org.testng.IClass;

/**
 * Test Class Comparator
 * 
 * @version 1.0.0
 */
class TestClassComparator implements Comparator<IClass> {
	public int compare(IClass class1, IClass class2) {
		return class1.getName().compareTo(class2.getName());
	}
}
