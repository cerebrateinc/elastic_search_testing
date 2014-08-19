package org.cerebrate.esjava;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;

public class CreateIndex extends ConnectionApi {
	public void create(String json, String index, String type)
	{
	if(existsIndex(index))
		deleteIndex(index);
	createIndexIfNeeded(index);
	PutMappingResponse response=client.admin().indices().preparePutMapping(index).setType(type).setSource(json).execute().actionGet();
	if(!response.isAcknowledged()){
		System.out.println("Something strange happens");
	}
	else 
		System.out.println("Successfully created index");
	OpenIndexResponse oresponse = client.admin().indices()
			.prepareOpen(index)
			.execute().actionGet();
	System.out.println("Index Open Ack: " + oresponse.isAcknowledged());
	}
}
