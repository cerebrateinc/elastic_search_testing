package org.cer.api;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;

import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class MappingOperations {

	public static void main( String[] args ) throws ElasticsearchException, IOException
	{
		String index="venus";
		String type="news";
		AbstractApi app = new AbstractApi();
		Client client = app.getClient();
		IndicesOperations io=new IndicesOperations(client);
		if(io.checkIndexExists(index))
			io.deleteIndex(index);
		io.createIndex(index);

		// Mapping using XcontentBuilder
		XContentBuilder builder = null;
		try {
			builder = jsonBuilder().
					startObject()
					.startObject(type)
					.startObject("properties")
					.startObject("title")
					//.field("analyzer", "whitespace")
					.field("type", "string")
					.field("store", "yes")
					.endObject()
					.startObject("cibi_id")
					.field("type", "integer")
					.field("store", "yes")
					.endObject()
					.startObject("descr")
					.field("type", "string")
					.field("store", "yes")
					.startObject("value")
					.field("type", "integer")
					.field("store", "yes")
					.endObject()
					.endObject()
					.endObject()
					.endObject()
					.endObject();
			System.out.println(builder.string());
			PutMappingResponse response=client.admin().indices().preparePutMapping(index).setType(type).setSource(builder).execute().actionGet();
			if(!response.isAcknowledged()){
				System.out.println("Something strange happens");
			}
		} catch (IOException e) {
			System.out.println("Unable to create mapping");
		}

		// Open response of the index
		OpenIndexResponse oresponse = client.admin().indices()
				.prepareOpen(index)
				.execute().actionGet();
		System.out.println("Index Open Ack: " + oresponse.isAcknowledged());
		XContentBuilder builde = null;
		XContentBuilder build1 = null;
		XContentBuilder build2 = null;

		builde = XContentFactory.jsonBuilder()				
				.startObject()
				.field("title", "Book A Title")
				.field("cibi_id", 1000110)
				.field("descr", "Venusssssss")
				.field("value", 100)
				.endObject();
		build1 = XContentFactory.jsonBuilder()
				.startObject()
				.field("title", "Book B Title")
				.field("cibi_id", 1000100)
				.field("descr", "drdrdrdr")
				.field("value", 102)
				.endObject();
		build2 = XContentFactory.jsonBuilder()
				.startObject()
				.field("title", "Book D Title")
				.field("cibi_id", 1000100)
				.field("descr", "hahahahahaha")
				.field("value", 104)
				.endObject();
		System.out.println(builde.string()+"    "+build1.string());

		client.prepareBulk()
		.add(new IndexRequestBuilder(client, index).setId("1").setType(type).setSource(
				builde
				).request())
				.add(new IndexRequestBuilder(client, index).setType(type).setSource(
				build1		
					).request()).add(new IndexRequestBuilder(client, index).setType(type).setSource(
							build2	
							).request())
						.execute().actionGet();
		System.out.println("Am in abstarctAPI");


		
		GetResponse gresponse = client.prepareGet(index,type,"1")
				.setFields("title", "cibi_id","descr")
				.execute().actionGet();				
		if (gresponse.isExists()) {
			GetField field1 = gresponse.getField("title");
			GetField field2 = gresponse.getField("cibi_id");
			GetField field3 = gresponse.getField("descr");

			System.out.println("Document: " + gresponse.getIndex() 
					+ "/" + gresponse.getType() 
					+ "/" + gresponse.getId());
			System.out.println("Title: " + field1.getValue());
			System.out.println("cibi_id: " + field2.getValue());
			System.out.println("descr: " + field3.getValue());
		} else {
			System.out.println("Document not found.");
		}

		// Term Query
        client.admin().indices().prepareRefresh().execute().actionGet();
        TermQueryBuilder qb = QueryBuilders.termQuery("cibi_id", 1000100);
		ExplainResponse eresponse = client.prepareExplain(index,type,"1")
				.setQuery(qb)
				.execute()
				.actionGet();
		if (eresponse.isExists()) {
			System.out.println(eresponse.getExplanation().getDescription());
		}


		// SORTING AND LIMITING WITH term query
        client.admin().indices().prepareRefresh().execute().actionGet();
		SearchResponse presponse = client.prepareSearch(index)
				.setTypes(type)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.termQuery("cibi_id", 1000100))
				.setFrom(0).setSize(2).setExplain(true)
				.addSort("value", SortOrder.DESC)
				// .addSort(new ScriptSortBuilder("doc['value'].value", "integer"))
			    .setTrackScores(true)              // Making this don't reflect the actual order in database 
				.execute()
				.actionGet();

		SearchHit[] results = presponse.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String,Object> result = hit.getSource();   
			System.out.println(result);
		}


		// Term query using facets
        client.admin().indices().prepareRefresh().execute().actionGet();
		FacetBuilder facetBuilder = FacetBuilders
				.filterFacet("test")
				.filter(FilterBuilders.termFilter("cibi_id",1000100));
		SearchResponse sresponse = client.prepareSearch(index)
				.addFacet(facetBuilder)
				.execute().actionGet();
		SearchHit[] results2 = sresponse.getHits().getHits();
		System.out.println(facetBuilder.toString());
		System.out.println("Current results: " + results2.length);

		for (SearchHit hit : results2) {
			System.out.println("------------------------------");
			Map<String,Object> result = hit.getSource();   
			System.out.println(result);
		}

        client.admin().indices().prepareRefresh().execute().actionGet();
		FilterBuilder filterBuilder =
				FilterBuilders.termFilter("cibi_id", 1000100);
		System.out.println(filterBuilder.toString());
		
		SearchResponse dresponse = client.prepareSearch(index)
				.setPostFilter(filterBuilder)
				.execute().actionGet();
		
		SearchHit[] results1 = dresponse.getHits().getHits();
		System.out.println("Current results: " + results1.length);

		for (SearchHit hit : results1) {
			System.out.println("------------------------------");
			Map<String,Object> result = hit.getSource();   
			System.out.println(result);
		}
		
		
	         							
		String ss = "{"+"term"+":"+ "{"+"cibi_id"+":"+" 1000100"+"}"+"}";
		
		XContentBuilder cB = XContentFactory.jsonBuilder().startObject()
				.startObject("term");
		cB.field("cibi_id",1000100).endObject().endObject();
		
		
		TermQueryBuilder qb1 = QueryBuilders.termQuery("cibi_id", 1000100);

		SumBuilder ab = sum("sum").script("doc['cibi_id'].value-doc['value'].value");
		
		// Aggregations with sum and termquery
		XContentBuilder contentBuilder = XContentFactory.jsonBuilder()
				.startObject("aggs").startObject("memory_average")
				.startObject("avg").field("field", "MEMORY").endObject()
				.endObject().endObject();
		
		
		System.out.println("It can be as"+qb1.toString());
		
		System.out.println("It can also be be as "+ ab);
		SearchResponse searchResponse = client.prepareSearch(index)
				.setQuery(cB)
                .addAggregation(ab)
                .execute().actionGet();
		SearchHit[] results5 = searchResponse.getHits().getHits();
		Sum sum = searchResponse.getAggregations().get("sum");
		for (SearchHit hit : results5) {
			System.out.println("----------jhgdsjahdjs--------------------");
			Map<String,Object> result = hit.getSource();   
			System.out.println(result);
		}
		System.out.println("Current results: " + sum.getValue());
		
		// Multi search query
		SearchRequestBuilder srb1 = client.prepareSearch(index)
				.setQuery(matchAllQuery());
			SearchRequestBuilder srb2 =client.prepareSearch(index)
			    .setQuery(QueryBuilders.matchQuery("descr", "Venusssssss"));

			MultiSearchResponse sr = client.prepareMultiSearch()
			        .add(srb1)
			        .add(srb2)
			        .execute().actionGet();
			
			long nbHits = 0;
			for (MultiSearchResponse.Item item : sr.getResponses()) {
			    SearchResponse res = item.getResponse();
			    SearchHit[] resultssss=res.getHits().getHits();
			    for (SearchHit hit : resultssss) {
					System.out.println("----------hereee--------------------");
					Map<String,Object> result = hit.getSource();   
					System.out.println(result);
				}
			    nbHits += res.getHits().getTotalHits();
			}
			System.out.println("Current Hitsss: " +nbHits);
			
		//String ss = 
		
			// Example of building Json input using XcomtentBuilder
			 XContentBuilder xb =  XContentFactory.jsonBuilder().startObject();

			    xb.startArray("eventnested");
			    for(int j=0;j<2;j++)
			    {
			        xb.startObject();
			        xb.field("event_type", "eventType");
			        xb.field("event_attribute_instance", "eventInstance");
			        xb.startArray("attributes");
			        for(int i=0;i<2;i++)
			        {
			            //xb.startObject();
			            xb.field("event_attribute_name", "attrName");
			            xb.field("event_attribute_value", "attrValue");
			            //xb.endObject();
			        }
			        xb.endArray();
			        xb.endObject();
			    }
			    xb.endArray();
			    
			    System.out.println(xb.string());	
			    
		// Aggregations with operations on sub-aggregations.
		SearchResponse srr = client.prepareSearch(index).setTypes(type)
				.setQuery(matchAllQuery())
				//.setFrom(0).setSize(0)
                .addAggregation(terms("cibi_id").field("cibi_id")	
                .subAggregation(sum("sum").script("doc['value'].value")).order(Terms.Order.count(true)))// true=ASC    
                .execute().actionGet();
		Terms  terms = srr.getAggregations().get("cibi_id");
		Collection<Terms.Bucket> buckets = terms.getBuckets();	
		System.out.println(buckets.size());
		 for (Terms.Bucket elem : buckets) {
		        Sum sum1 = elem.getAggregations().get("sum");
		        System.out.println("sum = " + sum1.getValue());
		        System.out.println("elem = " + elem.getKeyAsText().toString());
		        System.out.println("doc_count = " + elem.getDocCount());
		    }
		 
		 QueryBuilder qb11 = null;
	        // create the query
	        qb11 = QueryBuilders
	        		.disMaxQuery()
	                .add(QueryBuilders.termQuery("brand","heineken"))
	                .add(QueryBuilders.termQuery("colour","pale"))
	                .boost(1.2f)
	                .tieBreaker(0.7f);

	        System.out.println("Your query is: "+ qb11);		 
		
		client.admin().indices().prepareDeleteMapping(index).setType(type).execute().actionGet();
		io.deleteIndex(index);
	}
}



