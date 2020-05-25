package com.kooixiuhong.rewritesystem.app.rewriter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class is used to hold a search result
 *
 * @author Kooi
 * @date 18th March 2019
 */
@AllArgsConstructor
@Getter
public class SearchResult {
    private SearchNode searchTree;
    private SearchNode result;
}
