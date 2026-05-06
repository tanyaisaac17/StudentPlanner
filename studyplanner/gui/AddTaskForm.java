package com.studyplanner.gui;

import com.studyplanner.model.Task;
import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTaskForm extends JDialog implements ActionListener {
    
    // Modern Color Theme
    private static final Color BG_COLOR = new Color(244, 246, 248);      // #F4F6F8
    private static final Color PRIMARY_COLOR = new Color(76, 175, 80);   // #4CAF50
    private static final Color DANGER_COLOR = new Color(244, 67, 54);    // #F44336
    private static final Color TEXT_COLOR = new Color(51, 51, 51);       // #333333
    
    private PlannerService plannerService;
    
    // GUI Components
    private JTextArea txtTaskDescription;
    private JButton btnAdd;
    private JButton btnCancel;
    private JLabel lblStatus;
    
    public AddTaskForm(PlannerService plannerService) {
        this.plannerService = plannerService;
        
        setTitle("✓ Add Task / To-Do");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        
        initializeComponents();
        
        setVisible(true);
    }
    
    /**
     * Initialize form components with modern styling
     */
    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BG_COLOR);
        
        // ===== TITLE LABEL =====
        JLabel lblTitle = new JLabel("Add New Task / To-Do");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // ===== DESCRIPTION FIELD =====
        JLabel lblDescription = new JLabel("Task Description:");
        lblDescription.setFont(new Font("Arial", Font.BOLD, 14));
        lblDescription.setForeground(TEXT_COLOR);
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblDescription);
        mainPanel.add(Box.createVerticalStrut(5));
        
        /**
         * JTEXTAREA: Multi-line text input
         * JTextArea vs JTextField:
         * - JTextField: Single line input
         * - JTextArea: Multiple lines input
         */
        txtTaskDescription = new JTextArea(6, 40);
        txtTaskDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTaskDescription.setLineWrap(true); // Auto wrap lines
        txtTaskDescription.setWrapStyleWord(true);
        txtTaskDescription.setBackground(new Color(255, 255, 255));
        txtTaskDescription.setForeground(TEXT_COLOR);
        txtTaskDescription.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        // Add scrollbar for textarea
        JScrollPane scrollPane = new JScrollPane(txtTaskDescription);
        scrollPane.setPreferredSize(new Dimension(400, 120));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // ===== STATUS LABEL =====
        lblStatus = new JLabel("");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStatus.setForeground(new Color(100, 100, 100));
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(BG_COLOR);
        statusPanel.add(lblStatus);
        mainPanel.add(statusPanel);
        
        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BG_COLOR);
        
        btnAdd = createStyledButton("Add Task", PRIMARY_COLOR);
        btnCancel = createStyledButton("Cancel", DANGER_COLOR);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    /**
     * Create styled button with modern design
     */
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
    
    /**
     * Helper method to darken a color
     */
    private Color darkenColor(Color color, float factor) {
        return new Color(
            (int) (color.getRed() * factor),
            (int) (color.getGreen() * factor),
            (int) (color.getBlue() * factor)
        );
    }
    
    /**
     * ACTION LISTENER
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addTask();
        } else if (e.getSource() == btnCancel) {
            dispose();
        }
    }
    
    /**
     * Add task to database
     */
    private void addTask() {
        String description = txtTaskDescription.getText().trim();
        
        // Input validation
        if (description.isEmpty()) {
            showError("Please enter task description!");
            return;
        }
        
        if (description.length() > 100) {
            showError("Task description too long (max 100 characters)!");
            return;
        }
        
        try {
            Task task = new Task(description);
            
            if (plannerService.addTask(task)) {
                showSuccess("Task added successfully!");
                txtTaskDescription.setText("");
            } else {
                showError("Failed to add task!");
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    private void showError(String message) {
        lblStatus.setForeground(new Color(231, 76, 60));
        lblStatus.setText("❌ " + message);
    }
    
    private void showSuccess(String message) {
        lblStatus.setForeground(new Color(46, 204, 113));
        lblStatus.setText("✓ " + message);
    }
}
