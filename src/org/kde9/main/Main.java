package org.kde9.main;

import java.util.Map;
import org.kde9.model.Tree;

public class Main {
	public static void main(String[] args) {
		String wordFile = "蠕虫.txt.count1";
		String weightFile = "蠕虫.txt.count2";

		ResultTreeRelationCalculator calculator = new ResultTreeRelationCalculator(
				wordFile, weightFile, "gbk");
		calculator.setParameters(0.0015, 0.00047, 0.00062, 0.15, 0.0037);

		String sentence = "蠕虫 病毒 专题 瑞星 发布 紧急 疫情 报告 一个 利用 微软 最新 漏洞 传播 的 蠕虫 病毒 开始 在 网上 大肆 传播 有可能 在最近几天 给 整个 网络 带来 较大 威胁 根据 瑞星";
		Map<Tree, Double> result = calculator.calculate(sentence);

		for (Tree t : result.keySet()) {
			System.out.print(t.getRoot().getWord() + ": ");
			System.out.println(result.get(t));
		}
	}
}
