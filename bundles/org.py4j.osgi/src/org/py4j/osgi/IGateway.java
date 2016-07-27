/*******************************************************************************
 * Copyright (c) 2016 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.py4j.osgi;

public interface IGateway {

	static final String CONFIG_PROP = "org.py4j.osgi.config";
	static final String JAVA_PORT_PROP = "org.py4j.osgi.javaPort";
	static final String JAVA_ADDRESS_PROP = "org.py4j.osgi.javaAddress";

	IGatewayConfiguration getConfiguration();

	boolean isStarted();

	boolean start();

	boolean stop();

	boolean restart();
}
