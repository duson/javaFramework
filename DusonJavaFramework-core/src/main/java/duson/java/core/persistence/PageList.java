package duson.java.core.persistence;

import java.util.ArrayList;
import java.util.List;

public class PageList<T> {

	private int totalRecord;
	private List<T> list = new ArrayList<T>();
	
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
}
