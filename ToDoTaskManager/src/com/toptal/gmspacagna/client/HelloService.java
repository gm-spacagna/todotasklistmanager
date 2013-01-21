package com.toptal.gmspacagna.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

public interface HelloService extends RestService {

	@GET
	@Path("/hello")
	public void hello(MethodCallback<String> message);
		
}
