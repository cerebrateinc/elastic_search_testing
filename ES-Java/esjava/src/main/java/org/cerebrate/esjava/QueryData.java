package org.cerebrate.esjava;

import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.client.Client;

public class QueryData {
	private final Client client;

    public QueryData(Client client) {
        this.client = client;
    }
    /*	
	public void query(String index, String type)
	{
        client.admin().indices().prepareRefresh().execute().actionGet();
		ExplainResponse eresponse = client.prepareExplain(index,type,"1")
				.setQ
		
	}*/
	

}
