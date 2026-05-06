package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;
import com.studyplanner.service.PlannerServiceImpl;
import java.awt.CardLayout;

import javax.swing.*;

/**
 * MAIN FRAME - Single window with CardLayout
 * 
 * CARDLAYOUT CONCEPT:
 * Instead of opening multiple JFrames, CardLayout lets us switch between
 * different JPanels within a single JFrame - like deck of cards.
 * 
 * Benefits:
 * - Single window, better focus
 * - Smooth transitions between screens
 * - Shared state management
 * - Consistent theme across all screens
 * - Easy to add/remove screens
 * 
 * How CardLayout Works:
 * 1. Create CardLayout: new CardLayout()
 * 2. Set on container: container.setLayout(cardLayout)
 * 3. Add panels: cardLayout.addLayoutComponent(panel, "SCREEN_NAME")
 * 4. Switch screens: cardLayout.show(container, "SCREEN_NAME")
 * 
 * Screen Navigation:
 * - Login → Dashboard
 * - Dashboard → Add Subject/Session/Task/View Data
 * - Any Form → Dashboard (after save or back button)
 */
public class MainFrame extends JFrame {
    
    // ===== SCREEN NAMES (Card identifiers) =====
    public static final String LOGIN_SCREEN = "LOGIN";
    public static final String DASHBOARD_SCREEN = "DASHBOARD";
    public static final String ADD_SUBJECT_SCREEN = "ADD_SUBJECT";
    public static final String ADD_SESSION_SCREEN = "ADD_SESSION";
    public static final String ADD_TASK_SCREEN = "ADD_TASK";
    public static final String VIEW_DATA_SCREEN = "VIEW_DATA";
    
    // CardLayout container and manager
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Service layer (shared across all screens)
    private PlannerService plannerService;
    
    // Navigation listener interface for communication between panels
    private NavigationListener navigationListener;
    
    /**
     * CONSTRUCTOR - Initialize main frame with CardLayout
     */
    public MainFrame() {
        this.plannerService = new PlannerServiceImpl();
        this.navigationListener = new NavigationListenerImpl();
        
        // Configure main frame
        UITheme.configureFrame(
            this,
            "Study Planner - All in One",
            new java.awt.Dimension(900, 700),
            new java.awt.Dimension(850, 650),
            JFrame.EXIT_ON_CLOSE
        );
        
        // Initialize CardLayout
        initializeCardLayout();
        
        // Show login screen first
        cardLayout.show(mainPanel, LOGIN_SCREEN);
        
        setVisible(true);
    }
    
    /**
     * Initialize CardLayout and add all screen panels
     */
    private void initializeCardLayout() {
        // Create CardLayout manager
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(UITheme.COLOR_DARK_BG);
        
        // Create and add all screen panels
        mainPanel.add(new LoginPanel(navigationListener, plannerService), LOGIN_SCREEN);
        mainPanel.add(new DashboardPanel(navigationListener, plannerService), DASHBOARD_SCREEN);
        mainPanel.add(new AddSubjectPanel(navigationListener, plannerService), ADD_SUBJECT_SCREEN);
        mainPanel.add(new AddSessionPanel(navigationListener, plannerService), ADD_SESSION_SCREEN);
        mainPanel.add(new AddTaskPanel(navigationListener, plannerService), ADD_TASK_SCREEN);
        mainPanel.add(new ViewDataPanel(navigationListener, plannerService), VIEW_DATA_SCREEN);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    /**
     * Navigate to a specific screen
     * @param screenName Name of the screen (use static constants)
     */
    public void navigateToScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }
    
    /**
     * IMPLEMENTATION of NavigationListener
     * Handles screen navigation requests from panels
     */
    private class NavigationListenerImpl implements NavigationListener {
        @Override
        public void navigateTo(String screenName) {
            cardLayout.show(mainPanel, screenName);
        }
        
        @Override
        public PlannerService getPlannerService() {
            return plannerService;
        }
    }
    
    /**
     * Main entry point
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
