package org.kde9.model;

public class Edge implements Comparable<Edge> {
	private static int total = 0;

	private int index;
	private double weight;

	public Edge(double weight) {
		index = total++;
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public int compareTo(Edge e) {
		if (e.weight == weight) {
			return e.index - index;
		}
		return e.weight - weight < 0 ? -1 : 1;
	}
	
	@Override
	public String toString() {
		return "E(" + weight + ")";
	}
}
