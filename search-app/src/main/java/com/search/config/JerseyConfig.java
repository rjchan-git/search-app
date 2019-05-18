package com.search.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.search.exception.ExceptionHandler;
import com.search.resource.SearchReource;
 
@Component
public class JerseyConfig extends ResourceConfig 
{
    public JerseyConfig() 
    {
        register(SearchReource.class);
        register(ExceptionHandler.class);
    }
}
