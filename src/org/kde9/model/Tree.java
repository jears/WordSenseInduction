package org.kde9.model;

import java.util.Set;

public class Tree extends Graph {
	private Node root;
	
	public Node getRoot() {
		return root;
	}
	
	public void setRoot(Node root) {
		if(hasNode(root)) {
			this.root = root;
		} else {
			System.err.println("Warning: Root " + root + " is not found in tree!");
		}
	}
	
	@Override
	public String toString() {
		Set<Node> nodes = getAllNodes();
		nodes.remove(root);
		StringBuilder builder = new StringBuilder();
		builder.append(root.getWord());
		builder.append(": ");
		for(Node node : nodes) {
			builder.append(node.getWord());
			builder.append(", ");
		}
		return builder.toString();
	}
}
