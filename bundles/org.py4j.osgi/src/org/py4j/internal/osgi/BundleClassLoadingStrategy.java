/*******************************************************************************
 * Copyright (c) 2016 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.py4j.internal.osgi;

import java.util.Arrays;

import org.osgi.framework.Bundle;

import py4j.reflection.ClassLoadingStrategy;

public class BundleClassLoadingStrategy extends ClassLoader implements ClassLoadingStrategy {

	private final Bundle[] loadingBundles;

	public BundleClassLoadingStrategy(Bundle[] loadingBundles) {
		this.loadingBundles = loadingBundles;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		for (Bundle b : loadingBundles) {
			try {
				return b.loadClass(name);
			} catch (ClassNotFoundException e) {

			}
		}
		throw new ClassNotFoundException(
				"Class name=" + name + " cannot be found amnong bundles=" + Arrays.asList(loadingBundles));
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
