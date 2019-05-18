package com.search.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.search.domain.TreeDetail;
import com.search.exception.DataNotPresentException;
import com.search.exception.SearchException;

@Service
public class SearchService {

	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String URL="https://data.cityofnewyork.us/resource/nwxe-4ae8.json";
	
	@Inject
	private RestTemplate restTemplate;
	
	public Map<String,Integer> getTrees(Double pointX1,Double pointY1,Double radius) throws SearchException, DataNotPresentException{
		List<TreeDetail> treeList = getAllTreeDetails();
		Map<String,Integer> treesInGivenArea= filterList(treeList,pointX1,pointY1,radius);
		return treesInGivenArea;
	}

	private List<TreeDetail> getAllTreeDetails() throws SearchException, DataNotPresentException {
		ResponseEntity<List<TreeDetail>> response = restTemplate.exchange(
				URL,
				HttpMethod.GET, null,
				new ParameterizedTypeReference<List<TreeDetail>>() {
				});
		
		if(response.getStatusCode().is2xxSuccessful()){
			log.debug("Response from the server is success!");
			if(ObjectUtils.isEmpty(response.getBody())){
				throw new DataNotPresentException("Requested data is not Available!");
			}
			return response.getBody();
		}
		throw new SearchException("The site you are trying to reach is not avaialble! Please try again later");
	}
	
	private Map<String,Integer> filterList(List<TreeDetail> details, Double pointX1,Double pointY1,Double radius) {
		Map<String, Integer> treesInArea= new HashMap<String, Integer>();
		for(TreeDetail tree : details){
			Double pointX2= tree.getPointX();
			Double pointY2= tree.getPointY();
			boolean isPresent= checkForTrees(pointX1,pointX2,pointY1,pointY2,radius);
			//If a tree is present in given area add it to map and increment the count
			if(isPresent &&tree.getSpcCommon() !=null){
				if(treesInArea.containsKey(tree.getSpcCommon())){
					treesInArea.put(tree.getSpcCommon(), treesInArea.get(tree.getSpcCommon())+1);
				}else{
					treesInArea.put(tree.getSpcCommon(), 1);
				}
			}
		}
		return treesInArea;
	}
	
	/*
	 * This Method is to calculate the trees with in the given circle
	 * It calculates the distance between two points using pythogorous 
	 * theorem and if it is less than the square of the radius it returns 
	 * false
	 * 
	 */
	private boolean checkForTrees(Double pointX1, Double pointX2, Double pointY1,
			Double pointY2, Double radius) {
		if(Math.pow(pointX1-pointX2, 2)+Math.pow(pointY1-pointY2, 2)<=Math.pow(radius, 2)){
			return true;
		}
		return false;
	}
}
