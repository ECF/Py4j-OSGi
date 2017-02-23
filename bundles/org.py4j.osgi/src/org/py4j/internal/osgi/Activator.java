/*******************************************************************************
 * Copyright (c) 2016 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.py4j.internal.osgi;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.py4j.osgi.GatewayConfiguration;
import org.py4j.osgi.IGateway;
import org.py4j.osgi.IGatewayConfiguration;

public class Activator
		implements BundleActivator, ServiceTrackerCustomizer<IGatewayConfiguration, IGatewayConfiguration> {

	private BundleContext bundleContext;
	private ServiceTracker<IGatewayConfiguration, IGatewayConfiguration> configTracker;
	private ServiceRegistration<IGateway> gwReg;
	private Map<Bundle, GatewayServer> gwServers = new HashMap<Bundle, GatewayServer>();

	@Override
	public void start(BundleContext context) throws Exception {
		// register service factory
		bundleContext = context;
		configTracker = new ServiceTracker<IGatewayConfiguration, IGatewayConfiguration>(context,
				IGatewayConfiguration.class, this);
		configTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (configTracker != null) {
			configTracker.close();
			configTracker = null;
		}
		if (gwReg != null) {
			gwReg.unregister();
			gwReg = null;
		}
		bundleContext = null;
	}

	private Map<ServiceReference<IGatewayConfiguration>, ServiceRegistration<IGateway>> refToRegMap = new HashMap<ServiceReference<IGatewayConfiguration>, ServiceRegistration<IGateway>>();

	GatewayServer getServerForPort(int port) {
		synchronized (gwServers) {
			for (GatewayServer s : gwServers.values()) {
				if (port == s.getConfiguration().getListeningPort())
					return s;
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IGatewayConfiguration addingService(ServiceReference<IGatewayConfiguration> reference) {
		// First get configuration instance
		IGatewayConfiguration config = bundleContext.getService(reference);
		Hashtable props = new Hashtable();
		// Set the IGateway service props. Just the java address (typically
		// 127.0.0.1) and
		// the java port 23333
		InetAddress addr = config.getAddress();
		props.put(IGateway.CONFIG_PROP, config);
		props.put(IGateway.JAVA_ADDRESS_PROP, (addr == null) ? "127.0.0.1" : addr.toString());
		props.put(IGateway.JAVA_PORT_PROP, config.getPort());
		synchronized (refToRegMap) {
			// Register an IGateway instance with these props. The use of a
			// Service Factory
			// will delay the creation and starting of an actual IGateway
			// instance until the
			// client bundle *requests* the service. This is so the client
			// bundle's
			// Bundle can be passed in, and used for the GatewayServer's
			// classloading!
			gwReg = bundleContext.registerService(IGateway.class, new ServiceFactory<IGateway>() {
				@Override
				public IGateway getService(Bundle bundle, ServiceRegistration<IGateway> registration) {
					GatewayServer gwServer = null;
					IGatewayConfiguration config = (IGatewayConfiguration) registration.getReference()
							.getProperty(IGateway.CONFIG_PROP);
					if (config == null)
						config = new GatewayConfiguration.Builder().build();
					synchronized (gwServers) {
						gwServer = gwServers.get(bundle);
						if (gwServer == null && !bundle.getSymbolicName().startsWith("org.eclipse.kura")) {
							GatewayServer gws = getServerForPort(config.getPort());
							if (gws != null)
								gwServer = gws;
							else {
								gwServer = new GatewayServer(bundle, config);
								gwServers.put(bundle, gwServer);
							}
						}
					}
					return gwServer;
				}

				@Override
				public void ungetService(Bundle bundle, ServiceRegistration<IGateway> registration, IGateway service) {
					synchronized (gwServers) {
						GatewayServer gw = gwServers.remove(bundle);
						if (gw != null)
							gw.close();
					}
				}
			}, props);
		}
		return config;
	}

	@Override
	public void modifiedService(ServiceReference<IGatewayConfiguration> reference, IGatewayConfiguration service) {
	}

	@Override
	public void removedService(ServiceReference<IGatewayConfiguration> reference, IGatewayConfiguration service) {
		synchronized (refToRegMap) {
			ServiceRegistration<IGateway> reg = refToRegMap.remove(reference);
			if (reg != null)
				try {
					reg.unregister();
				} catch (Exception e) {
					// XXX log
					e.printStackTrace();
				}
		}
	}

}
