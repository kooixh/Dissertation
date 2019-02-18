package com.kooi.dissertation.drivertest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.Context;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.rewriter.RewriteRuleFactory;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

public class DriverProgram {

	public static void main(String args[]) {
		
		Set<Operator> opsB = new HashSet<>();
		opsB.add(new BinaryOperator("AND",2,DataType.BOOLEAN));
		opsB.add(new BinaryOperator("OR",2,DataType.BOOLEAN));
		opsB.add(new UnaryOperator("NOT",1,DataType.BOOLEAN));
		
		HashMap<String,DataType> variablesB = new HashMap<>();
		variablesB.put("T",DataType.BOOLEAN);
		variablesB.put("F",DataType.BOOLEAN);
		variablesB.put("B",DataType.BOOLEAN);
		
		Context cBool = new Context(opsB,variablesB);
		ASTParser boolP = new ASTParser(cBool);
		
		RewriteRuleFactory fB = new RewriteRuleFactory(boolP);
		
		Set<RewriteRule> rulesB = new HashSet<>();
		
		rulesB.add(fB.getRewriteRule("NOT NOT B", "B", "double negation"));
		rulesB.add(fB.getRewriteRule("B AND B", "B", "idempotent"));
		rulesB.add(fB.getRewriteRule("True OR B", "True", "identity"));
		rulesB.add(fB.getRewriteRule("B OR True", "True", "identity"));
		rulesB.add(fB.getRewriteRule("False OR False", "False", "identity"));
		rulesB.add(fB.getRewriteRule("B AND False", "False", "AND-identity"));
		rulesB.add(fB.getRewriteRule("False AND B", "False", "AND- identity"));

		RewriteEngine r = new RewriteEngine(rulesB,boolP);
		try {
			
			String s = "True AND False OR(True AND (False OR False)) AND True AND True";
			System.out.println("Expression: "+s);
			
			System.out.println("\n\nRewrite using rules: "+r.rewriteInfix(s));
			Node n = boolP.parseAST(s);
			//Node n = p.parseAST("NOT NOT TRUE");
			//prettyPrint(n);
			
			
			
			
			
			
			//System.out.println(p.toInfix("13 4 + 2 *"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	 public static void prettyPrint(Node root)
	    {
	        List<List<String>> lines = new ArrayList<List<String>>();

	        List<Node> level = new ArrayList<Node>();
	        List<Node> next = new ArrayList<Node>();

	        level.add(root);
	        int nn = 1;

	        int widest = 0;

	        while (nn != 0) {
	            List<String> line = new ArrayList<String>();

	            nn = 0;

	            for (Node n : level) {
	                if (n == null) {
	                    line.add(null);

	                    next.add(null);
	                    next.add(null);
	                } else {
	                    String aa = n.toString();
	                    line.add(aa);
	                    if (aa.length() > widest) widest = aa.length();

	                    next.add(n.getLeft());
	                    next.add(n.getRight());

	                    if (n.getLeft() != null) nn++;
	                    if (n.getRight() != null) nn++;
	                }
	            }

	            if (widest % 2 == 1) widest++;

	            lines.add(line);

	            List<Node> tmp = level;
	            level = next;
	            next = tmp;
	            next.clear();
	        }

	        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
	        for (int i = 0; i < lines.size(); i++) {
	            List<String> line = lines.get(i);
	            int hpw = (int) Math.floor(perpiece / 2f) - 1;

	            if (i > 0) {
	                for (int j = 0; j < line.size(); j++) {

	                    // split node
	                    char c = ' ';
	                    if (j % 2 == 1) {
	                        if (line.get(j - 1) != null) {
	                            c = (line.get(j) != null) ? '┴' : '┘';
	                        } else {
	                            if (j < line.size() && line.get(j) != null) c = '└';
	                        }
	                    }
	                    System.out.print(c);

	                    // lines and spaces
	                    if (line.get(j) == null) {
	                        for (int k = 0; k < perpiece - 1; k++) {
	                            System.out.print(" ");
	                        }
	                    } else {

	                        for (int k = 0; k < hpw; k++) {
	                            System.out.print(j % 2 == 0 ? " " : "─");
	                        }
	                        System.out.print(j % 2 == 0 ? "┌" : "┐");
	                        for (int k = 0; k < hpw; k++) {
	                            System.out.print(j % 2 == 0 ? "─" : " ");
	                        }
	                    }
	                }
	                System.out.println();
	            }

	            // print line of numbers
	            for (int j = 0; j < line.size(); j++) {

	                String f = line.get(j);
	                if (f == null) f = "";
	                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
	                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

	                // a number
	                for (int k = 0; k < gap1; k++) {
	                    System.out.print(" ");
	                }
	                System.out.print(f);
	                for (int k = 0; k < gap2; k++) {
	                    System.out.print(" ");
	                }
	            }
	            System.out.println();

	            perpiece /= 2;
	        }
	    }
	
	
	public static void printPreOrder(Node n) {
		if(n == null) {
			System.out.println("null");
			return;
		}
			
		
		System.out.println(n.getValue());
		System.out.print("left ");
		printPreOrder(n.getLeft());
		System.out.print("right ");
		printPreOrder(n.getRight());
	}
}
