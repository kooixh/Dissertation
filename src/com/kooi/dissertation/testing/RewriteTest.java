package com.kooi.dissertation.testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.Context;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.rewriter.RewriteRuleFactory;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

public class RewriteTest {
	
	
	static RewriteEngine r;
	static ASTParser p;
	@BeforeClass
	public static void initialise() {
		Set<Operator> ops = new HashSet<>();
		ops.add(new BinaryOperator("+",2,DataType.INT));
		ops.add(new BinaryOperator("-",2,DataType.INT));
		ops.add(new BinaryOperator("*",3,DataType.INT));
		ops.add(new BinaryOperator("/",3,DataType.INT));
		ops.add(new UnaryOperator("!",1,DataType.INT));
		ops.add(new UnaryOperator("succ",2,DataType.INT));
		
		HashMap<String,DataType> variables = new HashMap<>();
		variables.put("x",DataType.INT);
		variables.put("y",DataType.INT);
		
		Set<RewriteRule> rules = new HashSet<>();
		
		Context c = new Context(ops,variables);
		p = new ASTParser(c);
		
		RewriteRuleFactory f = new RewriteRuleFactory(p);
		
		rules.add(f.getRewriteRule("!!x", "x", "double negation"));
		rules.add(f.getRewriteRule("x+x", "x", "idempotent"));
		rules.add(f.getRewriteRule("True-x", "True", "identity"));
		rules.add(f.getRewriteRule("x-True", "True", "identity"));
		rules.add(f.getRewriteRule("x+0", "x", "adding zero"));
		rules.add(f.getRewriteRule("0+x", "x", "adding zero"));
		rules.add(f.getRewriteRule("succ(x)+y", "succ(x+y)", "succ rule"));
		
		r = new RewriteEngine(rules,c);
	}
	

	@Test
	public void testRewrite1() {
		
		String input = "True+True";
		String output = r.rewrite(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite2() {
		
		String input = "!!True";
		String output = r.rewrite(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite3() {
		
		String input = "True + False -(True + True)";
		String output = r.rewrite(input);
		String expected = "True False + True -";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite4() {
		
		String input = "True + False -(True + (False - False)) + True + True";
		String output = r.rewrite(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}
	@Test
	public void testRewrite5() {
		
		String input = "succ(succ(0)) + succ(0)";
		String output = r.rewrite(input);
		String expected = "0 succ succ succ";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone1() {
		String input = "True + False";
		String output = r.rewrite(input);
		String expected = "True + False";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone2() {
		String input = "True + 1";
		String output = r.rewrite(input);
		String expected = "True + 1";
		
		assertEquals(expected,output);
	}
	@Test 
	public void testRewriteNone3() {
		String input = "True";
		String output = r.rewrite(input);
		String expected = "True";
		
		assertEquals(expected,output);
	}

}
