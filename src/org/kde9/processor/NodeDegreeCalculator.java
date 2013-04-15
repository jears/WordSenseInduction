package org.kde9.processor;

import java.util.Set;

import org.kde9.model.Edge;
import org.kde9.model.Graph;
import org.kde9.model.Node;

public class NodeDegreeCalculator {
	private Graph partialGraph;

	public NodeDegreeCalculator(Graph graph, Graph tree) {
//		System.err.println("calculate the original degree of nodes in tree ... ");
		long start = System.currentTimeMillis();
		this.partialGraph = new Graph();
		Set<Node> nodes = tree.getAllNodes();
		Set<Edge> edges = graph.getAllEdge();
		for (Node node : nodes) {
			this.partialGraph.addNode(node);
		}
		for (Edge edge : edges) {
			Node[] points = graph.getNodes(edge);
			if (nodes.contains(points[0]) && nodes.contains(points[1])) {
				this.partialGraph.addEdge(edge, points[0], points[1]);
			}
		}
		this.partialGraph.calculateNodeWeight();
		long end = System.currentTimeMillis();
//		System.err.println(" finish. nodes num " + partialGraph.getNodeNum()
//				+ ", edges num " + partialGraph.getEdgeNum());
//		System.err.println((end - start) / 1000.0 + "s");
	}
	
	public int getDegree(Node node) {
		if(node != null && this.partialGraph.hasNode(node)) {
			return node.getDegree();
		}
		return 0;
	}
}
