package duson.ui.tree.ztree;

public class TreeView {
	private String id;
	private String pId;
	private String name;
	private boolean isParent;
	private boolean open;
	private boolean nocheck;
	
	private Object extra;
	private boolean drag;
	private boolean dropRoot;
	
	public TreeView(){}
	public TreeView(String id, String pId, String name){
		this.id = id;
		this.pId = pId;
		this.name = name;
	}
	public TreeView(String id, String pId, String name, boolean isParent, boolean nocheck){
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isParent = isParent;
		this.nocheck = nocheck;
	}
	public TreeView(String id, String pId, String name, Object extra, boolean drag, boolean dropRoot){
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.extra = extra;
		this.drag = drag;
		this.dropRoot = dropRoot;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isNocheck() {
		return nocheck;
	}
	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}
	public Object getExtra() {
		return extra;
	}
	public void setExtra(Object extra) {
		this.extra = extra;
	}
	public boolean isDrag() {
		return drag;
	}
	public void setDrag(boolean drag) {
		this.drag = drag;
	}
	public boolean isDropRoot() {
		return dropRoot;
	}
	public void setDropRoot(boolean dropRoot) {
		this.dropRoot = dropRoot;
	}
}
