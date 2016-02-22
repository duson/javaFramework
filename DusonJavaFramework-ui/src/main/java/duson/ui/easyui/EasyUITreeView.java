package duson.ui.easyui;

import java.util.ArrayList;
import java.util.List;

public class EasyUITreeView {
	private long id;
	private long parentId;
	private String text;
	private String state;
	private List<EasyUITreeView> children;
	
	public EasyUITreeView(long id, long parentId, String text){
		this.id = id;
		this.parentId = parentId;
		this.text = text;
		children = new ArrayList<EasyUITreeView>();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<EasyUITreeView> getChildren() {
		return children;
	}
	public void setChildren(List<EasyUITreeView> children) {
		this.children = children;
	}
}
