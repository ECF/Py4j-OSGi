package org.py4j.osgi;

import java.util.HashMap;
import java.util.Map;

import py4j.Gateway;
import py4j.Py4JPythonClient;

public class OSGiGateway extends Gateway {

	public OSGiGateway(Object entryPoint, Py4JPythonClient cbClient) {
		super(entryPoint, cbClient);
	}

	public void resetCallbackClient() {
		Py4JPythonClient client = getCallbackClient();
		synchronized (proxyIdToProxyMap) {
			super.resetCallbackClient(client.getAddress(), client.getPort());
		}
	}
	
	private Map<String, Object> proxyIdToProxyMap = new HashMap<String, Object>();

	@SuppressWarnings("rawtypes")
	public Object createProxy(ClassLoader cl, Class[] ca, String objectId) {
		Object result = super.createProxy(cl, ca, objectId);
		synchronized (proxyIdToProxyMap) {
			proxyIdToProxyMap.put(objectId, result);
		}
		return result;
	}

	public Object getProxy(String objectId) {
		synchronized (proxyIdToProxyMap) {
			return proxyIdToProxyMap.get(objectId);
		}
	}

	public String getIdForProxy(Object proxy) {
		synchronized (proxyIdToProxyMap) {
			for (String key : proxyIdToProxyMap.keySet()) {
				Object p = proxyIdToProxyMap.get(key);
				if (p != null)
					return key;
			}
		}
		return null;
	}

	public Object removeProxy(String objectId) {
		synchronized (proxyIdToProxyMap) {
			return proxyIdToProxyMap.remove(objectId);
		}
	}

	public void reset() {
		synchronized (proxyIdToProxyMap) {
			proxyIdToProxyMap.clear();
			resetCallbackClient();
		}
	}
}