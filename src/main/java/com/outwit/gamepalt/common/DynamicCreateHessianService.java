package com.outwit.gamepalt.common;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.outwit.das.annotation.Hessian;
import com.outwit.das.utils.CollectionUtil;
import com.outwit.das.utils.ConfigHelper;
import com.outwit.das.utils.StringUtil;
import com.outwit.das.utils.scan.ClassHelper;
public class DynamicCreateHessianService implements ApplicationContextAware,ApplicationListener<ContextRefreshedEvent>{
	private ApplicationContext applicationContext;  
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(null == event.getApplicationContext().getParent()){
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。  
			Log.getCommon().debug("自动生成hessain服务注入AuotoWire");
			//得到所有有hessian的注解
			ClassHelper.setBasePackage(ConfigHelper.getString("hessian.package"));
			List<Class<?>> hessianInterfaceList = ClassHelper.getClassListByAnnotation(Hessian.class);
			if (CollectionUtil.isNotEmpty(hessianInterfaceList)) {
				DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
				for (Class<?> hessianInterface : hessianInterfaceList) {
	                //获取 Hessian URL
	                String url = hessianInterface.getAnnotation(Hessian.class).value();
	                if("".equals(url)){
	                	String backpage = hessianInterface.getPackage().getName();
	                	url = backpage.replace("com.outwit.das", "").replace(".service", "").replace('.','/');
	                }
	                //密钥
	                String privateKey = "?" + ConfigHelper.getString("hessian.username") + "&" + ConfigHelper.getString("hessian.password");
	                //服务地址生成
	                String serviceUrl = ConfigHelper.getString("hessian.server") + CommonConstant.HESSIAN_PREFIX + url + "/"+hessianInterface.getSimpleName() + privateKey;
	                Log.getCommon().debug(serviceUrl);
	                //bean名称
	                String beanName = StringUtil.firstToLower(hessianInterface.getSimpleName());
	                if(!applicationContext.containsBeanDefinition(beanName)){
	                	BeanDefinitionBuilder userBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(HessianProxyFactoryBean.class);
						userBeanDefinitionBuilder.addPropertyValue("serviceUrl", serviceUrl );
						userBeanDefinitionBuilder.addPropertyValue("serviceInterface", hessianInterface.getName());
						acf.registerBeanDefinition(beanName, userBeanDefinitionBuilder.getBeanDefinition());
						Log.getCommon().info(beanName+"-"+userBeanDefinitionBuilder.getBeanDefinition());
	                }
	            }
	        }
			}
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
