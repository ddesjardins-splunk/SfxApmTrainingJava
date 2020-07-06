package com.signalfx.training;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.jmx.ConnectorServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;

public class SfxCurrencyConverterServerAutoJmx extends SfxCurrencyConverterServerAuto {
	
	public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
	
		if ( target.equals("/deadlock") ) {
			DeadLockTest.createDeadlock();
		}else {
			super.handle(target, baseRequest, request, response);
		}
	}
	
	public static void main(String[] args) throws Exception {
	    	
		Server server = new Server(8888);
		
		// Setup JMX
		MBeanContainer mbeanContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		server.addBean(mbeanContainer);

		// Setup ConnectorServer
		JMXServiceURL jmxURL = new JMXServiceURL("rmi", null, 1999, "/jndi/rmi:///jmxrmi");
		ConnectorServer jmxServer = new ConnectorServer(jmxURL, "org.eclipse.jetty.jmx:name=rmiconnectorserver");
		server.addBean(jmxServer);
		  
		 
	    server.setHandler(new SfxCurrencyConverterServerAutoJmx());
	    server.start();
	    server.join();	    	
	 }

}
