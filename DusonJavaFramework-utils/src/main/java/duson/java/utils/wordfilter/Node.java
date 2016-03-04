package duson.java.utils.wordfilter;

import java.util.HashMap;
import java.util.Map;

public class Node {
	private Map<String,Node> children = new HashMap<String,Node>(0);
	private boolean isEnd = false;
	private int level =0;
	
	public Node addChar(char c) {
		String cStr = String.valueOf(c);
		Node node = children.get(cStr);
		if(node == null){
			node = new Node();
			children.put(cStr, node);
		}
		return node;
	}
	
	public Node findChar(char c){
		String cStr = String.valueOf(c);
		return children.get(cStr);
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
