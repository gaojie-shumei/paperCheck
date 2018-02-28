package com.csuweb.PaperCheck.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.FieldSortBuilder;

import com.csuweb.PaperCheck.core.common.ClientFactory;


public class ESUtils {
	static Pattern badChars = Pattern.compile("\\s*[\\s~!\\^&\\(\\)\\-\\+:\\|\\\\\"\\\\$]+\\s*");
	private static Client client = ClientFactory.getClient();

	/**
	 * 关闭对应client
	 * 
	 * @param client
	 */
	public static void close(Client client) {
		if (client != null) {
			try {
				client.close();
			} catch (Exception e) {
			}
			client = null;
		}
	}

	public static void flush(Client client, String indexName, String indexType) {
		try {
			client.admin().indices().flush(new FlushRequest(indexName.toLowerCase(), indexType)).actionGet();
		} catch (Exception e) {
		}
		;
	}

	public static boolean indicesExists(String indexName) {
		return indicesExists(client, indexName);
	}

	public static boolean indicesExists(Client client, String indexName) {
		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName.toLowerCase() });

		return client.admin().indices().exists(ier).actionGet().isExists();
	}

	public static boolean typesExists(Client sClient, String indexName, String indexType) {
		if (indicesExists(sClient, indexName)) {
			TypesExistsRequest ter = new TypesExistsRequest(new String[] { indexName.toLowerCase() }, indexType);
			return sClient.admin().indices().typesExists(ter).actionGet().isExists();
		}
		return false;
	}

	public static void createIndex(String indexName, String indexType) throws IOException {
		Map<String, Object> map = Maps.newHashMap();
		map.put("number_of_replicas", "0");

		Settings settings = ImmutableSettings.settingsBuilder().put(map).build();
		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName.toLowerCase() });

		boolean exists = client.admin().indices().exists(ier).actionGet().isExists();
		if (!exists) {
			client.admin().indices().prepareCreate(indexName.toLowerCase()).setSettings(settings).execute().actionGet();
		}
	}

	public static boolean createArticleIndex(String indexName) throws IOException {
		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName.toLowerCase() });
		Map<String, Object> map = Maps.newHashMap();
		map.put("number_of_replicas", "1");

		Settings settings = ImmutableSettings.settingsBuilder().put(map).build();

		boolean exists = client.admin().indices().exists(ier).actionGet().isExists();
		if (!exists) {
			CreateIndexResponse response = client.admin().indices().prepareCreate(indexName.toLowerCase())
					.setSettings(settings).execute().actionGet();
			if (!typesExists(client, indexName, "Article"))
				createArticleMapping(indexName);
			return response.isAcknowledged();
		} else {
			return true;
		}
	}

	public static boolean createSentenceIndex(String indexName) throws IOException {
		return createSentenceIndex(indexName, client);
	}

	public static boolean createSentenceIndex(String indexName, Client sClient) throws IOException {
		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName.toLowerCase() });

		boolean exists = sClient.admin().indices().exists(ier).actionGet().isExists();
		if (!exists) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("number_of_replicas", "0");
			// map.put("index.refresh_interval", "-1");
			map.put("index.translog.flush_threshold_ops", "100000");

			Settings settings = ImmutableSettings.settingsBuilder().put(map).build();
			CreateIndexResponse response = sClient.admin().indices().prepareCreate(indexName.toLowerCase())
					.setSettings(settings).execute().actionGet();
			if (!typesExists(sClient, indexName, "SamllSentence")) {
				// createSentenceMapping(sClient,indexName);
			}
			return response.isAcknowledged();
		} else {
			return true;
		}
	}

	public static boolean createArticleMapping(String indexName) throws IOException {
		PutMappingResponse response = client.admin().indices().preparePutMapping(indexName).setType("article")
				.setSource(XContentFactory.jsonBuilder().startObject().startObject("article").startObject("properties")
						.startObject("id").field("type", "string").field("index", "not_analyzed").field("store", "no")
						.startObject("title").field("type", "string").field("index", "analyzed").field("store", "yes")
						.startObject("keyword").field("type", "string").field("index", "analyzed").field("store", "yes")
						.startObject("remark").field("type", "string").field("index", "analyzed").field("store", "yes")
						.startObject("author").field("type", "string").field("index", "not_analyzed")
						.field("store", "no").startObject("content").field("type", "string").field("index", "analyzed")
						.field("store", "yes").startObject("times").field("type", "integer").field("index", "no")
						.field("store", "no")

		.endObject().endObject().endObject().endObject().endObject().endObject().endObject().endObject().endObject()
						.endObject())
				.execute().actionGet();
		return response.isAcknowledged();
	}

	public static boolean createSentenceMapping(Client scClient, String indexName) throws IOException {
		PutMappingResponse response = scClient.admin().indices().preparePutMapping(indexName).setType("SamllSentence")
				.setSource(XContentFactory.jsonBuilder().startObject().startObject("SmallSentence")
						.startObject("properties").startObject("id").field("type", "string")
						.field("index", "not_analyzed").field("store", "no")
						// .startObject("paperid").field("type",
						// "string").field("index",
						// "not_analyzed").field("store", "yes")
						.startObject("title").field("type", "string").field("index", "analyzed").field("store", "yes")
						.startObject("originSentence").field("type", "string").field("index", "analyzed")
						.field("store", "yes").startObject("foreSentence").field("type", "string").field("index", "no")
						.field("store", "yes").startObject("behindSentence").field("type", "string")
						.field("index", "no").field("store", "yes").startObject("length").field("type", "integer")
						.field("index", "no").field("store", "no").startObject("shingle").field("type", "string")
						.field("index", "no").field("store", "yes")
						// .startObject("existXs").field("type",
						// "boolean").field("index", "no").field("store", "no")
						// .startObject("maxXsl").field("type",
						// "float").field("index", "no").field("store", "no")
						.endObject().endObject().endObject().endObject().endObject().endObject().endObject().endObject()
						.endObject()
						// .endObject()
						// .endObject()
						// .endObject()
						.endObject())
				.execute().actionGet();
		return response.isAcknowledged();
	}

	/**
	 * 根据索引名称删除索引
	 * 
	 * @param indexName
	 *            索引名称
	 */
	public static void deleteIndex(String indexName) {

		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName.toLowerCase() });

		boolean exists = client.admin().indices().exists(ier).actionGet().isExists();
		if (exists) {
			client.admin().indices().prepareDelete(indexName.toLowerCase()).execute().actionGet();
		}

	}

	public synchronized static void indexSentence(String indexName, String typeNmae, List<?> entitys) throws Exception {
		createSentenceIndex(indexName);
		index(indexName, typeNmae, entitys, client);
	}

	public static void index(String indexName, String typeNmae, List<?> entitys, Client scClient) throws Exception {
		if (entitys == null || entitys.size() == 0)
			return;
		BulkRequestBuilder bulkRequest = scClient.prepareBulk();
		for (Object object : entitys) {
			String json = JsonUtil.generateJson(object);
			IndexRequest request = scClient.prepareIndex(indexName, typeNmae).setSource(json).request();
			bulkRequest.add(request);
		}

		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			System.out.println("批量创建索引错误！");
		}

	}

	public static void indexArticle(String indexName, String typeNmae, List<?> entitys) throws Exception {
		createArticleIndex(indexName);
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Object object : entitys) {
			String json = JsonUtil.generateJson(object);
			IndexRequest request = client.prepareIndex(indexName, typeNmae).setSource(json).request();
			bulkRequest.add(request);
		}

		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			System.out.println("批量创建索引错误！");
		}
	}

	
	/**
	 * 过滤自我文章查询句子
	 * @param indexName
	 * @param indexTypes
	 * @param fields
	 * @param queryString
	 * @param from
	 * @param size
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	public static List<Object> search(String indexName, String indexTypes, String fields, String queryString,
				int from, int size,	Class<?> valueType) throws Exception {
		List<FilterBuilder> filterBuilders = new ArrayList<>();		
		filterBuilders.add(FilterBuilders.termFilter("paperId", indexName));
			
		return search(indexName, indexTypes, fields, queryString, filterBuilders, from, size, valueType);
	}
	
	/**
	 * 过滤自我文章查询文章
	 * @param indexName
	 * @param indexTypes
	 * @param fields
	 * @param queryString
	 * @param from
	 * @param size
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	public static List<Object> search(String indexName, String indexTypes, String fields, List<String> words,
			String filterTitle,
				int from, int size,	Class<?> valueType) throws Exception {
		List<FilterBuilder> filterBuilders = new ArrayList<>();		
		filterBuilders.add(FilterBuilders.queryFilter(QueryBuilders.queryStringQuery(filterTitle).defaultField("title")));
			
		return search(indexName, indexTypes, fields, words, filterBuilders, from, size, valueType);
	}
	
	/**
	 * 过滤自我文章查询文章
	 * @param indexName
	 * @param indexTypes
	 * @param fields
	 * @param queryString
	 * @param from
	 * @param size
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	public static List<Object> search(String indexName, String indexTypes, String fields, List<String> words,
			List<FilterBuilder> filterBuilders, int from, int size, Class<?> valueType) throws Exception {
		indexName = indexName.toLowerCase();
		client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

		if(words==null)return null;
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName);
		searchRequestBuilder.setTypes(indexTypes);
		BoolQueryBuilder booleanquery = QueryBuilders.boolQuery();
		for (int i = 0; i < words.size(); i++) {
			booleanquery.should(new TermQueryBuilder(fields, words.get(i)));
		}
		searchRequestBuilder.setQuery(booleanquery).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from)
				.setSize(size).setExplain(false);
		
		if (filterBuilders != null && filterBuilders.size() > 0) {
			BoolFilterBuilder boolFilterBuilder = null;
			for (FilterBuilder filterBuilder : filterBuilders) {
				boolFilterBuilder = FilterBuilders.boolFilter().mustNot(filterBuilder);//过滤此条件
			}
			searchRequestBuilder.setPostFilter(boolFilterBuilder);
		}
		SearchHits searchHits = searchRequestBuilder.execute().actionGet().getHits();
		return swapResult(searchHits, valueType);
	}

	public static List<Object> search(String indexName, String indexTypes, String fields, String queryString,
			List<FilterBuilder> filterBuilders,int from, int size, Class<?> valueType) throws Exception {
		indexName = indexName.toLowerCase();
		client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName);
		searchRequestBuilder.setTypes(indexTypes);
		
		if(StringUtils.isBlank(queryString))return null;
		queryString = QueryParser.escape(queryString);
		searchRequestBuilder.setQuery(QueryBuilders.queryStringQuery(queryString).field(fields))
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from).setSize(size).setExplain(false);

		if (filterBuilders != null && filterBuilders.size() > 0) {
			BoolFilterBuilder boolFilterBuilder = null;
			for (FilterBuilder filterBuilder : filterBuilders) {
				boolFilterBuilder = FilterBuilders.boolFilter().mustNot(filterBuilder);//过滤此条件
			}
			searchRequestBuilder.setPostFilter(boolFilterBuilder);
		}
		SearchHits searchHits = searchRequestBuilder.execute().actionGet().getHits();
		return swapResult(searchHits, valueType);
	}

	public static HashMap<String, Long> search(String indexName, String indexTypes, String fields, String queryString,
			List<FieldSortBuilder> sortBuilders, int from, int size, Class<?> valueType, String aggField)
					throws Exception {
		HashMap<String, Long> hashMap = new HashMap<>();

		indexName = indexName.toLowerCase();
		client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName);
		searchRequestBuilder.setTypes(indexTypes);

		searchRequestBuilder.setQuery(QueryBuilders.queryStringQuery(queryString).field(fields))
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(from).setSize(size).setExplain(false)
				.addAggregation(AggregationBuilders.terms("aggName").field(aggField));
		SearchResponse sr = searchRequestBuilder.execute().actionGet();

		TopHits topHits = sr.getAggregations().get("aggName");
		for (SearchHit sh1 : topHits.getHits()) {
			System.out.println(sh1.sourceAsMap().get("akc194").toString());
		}

		Terms terms = sr.getAggregations().get("aggName");
		List<Terms.Bucket> collection = terms.getBuckets();
		for (Terms.Bucket bucket : collection) {
			String region = bucket.getKey();
			long count = bucket.getDocCount();
			hashMap.put(region, count);
		}
		return hashMap;
	}

	/**
	 * 查询数据
	 * 
	 * @param indexName
	 *            索引名称
	 * @param indexType
	 *            索引类型
	 * @param id
	 *            数据id
	 * @return 如果不存在，返回<code>null</code>
	 */
	public static Map<String, Object> query(String indexName, String indexType, String id) {
		indexName = indexName.toLowerCase();
		indexType = indexType.toLowerCase();

		IndicesExistsRequest ier = new IndicesExistsRequest();
		ier.indices(new String[] { indexName });
		boolean exists = client.admin().indices().exists(ier).actionGet().isExists();
		if (!exists) {
			// 索引不存在
			return null;
		}

		client.admin().indices().open(new OpenIndexRequest(indexName)).actionGet();
		client.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

		GetRequest gr = new GetRequest(indexName, indexType, id);

		ActionFuture<GetResponse> future = client.get(gr);
		GetResponse response = future.actionGet();
		return swapResult(response);
	}

	public static List<Object> swapResult(SearchHits hits, Class<?> valueType) throws Exception {
		List<Object> articles = new ArrayList<Object>();

		if (hits == null || hits.getTotalHits() <= 0) {
			return null;
		}

		for (int i = 0; i < hits.hits().length; i++) {
			SearchHit hit = hits.getAt(i);
			String json = hit.getSourceAsString();
			Object article = JsonUtil.generateObject(json, valueType);
			articles.add(article);
		}

		return articles;
	}

	public static Map<String, Object> swapResult(GetResponse response) {
		if (response == null || !response.isExists()) {
			return null;
		}

		Map<String, Object> rowData = response.getSourceAsMap();
		rowData.put("_index", response.getIndex());
		rowData.put("_type", response.getType());
		rowData.put("_id", response.getId());

		return rowData;
	}
}
