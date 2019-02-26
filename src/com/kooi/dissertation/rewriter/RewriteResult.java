package com.kooi.dissertation.rewriter;

import java.util.ArrayList;
import java.util.List;

import com.kooi.dissertation.syntaxtree.ASTNode;
import com.kooi.dissertation.syntaxtree.Node;

/**
 * 
 * This class stores a rewrite result. It stores a list of rewrite state. 
 * This class is immutable
 * 
 * @author Kooi
 *	@date 21 February 2019 
 */
public final class RewriteResult {

	
	private final String initialTerm;
	private final List<RewriteStep> listOfSteps; 
	private final Node finalTerm;
	
	public RewriteResult(String initial,List<RewriteStep> steps,Node finalTerm) {
		this.initialTerm = initial;
		this.listOfSteps = new ArrayList<>(steps);
		this.finalTerm = copy(finalTerm);
	}
	
	
	/**
	 * 
	 * 
	 * Get the initial term
	 * 
	 * @return the initial term string.
	 */
	public String getInitialTerm() {
		return this.initialTerm;
	}
	
	/**
	 * 
	 * A getter method for steps with defensive copying
	 * 
	 * @return a defensive copied list of rewrite step
	 */
	public List<RewriteStep> getListOfSteps() {
		return new ArrayList<>(listOfSteps);
	}
	
	
	/**
	 * 
	 * A getter method for final term with defensive copying
	 * 
	 * @return
	 */
	public Node getFinalTerm() {
		return copy(finalTerm);
	}
	
	
	private Node copy(Node n) {
		
		if(n == null)
			return null;
		Node newNode = new ASTNode(n.getValue(),copy(n.getLeft()),copy(n.getRight()),n.getType(),n.getNodeType());
		return newNode;
		
	}
}
