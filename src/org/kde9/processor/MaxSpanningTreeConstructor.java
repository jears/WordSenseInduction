package org.kde9.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.kde9.model.Edge;
import org.kde9.model.Graph;
import org.kde9.model.Node;

public class MaxSpanningTreeConstructor {
	private Graph graph;
	private ArrayList<Edge> sortedEdges;
	private TreeMap<Node, Set<Node>> connectedComponents;
	private Graph tree;

	public MaxSpanningTreeConstructor(Graph graph) {
		System.err.println("making max spanning tree ... ");
		long start = System.currentTimeMillis();
		this.graph = graph;
		sortedEdges = sortEdges(graph);
		connectedComponents = new TreeMap<Node, Set<Node>>();
		tree = makeTree();
		long end = System.currentTimeMillis();
		System.err.println(" finish. nodes num " + tree.getNodeNum()
				+ ", edges num " + tree.getEdgeNum());
		System.err.println((end - start) / 1000.0 + "s");
	}

	public Graph getTree() {
		return tree;
	}

	private Graph makeTree() {
		Graph tree = new Graph();
		int edgeNum = 0;
		int maxTreeEdgeNum = graph.getNodeNum() - 1;
		int maxEdgeNum = graph.getEdgeNum();
		for (int i = 0; i < maxEdgeNum && edgeNum < maxTreeEdgeNum; ++i) {
			Edge edge = sortedEdges.get(i);
			Node[] nodes = graph.getNodes(edge);
			Node node1 = nodes[0];
			Node node2 = nodes[1];
			if (!checkCircle(node1, node2)) {
				tree.addNode(node1);
				tree.addNode(node2);
				tree.addEdge(edge, node1, node2);
				++edgeNum;
			}
		}
		return tree;
	}

	private boolean checkCircle(Node node1, Node node2) {
		Set<Node> component1 = connectedComponents.get(node1);
		Set<Node> component2 = connectedComponents.get(node2);
		if (component1 == null && component2 == null) {
			Set<Node> component = new TreeSet<Node>();
			component.add(node1);
			component.add(node2);
			connectedComponents.put(node1, component);
			connectedComponents.put(node2, component);
		} else if (component1 == null) {
			component2.add(node1);
			connectedComponents.put(node1, component2);
		} else if (component2 == null) {
			component1.add(node2);
			connectedComponents.put(node2, component1);
		} else if (component1 != component2) {
			component1.addAll(component2);
			for (Node node : component2) {
				connectedComponents.put(node, component1);
			}
		} else {
			return true;
		}
		return false;
	}

	private ArrayList<Edge> sortEdges(Graph graph) {
		ArrayList<Edge> edges = new ArrayList<Edge>(graph.getAllEdge());
		Collections.sort(edges);
		return edges;
	}
}
