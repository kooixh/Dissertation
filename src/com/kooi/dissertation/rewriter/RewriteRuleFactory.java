package com.kooi.dissertation.rewriter;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.syntaxtree.Node;

/**
 * 
 * 
 * Factory class to generate a rewrite rule given a lhs, rhs and rule name
 * 
 * @author Kooi
 * @date 13 February 2019
 *
 */
public class RewriteRuleFactory {

	
	private ASTParser p;
	
	public RewriteRuleFactory(ASTParser p) {
		this.p = p;
		
	}
	
	
	public RewriteRule getRewriteRule(String l,String r,String name) {
		
	
		
		try {
			Node lhs = p.parseAST(l);
			Node rhs = p.parseAST(r);
			return new RewriteRule(lhs,rhs,name);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
}
