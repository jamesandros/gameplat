import java.util.List;

import com.outwit.das.annotation.Hessian;
import com.outwit.das.utils.CollectionUtil;
import com.outwit.das.utils.ConfigHelper;
import com.outwit.das.utils.scan.ClassHelper;


public class Test {
	public static void main(String[] args) {
		//设置扫描包
		ClassHelper.setBasePackage(ConfigHelper.getString("hessian.package"));
		List<Class<?>> hessianInterfaceList = ClassHelper.getClassListByAnnotation(Hessian.class);
		System.out.println(hessianInterfaceList.size());
		if (CollectionUtil.isNotEmpty(hessianInterfaceList)) {
			
        }
		
	}
}
