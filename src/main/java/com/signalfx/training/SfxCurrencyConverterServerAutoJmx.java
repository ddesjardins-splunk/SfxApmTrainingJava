package com.signalfx.training;

import java.lang.management.ManagementFactory;

import javax.management.remote.JMXServiceURL;

import org.eclipse.jetty.jmx.ConnectorServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;

public class SfxCurrencyConverterServerAutoJmx extends SfxCurrencyConverterServerAuto {
	 
	
	public static void main(String[] args) throws Exception {
	    	
		 Server server = new Server(8888);
		
		// Setup JMX
		MBeanContainer mbeanContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		server.addBean(mbeanContainer);

		// Setup ConnectorServer
		JMXServiceURL jmxURL = new JMXServiceURL("rmi", null, 1999, "/jndi/rmi:///jmxrmi");
		ConnectorServer jmxServer = new ConnectorServer(jmxURL, "org.eclipse.jetty.jmx:name=rmiconnectorserver");
		server.addBean(jmxServer);
		  
		 
	     server.setHandler(new SfxCurrencyConverterServerAuto());
	     server.start();
	     server.join();	    	
	 }

}
