package com.kooixiuhong.rewritesystem.app.rewriter;

import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * This class represents a rewrite rule. A rule is made up of two terms. Here the
 * terms are fields with the root of their respective syntax tree.
 *
 * @author Kooi
 * @date 13 February 2019
 */
@Data
@AllArgsConstructor
public class RewriteRule implements Serializable {
    private Node lhs;
    private Node rhs;
    private String name;
}
