package com.csuweb.PaperCheck.core.common;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ClientFactory {
	private static org.elasticsearch.client.Client client;
	
	public static Client getClient(){
		if(client==null){
			String clusterName ="elasticsearch";
			String _clientTransportSniff =  "true";
			String _port =  "9300";
			String hostname =  "127.0.0.1";
			String hostnames[] = hostname.split(",");
			
			boolean clientTransportSniff = false;
			try{
				if( !"false".equals(_clientTransportSniff.toLowerCase().trim())) {
					clientTransportSniff = true;
				}
			}catch(Exception e){};
			int port = 9300;
			try{
				port = Integer.parseInt(_port);
			}catch(Exception e){};
			
			Settings settings = ImmutableSettings.settingsBuilder()
					.put("cluster.name", clusterName)
					.put("client.transport.sniff", clientTransportSniff)
					.build();
			
			TransportClient transportClient = new TransportClient(settings);
			if(hostnames!=null){
				for(String host: hostnames) {
					transportClient.addTransportAddress(new InetSocketTransportAddress(host, port));
				}
			}
			client=transportClient;
		}
		return client;
	}
}
