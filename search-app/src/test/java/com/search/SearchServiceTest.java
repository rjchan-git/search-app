package com.search;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.search.domain.TreeDetail;
import com.search.exception.DataNotPresentException;
import com.search.exception.SearchException;
import com.search.service.SearchService;

@RunWith( SpringRunner.class )
@ContextConfiguration
public class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private RestTemplate restTemplate;

    @Rule
    public ExpectedException expect = ExpectedException.none();

    @Test
    public void testSearchTreesWhenNoData() throws SearchException, DataNotPresentException {

        Double pointX1 = 1d;
        Double pointY1 = 2d;
        Double radius = 12345678d;

        TreeDetail treeDetail = new TreeDetail();
        ResponseEntity<List<TreeDetail>> myEntity = new ResponseEntity<>( HttpStatus.valueOf(200) );
        getMockedResponseEntity( myEntity );

        expect.expect( DataNotPresentException.class );
        expect.expectMessage( "Requested data is not Available!" );

        searchService.getTrees( pointX1, pointY1, radius );

    }

    @SuppressWarnings("unchecked")
	@Test
    public void testSearchTreesWhenSiteisDown() throws SearchException, DataNotPresentException {

        Double pointX1 = 1d;
        Double pointY1 = 2d;
        Double radius = 12345678d;

        TreeDetail treeDetail = new TreeDetail();
        ResponseEntity<List<TreeDetail>> myEntity = new ResponseEntity<>( HttpStatus.valueOf(500) );
        Mockito.when( restTemplate.exchange(
            Matchers.eq( "https://data.cityofnewyork.us/resource/nwxe-4ae8.json" ),
            Matchers.eq( HttpMethod.GET ),
            Matchers.<HttpEntity<List<TreeDetail>>>any(),
            Matchers.<ParameterizedTypeReference<List<TreeDetail>>>any() ) ).thenReturn(myEntity);

        expect.expect( SearchException.class );
        searchService.getTrees( pointX1, pointY1, radius );

    }

    @Test
    public void testGetTressService() throws SearchException, DataNotPresentException {

        Double pointX1 = 1d;
        Double pointY1 = 2d;
        Double radius = 12345678d;
        List<TreeDetail> treeDetail = buildTreeDetail();
        ResponseEntity<List<TreeDetail>> myEntity = new ResponseEntity<>( treeDetail, HttpStatus.valueOf(200) );
        getMockedResponseEntity( myEntity );

        Map<String, Integer> treeCountMap = searchService.getTrees( pointX1, pointY1, radius );
        assertThat( treeCountMap.size(), is( 1 ) );
        assertThat( treeCountMap, IsMapContaining.hasEntry( "red maple", 1 ) );

    }

    @Test
    public void testGetTressWithNoDataReturned() throws SearchException, DataNotPresentException {

        Double pointX1 = 1d;
        Double pointY1 = 5d;
        Double radius = 1d;
        List<TreeDetail> treeDetail = buildTreeDetail();
        ResponseEntity<List<TreeDetail>> myEntity = new ResponseEntity<>( treeDetail, HttpStatus.valueOf(200) );
        getMockedResponseEntity( myEntity );

        Map<String, Integer> treeCountMap = searchService.getTrees( pointX1, pointY1, radius );
        assertThat( treeCountMap.size(), is( 0 ) );

    }

    private void getMockedResponseEntity( ResponseEntity<List<TreeDetail>> myEntity ) {
        Mockito.when( restTemplate.exchange(
            Matchers.eq( "https://data.cityofnewyork.us/resource/nwxe-4ae8.json" ),
            Matchers.eq( HttpMethod.GET ),
            Matchers.<HttpEntity<List<TreeDetail>>>any(),
            Matchers.<ParameterizedTypeReference<List<TreeDetail>>>any() ) ).thenReturn( myEntity );
    }

    private List<TreeDetail> buildTreeDetail() {
        TreeDetail treeDetail = new TreeDetail();
        treeDetail.setAddress( "108-005 70 AVENUE" );
        treeDetail.setBbl( "4022210001" );
        treeDetail.setBin( "4052307" );
        treeDetail.setBlockId( "348711" );
        treeDetail.setSpcCommon( "red maple" );
        treeDetail.setPointX( 1027431.148d );
        treeDetail.setPointY( 202756.7687d );
        return Arrays.asList( treeDetail );
    }
}