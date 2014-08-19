package org.cerebrate.esjava;
import java.util.logging.Logger;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class ConnectionApi {
	protected static final Logger LOG = Logger.getLogger("TEST");

	protected Client client;
	protected Node node;

	/**
	 * Returns ElasticSearch client.
	 * @return client
	 */
	public Client getClient() {
		if (this.client != null) {
			return this.client;
		}

		NodeBuilder builder = NodeBuilder.nodeBuilder();
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("gateway.type", "local")
				.put("client.transport.sniff", true)
				.build();
		builder.settings(settings).local(true).data(true);

		this.node = builder.node();
		this.client = node.client();

		return this.client;
	}

	
	public void createTransportClient (final String name)
	{
		createLocalCluster(name);

		//Create the configuration - you can omit this step and use 
		//non-argument constructor of TransportClient
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", name).build();

		//Create the transport client instance
		TransportClient client = new TransportClient(settings);

		//add some addresses of ElasticSearch cluster nodes
		client.addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
	}
	
	public void createLocalCluster(final String clusterName) {
		NodeBuilder builder = NodeBuilder.nodeBuilder();

		Settings settings = ImmutableSettings.settingsBuilder()
				.put("gateway.type", "local")
				.put("cluster.name", clusterName)
				.build();

		builder.settings(settings).local(true).data(true);

		this.node = builder.node();
		this.client = node.client();		
	}

	/**
	 * Create index with given name if index doesn't exist.
	 * @param index index name
	 */
	public void createIndexIfNeeded(final String index) {
		if (!existsIndex(index)) {
			getClient().admin().indices().prepareCreate(index).execute().actionGet();
		}
	}

	public void recreateIndex(final String index) {
		logger("Recreting index: " + index);
		if (existsIndex(index)) {
			getClient().admin().indices().prepareDelete(index).execute().actionGet();
		}
		getClient().admin().indices().prepareCreate(index).execute().actionGet();
	}

	public boolean existsIndex(final String index) {
		IndicesExistsResponse response = getClient().admin().indices().prepareExists(index).execute().actionGet();
		return response.isExists();		
	}

	protected void logger(final String msg) {
		LOG.info("* " + msg);
	}

	public void deleteIndex(String name){
		getClient().admin().indices().prepareDelete(name).execute().actionGet();
	}

	public void closeIndex(String name){
		getClient().admin().indices().prepareClose(name).execute().actionGet();
	}

	public void openIndex(String name){
		    getClient().admin().indices().prepareRefresh().execute().actionGet();

		if (existsIndex(name)) {
			getClient().admin().indices().prepareOpen(name).execute().actionGet();

		}
			}

	public void close() {
		if(client != null) {
			client.close();
		}

		if (node != null) {
			node.close();
		}
	}

	public void putMapping(String index, String typeName, String source){
		getClient().admin().indices().preparePutMapping(index).setType(typeName).setSource(source).execute().actionGet();
	}

	public void deleteMapping(String index, String typeName){
		getClient().admin().indices().prepareDeleteMapping(index).setType(typeName).execute().actionGet();
	}


}
