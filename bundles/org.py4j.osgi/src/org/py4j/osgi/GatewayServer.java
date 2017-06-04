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
import py4j.GatewayServerListener;
import py4j.Py4JException;
import py4j.Py4JServerConnection;
import py4j.reflection.ClassLoadingStrategy;
import py4j.reflection.ReflectionUtil;

public class GatewayServer {

	private final GatewayConfiguration config;
	private boolean isStarted;
	private py4j.GatewayServer gateway;
	private Collection<GatewayServerListener> listeners = new ArrayList<GatewayServerListener>();
	private ClassLoadingStrategy existingClassLoadingStrategy;

	private final GatewayServerListener gatewayServerListener = new GatewayServerListener() {

		@Override
		public void connectionError(Exception arg0) {
		}

		@Override
		public void connectionStarted(Py4JServerConnection arg0) {
		}

		@Override
		public void connectionStopped(Py4JServerConnection arg0) {
		}

		@Override
		public void serverError(Exception arg0) {
		}

		@Override
		public void serverPostShutdown() {
			if (config.autoRestart())
				restart();
		}

		@Override
		public void serverPreShutdown() {
		}

		@Override
		public void serverStarted() {
		}

		@Override
		public void serverStopped() {
		}
	};

	public GatewayServer(GatewayConfiguration config) {
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
			if (this.gateway == null)
				this.gateway = new py4j.GatewayServer(config.getEntryPoint(), config.getPort(), config.getAddress(),
						config.getConnectTimeout(), config.getReadTimeout(), null,
						new CallbackClient(config.getPythonPort(), config.getPythonAddress(),
								config.getPythonMinConnectionTime(), config.getPythonMinConnectionTimeUnit(),
								config.getPythonSocketFactory(), config.isPythonEnableMemoryManagement()),
						config.getServerSocketFactory());
			this.gateway.addListener(gatewayServerListener);
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

	public boolean restart() {
		synchronized (this) {
			stop();
			return start();
		}
	}

	public boolean stop() {
		synchronized (this) {
			if (!isStarted())
				return false;
			for (GatewayServerListener l : this.listeners)
				this.gateway.removeListener(l);
			this.gateway.removeListener(gatewayServerListener);
			this.listeners.clear();
			if (this.existingClassLoadingStrategy != null)
				ReflectionUtil.setClassLoadingStrategy(this.existingClassLoadingStrategy);
			this.gateway.shutdown();
			this.gateway = null;
			this.isStarted = false;
			return true;
		}
	}

	public GatewayConfiguration getConfiguration() {
		return this.config;
	}

	public void close() {
		stop();
	}
}
