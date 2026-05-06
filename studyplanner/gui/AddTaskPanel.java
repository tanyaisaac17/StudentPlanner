package com.studyplanner.gui;

import com.studyplanner.model.Task;
import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ADD TASK PANEL - Converted from AddTaskForm to JPanel for CardLayout
 * 
 * KEY IMPROVEMENTS:
 * - Uses CardLayout for navigation
 * - Top navigation bar with back button
 * - Auto-close after successful save
 * - Button disabled when empty
 * - Multi-line text area for task descriptions
 */
public class AddTaskPanel extends JPanel implements ActionListener {
    
    private NavigationListener navigationListener;
    private PlannerService plannerService;
    
    private JTextArea txtTaskDescription;
    private JButton btnAdd;
    private JButton btnCancel;
    private JLabel lblStatus;
    
    /**
     * CONSTRUCTOR
     */
    public AddTaskPanel(NavigationListener navigationListener, PlannerService plannerService) {
        this.navigationListener = navigationListener;
        this.plannerService = plannerService;
        
        initializeComponents();
    }
    
    /**
     * Initialize UI components
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UITheme.PRIMARY_BACKGROUND);
        
        // Top navigation bar
        TopNavigationBar navBar = new TopNavigationBar(navigationListener, "Add New Task");
        add(navBar, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = UITheme.createRootPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Form panel (centered)
        JPanel formPanel = UITheme.createCardPanel(new BorderLayout(15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setMaximumSize(new Dimension(500, 450));
        formPanel.setPreferredSize(new Dimension(500, 450));
        
        // Description label
        JLabel lblDescription = UITheme.createNormalLabel("Task Description:");
        JPanel descLabelPanel = UITheme.createTransparentPanel(new BorderLayout());
        descLabelPanel.add(lblDescription, BorderLayout.NORTH);
        formPanel.add(descLabelPanel, BorderLayout.NORTH);
        
        // Text area
        txtTaskDescription = new JTextArea(6, 40);
        txtTaskDescription.setFont(UITheme.FONT_NORMAL);
        txtTaskDescription.setLineWrap(true);
        txtTaskDescription.setWrapStyleWord(true);
        txtTaskDescription.setBackground(UITheme.SECONDARY_BACKGROUND);
        txtTaskDescription.setForeground(UITheme.TEXT_COLOR);
        txtTaskDescription.setCaretColor(UITheme.ACCENT_COLOR);
        txtTaskDescription.setBorder(BorderFactory.createLineBorder(UITheme.CARD_COLOR, 2));
        
        // Add listener to enable/disable save button
        txtTaskDescription.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSaveButton(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSaveButton(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSaveButton(); }
        });
        
        // Scroll pane for text area
        JScrollPane scrollPane = new JScrollPane(txtTaskDescription);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.SECONDARY_BACKGROUND);
        formPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Status label
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_NORMAL);
        lblStatus.setForeground(new Color(100, 116, 139));
        JPanel statusPanel = UITheme.createTransparentPanel(new BorderLayout());
        statusPanel.add(lblStatus, BorderLayout.CENTER);
        formPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Center form in main panel
        JPanel centerWrapper = UITheme.createTransparentPanel(new GridBagLayout());
        centerWrapper.add(formPanel, new GridBagConstraints());
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = UITheme.createTransparentPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnAdd = UITheme.primaryButton("Add Task");
        btnAdd.setPreferredSize(new Dimension(140, 45));
        btnAdd.addActionListener(this);
        
        btnCancel = UITheme.secondaryButton("Cancel");
        btnCancel.setPreferredSize(new Dimension(140, 45));
        btnCancel.addActionListener(this);
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Initialize save button state
        updateSaveButton();
    }
    
    /**
     * Update save button state based on field validity
     */
    private void updateSaveButton() {
        String description = txtTaskDescription.getText().trim();
        btnAdd.setEnabled(!description.isEmpty());
        if (description.isEmpty()) {
            btnAdd.setOpaque(true);
            btnAdd.setBackground(new Color(148, 163, 184));
        } else {
            btnAdd.setBackground(UITheme.PRIMARY_BUTTON);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addTask();
        } else if (e.getSource() == btnCancel) {
            navigationListener.navigateTo(MainFrame.DASHBOARD_SCREEN);
        }
    }
    
    /**
     * Add task to database with auto-close
     * 
     * FLOW:
     * 1. Validate input
     * 2. Insert to database
     * 3. Show success dialog
     * 4. Clear fields
     * 5. Navigate to DASHBOARD (auto-close)
     */
    private void addTask() {
        String description = txtTaskDescription.getText().trim();
        
        // Validation
        if (description.isEmpty()) {
            showError("❌ Please enter task description!");
            return;
        }
        
        if (description.length() > 500) {
            showError("❌ Task description too long (max 500 characters)!");
            return;
        }
        
        try {
            Task task = new Task(description);
            
            if (plannerService.addTask(task)) {
                // Show success dialog
                JOptionPane.showMessageDialog(
                    this,
                    "Task added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Clear fields
                txtTaskDescription.setText("");
                lblStatus.setText(" ");
                
                // Auto-close: navigate back to dashboard
                SwingUtilities.invokeLater(() -> {
                    navigationListener.navigateTo(MainFrame.DASHBOARD_SCREEN);
                });
            } else {
                showError("❌ Failed to add task. Try again.");
            }
        } catch (Exception ex) {
            showError("❌ Error: " + ex.getMessage());
        }
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.setForeground(UITheme.DANGER_BUTTON);
    }
}
