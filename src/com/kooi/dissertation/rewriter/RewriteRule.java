package com.kooi.dissertation.rewriter;

import java.io.Serializable;

import com.kooi.dissertation.syntaxtree.Node;

/**
 * 
 * This class represents a rewrite rule. A rule is made up of two terms. Here the 
 * terms are fields with the root of their respective syntax tree.
 * 
 * 
 * @author Kooi
 * @date 13 February 2019 
 *
 */
public class RewriteRule implements Serializable {
	
	

	private Node lhs;
	private Node rhs;
	private String name;
	
	
	public RewriteRule(Node l, Node r, String name) {
		this.lhs = l;
		this.rhs = r;
		this.name = name;
	}

	//getters
	public Node getLhs() {
		return lhs;
	}
	public Node getRhs() {
		return rhs;
	}
	public String getName() {
		return name;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RewriteRule other = (RewriteRule) obj;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rhs == null) {
			if (other.rhs != null)
				return false;
		} else if (!rhs.equals(other.rhs))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
	

}
