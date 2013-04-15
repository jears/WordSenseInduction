package org.kde9.processor;

import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.kde9.model.Edge;
import org.kde9.model.Graph;
import org.kde9.model.Node;
import org.kde9.model.NodePool;
import org.kde9.util.fileOperation.ReadFile;

public class OriginalGraphConstructor {
	private Graph graph;
	private NodePool pool;
	private TreeMap<Integer, Node> nodes;
	private Node query;
	private TreeSet<Node> nodesNeedFilter;
	private String charset;

	public OriginalGraphConstructor(String wordFile, String weightFile,
			String charset) {
		System.err.println("loading file and generate graph ... ");
		long start = System.currentTimeMillis();
		graph = new Graph();
		pool = new NodePool();
		nodes = new TreeMap<Integer, Node>();
		nodesNeedFilter = new TreeSet<Node>();
		this.charset = charset;
		readWords(wordFile);
		readWeight(weightFile);
		long end = System.currentTimeMillis();
		System.err.println(" finish. nodes num " + graph.getNodeNum()
				+ ", edge num " + graph.getEdgeNum());
		System.err.println((end - start) / 1000.0 + "s");
	}

	public NodePool getPool() {
		return pool;
	}

	public Graph getGraph() {
		return graph;
	}

	public Set<Node> getNodesNeedFilter() {
		return nodesNeedFilter;
	}

	public Node getQuery() {
		return query;
	}

	private void readWords(String wordFile) {
		try {
			ReadFile readFile = new ReadFile(wordFile, charset);
			String line = readFile.readLine();
			query = addNode(line.trim());
			while ((line = readFile.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty()) {
					addNode(line);
				}
			}
			readFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Node addNode(String line) {
		String[] values = line.split("\t");
		int index = Integer.valueOf(values[0]);
		String value = values[1];
		Node node = new Node(value, Integer.valueOf(values[2]));
		pool.add(value, node);
		if (nodes.put(index, node) != null) {
			System.err.println("Warning: Word index repeat in word file!");
		}
		if (Integer.valueOf(values[3]) == 1) {
			nodesNeedFilter.add(node);
		}
		graph.addNode(node);
		return node;
	}

	private void readWeight(String weightFile) {
		try {
			ReadFile readFile = new ReadFile(weightFile, charset);
			String line;
			while ((line = readFile.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty()) {
					String[] values = line.split("\t");
					int index1 = Integer.valueOf(values[0]);
					int index2 = Integer.valueOf(values[1]);
					if (nodes.containsKey(index1) && nodes.containsKey(index2)) {
						Node node1 = nodes.get(index1);
						Node node2 = nodes.get(index2);
						graph.addEdge(new Edge(Integer.valueOf(values[2])),
								node1, node2);
					}
				}
			}
			readFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
