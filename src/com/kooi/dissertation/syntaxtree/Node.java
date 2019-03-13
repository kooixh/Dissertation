package com.kooi.dissertation.syntaxtree;

import java.io.Serializable;

/**
 * 
 * This is the interface for a Node. 
 * 
 * @author Kooi
 * @date 12 February 2019
 *
 */
public interface Node extends Serializable{
	
	String getValue();
	public DataType getType();
	public NodeType getNodeType();
	Node getRight();
	Node getLeft();
	public int hashCode();
	public boolean equals(Object obj);

}
