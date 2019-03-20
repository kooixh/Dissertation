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

public class SearchTreeVisualiser extends JFrame{
	
	
	
	//graphs components
	private mxGraph graph;
	private Object parent;
	
	
	
	private Map<mxICell,SearchNode> mapping;
	
	//engines and result
	private HomeScreen home;
	private SearchResult result;
	
	
	public SearchTreeVisualiser(HomeScreen home,SearchResult result) {
		
		
		this.home = home;
		this.result = result;
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		
		
		mapping = new HashMap<>();
		
		
		//Object parent = graph.getDefaultParent();
		
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				dispose();
			}
		});
		
		// define layout
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		//mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false);  
		
		//((mxHierarchicalLayout)layout).setParentBorder(10);
		
		layout.setUseBoundingBox(false);

		graph.setCellsEditable(false);
		graph.setCellsMovable(false);
		graph.setCellsLocked(true);
		graph.setCellsResizable(false);
		
		
		try {
			buildGraph();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
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
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        }
			    }
				

				
			});
		
		
		//JPanel panel = new JPanel();
		//panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        //panel.setPreferredSize(new Dimension(100, 100));
		graphComponent.setConnectable(false);
		//graphComponent.setEnabled(true);
		//panel.add(graphComponent);
		
		//panel.setfor
		
		setMinimumSize(new Dimension(500,500));
		pack();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(graphComponent,BorderLayout.CENTER);
		getContentPane().setBackground(Color.WHITE);
	}
	
	
	private void buildGraph() throws ParseException {
		SearchNode searchRoot = result.getSearchTree();
		mxICell graphRoot = (mxICell) graph.insertVertex(parent, null, home.getParser().toInfix(home.getParser().postOrderTreverse(searchRoot.getTermNode())), 250, 250, 80, 30);
		
		graph.updateCellSize(graphRoot);
		
		buildGraphUtil(searchRoot,graphRoot,1);

	}
	
	
	private void buildGraphUtil(SearchNode sNode, mxICell currentCell,int depth) throws ParseException {
		
		for(SearchNode n : sNode.getChildNodes()) {
			//mxICell child = (mxICell) graph.insertVertex(parent, null, home.getParser().toInfix(home.getParser().postOrderTreverse(n.getTermNode())), 0, 0, 80, 30);
			mxICell child = (mxICell) graph.insertVertex(parent, null, "view term", 0, 0, 80, 30);
			mapping.put(child, n);

			graph.insertEdge(parent, "", "", currentCell, child);
			buildGraphUtil(n,child,depth+1);
		}
	}
	
	

}
