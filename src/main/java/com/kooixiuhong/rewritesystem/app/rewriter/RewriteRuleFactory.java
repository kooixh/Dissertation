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
     * @param leftHandSide    expression of lhs of rule in infix.
     * @param rightHandSide    expression of rhs of rule in infix.
     * @param name name of the rule.
     * @return a rewrite rule from leftHandSide's AST to rightHandSide's AST
     */
    public RewriteRule getRewriteRule(String leftHandSide, String rightHandSide, String name) {
        try {
            Node lhs = parser.parseAST(leftHandSide);
            Node rhs = parser.parseAST(rightHandSide);
            return new RewriteRule(lhs, rhs, name);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
