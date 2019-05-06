package cn.jbolt.api;

import java.util.Map;

import com.jfinal.core.Controller;

import cn.jbolt.util.RSAUtil;

public class ApiController extends Controller {

	private static Map<String, String> keys = RSAUtil.getKey();

	public void getKeys() {
		KeyBean keyBean = new KeyBean();
		keyBean.setCode(1);
		keyBean.setPublicKey(RSAUtil.getPublicKey(keys));
		keyBean.setPrivateKey(RSAUtil.getPrivateKey(keys));
		renderJson(keyBean);
	}

}
