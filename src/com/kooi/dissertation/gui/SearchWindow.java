package com.kooi.dissertation.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.rewriter.RewriteException;
import com.kooi.dissertation.rewriter.SearchEngine;
import com.kooi.dissertation.rewriter.SearchNode;
import com.kooi.dissertation.rewriter.SearchResult;
import com.kooi.dissertation.syntaxtree.Node;

public class SearchWindow extends JFrame {

	
	//components
	private JPanel contentPane;
	private JTextField initialTermField;
	private JTextField goalTermField;
	private JTextField boundField;
	private JButton searchBtn;
	
	//reference to parent
	private InteractPanel iPanel;
	private HomeScreen home;
	
	//engines
	private SearchEngine sEngine;



	/**
	 * Create the frame.
	 */
	public SearchWindow(InteractPanel iPanel,HomeScreen h) {
		
		//init
		this.home = h;
		this.iPanel = iPanel;
		sEngine = new SearchEngine(home.getEngine());
		
		
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				dispose();
			}
		});
		
		
		//initial term panel
		JPanel panelInitial = new JPanel();
		initialTermField = new JTextField(20);
		initialTermField.setMaximumSize(initialTermField.getPreferredSize());
		panelInitial.setLayout(new BoxLayout(panelInitial, BoxLayout.X_AXIS));
		JLabel iniLab = new JLabel("Initial term");
		iniLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		panelInitial.add(iniLab);
		panelInitial.add(initialTermField);
		
		
		//goal panel
		JPanel panelGoal = new JPanel();
		goalTermField = new JTextField(20);
		panelGoal.setLayout(new BoxLayout(panelGoal, BoxLayout.X_AXIS));
		goalTermField.setMaximumSize(goalTermField.getPreferredSize());
		JLabel goalLab = new JLabel("Goal term");
		goalLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		panelGoal.add(goalLab);
		panelGoal.add(goalTermField);
		
		//bound panel
		JPanel boundPanel = new JPanel();
		boundField = new JTextField(10);
		boundPanel.setLayout(new BoxLayout(boundPanel, BoxLayout.X_AXIS));
		boundField.setMaximumSize(boundField.getPreferredSize());
		JLabel boundLab = new JLabel("Bound");
		boundLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		boundPanel.add(boundLab);
		boundPanel.add(boundField);
		
		
		//search button
		
		searchBtn = new JButton("Search");
		
		searchBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					
					
					String initTermString = initialTermField.getText();
					String goalTermString = goalTermField.getText();
					String boundString = boundField.getText();
					
					if(initTermString.equals("") || goalTermString.equals("") ||boundString.equals("")) {
						JOptionPane.showMessageDialog(SearchWindow.this, "Fields cannot be empty.","Missing values",JOptionPane.ERROR_MESSAGE);

					}else {
						Node initTermNode = home.getParser().parseAST(initTermString);
						Node goalTermNode = home.getParser().parseAST(goalTermString);
						int bound = Integer.parseInt(boundString);
						
						if(bound <= 0) {
							JOptionPane.showMessageDialog(SearchWindow.this, "Bound must be a number greater than 0.","Invalid values",JOptionPane.ERROR_MESSAGE);

						}else {
							SearchResult result = sEngine.searchTerm(initTermNode, goalTermNode, bound);

							if(result.getResult() == null) {
								iPanel.setResultArea("Term is not reachable");
								
							}else {
								StringBuilder sb = new StringBuilder();
								
								sb.append("Reachable!\n");
								sb.append(initialTermField.getText()+"\n");
								sb.append(buildResultText(result));
								
								
								iPanel.setResultArea(sb.toString());								
								
								
							}
							
							//thread for building the visualiser 
							(new Thread() {
								public void run() {
									SearchTreeVisualiser stv = new SearchTreeVisualiser(home,result.getSearchTree());
									stv.setVisible(true);
								  }
							}).start();
							SearchWindow.this.dispose();
						}
					}
					
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(SearchWindow.this, "An error is encountered during parsing, check for mismatch parenthesis.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}catch(NumberFormatException e2) {
					JOptionPane.showMessageDialog(SearchWindow.this, "Bound must be a number greater than 0.","Invalid values",JOptionPane.ERROR_MESSAGE);
					
				} catch (RewriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
	
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		contentPane.add(panelInitial);
		contentPane.add(panelGoal);
		contentPane.add(boundPanel);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
		btnPanel.add(searchBtn);
		contentPane.add(btnPanel);
		
		
		setContentPane(contentPane);
	}
	
	
	//this method builds the trace of the search
	private String buildResultText(SearchResult result) throws ParseException  {
		
		
		StringBuilder trace = new StringBuilder();
		
		Stack<SearchNode> nodes = new Stack<>();
		SearchNode res = result.getResult();
		
		while(res.getParentNode() != null) {
			nodes.push(res);
			res = res.getParentNode();
		}
		
		while(!nodes.isEmpty()) {
			SearchNode curr = nodes.pop();
			trace.append("\u2b91Apply "+curr.getPrevRule()+" to get "+home.getParser().toInfix(home.getParser().postOrderTreverse(curr.getTermNode())));
			trace.append("\n");
		}
		
		return trace.toString();
		
		
		
	}

}
