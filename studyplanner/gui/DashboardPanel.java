package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * DASHBOARD PANEL - Main menu, converted from DashboardFrame to work with CardLayout
 * 
 * CHANGES FROM DASHBOARDFRAME:
 * - Extends JPanel instead of JFrame
 * - Uses NavigationListener to switch screens instead of creating new JFrame instances
 * - No frame configuration (handled by MainFrame)
 * - Navigation uses cardLayout.show() via NavigationListener
 */
public class DashboardPanel extends JPanel implements ActionListener {
    
    private NavigationListener navigationListener;
    private PlannerService plannerService;
    
    private StyledButton btnAddSubject;
    private StyledButton btnAddSession;
    private StyledButton btnAddTask;
    private StyledButton btnViewData;
    private StyledButton btnExit;
    
    /**
     * CONSTRUCTOR
     */
    public DashboardPanel(NavigationListener navigationListener, PlannerService plannerService) {
        this.navigationListener = navigationListener;
        this.plannerService = plannerService;
        
        initializeComponents();
    }
    
    /**
     * Initialize UI components
     */
    private void initializeComponents() {
        JPanel root = UITheme.createRootPanel();
        this.setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = UITheme.createTransparentPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        titlePanel.add(UITheme.createTitleLabel("STUDY PLANNER DASHBOARD"), BorderLayout.CENTER);
        root.add(titlePanel, BorderLayout.NORTH);
        
        // Center wrapper with buttons
        JPanel centerWrapper = UITheme.createTransparentPanel(new GridBagLayout());
        JPanel buttonsPanel = UITheme.createTransparentPanel(null);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        
        // Create menu buttons
        btnAddSubject = UITheme.primaryButton("➕ Add Subject");
        btnAddSession = UITheme.primaryButton("⏱️ Add Study Session");
        btnAddTask = UITheme.primaryButton("✓ Add Task");
        btnViewData = UITheme.secondaryButton("📊 View Data");
        btnExit = UITheme.dangerButton("⏻ Exit App");
        
        configureMenuButton(btnAddSubject);
        configureMenuButton(btnAddSession);
        configureMenuButton(btnAddTask);
        configureMenuButton(btnViewData);
        configureMenuButton(btnExit);
        
        buttonsPanel.add(btnAddSubject);
        buttonsPanel.add(Box.createVerticalStrut(12));
        buttonsPanel.add(btnAddSession);
        buttonsPanel.add(Box.createVerticalStrut(12));
        buttonsPanel.add(btnAddTask);
        buttonsPanel.add(Box.createVerticalStrut(12));
        buttonsPanel.add(btnViewData);
        buttonsPanel.add(Box.createVerticalStrut(18));
        buttonsPanel.add(btnExit);
        
        centerWrapper.add(buttonsPanel, new GridBagConstraints());
        root.add(centerWrapper, BorderLayout.CENTER);
        
        add(root);
    }
    
    /**
     * Configure menu button appearance
     */
    private void configureMenuButton(StyledButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(360, 54));
        button.setPreferredSize(new Dimension(360, 54));
        button.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddSubject) {
            navigationListener.navigateTo(MainFrame.ADD_SUBJECT_SCREEN);
        } else if (e.getSource() == btnAddSession) {
            navigationListener.navigateTo(MainFrame.ADD_SESSION_SCREEN);
        } else if (e.getSource() == btnAddTask) {
            navigationListener.navigateTo(MainFrame.ADD_TASK_SCREEN);
        } else if (e.getSource() == btnViewData) {
            navigationListener.navigateTo(MainFrame.VIEW_DATA_SCREEN);
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }
}
