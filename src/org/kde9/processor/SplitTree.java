package org.kde9.processor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.kde9.model.Graph;
import org.kde9.model.Node;
import org.kde9.model.Tree;

public class SplitTree {
	private List<Tree> trees;

	public SplitTree(Graph tree, Node root) {
		System.err.println("spliting tree ... ");
		long start = System.currentTimeMillis();
		trees = new ArrayList<Tree>();
		Set<Node> hubs = tree.getNeighbors(root);
		tree.removeNode(root);
		for (Node hub : hubs) {
			trees.add(getTree(hub, tree));
		}
		long end = System.currentTimeMillis();
		System.err.println(" finish. tree num " + trees.size());
		System.err.println((end - start) / 1000.0 + "s");
	}

	public List<Tree> getTrees() {
		return trees;
	}

	private Tree getTree(Node root, Graph graph) {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(root);
		Tree tree = new Tree();
		tree.addNode(root);
		while (!queue.isEmpty()) {
			Node node = queue.pop();
			Set<Node> neighbors = graph.getNeighbors(node);
			for (Node n : neighbors) {
				if (!tree.hasNode(n)) {
					queue.add(n);
					tree.addNode(n);
					tree.addEdge(graph.getEdge(node, n), node, n);
				}
			}
		}
		tree.setRoot(root);
		return tree;
	}
}
