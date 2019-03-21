package com.kooi.dissertation.rewriter;
/**
 * 
 * 
 * This class is used to hold a search result
 * 
 * 
 * @author Kooi
 * @date 18th March 2019 
 *
 */
public class SearchResult {

	private SearchNode searchTree;
	private SearchNode result;
	
	public SearchResult(SearchNode tree,SearchNode res) {
		this.searchTree = tree;
		this.result = res;
	}

	public SearchNode getSearchTree() {
		return searchTree;
	}

	public SearchNode getResult() {
		return result;
	}
	
	
	
	
}
