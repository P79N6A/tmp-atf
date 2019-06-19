package com.bj58.daojia.qa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Initialization Properties
 * 
 * @author nihuaiqing
 */
public class InitProperties {
	public static final String PFILEPATH = File.separatorChar + "resources" + File.separatorChar + "config"
			+ File.separatorChar + "config.properties";

	private Properties config = new Properties();

	public static Map<String, String> mapproperties = new HashMap<String, String>();

	public static final String ATFPFILEPATH = File.separatorChar + "src" + File.separatorChar + "main"
			+ File.separatorChar + "resources" + File.separatorChar + "config.properties";

	public InitProperties() {
		// 构造初始配置文件
		init();
		atfInit();// 框架中Login参数的初始化，如果有用户配置的话，优先使用用户配置
	}

	/**
	 * 从配置文件中获取登录所需参数，如果用户有配置则优先使用用户配置
	 */
	private void atfInit() {
		Logger.log("加载atf配置%s", "");
		StringBuilder content = new StringBuilder();
		try {
			InputStream inputStream = this.getClass().getResourceAsStream("/config.properties");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() > 0 && !line.startsWith("#")) {
					if (line.contains("=") && line.split("=").length == 2) {
						String key = line.split("=")[0].trim();
						String value = line.split("=")[1].trim();
						if (!System.getProperties().containsKey(key.toString()) && !value.isEmpty()) {
							InitProperties.mapproperties.put(key, value);
							System.setProperty(key, value);
						}
					}
				}

			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void init() {
		String configPath = System.getProperty("user.dir") + PFILEPATH;
		File file = new File(configPath);
		Logger.log("加载配置文件%s", configPath);
		InputStreamReader fn = null;
		if (file.exists()) {
			try {
				fn = new InputStreamReader(new FileInputStream(configPath), "UTF-8");
				config.load(fn);
				if (!config.isEmpty()) {
					Set<Object> keys = config.keySet();
					for (Object key : keys) {
						InitProperties.mapproperties.put(key.toString(), config.getProperty(key.toString()));
						if (!System.getProperties().containsKey(key.toString())
								&& !config.getProperty(key.toString()).isEmpty()) {
							System.setProperty(key.toString(), config.getProperty(key.toString()));
						}
					}
					keys.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 对外调试使用
	public static void showAllSystemProperties() {
		Set<String> syskeys = InitProperties.mapproperties.keySet();
		for (Object key : syskeys) {
			if (System.getProperties().containsKey(key)) {
				System.out.println(key.toString() + "  " + System.getProperty(key.toString()));
			}
		}
		syskeys.clear();
	}
}
