package org.py4j.osgi;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ServerSocketFactory;

import py4j.GatewayServerListener;
import py4j.commands.Command;

public class GatewayConfiguration implements IGatewayConfiguration {

	protected Object entryPoint = null;
	protected InetAddress address = py4j.GatewayServer.defaultAddress();
	protected int port = py4j.GatewayServer.DEFAULT_PORT;
	protected int pythonPort = py4j.GatewayServer.DEFAULT_PYTHON_PORT;
	protected InetAddress pythonAddress = py4j.GatewayServer.defaultAddress();
	protected int listeningPort = py4j.GatewayServer.DEFAULT_PORT;
	protected ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
	protected boolean useLoadingBundleClassLoadingStrategy = false;
	protected boolean autoStart = true;
	protected Collection<GatewayServerListener> gatewayServerListeners = new ArrayList<GatewayServerListener>();
	protected List<Class<? extends Command>> commands = null;
	protected boolean forkOnStart = true;
	protected boolean autoRestart = true;
	protected int readTimeout = py4j.GatewayServer.DEFAULT_READ_TIMEOUT;
	protected int connectTimeout = py4j.GatewayServer.DEFAULT_CONNECT_TIMEOUT;

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

		public Builder setListeningPort(int listeningPort) {
			c.listeningPort = listeningPort;
			return this;
		}

		public Builder setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
			c.serverSocketFactory = serverSocketFactory;
			return this;
		}

		public Builder setUseLoadingBundleClassLoadingStrategy(boolean useLoadingBundleClassLoadingStrategy) {
			c.useLoadingBundleClassLoadingStrategy = useLoadingBundleClassLoadingStrategy;
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
	}

	public GatewayConfiguration() {
	}

	@Override
	public Object getEntryPoint() {
		return entryPoint;
	}

	@Override
	public InetAddress getAddress() {
		return address;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public int getListeningPort() {
		return listeningPort;
	}

	@Override
	public int getConnectTimeout() {
		return connectTimeout;
	}

	@Override
	public int getReadTimeout() {
		return readTimeout;
	}

	@Override
	public InetAddress getPythonAddress() {
		return pythonAddress;
	}

	@Override
	public int getPythonPort() {
		return pythonPort;
	}

	@Override
	public ServerSocketFactory getServerSocketFactory() {
		return serverSocketFactory;
	}

	@Override
	public boolean useLoadingBundleClassLoadingStrategy() {
		return useLoadingBundleClassLoadingStrategy;
	}

	@Override
	public boolean autoStart() {
		return autoStart;
	}

	@Override
	public Collection<GatewayServerListener> getGatewayServerListeners() {
		return gatewayServerListeners;
	}

	@Override
	public List<Class<? extends Command>> getCommands() {
		return commands;
	}

	@Override
	public boolean forkOnStart() {
		return forkOnStart;
	}

	@Override
	public boolean autoRestart() {
		return autoRestart;
	}

}
