package org.kde9.model;

import java.util.Comparator;

public class Node implements Comparable<Node> {
	private static int total = 0;

	private int index;
	private String word;
	private int frequency;
	private int degree;
	private double totalWeight;

	public Node(String word, int frequency) {
		this.index = total++;
		this.word = word;
		this.frequency = frequency;
	}

	public int getIndex() {
		return index;
	}

	public String getWord() {
		return word;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public int getFrequency() {
		return frequency;
	}

	@Override
	public int compareTo(Node n) {
		if (n.frequency == frequency) {
			return n.index - index;
		}
		return n.frequency - frequency;
	}

	@Override
	public String toString() {
		return "N(" + word + ")";
	}

	public static class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			if (n2.frequency == n1.frequency) {
				if (n2.degree == n1.degree) {
					return n2.index - n1.index;
				}
				return n2.degree - n1.degree;
			}
			return n2.frequency - n1.frequency;
		}
	}

}
