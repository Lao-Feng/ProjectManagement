package com.yonglilian.generator.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
/**
 * 
 * @author nfzr
 *
 */
public class ConfigUtils {
	/**
	 * 获取配置信息
	 */
	public static Configuration getConfig() {
		try {
			return new PropertiesConfiguration( "generator.properties" );
		} catch( ConfigurationException e ) {
			throw new RRException( "获取配置文件失败，", e );
		}
	}
}
