package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.parser.ASTParser;
import com.kooixiuhong.rewritesystem.app.parser.ParseException;
import com.kooixiuhong.rewritesystem.app.parser.Signature;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteEngine;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteRule;
import com.kooixiuhong.rewritesystem.app.syntaxtree.BinaryOperator;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Operator;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
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

/**
 * This class is the entry point of the system. Contains the main method that loads the
 * UI.
 *
 * @author Kooi
 */
public class HomeScreen extends JFrame {


    //components
    private JPanel contentPane;
    private JPanel rulePane;
    private JPanel sigPanel;
    private JPanel opPanel;
    private JPanel varPanel;
    private InteractPanel interactPanel;


    //engine, signature and parser
    @Getter private RewriteEngine engine;
    @Getter private ASTParser parser;
    @Getter private Signature signature;


    //file paths
    private final String SAVE_PATH = "saves";

    /**
     * Create the frame.
     */
    public HomeScreen() {

        signature = new Signature();
        parser = new ASTParser(signature);
        engine = new RewriteEngine(parser);

        final File saveDir = new File(SAVE_PATH);
        if (!saveDir.exists()) {
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


        JButton newBut = new JButton("New");

        newBut.addActionListener(e -> {


            int n = JOptionPane.showConfirmDialog(
                    HomeScreen.this,
                    "Do you wish to start a new rewrite system? Signature and rules will be cleared.",
                    "Start new",
                    JOptionPane.YES_NO_OPTION);

            if (n == 0) {
                HomeScreen.this.signature = new Signature();
                parser = new ASTParser(signature);
                engine = new RewriteEngine(parser);


                HomeScreen.this.remove(interactPanel);

                interactPanel = new InteractPanel(HomeScreen.this, engine);
                HomeScreen.this.add(interactPanel, BorderLayout.CENTER);
                HomeScreen.this.revalidate();
                HomeScreen.this.repaint();

                updateUI();
            }

        });

        JButton saveBut = new JButton("Save");

        //create a save dialog and save file as .rwr file
        saveBut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(saveDir);
                FileFilter filter = new FileNameExtensionFilter("Rewrite File", "rwr");
                fileChooser.setFileFilter(filter);
                if (fileChooser.showSaveDialog(HomeScreen.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    StringBuilder path = new StringBuilder(file.toString());

                    if (!(path.toString()).endsWith(".rwr"))
                        path.append(".rwr");

                    try {
                        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path.toString()));
                        out.writeObject(new Configuration(HomeScreen.this.engine, HomeScreen.this.parser, HomeScreen.this.signature));
                        out.close();
                        JOptionPane.showMessageDialog(null, "Saved at" + path.toString());

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Problem writing configuration file.");
                    }
                }

            }

        });


        JButton loadBut = new JButton("Load");
        loadBut.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(saveDir);
            FileFilter filter = new FileNameExtensionFilter("Rewrite File", "rwr");
            fileChooser.setFileFilter(filter);

            if (fileChooser.showOpenDialog(HomeScreen.this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toString()));
                    Configuration c = (Configuration) in.readObject();

                    HomeScreen.this.signature = c.getSignature();
                    HomeScreen.this.engine = c.getRewriteEngine();
                    HomeScreen.this.parser = new ASTParser(HomeScreen.this.signature);

                    HomeScreen.this.remove(interactPanel);

                    interactPanel = new InteractPanel(HomeScreen.this, engine);
                    HomeScreen.this.add(interactPanel, BorderLayout.CENTER);
                    HomeScreen.this.revalidate();
                    HomeScreen.this.repaint();

                    updateUI();
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Problem reading configuration file.");
                } catch (ClassNotFoundException cnfe) {
                    JOptionPane.showMessageDialog(null, "Problem reading configuration file.");
                }

            }
        });

        jtb.add(newBut);
        jtb.add(loadBut);
        jtb.add(saveBut);

        this.add(jtb, BorderLayout.PAGE_START);

        sigPanel = new JPanel();
        sigPanel.setLayout(new BoxLayout(sigPanel, BoxLayout.Y_AXIS));

        JLabel sigLabel = new JLabel("Signature \u03a3");
        sigLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        sigPanel.add(sigLabel);


        //begin of op


        JLabel opLabel = new JLabel("Operators:");
        opLabel.setFont(new Font("Century Gothic", Font.BOLD, 14));
        sigPanel.add(opLabel);

        //right
        JPanel sidePane = new JPanel();
        sidePane.setLayout(new BoxLayout(sidePane, BoxLayout.Y_AXIS));


        JScrollPane scrollPane = new JScrollPane(sigPanel);

        sidePane.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(200, 200));

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
        varLabel.setFont(new Font("Century Gothic", Font.BOLD, 14));

        varPanel.add(varLabel);


        sigPanel.add(varPanel);


        rulePane = new JPanel();

        rulePane.setLayout(new BoxLayout(rulePane, BoxLayout.Y_AXIS));
        JLabel ruleLabel = new JLabel("Rules:");
        ruleLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        rulePane.add(ruleLabel);


        JScrollPane ruleScrollPane = new JScrollPane(rulePane);
        ruleScrollPane.setPreferredSize(new Dimension(200, 200));


        sidePane.add(ruleScrollPane);

        interactPanel = new InteractPanel(this, engine);

        contentPane.add(interactPanel, BorderLayout.CENTER);
        contentPane.add(sidePane, BorderLayout.EAST);
        setMinimumSize(new Dimension(700, 500));

    }

    /**
     * Refresh the panel for operators, variables and rules
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

        for (final String operatorSymbol : signature.getOperatorSet()) {

            StringBuilder opText = new StringBuilder();

            if (signature.getOperator(operatorSymbol) instanceof BinaryOperator) {
                opText.append(": op1 \u2715 op2 \u2192 ");
                opText.append(signature.getOperator(operatorSymbol).getReturnType());
            } else {
                opText.append(": op1 \u2192 ").append(signature.getOperator(operatorSymbol).getReturnType());
            }

            JLabel l = new JLabel(operatorSymbol + opText.toString());
            l.setFont(new Font("Century Gothic", Font.PLAIN, 12));

            l.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {

                    Object[] options = {"Okay", "Delete Operator!"};
                    Operator operator = signature.getOperator(operatorSymbol);

                    int n = JOptionPane.showOptionDialog(
                            HomeScreen.this,
                            "Operator symbol: " + operatorSymbol + "\nType: " +
                                    ((operator instanceof BinaryOperator) ? "Binary" : "Unary") + "\nReturn type:"
                                    + operator.getReturnType() + "\nPrecedence: " + operator.getPrecedence(),
                            "Operator info",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (n == 1) {
                        signature.deleteOperator(operatorSymbol);
                        updateUI();
                    }
                }
            });
            opPanel.add(l);
        }

        JSeparator jSeparator = new JSeparator();
        jSeparator.setMaximumSize(jSeparator.getPreferredSize());
        opPanel.add(jSeparator);

        JLabel varLabel = new JLabel("Variables:");
        varLabel.setFont(new Font("Century Gothic", Font.BOLD, 14));
        varPanel.add(varLabel);

        for (final String id : signature.getVariableSet()) {
            JLabel l = new JLabel(id + ":" + signature.getVariable(id));
            l.setFont(new Font("Century Gothic", Font.PLAIN, 12));

            l.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    Object[] options = {"Okay",
                            "Delete variable!"};
                    //default icon, custom title
                    int n = JOptionPane.showOptionDialog(
                            HomeScreen.this,
                            "Varibale id: " + id + "\nType:" + signature.getVariable(id),
                            "Varibale info",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (n == 1) {
                        signature.deleteVariable(id);
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
        ruleLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        rulePane.add(ruleLabel);

        for (final RewriteRule rule : engine.getRules()) {
            try {
                final JLabel label = new JLabel(parser.toInfix(parser.postOrderTreverse(rule.getLhs())) +
                        "\u27f6" + parser.toInfix(parser.postOrderTreverse(rule.getRhs())));
                label.setFont(new Font("Century Gothic", Font.PLAIN, 12));

                label.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Object[] options = {"Okay", "Delete rule!"};
                        //default icon, custom title
                        int n = JOptionPane.showOptionDialog(
                                HomeScreen.this,
                                label.getText() + "\nRule name:" + rule.getName(),
                                "Rewrite Rule info",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                options,
                                options[0]);

                        if (n == 1) {
                            engine.deleteRule(rule);
                            updateUI();
                        }

                    }

                });

                rulePane.add(label);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(HomeScreen.this, "An error is encountered during parsing, check for mismatch parenthesis.", "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

}
