package com.bj58.daojia.qa.dataprovider;

import java.util.ArrayList;
import java.util.List;

/**
 * Data DataProvider Impl
 * 
 * @author HanLingZhi
 */
public class DataProviderImpl implements IData{
	
	protected String defaultPath = "resources/data/";
	
	protected int start = 1;
	
	protected int max = Integer.MAX_VALUE;
	
	protected List<Object[]> data = new ArrayList<Object[]>();
	
	/**
	 * 从文件中获取数据
	 * @param caseName 数据集名称
	 * @param dataFile 数据文件
	 */
	@Override
	public Object[][] getData(String caseName, String dataFile) {
		generateData(caseName, dataFile);
		return generateArrayData();
	}

	/**
	 * 从文件中获取数据
	 * @param caseName 数据集名称
	 * @param dataFile 数据文件
	 * @param startRowNum 起始的数据记录，默认为1
	 */
	@Override
	public Object[][] getData(String caseName, String dataFile, int startRowNum) {
		start = startRowNum;
		return getData(caseName, dataFile);
	}

	/**
	 * 从文件中获取数据
	 * @param caseName 数据集名称
	 * @param dataFile 数据文件
	 * @param startRowNum 起始的数据记录，默认为1
	 * @param Length 获取数据的长度，默认为全部，包括起始数据
	 */
	@Override
	public Object[][] getData(String caseName, String dataFile,
			int startRowNum, int Length) {
		max = startRowNum + Length;
		return getData(caseName, dataFile, startRowNum);
	}

	public String getDefaultPath() {
		return defaultPath;
	}

	public void setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
	}

	/**
	 * 具体的文件驱动需要重写这个函数
	 */
	protected void generateData(String caseName, String dataFile) {}

	protected Object[][] generateArrayData() {
		int i = 0;
		Object[][] o = new Object[data.size()][];
		for (Object[] oo : data) {
			o[i++] = oo;
		}
		data.clear();
		return o;
	}
}
