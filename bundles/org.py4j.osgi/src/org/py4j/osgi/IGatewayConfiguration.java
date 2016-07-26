package org.py4j.osgi;

import java.net.InetAddress;
import java.util.Collection;
import java.util.List;

import javax.net.ServerSocketFactory;

import py4j.GatewayServerListener;
import py4j.commands.Command;

public interface IGatewayConfiguration {

	Object getEntryPoint();

	InetAddress getAddress();

	int getPort();

	InetAddress getPythonAddress();

	int getPythonPort();

	int getListeningPort();

	int getConnectTimeout();

	int getReadTimeout();

	ServerSocketFactory getServerSocketFactory();

	boolean useLoadingBundleClassLoadingStrategy();

	boolean autoStart();

	Collection<GatewayServerListener> getGatewayServerListeners();

	List<Class<? extends Command>> getCommands();

	boolean forkOnStart();

	boolean autoRestart();
	
	boolean debugOn();
}
