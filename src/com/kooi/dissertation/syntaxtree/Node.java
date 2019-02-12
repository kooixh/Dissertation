package com.kooi.dissertation.syntaxtree;


/**
 * 
 * This is the interface for a Node. 
 * 
 * @author Kooi
 * @date 12 February 2019
 *
 */
public interface Node {
	
	String getValue();
	Node getRight();
	Node getLeft();
	public int hashCode();
	public boolean equals(Object obj);

}
