package org.kde9.processor;

import java.util.Set;

import org.kde9.model.Edge;
import org.kde9.model.Graph;
import org.kde9.model.Node;

public class GraphConstructor {
	private Graph originalGraph;
	private Node query;
	private Set<Node> nodesNeedFilter;
	private double totalDelta1;
	private double totalDelta2;
	private double totalTheta;
	private int deltaNum;
	private int thetaNum;

	public GraphConstructor(Graph originalGraph, Node query,
			Set<Node> nodesNeedFilter) {
		this.originalGraph = originalGraph;
		this.query = query;
		this.nodesNeedFilter = nodesNeedFilter;
	}

	public Graph getGraph(double delta1, double delta2, double theta) {
		System.err.println("filter nodes and edges ... ");
		long start = System.currentTimeMillis();
		Graph graph = new Graph();
		addNodes(graph, delta1, delta2);
		addEdges(graph, theta);
		clearNodes(graph);
		graph.calculateNodeWeight();
		long end = System.currentTimeMillis();
		System.err.println(" finish. nodes num " + graph.getNodeNum()
				+ ", edge num " + graph.getEdgeNum());
		System.err.println(" average delta1 " + averageDelta1()
				+ ", average delta2 " + averageDelta2()
				+ ", average theta " + averageTheta());
		System.err.println((end - start) / 1000.0 + "s");
		return graph;
	}
	
	public double averageDelta1() {
		return totalDelta1 / deltaNum;
	}
	
	public double averageDelta2() {
		return totalDelta2 / deltaNum;
	}
	
	public double averageTheta() {
		return totalTheta / thetaNum;
	}

	private void addNodes(Graph graph, double delta1, double delta2) {
		Set<Node> nodes = originalGraph.getAllNodes();
		nodes.remove(query);
		for (Node node : nodes) {
			if (nodesNeedFilter.contains(node)) {
				Edge edge = originalGraph.getEdge(query, node);
				double c_qw = edge.getWeight();
				double c_q = query.getFrequency();
				double c_w = node.getFrequency();
				double d1 = c_qw / c_q;
				totalDelta1 += d1;
				double d2 = 2 * c_qw / (c_q + c_w);
				totalDelta2 += d2;
				++deltaNum;
				if (d1 < delta1 || d2 < delta2) {
					continue;
				}
			}
			graph.addNode(node);
		}
	}

	private void addEdges(Graph graph, double theta) {
		Object[] nodes = graph.getAllNodes().toArray();
		int size = nodes.length;
		for (int i = 0; i < size; ++i) {
			Node node1 = (Node) nodes[i];
			for (int j = i + 1; j < size; ++j) {
				Node node2 = (Node) nodes[j];
				Edge edge = originalGraph.getEdge(node1, node2);
				if (edge == null) {
					continue;
				}
				double c_ij = edge.getWeight();
				double c_i = node1.getFrequency();
				double c_j = node2.getFrequency();
				double d_ij = 2.0 * c_ij / (c_i + c_j);
				totalTheta += d_ij;
				++thetaNum;
				if (d_ij < theta) {
					continue;
				}
				graph.addEdge(new Edge(d_ij), node1, node2);
			}
		}
	}

	private void clearNodes(Graph graph) {
		Set<Node> nodes = graph.getAllNodes();
		for (Node node : nodes) {
			if (graph.getEdges(node).isEmpty()) {
				graph.removeNode(node);
			}
		}
	}
}
