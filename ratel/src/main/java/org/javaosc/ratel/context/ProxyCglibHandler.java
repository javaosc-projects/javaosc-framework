package org.javaosc.ratel.context;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.javaosc.ratel.assist.MethodParamHandler;
import org.javaosc.ratel.constant.Constant;
import org.javaosc.ratel.jdbc.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */

public class ProxyCglibHandler implements MethodInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(ProxyCglibHandler.class);
	
	private Enhancer enhancer = new Enhancer();
	
	private Object target;
	
	private boolean isTransaction;
	
	protected ProxyCglibHandler(Object target, boolean isTransaction) {
		this.target = target;
		this.isTransaction = isTransaction;
	}

	@SuppressWarnings("unchecked")
	protected <T> T  proxyInstance() {
		enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return (T)enhancer.create();
	}

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		boolean isHasTx = false;
		Object returnObj = null;
		if(isTransaction && ConfigHandler.getMethodKeyword() != null){
			for(String keyword:ConfigHandler.getMethodKeyword()){
				if(method.getName().startsWith(keyword)){
					isHasTx = true;
					break;
				}
			}
			ConnectionHandler.getConnection();
			if(isHasTx){
				try {
					ConnectionHandler.beginTransaction();
					returnObj = proxy.invokeSuper(obj, args);
					ConnectionHandler.commit();
				} catch (Exception e) {
					ConnectionHandler.rollback();
					MethodParamHandler.getMethodParam(method, args);
					log.error(Constant.RATEL_EXCEPTION, e);
				}
			}else{
				try {
					returnObj = proxy.invokeSuper(obj, args);
				} catch (Exception e) {
					MethodParamHandler.getMethodParam(method, args);
					log.error(Constant.RATEL_EXCEPTION, e);
				}
			}
			ConnectionHandler.close();
		}else{
			returnObj = proxy.invokeSuper(obj, args);
		}	
		return returnObj;
	}
}