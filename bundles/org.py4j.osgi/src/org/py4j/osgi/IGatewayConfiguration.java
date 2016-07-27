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
