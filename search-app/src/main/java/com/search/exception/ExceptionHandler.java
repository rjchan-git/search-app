package com.search.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

@Provider
@Component
public class ExceptionHandler implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable exception) {
		SearchError error = new SearchError(exception.getMessage(), 500);
		if(exception instanceof SearchException){
			Response response= Response.status(error.getStatusCode()).entity(error).build();
			return response;
		}
		if(exception instanceof DataNotPresentException){
			SearchError errorMessage = new SearchError(exception.getMessage(), 204);
			Response response= Response.status(errorMessage.getStatusCode()).entity(error).build();
			return response;
		}
		return null;
	}

}
