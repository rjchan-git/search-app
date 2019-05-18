package com.search.resource;

import java.util.Map;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.search.exception.DataNotPresentException;
import com.search.exception.SearchException;
import com.search.service.SearchService;

@Path("/search")
@Component
public class SearchReource {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Inject
	private SearchService searchService;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/test")
	public String searchResourcse(){
		return "I am in search resource!";
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers(@QueryParam("x") Double pointX,
			@QueryParam("y") Double pointY, @QueryParam("radius") Double radius)
			throws SearchException, DataNotPresentException {
		log.info("Serach point is : ("+pointX +"," +pointY +") " + "and radius is :" + radius );
		Map<String, Integer> treesinGivenArea = searchService.getTrees(pointX,
				pointY, radius);
		if (treesinGivenArea.isEmpty()) {
			return Response.status(200)
					.entity("No trees present in the given Area!").build();
		}
		return Response.status(200).entity(treesinGivenArea).build();
	}

}
