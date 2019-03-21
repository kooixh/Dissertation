package com.kooi.dissertation.drivertest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class GraphicTest extends JFrame{
	
	
	
	
	void buildGraph(mxGraph g) {
		
		String strings[] = {"one","two","three","four","five","six"};
		Object parent = g.getDefaultParent();
		
		
		HashMap<String,mxICell> mapping = new HashMap<>();
		
		//List<mxICell> vertex = new ArrayList<>();		
		g.getModel().beginUpdate();
		for(int i=0;i<6;i++) {
			
			mxICell a = (mxICell) g.insertVertex(parent, null, strings[i], 0, 0, 80, 30);
			
			mapping.put(strings[i], a);
		}
		
		for(int i=1;i<strings.length;i++) {
			//g.insertEdge(parent, null, "", mapping.get(strings[i-1]), mapping.get(strings[i]));
		}
		
		g.insertEdge(parent, null, "", mapping.get("one"), mapping.get("two"));
		g.insertEdge(parent, null, "", mapping.get("one"), mapping.get("three"));
		g.insertEdge(parent, null, "", mapping.get("two"), mapping.get("four"));
		g.insertEdge(parent, null, "", mapping.get("three"), mapping.get("five"));
		g.insertEdge(parent, null, "", mapping.get("three"), mapping.get("six"));
		
		g.getModel().endUpdate();
	}
	
	
	public GraphicTest() {
		final mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		// define layout
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		
		//((mxHierarchicalLayout)layout).setParentBorder(10);
		
		layout.setUseBoundingBox(false);

		graph.setCellsEditable(false);
		graph.setCellsMovable(false);
		graph.setCellsLocked(true);
		graph.setCellsResizable(false);
		//graph.setEnabled(false);
		buildGraph(graph);
		
		
		
		
		
		layout.execute(graph.getDefaultParent());

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setPreferredSize(new Dimension(400,400));
		
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() 
		{
				@Override
			    public void mouseReleased(MouseEvent e) 
			    {    
			        mxCell cell =(mxCell) graphComponent.getCellAt(e.getX(), e.getY());
			        if(cell != null && !cell.isEdge())
			        {
			        	JOptionPane.showMessageDialog(null, cell.getValue());
			        }
			    }
				

				
			});
		
		//graphComponent.getViewport().setBackground(Color.white);
		//graphComponent.getViewport().setPreferredSize(new Dimension(400,400));
		
		JPanel panel = new JPanel();
		//panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        //panel.setPreferredSize(new Dimension(100, 100));
		graphComponent.setConnectable(false);
		//graphComponent.setEnabled(true);
		panel.add(graphComponent);
		
		//panel.setfor
		
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(graphComponent,new GridBagConstraints());
		getContentPane().setBackground(Color.WHITE);

	}
	
	

	
	public static void main(String args[]) throws Exception{
		   GraphicTest gt = new GraphicTest();
		   gt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   gt.setMinimumSize(new Dimension(500,500));
		   gt.pack();
		   //gt.setSize(1000, 1000);
		   gt.setVisible(true);
	        
	}
	
}
