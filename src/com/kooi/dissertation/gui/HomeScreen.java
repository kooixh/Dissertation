package com.kooi.dissertation.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.parser.Signature;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.Operator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.BoxLayout;
/**
 * 
 * This class is the entry point of the system. Contains the main method that loads the
 * UI.
 * 
 * @author Kooi
 *
 */
public class HomeScreen extends JFrame {

	
	//components
	private JPanel contentPane;
	private JPanel rulePane;
	private JPanel sigPanel;
	private JPanel opPanel;
	private JPanel varPanel;
	private InteractPanel iPanel;
	
	
	//engine, signature and parser
	private RewriteEngine engine;
	private ASTParser parser;
	private Signature sig;

	
	//file paths
	private final String SAVE_PATH = "saves";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeScreen frame = new HomeScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HomeScreen() {
		
		
		sig = new Signature();
		parser = new ASTParser(sig);
		engine = new RewriteEngine(parser);
		
		
		File saveDir = new File(SAVE_PATH);
		if(!saveDir.exists()) {
			saveDir.mkdir();
		}
		
		
		setTitle("Rewrite System");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 0));
		
		
		
		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);
		
		JButton saveBut = new JButton("Save");
		
		//create a save dialog and save file as .rwr file
		saveBut.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(saveDir);
				FileFilter filter = new FileNameExtensionFilter("Rewrite File","rwr");
				fileChooser.setFileFilter(filter);
				if (fileChooser.showSaveDialog(HomeScreen.this) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  
				  StringBuilder path = new StringBuilder(file.toString());
				  
				  if(!(path.toString()).endsWith(".rwr"))
					  path.append(".rwr");
				  
				  try{
						ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path.toString()));
						out.writeObject(new Configuration(HomeScreen.this.engine,HomeScreen.this.parser,HomeScreen.this.sig));
						out.close();
						JOptionPane.showMessageDialog(null, "Saved at"+path.toString());

						}catch(IOException ioe){
							ioe.printStackTrace();
							JOptionPane.showMessageDialog(null, "Problem writing configuration file.");
						}
				}
				
				
			}
			
		});
		
		
		JButton loadBut = new JButton("Load");
		loadBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(saveDir);
				FileFilter filter = new FileNameExtensionFilter("Rewrite File","rwr");
				fileChooser.setFileFilter(filter);
				
				if (fileChooser.showOpenDialog(HomeScreen.this) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  
				  try{
						ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toString()));
						Configuration c = (Configuration) in.readObject();
						
						HomeScreen.this.sig = c.getSig();
						HomeScreen.this.engine = c.getRw();
						HomeScreen.this.parser = new ASTParser(HomeScreen.this.sig);
						
						
						HomeScreen.this.remove(iPanel);
						
						iPanel = new InteractPanel(HomeScreen.this,engine);
						HomeScreen.this.add(iPanel,BorderLayout.CENTER);
						HomeScreen.this.revalidate();
						HomeScreen.this.repaint();
						
						
						
						updateUI();
						in.close();
					}catch(IOException ioe){
						ioe.printStackTrace();
						JOptionPane.showMessageDialog(null, "Problem reading configuration file.");
					}catch(ClassNotFoundException cnfe){
						JOptionPane.showMessageDialog(null, "Problem reading configuration file.");
					}
				  
				}
			}
		});
		
		jtb.add(loadBut);
		jtb.add(saveBut);
		
		this.add(jtb,BorderLayout.PAGE_START);
		
		
		sigPanel = new JPanel();
		sigPanel.setLayout(new BoxLayout(sigPanel, BoxLayout.Y_AXIS));
		
		JLabel sigLabel = new JLabel("Signature \u03a3");
		sigLabel.setFont(new Font("Century Gothic",Font.BOLD,24));		
		sigPanel.add(sigLabel);
		
		
		
		
		//begin of op 
		
		
		JLabel opLabel = new JLabel("Operators:");
		opLabel.setFont(new Font("Century Gothic",Font.BOLD,14));
		sigPanel.add(opLabel);
	
		//right
		JPanel sidePane = new JPanel();
		sidePane.setLayout(new BoxLayout(sidePane,BoxLayout.Y_AXIS));
		
		
		JScrollPane scrollPane = new JScrollPane(sigPanel);
		
		sidePane.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(200,200));
		
		opPanel = new JPanel();
		opPanel.setLayout(new BoxLayout(opPanel, BoxLayout.Y_AXIS));
		sigPanel.add(opPanel);
		
		
		JSeparator js = new JSeparator();
		js.setMaximumSize(js.getPreferredSize());
		
		opPanel.add(js);

		
		
		// begin of vars
		
		varPanel = new JPanel();
		varPanel.setLayout(new BoxLayout(varPanel, BoxLayout.Y_AXIS));

		
		JLabel varLabel = new JLabel("Variables:");
		varLabel.setFont(new Font("Century Gothic",Font.BOLD,14));
		
		varPanel.add(varLabel);
		
		
		sigPanel.add(varPanel);
		
		
		
		
		rulePane = new JPanel();
		
		rulePane.setLayout(new BoxLayout(rulePane,BoxLayout.Y_AXIS));
		JLabel ruleLabel = new JLabel("Rules:");
		ruleLabel.setFont(new Font("Century Gothic",Font.BOLD,24));
		rulePane.add(ruleLabel);

		
		JScrollPane ruleScrollPane = new JScrollPane(rulePane);
		ruleScrollPane.setPreferredSize(new Dimension(200,200));
		
		
		sidePane.add(ruleScrollPane);
		
		iPanel = new InteractPanel(this,engine);
		
		contentPane.add(iPanel,BorderLayout.CENTER);
		contentPane.add(sidePane,BorderLayout.EAST);
		setMinimumSize(new Dimension(700,500));
		
	}
	
	/**
	 * 
	 * 
	 * Refresh the panel for operators, variables and rules
	 * 
	 */
	public void updateUI() {
		opPanel.removeAll();
		opPanel.revalidate();
		opPanel.repaint();
		varPanel.removeAll();
		varPanel.revalidate();
		varPanel.repaint();
		rulePane.removeAll();
		rulePane.revalidate();
		rulePane.repaint();
		
		
		for(String op : sig.getOperatorSet()) {
			
			StringBuilder opText = new StringBuilder();
			
			if(sig.getOperator(op) instanceof BinaryOperator) {
				opText.append(": op1 \u2715 op2 \u2192 ");
				opText.append(sig.getOperator(op).getReturnType());
			}else {
				opText.append(": op1 \u2192 "+sig.getOperator(op).getReturnType());
			}
			
			JLabel l = new JLabel(op+opText.toString());
			l.setFont(new Font("Century Gothic",Font.PLAIN,12));
			
			l.addMouseListener(new MouseAdapter() {
				
				@Override
			    public void mouseReleased(MouseEvent e) 
			    {    
					
					Object[] options = {"Okay",
                    "Delete Operator!"};
					
					Operator o = sig.getOperator(op);
					
					
					int n = JOptionPane.showOptionDialog(
					    HomeScreen.this,
					    "Operator symbol: "+op+"\nType: "+((o instanceof BinaryOperator)?"Binary":"Unary")+"\nReturn type:"+o.getReturnType()+"\nPrecedence: "+o.getPrecedence(),
					    "Operator info",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.INFORMATION_MESSAGE,
					    null,
					    options,
					    options[0]);
					
					
					if(n == 1) {
						sig.deleteOperator(op);
				        updateUI();
					}
			    }
				
			});
			
			opPanel.add(l);
		}
		
		JSeparator js = new JSeparator();
		js.setMaximumSize(js.getPreferredSize());
		
		opPanel.add(js);
		
		
		JLabel varLabel = new JLabel("Variables:");
		varLabel.setFont(new Font("Century Gothic",Font.BOLD,14));
		varPanel.add(varLabel);
		
		
		for(String id: sig.getVariableSet()) {
			JLabel l = new JLabel(id+":"+sig.getVariable(id));
			l.setFont(new Font("Century Gothic",Font.PLAIN,12));
			
			l.addMouseListener(new MouseAdapter() {
				
				@Override
			    public void mouseReleased(MouseEvent e) 
			    {    
					
					Object[] options = {"Okay",
                    "Delete variable!"};
					
					//default icon, custom title
					int n = JOptionPane.showOptionDialog(
					    HomeScreen.this,
					    "Varibale id: "+id+"\nType:"+sig.getVariable(id),
					    "Varibale info",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.INFORMATION_MESSAGE,
					    null,
					    options,
					    options[0]);
					
					
					if(n == 1) {
						sig.deleteVariable(id);
				        updateUI();
					}
			        
			    }
				
			});
			
			varPanel.add(l);
		}
		
		rulePane.removeAll();
		rulePane.revalidate();
		rulePane.repaint();
		//show the rules
		JLabel ruleLabel = new JLabel("Rules:");
		ruleLabel.setFont(new Font("Century Gothic",Font.BOLD,24));
		rulePane.add(ruleLabel);
		
		for(RewriteRule r : engine.getRules()) {
			try {
				JLabel l = new JLabel(parser.toInfix(parser.postOrderTreverse(r.getLhs()))+"\u27f6"+parser.toInfix(parser.postOrderTreverse(r.getRhs())));
				l.setFont(new Font("Century Gothic",Font.PLAIN,12));
				
				l.addMouseListener(new MouseAdapter() {
					
					@Override
				    public void mouseReleased(MouseEvent e) 
				    {    
						
						
						Object[] options = {"Okay",
	                    "Delete rule!"};
						
						//default icon, custom title
						int n = JOptionPane.showOptionDialog(
						    HomeScreen.this,
						    l.getText()+"\nRule name:"+r.getName(),
						    "Rewrite Rule info",
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.INFORMATION_MESSAGE,
						    null,
						    options,
						    options[0]);
						
						if(n == 1) {
							engine.deleteRule(r);
							updateUI();
						}
							
				    }
					
				});
				
				rulePane.add(l);
			} catch (ParseException e) {
				JOptionPane.showMessageDialog(HomeScreen.this, "An error is encountered during parsing, check for mismatch parenthesis.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	
	//getters for engine, parsers and sig 

	public RewriteEngine getEngine() {
		return engine;
	}

	public ASTParser getParser() {
		return parser;
	}

	public Signature getSig() {
		return sig;
	}

}
