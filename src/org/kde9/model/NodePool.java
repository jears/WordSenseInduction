package org.kde9.model;

import java.util.TreeMap;

public class NodePool {
	private TreeMap<String, Node> index;

	public NodePool() {
		index = new TreeMap<String, Node>();
	}

	public void add(String word, Node node) {
		index.put(word, node);
	}
	
	public Node getNode(String word) {
		return index.get(word);
	}
}
