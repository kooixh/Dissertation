package com.kooi.dissertation.syntaxtreetests;

import org.junit.Test;

import com.kooi.dissertation.syntaxtree.ASTNode;

import junit.framework.TestCase;

public class SyntaxTreeUnitTests extends TestCase {
	
	
	@Test
	public void testHashCodeTwoChild() {
		
		//create node 1
		ASTNode node1 = new ASTNode("Node 1");
		ASTNode node1_left = new ASTNode("Node 1 left");
		ASTNode node1_right = new ASTNode("Node 1 right");
		node1.setLeft(node1_left);
		node1.setRight(node1_right);
		
		
		//create node 2 with same values as node one but different objects
		ASTNode node2 = new ASTNode("Node 1");
		ASTNode node2_left = new ASTNode("Node 1 left");
		ASTNode node2_right = new ASTNode("Node 1 right");
		node2.setLeft(node2_left);
		node2.setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertEquals(node1.hashCode(),node2.hashCode());
	}
	@Test
	public void testHashCodeOneChild() {
		
		//create node 1
		ASTNode node1 = new ASTNode("Node 1");
		ASTNode node1_left = new ASTNode("Node 1 left");
		node1.setLeft(node1_left);

		
		
		//create node 2 with same values as node one but different objects
		ASTNode node2 = new ASTNode("Node 1");
		ASTNode node2_left = new ASTNode("Node 1 left");
		node2.setLeft(node2_left);
		
		assertNotSame(node1,node2);
		assertEquals(node1.hashCode(),node2.hashCode());
	}
	
	@Test
	public void testHashCodeDifferentTwoNodes() {
		
		//create node 1
		ASTNode node1 = new ASTNode("Node 1");
		ASTNode node1_left = new ASTNode("Node 1 left");
		ASTNode node1_right = new ASTNode("Node 1 right");
		node1.setLeft(node1_left);
		node1.setRight(node1_right);
		
		//create node 2 with different values as node one but different objects
		ASTNode node2 = new ASTNode("Node 1");
		ASTNode node2_left = new ASTNode("Node 1 left");
		ASTNode node2_right = new ASTNode("Node 1 right");
		node2.setRight(node2_left);
		node2.setLeft(node2_right); //childs swapped
		
		assertNotSame(node1,node2);
		assertFalse("Same hashcode", node1.hashCode()==node2.hashCode() );
	}
	
	@Test
	public void testEquals() {
		
		//create node 1
		ASTNode node1 = new ASTNode("Node 1");
		ASTNode node1_left = new ASTNode("Node 1 left");
		ASTNode node1_right = new ASTNode("Node 1 right");
		node1.setLeft(node1_left);
		node1.setRight(node1_right);
		
		
		//create node 2 with same values as node one but different objects
		ASTNode node2 = new ASTNode("Node 1");
		ASTNode node2_left = new ASTNode("Node 1 left");
		ASTNode node2_right = new ASTNode("Node 1 right");
		node2.setLeft(node2_left);
		node2.setRight(node2_right);
		
		assertNotSame(node1,node2);
		assertEquals(node1,node2);
	}
	
	@Test
	public void testNotEquals() {
		
		//create node 1
		ASTNode node1 = new ASTNode("Node 1");
		ASTNode node1_left = new ASTNode("Node 1 left");
		ASTNode node1_right = new ASTNode("Node 1 right");
		node1.setLeft(node1_left);
		node1.setRight(node1_right);
		
		
		//create node 2 with different values
		ASTNode node2 = new ASTNode("Node 2");
		ASTNode node2_left = new ASTNode("Node 2 left");
		node2.setLeft(node2_left);
		
		assertNotSame(node1,node2);
		assertFalse(node1.equals(node2));
	}

}
