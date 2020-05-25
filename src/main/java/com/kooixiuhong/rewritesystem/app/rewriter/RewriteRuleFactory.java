package com.kooixiuhong.rewritesystem.app.rewriter;

import com.kooixiuhong.rewritesystem.app.parser.ASTParser;
import com.kooixiuhong.rewritesystem.app.parser.ParseException;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import lombok.AllArgsConstructor;

/**
 * Factory class to generate a rewrite rule given a lhs, rhs and rule name
 *
 * @author Kooi
 * @date 13 February 2019
 */
@AllArgsConstructor
public class RewriteRuleFactory {

    private ASTParser parser;

    /**
     * This methods generates a rewrite rule given infix string of lhs and rhs, rule also
     * requires a name.
     *
     * @param l    expression of lhs of rule in infix.
     * @param r    expression of rhs of rule in infix.
     * @param name name of the rule.
     * @return a rewrite rule from l's AST to r's AST
     */
    public RewriteRule getRewriteRule(String l, String r, String name) {
        try {
            Node lhs = parser.parseAST(l);
            Node rhs = parser.parseAST(r);
            return new RewriteRule(lhs, rhs, name);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
