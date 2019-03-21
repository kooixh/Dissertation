package com.kooi.dissertation.rewriter;

import com.kooi.dissertation.syntaxtree.Node;

/**
 * 
 * 
 * This class provides analysis functionalities to search for if a term is reachable. 
 * 
 * @author Kooi
 * @date 18th March 2019 
 *
 */
public class SearchEngine {
	
	
	private RewriteEngine engine;
	
	
	public SearchEngine(RewriteEngine e) {
		this.engine = e;
	}
	
	
	/**
	 * 
	 * Build a search tree 
	 * 
	 * @param initialTerm root of the tree
	 * @param bound maximum depth
	 * @return root node of the search tree
	 */
	public SearchNode buildSearchTree(Node initialTerm,int bound) {
		
		SearchNode root = new SearchNode(engine.copy(initialTerm),null,"");
		
		buildUtil(root,bound,0);
		
		return root;
	}
	
	/**
	 * 
	 * 
	 * This method uses a bounded depth-first search to find if a goal term is reachable.
	 * Return the search tree and node of result, null if not reachable.
	 * 
	 * 
	 * @param initialTerm
	 * @param goalTerm
	 * @param bound
	 * @return a SearchResult object
	 */
	public SearchResult searchTerm(Node initialTerm, Node goalTerm, int bound) {
		
		SearchNode searchRoot = buildSearchTree(initialTerm,bound);
	
		SearchNode resNode = searchUtil(searchRoot,goalTerm, bound,0);
		
		return new SearchResult(searchRoot,resNode);
		
	}
	
	
	//use a bounded dfs to search for a match
	private SearchNode searchUtil(SearchNode sr, Node gt, int bound,int curB) {
		
		
		//out of bounds
		if(curB > bound)
			return null;
		
		//if found
		if(sr.getTermNode().equals(gt))
			return sr;
		
		
		
		//continue
		for(SearchNode s:sr.getChildNodes()) {
			
			SearchNode next = searchUtil(s,gt,bound,curB+1) ;
			
			if(next != null)
				return next;
		}
		
		return null;
		
	}
	
	//use a bounded dfs to build a search tree
	private void buildUtil(SearchNode sr, int bound,int curB) {
		
		if(curB > bound)
			return;
		
		
		//apply all possible rule on current node and add as child
		for(RewriteRule r : engine.getRules()) {
			Node copyOfTerm = engine.copy(sr.getTermNode());
			if(engine.singleSearch(copyOfTerm, r))
				sr.addChild(new SearchNode(copyOfTerm,sr,r.getName()));
		}
		
		
		
		//dfs children 
		for(SearchNode c : sr.getChildNodes()) {
			buildUtil(c,bound,curB+1);
		}
		
		
	}
	
	
	
	
	
	

}
