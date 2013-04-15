package org.kde9.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Graph {
	private TreeMap<Edge, Node[]> nodesOfEdge;
	private TreeMap<Node, Map<Node, Edge>> edgeOfNodeAndNode;
	private int maxDegree;

	public Graph() {
		nodesOfEdge = new TreeMap<Edge, Node[]>();
		edgeOfNodeAndNode = new TreeMap<Node, Map<Node, Edge>>();
		maxDegree = 0;
	}

	public Edge getEdge(Node node1, Node node2) {
		Map<Node, Edge> edges = edgeOfNodeAndNode.get(node1);
		return edges.get(node2);
	}

	public void addNode(Node node) {
		if (!edgeOfNodeAndNode.containsKey(node)) {
			edgeOfNodeAndNode.put(node, new TreeMap<Node, Edge>());
		}
	}

	public void removeNode(Node node) {
		Set<Edge> edges = getEdges(node);
		for (Edge edge : edges) {
			removeEdge(edge);
		}
		edgeOfNodeAndNode.remove(node);
	}

	public boolean hasNode(Node node) {
		return edgeOfNodeAndNode.containsKey(node);
	}
	
	public void addEdge(Edge edge, Node left, Node right) {
		if (!nodesOfEdge.containsKey(edge)) {
			nodesOfEdge.put(edge, new Node[] { left, right });
			edgeOfNodeAndNode.get(left).put(right, edge);
			edgeOfNodeAndNode.get(right).put(left, edge);
		}
	}

	public void removeEdge(Edge edge) {
		Node[] points = nodesOfEdge.remove(edge);
		edgeOfNodeAndNode.get(points[0]).remove(points[1]);
		edgeOfNodeAndNode.get(points[1]).remove(points[0]);
	}

	public void calculateNodeWeight() {
		for (Map.Entry<Node, Map<Node, Edge>> entry : edgeOfNodeAndNode
				.entrySet()) {
			Node node = entry.getKey();
			Collection<Edge> edges = entry.getValue().values();
			int degree = edges.size();
			node.setDegree(edges.size());
			if (degree > maxDegree) {
				maxDegree = degree;
			}
			double totalWeight = 0;
			for (Edge edge : edges) {
				totalWeight += edge.getWeight();
			}
			node.setTotalWeight(totalWeight);
		}
	}

	public Set<Node> getAllNodes() {
		return new TreeSet<Node>(edgeOfNodeAndNode.keySet());
	}

	public Node[] getNodes(Edge edge) {
		return nodesOfEdge.get(edge);
	}

	public Set<Node> getNeighbors(Node node) {
		return new TreeSet<Node>(edgeOfNodeAndNode.get(node).keySet());
	}

	public Set<Edge> getAllEdge() {
		return new TreeSet<Edge>(nodesOfEdge.keySet());
	}

	public Set<Edge> getEdges(Node node) {
		return new TreeSet<Edge>(edgeOfNodeAndNode.get(node).values());
	}

	public int getNodeNum() {
		return edgeOfNodeAndNode.size();
	}

	public int getEdgeNum() {
		return nodesOfEdge.size();
	}

	public int getMaxDegree() {
		return maxDegree;
	}
}
