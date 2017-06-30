package org.py4j.osgi;

import java.util.concurrent.ConcurrentHashMap;

import py4j.Gateway;
import py4j.Py4JPythonClient;

public class OSGiGateway extends Gateway {

	public OSGiGateway(Object entryPoint, Py4JPythonClient cbClient) {
		super(entryPoint, cbClient);
	}

	private ConcurrentHashMap<String, Object> proxyIdToProxyMap = new ConcurrentHashMap<String, Object>();

	@SuppressWarnings("rawtypes")
	public Object createProxy(ClassLoader cl, Class[] ca, String objectId) {
		Object result = super.createProxy(cl, ca, objectId);
		proxyIdToProxyMap.put(objectId, result);
		return result;
	}

	public Object getProxy(String objectId) {
		return proxyIdToProxyMap.get(objectId);
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
		return proxyIdToProxyMap.remove(objectId);
	}

	public void clearProxies() {
		proxyIdToProxyMap.clear();
	}
}