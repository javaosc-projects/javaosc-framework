package org.javaosc.framework.context;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.util.PathUtil;
import org.javaosc.framework.util.StringUtil;
import org.javaosc.framework.util.StringUtil.PatternValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ConfigExtHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigExtHandler.class);
	
	private static Properties properties;
	
	public static void load(String configFileName) {
		if(StringUtil.isNotBlank(configFileName)){
			String [] extConfig = null;
			configFileName = StringUtil.clearSpace(configFileName, PatternValue.ALL);
			if(configFileName.indexOf(Constant.COMMA)>0){
				extConfig = configFileName.split(Constant.COMMA);
			}else{
				extConfig = new String[]{configFileName};
			}
			init(extConfig);
		}else{
			log.warn("context-param: expandConfig is not set in the web.xml. Suggest set this context-param to express the business settings!");
		}
	}
	
	private static void init(String[] configs){
			InputStream inputStream = null;
			try {
				properties = new Properties();
				for(int i=0;i<configs.length;i++){
					String configPath = PathUtil.getClassPath() + configs[i];
					inputStream = new FileInputStream(configPath);
					properties.load(inputStream);
				}
				log.info("Initializing " + StringUtil.join(configs, Constant.COMMA));
			} catch (Exception e) {
				log.error(Constant.JAVAOSC_EXCEPTION, e);
			}finally{
				try {
					if(inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					log.error(Constant.JAVAOSC_EXCEPTION, e);
				}	
			}	
	}
	
	public static String getValue(String key){
		return properties.getProperty(key);
	}
	
	public static void clear(){
		properties.clear();
		properties = null;
	}
		
}
