package org.javaosc.galaxy.pool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.javaosc.galaxy.assist.PropertyConvert;
import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.context.ConfigHandler;
import org.javaosc.galaxy.util.GalaxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceMatch {
	
	private static final Logger log = LoggerFactory.getLogger(DataSourceMatch.class);
	
	public static DataSource get(){
		DataSource ds = null; 
		String dataSourceName = ConfigHandler.getDataSourceName();
		if(GalaxyUtil.isEmpty(dataSourceName)){
			log.error("pool.dataSource must be not null!");
		}
		if(dataSourceName.indexOf(".hikari.")>0){
			 HikariConfig config = PropertyConvert.convertMapToEntity(ConfigHandler.getPoolParam(), HikariConfig.class);
			 ds = new HikariDataSource(config);
		}else if(dataSourceName.indexOf("java:")==0){ //tomcat jdbc
			try {
				Context c = new InitialContext();
				ds = (DataSource)c.lookup(dataSourceName);
			} catch (NamingException e) {
				log.error(Constant.GALAXY_EXCEPTION, e);
			}
		}else if(dataSourceName.indexOf(".c3p0.")>0){ //c3p0 pool
			C3p0Handler c3p0Handler = null;
			try {
				c3p0Handler = PropertyConvert.convertMapToEntity(ConfigHandler.getPoolParam(), C3p0Handler.class);
				ds = c3p0Handler.getDataSource();
			} catch (Exception e) {
				log.error(Constant.GALAXY_EXCEPTION, e);
			} 	
		}
		return ds;
	}
}
