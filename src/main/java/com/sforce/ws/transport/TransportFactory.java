package com.sforce.ws.transport;

/**
 * Defines an interface for creating Transport instances
 */
public interface TransportFactory {
	public Transport createTransport();
}
