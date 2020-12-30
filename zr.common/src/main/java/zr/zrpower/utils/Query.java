package zr.zrpower.utils;


import groovy.transform.EqualsAndHashCode;
import zr.zrpower.defence.SQLFilter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author nfzr
 *
 */
@EqualsAndHashCode(callSuper=true)
public class Query extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	// 当前页码
	private int page;

	// 每页条数
	private int limit;

	// 每页条数
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@SuppressWarnings("unused")
	public Query(Map<String, Object> params ) {
		this.putAll( params );

		// 分页参数
		this.page = Integer.parseInt( params.get( "page" ).toString() );
		this.limit = Integer.parseInt( params.get( "limit" ).toString() );
		this.put( "offset", ( page - 1 ) * limit );
		this.put( "page", page );
		this.put( "limit", limit );


		// 防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
		String sidx = params.get( "sidx" ).toString();
		String order = params.get( "order" ).toString();
		this.put( "sidx", SQLFilter.sqlInject( sidx ) );
		this.put( "order", SQLFilter.sqlInject( order ) );

		//for循环，将实体.属性类似project[key] 转换为project.key
		int size=params.size();
		for(Map.Entry<String, Object> entry : params.entrySet()){
			String key = entry.getKey();
			if(key.indexOf("[")>-1){
				Object value = entry.getValue();
				String newKey=key.replaceAll("]","").replaceAll("\\[","_");
				this.remove( key);
				this.put(newKey,value);
			}
		}
	}
}
