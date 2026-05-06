package com.studyplanner.gui;

import com.studyplanner.model.Subject;
import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddSubjectForm extends JDialog implements ActionListener {
    
    // Modern Color Theme
    private static final Color BG_COLOR = new Color(244, 246, 248);      // #F4F6F8
    private static final Color PRIMARY_COLOR = new Color(76, 175, 80);   // #4CAF50
    private static final Color DANGER_COLOR = new Color(244, 67, 54);    // #F44336
    private static final Color TEXT_COLOR = new Color(51, 51, 51);       // #333333
    
    private PlannerService plannerService;
    
    // GUI Components
    private JTextField txtSubjectName;
    private JSpinner spinnerCredits;
    private JButton btnAdd;
    private JButton btnCancel;
    private JLabel lblStatus;
    

    public AddSubjectForm(PlannerService plannerService) {
        this.plannerService = plannerService;
        
        // Dialog configuration
        setTitle("Add New Subject");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true); // Block parent window
        
        initializeComponents();
        
        setVisible(true);
    }
    

    private void initializeComponents() {
        // Main panel with BoxLayout for vertical arrangement
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BG_COLOR);
        
        // ===== TITLE LABEL =====
        JLabel lblTitle = new JLabel("Add New Subject");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // ===== SUBJECT NAME FIELD =====
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        namePanel.setBackground(BG_COLOR);
        
        JLabel lblName = new JLabel("Subject Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setForeground(TEXT_COLOR);
        lblName.setPreferredSize(new Dimension(130, 25));
        
        txtSubjectName = new JTextField(20);
        txtSubjectName.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSubjectName.setPreferredSize(new Dimension(240, 35));
        
        namePanel.add(lblName);
        namePanel.add(txtSubjectName);
        
        // ===== CREDITS FIELD =====
        JPanel creditsPanel = new JPanel();
        creditsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        creditsPanel.setBackground(BG_COLOR);
        
        JLabel lblCredits = new JLabel("Credits (1-6):");
        lblCredits.setFont(new Font("Arial", Font.BOLD, 14));
        lblCredits.setForeground(TEXT_COLOR);
        lblCredits.setPreferredSize(new Dimension(130, 25));
        

        spinnerCredits = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));
        spinnerCredits.setPreferredSize(new Dimension(240, 35));
        spinnerCredits.setFont(new Font("Arial", Font.PLAIN, 14));
        
        creditsPanel.add(lblCredits);
        creditsPanel.add(spinnerCredits);
        
        // ===== STATUS LABEL =====
        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(100, 100, 100));
        
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(BG_COLOR);
        statusPanel.add(lblStatus);
        
        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BG_COLOR);
        
        btnAdd = createStyledButton("Add Subject", PRIMARY_COLOR);
        btnCancel = createStyledButton("Cancel", DANGER_COLOR);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        
        // Add all panels to main panel
        mainPanel.add(namePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(creditsPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(statusPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.CENTER);
        btn.addActionListener(this);
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color originalColor = bgColor;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(darkenColor(originalColor, 0.8f));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(originalColor);
            }
        });
        
        return btn;
    }
    

    private Color darkenColor(Color color, float factor) {
        return new Color(
            (int) (color.getRed() * factor),
            (int) (color.getGreen() * factor),
            (int) (color.getBlue() * factor)
        );
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addSubject();
        } else if (e.getSource() == btnCancel) {
            dispose();
        }
    }
    
    /**
     * Add subject to database
     * Demonstrates input validation before database insertion
     */
    private void addSubject() {
        // Input validation
        String subjectName = txtSubjectName.getText().trim();
        
        if (subjectName.isEmpty()) {
            showError("Please enter subject name!");
            return;
        }
        
        if (subjectName.length() > 50) {
            showError("Subject name too long (max 50 characters)!");
            return;
        }
        
        /**
         * UNBOXING: Get Integer from JSpinner, convert to primitive int
         * (Integer) spinnerCredits.getValue() -> automatic unboxing
         */
        int credits = (Integer) spinnerCredits.getValue();
        
        try {
            // Create Subject object with data from form
            Subject subject = new Subject(subjectName);
            subject.setCredits(credits);
            
            // Call service to add subject
            if (plannerService.addSubject(subject)) {
                showSuccess("Subject added successfully!");
                // Clear form
                txtSubjectName.setText("");
                spinnerCredits.setValue(1);
            } else {
                showError("Failed to add subject!");
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        lblStatus.setForeground(new Color(231, 76, 60));
        lblStatus.setText("❌ " + message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show success message
     */
    private void showSuccess(String message) {
        lblStatus.setForeground(new Color(46, 204, 113));
        lblStatus.setText("✓ " + message);
    }
}
