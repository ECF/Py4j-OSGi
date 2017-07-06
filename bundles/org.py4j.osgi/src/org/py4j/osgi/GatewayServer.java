/*******************************************************************************
 * Copyright (c) 2016 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.py4j.osgi;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.Bundle;

import py4j.CallbackClient;
import py4j.Gateway;
import py4j.GatewayServerListener;
import py4j.Py4JException;
import py4j.reflection.ClassLoadingStrategy;
import py4j.reflection.ReflectionUtil;

public class GatewayServer {

	private final GatewayServerConfiguration config;
	private boolean isStarted;
	private py4j.GatewayServer gateway;
	private Collection<GatewayServerListener> listeners = new ArrayList<GatewayServerListener>();
	private ClassLoadingStrategy existingClassLoadingStrategy;

	public GatewayServer(GatewayServerConfiguration config) {
		this.config = config;
		if (config.autoStart())
			start();
	}

	public boolean isStarted() {
		return isStarted;
	}

	public boolean start() throws Py4JException {
		synchronized (this) {
			if (isStarted())
				return false;
			if (config.debugOn())
				py4j.GatewayServer.turnAllLoggingOn();

			Gateway gw = config.getGateway();
			if (gw == null) {
				gw = new OSGiGateway(config.getEntryPoint(),
						new CallbackClient(config.getPythonPort(), config.getPythonAddress(),
								config.getPythonMinConnectionTime(), config.getPythonMinConnectionTimeUnit(),
								config.getPythonSocketFactory(), config.isPythonEnableMemoryManagement()));
			}
			this.gateway = new py4j.GatewayServer(gw, config.getPort(), config.getAddress(), config.getConnectTimeout(),
					config.getReadTimeout(), config.getCommands(), config.getServerSocketFactory());
			Collection<GatewayServerListener> ls = config.getGatewayServerListeners();
			if (ls != null)
				this.listeners.addAll(ls);
			for (GatewayServerListener l : this.listeners)
				this.gateway.addListener(l);
			// XXX this should be done on a GatewayServer-specific basis rather than
			// globally with each
			// Gateway Server as this code assumes that there is only <b>one</b>
			// gateway server
			this.existingClassLoadingStrategy = ReflectionUtil.getClassLoadingStrategy();
			Bundle[] b = config.getClassLoadingStrategyBundles();
			if (b != null)
				ReflectionUtil.setClassLoadingStrategy(new BundleClassLoadingStrategy(b));
			this.gateway.start(this.config.forkOnStart());
			this.isStarted = true;
			return true;
		}
	}

	public void shutdown() {
		synchronized (this) {
			for (GatewayServerListener l : this.listeners)
				this.gateway.removeListener(l);
			this.listeners.clear();
			if (this.existingClassLoadingStrategy != null)
				ReflectionUtil.setClassLoadingStrategy(this.existingClassLoadingStrategy);
			this.gateway.shutdown();
			this.gateway = null;
			this.isStarted = false;
		}
	}

	public OSGiGateway getOSGiGateway() {
		return (OSGiGateway) ((this.gateway == null) ? null : this.gateway.getGateway());
	}

	public GatewayServerConfiguration getConfiguration() {
		return this.config;
	}

}
