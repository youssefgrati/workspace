package com.tcb.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcb.model.Dataset;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ElasticSearchDatasetsRepository implements DatasetRepository {

    private final Client client;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ElasticSearchDatasetsRepository(@NonNull Client client) {
        this.client = client;
    }

//    @Override
//    public void save(List<Event> events) {
//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        events.forEach(event -> bulkRequest.add(prepareIndex(event)));
//        BulkResponse bulkResponse = bulkRequest.get();
//
//        if (bulkResponse.hasFailures()) {
////            LOGGER.error("Failed to save");
//        	System.out.println("failed");
//        }
//    }

//    private IndexRequestBuilder prepareIndex(Event event) {
//        try {
//            return client.prepareIndex("events", "event", event.getId()).setSource(objectMapper.writeValueAsBytes(event));
//        } catch (JsonProcessingException e) {
////            LOGGER.error("Error when parsing event {}", event.toString(), e);
//        System.out.println("error");
//        	return null;
//        }
//    }

//	@Override
//	public List<Dataset> findDatasetByAuthor(String author) {
//		// TODO Auto-generated method stub
//		return null;
//	}

@Override
    public List<Dataset> findDatasetByName(String name) {
        
        
        List<Dataset> test = new ArrayList<>();
        
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest("datasets");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", name));
        searchRequest.source(searchSourceBuilder);
        try {
        SearchResponse searchResponse = client.search(searchRequest).get();
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        test = Arrays.asList(searchHits).stream() //
                .map(h -> {

                    String source = h.getSourceAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    Dataset data = new Dataset();
                    try {
                        data = mapper.readValue(source, Dataset.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    data.setId(h.getId());
                    return data;
                }) //
                .collect(Collectors.toList());
        
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            searchResponse = client.searchScroll(scrollRequest).get();
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
            
            List<Dataset> test1 = Arrays.asList(searchHits).stream() //
            .map(h -> {

                String source = h.getSourceAsString();
                ObjectMapper mapper = new ObjectMapper();
                Dataset data = new Dataset();
                
                    try {
                        data = mapper.readValue(source, Dataset.class);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            
                data.setId(h.getId());
                return data;
            }) //
            .collect(Collectors.toList());
            test.addAll(test1);

        }

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse;
        
            clearScrollResponse = client.clearScroll(clearScrollRequest).get();
        
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return test;
        
    }

	@Override
	public List<Dataset> findAll() {
		List<Dataset> test = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("datasets");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchRequest.source(searchSourceBuilder);
		
		try {
			SearchResponse searchResponse =   client
					.search(searchRequest).get();
			test = 
					Arrays.asList(searchResponse.getHits().getHits()).stream() //
					.map(h ->
					{Dataset data= new Dataset();
					String dataasString=h.getSourceAsString();
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						data=	objectMapper.readValue(dataasString, Dataset.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					data.setId(h.getId());
							return data;
					}
					
					) //
					.collect(Collectors.toList());
					
			
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return test ; 
	}


}
