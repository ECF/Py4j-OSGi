package org.py4j.internal.osgi;

import org.osgi.framework.Bundle;

import py4j.reflection.ClassLoadingStrategy;

public class BundleClassLoadingStrategy extends ClassLoader implements ClassLoadingStrategy {

	private final Bundle loadingBundle;

	public BundleClassLoadingStrategy(Bundle loadingBundle) {
		this.loadingBundle = loadingBundle;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return loadingBundle.loadClass(name);
	}

	@Override
	public Class<?> classForName(String arg0) throws ClassNotFoundException {
		return loadClass(arg0);
	}

	@Override
	public ClassLoader getClassLoader() {
		return this;
	}

}
