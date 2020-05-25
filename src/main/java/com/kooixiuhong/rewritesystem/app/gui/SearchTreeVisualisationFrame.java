package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.parser.ParseException;
import com.kooixiuhong.rewritesystem.app.rewriter.SearchNode;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a window for the tree visuliser, it uses JGraph's mxgraph for the graphing
 *
 * @author Kooi
 * @date 21th March 2019
 */
public class SearchTreeVisualisationFrame extends JFrame {


    //graphs components
    private mxGraph graph;
    private Object parent;

    private Map<mxICell, SearchNode> mapping;

    //engines and result
    private HomeScreen home;
    private SearchNode root;

    public SearchTreeVisualisationFrame(HomeScreen home, SearchNode root) {

        this.home = home;
        this.root = root;
        this.graph = new mxGraph();
        this.parent = graph.getDefaultParent();
        this.mapping = new HashMap<>();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);

        layout.setUseBoundingBox(false);

        graph.setCellsEditable(false);
        graph.setCellsMovable(false);
        graph.setCellsLocked(true);
        graph.setCellsResizable(false);

        try {
            buildGraph();
        } catch (ParseException e1) {
            JOptionPane.showMessageDialog(SearchTreeVisualisationFrame.this, "An error is encountered during parsing, check for mismatch parenthesis.", "Parsing Exception", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }

        layout.execute(graph.getDefaultParent());

        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        //graphComponent.setPreferredSize(new Dimension(400,400));
        graphComponent.setMinimumSize(new Dimension(500, 500));

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && !cell.isEdge()) {
                    try {

                        if (mapping.get(cell) != null) {

                            SearchNode selNode = mapping.get(cell);

                            StringBuilder sb = new StringBuilder();

                            sb.append("Current state: ")
                                    .append(home.getParser().toInfix(home.getParser().postOrderTreverse(selNode.getTermNode())))
                                    .append("\n");
                            sb.append("Applied: ").append(selNode.getPrevRule()).append(" on \n");
                            sb.append("Previous state: ")
                                    .append(home.getParser().toInfix(home.getParser().postOrderTreverse(selNode.getParentNode().getTermNode())))
                                    .append("\n");

                            JOptionPane.showMessageDialog(null, sb.toString());
                        }
                    } catch (ParseException pe) {
                        JOptionPane.showMessageDialog(SearchTreeVisualisationFrame.this, "An error is encountered during parsing, check for mismatch parenthesis.", "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                        pe.printStackTrace();
                    }
                }
            }


        });


        graphComponent.setConnectable(false);

        setMinimumSize(new Dimension(500, 500));
        pack();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(graphComponent, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);

    }

    private void buildGraph() throws ParseException {
        mxICell graphRoot = (mxICell) graph.insertVertex(parent, null,
                home.getParser().toInfix(home.getParser().postOrderTreverse(root.getTermNode())), 250, 250,
                80, 30);
        graph.updateCellSize(graphRoot);
        buildGraphUtil(root, graphRoot, 1);
    }

    //build the graph using a depth first search
    private void buildGraphUtil(SearchNode sNode, mxICell currentCell, int depth) throws ParseException {
        for (SearchNode node : sNode.getChildNodes()) {
            mxICell child = (mxICell) graph.insertVertex(parent, null, home.getParser().toInfix(home.getParser().postOrderTreverse(node.getTermNode())), 0, 0, 80, 30);
            mapping.put(child, node);

            //insert edge then go further down
            graph.insertEdge(parent, "", node.getPrevRule(), currentCell, child);
            graph.updateCellSize(child);
            buildGraphUtil(node, child, depth + 1);
        }
    }

}
