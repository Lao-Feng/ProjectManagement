package zr.zrpower.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zr.zrpower.model.PageInfoBean;


/**
 * 
 * @author nfzr
 *
 * @param <T>
 */
public class PageUtil<T> {

	  private static final Logger LOGGER = LoggerFactory.getLogger(PageUtil.class);
	
	  private Long total;
	    
	  private int limit=0;
	    
	  private int offset = 0;
	  
	  private int page = 0;
	    
	  private String order ="asc" ;
	  
	  private String orderBy;
	  
	  private  Object obj;
	  
	  @SuppressWarnings("unused")
	private T bo;
	  
	  @SuppressWarnings("unused")
	private PageInfoBean pageInfo;
	  
	  public PageUtil(){
		  
	  }
	  
	  /** 构造分页工具类，返回参数BO
	 * @param json
	 * @param clz
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  PageUtil(String json , Class clz){
		  JSONObject jsonObj = JSONObject.parseObject( json );
		  this.total = jsonObj.getLongValue( "total" );
		  this.limit = jsonObj.getIntValue( "limit" );
		  this.offset = jsonObj.getIntValue( "offset" );
		  this.orderBy = jsonObj.getString( "sidx" );
		  this.order = jsonObj.getString( "order" );
		  this.page = jsonObj.getIntValue( "page" );
		  this.obj = JSON.parseObject( json, clz );
		  if(this.obj == null){
			  try {
				this.obj = clz.newInstance();
			} catch( InstantiationException e ) {
				LOGGER.error( e.getMessage() , e );
			} catch( IllegalAccessException e ) {
				LOGGER.error( e.getMessage() , e );
			}
		  }
	  }
	
	/**
	   * 返回用户输入参数
	   *  @return
	   */
		@SuppressWarnings("unchecked")
		public T getBo() {
				return ( T )this.obj;
			}
		
		
		public PageInfoBean getPageInfo() {
			PageInfoBean pageInfo = new PageInfoBean(this.getLimit() , this.getOffset() , this.getOrderBy() , this.order);
			return pageInfo;
		}
		
		
		public int getOffset() {
			if(offset == 0){
				offset = this.getLimit() * ( page -1 );
			}
			return offset;
		}

		public Long getTotal() {
			return total;
		}

		public void setTotal(Long total) {
			this.total = total;
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

		public String getOrderBy() {
			return orderBy;
		}

		public void setOrderBy(String orderBy) {
			this.orderBy = orderBy;
		}

		public Object getObj() {
			return obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public void setBo(T bo) {
			this.bo = bo;
		}

		public void setPageInfo(PageInfoBean pageInfo) {
			this.pageInfo = pageInfo;
		}
		
		
	  
}
