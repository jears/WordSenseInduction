package org.kde9.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.kde9.model.Edge;
import org.kde9.model.Graph;
import org.kde9.model.Node;

public class AddingRootOfHubsToGraph {
	private static double ROOT_WEIGHT = 0xffffff;

	private Node root;
	private List<Node> hubs;
	private Graph graph;
	private double totalSigma1;
	private double totalSigma2;
	private int sigmaNum;

	public AddingRootOfHubsToGraph(Graph graph, Node query) {
		this.graph = graph;
		root = query;
	}

	public void addRoot(double sigma1, double sigma2) {
		System.err.println("finding hubs and add root to graph ... ");
		long start = System.currentTimeMillis();
		hubs = getHubs(sigma1, sigma2);
		addRoot();
		long end = System.currentTimeMillis();
		System.err.println(" finish. hubs num " + hubs.size());
		System.err.println(" average sigma1 " + averageSigma1()
				+ ", average sigma2 " + averageSigma2());
		System.err.println((end - start) / 1000.0 + "s");
	}

	public double averageSigma1() {
		return totalSigma1 / sigmaNum;
	}
	
	public double averageSigma2() {
		return totalSigma2 / sigmaNum;
	}
	
	public List<Node> getHubs() {
		return hubs;
	}

	private List<Node> getHubs(double sigma1, double sigma2) {
		List<Node> hubs = new ArrayList<Node>();
		List<Node> nodes = new ArrayList<Node>(graph.getAllNodes());
		Set<Node> removedNodes = new TreeSet<Node>();
		Collections.sort(nodes, new Node.NodeComparator());
		for (Node node : nodes) {
			if (removedNodes.contains(node)) {
				continue;
			}
			double degree = node.getDegree();
			double s1 = degree / graph.getMaxDegree();
			totalSigma1 += s1;
			double s2 = node.getTotalWeight() / degree;
			totalSigma2 += s2;
			++sigmaNum;
			if (s1 < sigma1 || s2 < sigma2) {
				continue;
			}
			hubs.add(node);
			removedNodes.addAll(graph.getNeighbors(node));
		}
		return hubs;
	}

	private void addRoot() {
		graph.addNode(root);
		for (Node node : hubs) {
			graph.addEdge(new Edge(ROOT_WEIGHT), root, node);
		}
	}
}
