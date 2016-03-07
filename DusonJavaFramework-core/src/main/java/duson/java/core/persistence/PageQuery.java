package duson.java.core.persistence;

public class PageQuery<T> {
	private int pageIndex = 1;
	private int pageSize = 10;
	private String orderBy = "";
	
	private T filter;

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public T getFilter() {
		return filter;
	}

	public void setFilter(T filter) {
		this.filter = filter;
	}

}
