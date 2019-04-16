package com.kooi.dissertation.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.rewriter.SearchNode;
import com.kooi.dissertation.rewriter.SearchResult;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

/**
 * 
 * 
 * This class is a window for the tree visuliser, it uses JGraph's mxgraph for the graphing
 * 
 * @author Kooi
 * @date 21th March 2019 
 *
 */
public class SearchTreeVisualiser extends JFrame{
	
	
	
	//graphs components
	private mxGraph graph;
	private Object parent;
	
	
	
	private Map<mxICell,SearchNode> mapping;
	
	//engines and result
	private HomeScreen home;
	private SearchNode root;
	
	
	public SearchTreeVisualiser(HomeScreen home,SearchNode r) {
		
		
		this.home = home;
		this.root = r;
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		
		
		mapping = new HashMap<>();
		
		
		
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
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
			JOptionPane.showMessageDialog(SearchTreeVisualiser.this, "An error is encountered during parsing, check for mismatch parenthesis.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
		
		
		
		
		layout.execute(graph.getDefaultParent());

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		//graphComponent.setPreferredSize(new Dimension(400,400));
		graphComponent.setMinimumSize(new Dimension(500,500));

		
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() 
		{
				@Override
			    public void mouseReleased(MouseEvent e) 
			    {    
			        mxCell cell =(mxCell) graphComponent.getCellAt(e.getX(), e.getY());
			        if(cell != null && !cell.isEdge())
			        {
			        	try {
			        		
			        		if(mapping.get(cell) != null) {
			        			
			        			SearchNode selNode = mapping.get(cell);
			        			
			        			StringBuilder sb = new StringBuilder();
			        			
			        			sb.append("Current state: "+home.getParser().toInfix(home.getParser().postOrderTreverse(selNode.getTermNode()))+"\n");
			        			sb.append("Applied: "+selNode.getPrevRule()+" on \n");
			        			sb.append("Previous state: "+home.getParser().toInfix(home.getParser().postOrderTreverse(selNode.getParentNode().getTermNode()))+"\n");
			        			
			        			
			        			
			        			JOptionPane.showMessageDialog(null, sb.toString());			        			
			        		}
						} catch (ParseException e1) {
							JOptionPane.showMessageDialog(SearchTreeVisualiser.this, "An error is encountered during parsing, check for mismatch parenthesis.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
			        }
			    }
				

				
			});
		
		

		graphComponent.setConnectable(false);

		
		setMinimumSize(new Dimension(500,500));
		pack();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(graphComponent,BorderLayout.CENTER);
		getContentPane().setBackground(Color.WHITE);
	}
	
	
	private void buildGraph() throws ParseException {
		mxICell graphRoot = (mxICell) graph.insertVertex(parent, null, home.getParser().toInfix(home.getParser().postOrderTreverse(root.getTermNode())), 250, 250, 80, 30);
		
		graph.updateCellSize(graphRoot);
		
		buildGraphUtil(root,graphRoot,1);

	}
	
	//build the graph using a depth first search
	private void buildGraphUtil(SearchNode sNode, mxICell currentCell,int depth) throws ParseException {
		
		for(SearchNode n : sNode.getChildNodes()) {
			
			
			mxICell child = (mxICell) graph.insertVertex(parent, null, home.getParser().toInfix(home.getParser().postOrderTreverse(n.getTermNode())), 0, 0, 80, 30);
			mapping.put(child, n);
			
			
			//insert edge then go further down
			graph.insertEdge(parent, "", n.getPrevRule(), currentCell, child);
			graph.updateCellSize(child);
			buildGraphUtil(n,child,depth+1);
		}
	}
	
	

}
