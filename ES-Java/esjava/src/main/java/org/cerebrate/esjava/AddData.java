package org.cerebrate.esjava;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.get.GetField;

public class AddData  {
	private final Client client;

    public AddData(Client client) {
        this.client = client;
    }
    
	public void add(String json1,String json2,String type, String index)
	{
		client.prepareBulk()
		.add(new IndexRequestBuilder(client, index).setId("1").setType(type).setSource(
				json1
				).request()).execute().actionGet();
		GetResponse gresponse = client.prepareGet(index,type,"1")
				.setFields("title", "cibi_id","descr")
				.execute().actionGet();				
		if (gresponse.isExists()) {
			GetField field1 = gresponse.getField("title");
			GetField field2 = gresponse.getField("cibi_id");
			GetField field3 = gresponse.getField("descr");

			/*System.out.println("Document: " + gresponse.getIndex() 
					+ "/" + gresponse.getType() 
					+ "/" + gresponse.getId());
			System.out.println("Title: " + field1.getValue());
			System.out.println("cibi_id: " + field2.getValue());
			System.out.println("descr: " + field3.getValue());*/
		} else {
			System.out.println("Document not found.");
		}
	}
}
