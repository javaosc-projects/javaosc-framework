package org.javaosc.framework.context;

import org.javaosc.galaxy.annotation.Mapping;
import org.javaosc.galaxy.annotation.Service;
import org.javaosc.galaxy.util.GalaxyUtil;

public class ScanAnnotationTest {
	
	public static void main(String[] args) {
		boolean isService = UserServiceImpl.class.isAnnotationPresent(Service.class);
		System.out.println(isService);
		
		boolean isAction = UserAction.class.isAnnotationPresent(Mapping.class);
		System.out.println(isAction);
		
		System.out.println(GalaxyUtil.formatFirstChar("Dylan",true));
		
//		UserService userService = BeanFactory.getService(UserServiceImpl.class);
//		System.out.println(userService.getUserName());
		
		
	    
		System.out.println(UserServiceImpl.class.getSimpleName());
//		Class<?> cls = UserServiceImpl.class;
//		  ScanAnnotation.setClassGlobalParam(UserServiceImpl.class);
//		  UserService service = null;
//			try {
//				service = (UserService) cls.newInstance();
//			}  catch (Exception e) {
//				e.printStackTrace();
//			}
//		     System.out.println(service.getUserName());
		     
//		 	Class<?> cls = UserAction.class;
//			  ScanAnnotation.setServiceField(UserAction.class);
	}
	
}
