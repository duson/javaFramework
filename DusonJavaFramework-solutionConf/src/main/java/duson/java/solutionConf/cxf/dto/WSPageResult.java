package duson.java.solutionConf.cxf.dto;

import java.io.Serializable;

public class WSPageResult extends WSResult implements Serializable {
  
    private int totalRecord;

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

}
