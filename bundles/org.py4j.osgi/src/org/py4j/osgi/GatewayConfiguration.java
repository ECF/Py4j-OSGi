/*******************************************************************************
 * Copyright (c) 2016 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.py4j.osgi;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import org.osgi.framework.Bundle;

import py4j.GatewayServerListener;
import py4j.commands.Command;

public class GatewayConfiguration {

	protected Object entryPoint = null;
	protected InetAddress address = py4j.GatewayServer.defaultAddress();
	protected int port = py4j.GatewayServer.DEFAULT_PORT;
	protected int pythonPort = py4j.GatewayServer.DEFAULT_PYTHON_PORT;
	protected InetAddress pythonAddress = py4j.GatewayServer.defaultAddress();

	protected long pythonMinConnectionTime = 0;
	protected TimeUnit pythonMinConnectionTimeUnit = TimeUnit.SECONDS;
	protected SocketFactory pythonSocketFactory = SocketFactory.getDefault();
	protected boolean pythonEnableMemoryManagement = true;

	protected int listeningPort = py4j.GatewayServer.DEFAULT_PORT;
	protected ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();

	protected Bundle[] classLoadingStrategyBundles = null;
	protected boolean autoStart = true;
	protected Collection<GatewayServerListener> gatewayServerListeners = new ArrayList<GatewayServerListener>();
	protected List<Class<? extends Command>> commands = null;
	protected boolean forkOnStart = true;
	protected boolean autoRestart = true;
	protected int readTimeout = py4j.GatewayServer.DEFAULT_READ_TIMEOUT;
	protected int connectTimeout = py4j.GatewayServer.DEFAULT_CONNECT_TIMEOUT;
	// XXX by default we'll have debug on...for now
	protected boolean debugOn = true;

	public static class Builder {

		private GatewayConfiguration c;

		public GatewayConfiguration build() {
			return c;
		}

		public Builder() {
			this.c = new GatewayConfiguration();
		}

		public Builder(Object entryPoint) {
			this();
			setEntryPoint(entryPoint);
		}

		public Builder setEntryPoint(Object entryPoint) {
			c.entryPoint = entryPoint;
			return this;
		}

		public Builder setAddress(InetAddress address) {
			c.address = address;
			return this;
		}

		public Builder setPort(int port) {
			c.port = port;
			return this;
		}

		public Builder setPythonPort(int pythonPort) {
			c.pythonPort = pythonPort;
			return this;
		}

		public Builder setPythonAddress(InetAddress pythonAddress) {
			c.pythonAddress = pythonAddress;
			return this;
		}

		public Builder setPythonMinConnectionTime(long pythonMinConnectionTime) {
			c.pythonMinConnectionTime = pythonMinConnectionTime;
			return this;
		}

		public Builder setPythonMinConnectionTimeUnit(TimeUnit pythonMinConnectionTimeUnit) {
			c.pythonMinConnectionTimeUnit = pythonMinConnectionTimeUnit;
			return this;
		}

		public Builder setPythonSocketFactory(SocketFactory pythonSocketFactory) {
			c.pythonSocketFactory = pythonSocketFactory;
			return this;
		}

		public Builder setPythonEnableMemoryManagement(boolean pythonEnableMemoryManagement) {
			c.pythonEnableMemoryManagement = pythonEnableMemoryManagement;
			return this;
		}

		public Builder setListeningPort(int listeningPort) {
			c.listeningPort = listeningPort;
			return this;
		}

		public Builder setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
			c.serverSocketFactory = serverSocketFactory;
			return this;
		}

		public Builder setClassLoadingStrategyBundles(Bundle[] bundles) {
			c.classLoadingStrategyBundles = bundles;
			return this;
		}

		public Builder setAutoStart(boolean autoStart) {
			c.autoStart = autoStart;
			return this;
		}

		public Builder addGatewayServerListener(GatewayServerListener gatewayServerListener) {
			c.gatewayServerListeners.add(gatewayServerListener);
			return this;
		}

		public Builder setCommands(List<Class<? extends Command>> commands) {
			c.commands = commands;
			return this;
		}

		public Builder setForkOnStart(boolean forkOnStart) {
			c.forkOnStart = forkOnStart;
			return this;
		}

		public Builder setAutoRestart(boolean autoRestart) {
			c.autoRestart = autoRestart;
			return this;
		}

		public Builder setReadTimeout(int readTimeout) {
			c.readTimeout = readTimeout;
			return this;
		}

		public Builder setConnectTimeout(int connectTimeout) {
			c.connectTimeout = connectTimeout;
			return this;
		}

		public Builder setDebug(boolean debug) {
			c.debugOn = debug;
			return this;
		}
	}

	public GatewayConfiguration() {
	}

	public Object getEntryPoint() {
		return entryPoint;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public int getListeningPort() {
		return listeningPort;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public InetAddress getPythonAddress() {
		return pythonAddress;
	}

	public int getPythonPort() {
		return pythonPort;
	}

	public ServerSocketFactory getServerSocketFactory() {
		return serverSocketFactory;
	}

	public Bundle[] getClassLoadingStrategyBundles() {
		return classLoadingStrategyBundles;
	}

	public boolean autoStart() {
		return autoStart;
	}

	public Collection<GatewayServerListener> getGatewayServerListeners() {
		return gatewayServerListeners;
	}

	public List<Class<? extends Command>> getCommands() {
		return commands;
	}

	public boolean forkOnStart() {
		return forkOnStart;
	}

	public boolean autoRestart() {
		return autoRestart;
	}

	public boolean debugOn() {
		return debugOn;
	}

	public long getPythonMinConnectionTime() {
		return pythonMinConnectionTime;
	}

	public TimeUnit getPythonMinConnectionTimeUnit() {
		return pythonMinConnectionTimeUnit;
	}

	public SocketFactory getPythonSocketFactory() {
		return pythonSocketFactory;
	}

	public boolean isPythonEnableMemoryManagement() {
		return pythonEnableMemoryManagement;
	}

}
