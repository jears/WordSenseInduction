package org.kde9.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kde9.model.Graph;
import org.kde9.model.Node;
import org.kde9.model.NodePool;
import org.kde9.model.Tree;
import org.kde9.processor.AddingRootOfHubsToGraph;
import org.kde9.processor.GraphConstructor;
import org.kde9.processor.MaxSpanningTreeConstructor;
import org.kde9.processor.NodeDegreeCalculator;
import org.kde9.processor.OriginalGraphConstructor;
import org.kde9.processor.SplitTree;

public class ResultTreeRelationCalculator {
	private Graph originalGraph;
	private NodePool pool;
	private Node query;
	private Set<Node> nodesNeedFilter;
	private Graph graph;
	HashMap<Tree, NodeDegreeCalculator> calculators;

	public ResultTreeRelationCalculator(String wordFile, String weightFile,
			String charset) {
		OriginalGraphConstructor step1 = new OriginalGraphConstructor(wordFile,
				weightFile, charset);
		originalGraph = step1.getGraph();
		pool = step1.getPool();
		query = step1.getQuery();
		nodesNeedFilter = step1.getNodesNeedFilter();
	}

	public void setParameters(double delta1, double delta2, double theta,
			double sigma1, double sigma2) {
		GraphConstructor step2 = new GraphConstructor(originalGraph, query,
				nodesNeedFilter);
		graph = step2.getGraph(delta1, delta2, theta);
		AddingRootOfHubsToGraph step3 = new AddingRootOfHubsToGraph(graph,
				query);
		step3.addRoot(sigma1, sigma2);
		MaxSpanningTreeConstructor step4 = new MaxSpanningTreeConstructor(graph);
		Graph tree = step4.getTree();
		SplitTree step5 = new SplitTree(tree, query);
		List<Tree> trees = step5.getTrees();
		calculators = new HashMap<Tree, NodeDegreeCalculator>();
		for (Tree t : trees) {
			calculators.put(t, new NodeDegreeCalculator(graph, t));
		}
	}

	public Map<Tree, Double> calculate(String sentence) {
		String[] words = sentence.split(" ");
		HashMap<Tree, Double> result = new HashMap<Tree, Double>();
		for (Tree t : calculators.keySet()) {
			double value = 0;
			NodeDegreeCalculator calculator = calculators.get(t);
			for (String word : words) {
				value += calculator.getDegree(pool.getNode(word));
			}
			result.put(t, value / t.getNodeNum());
		}
		return result;
	}
}
