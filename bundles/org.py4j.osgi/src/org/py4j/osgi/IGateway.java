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
