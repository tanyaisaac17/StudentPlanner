package com.studyplanner.gui;

import com.studyplanner.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class AppFrame extends JFrame implements SideNavigationPanel.NavigationCallback {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JPanel contentPanel;
    private CardLayout contentCardLayout;
    private SideNavigationPanel navigationPanel;

    private DashboardHomePanel dashboardPanel;
    private SubjectsPanel subjectsPanel;
    private TasksPanel tasksPanel;
    private StudySessionPanel studySessionPanel;
    private SummaryPanel summaryPanel;

    private LoginPanelSimple loginPanel;
    private SignUpPanelSimple signUpPanel;
    private User currentUser;
    
    private static final Color MAIN_BG = new Color(15, 14, 46);
    private static final String LOGIN_BG_PATH = "C:\\Users\\DELL\\OneDrive\\Desktop\\image.png";
    
    private BufferedImage backgroundImage;
    
    public AppFrame() {
        loadBackgroundImage();
        initFrame();
    }
    
    private void loadBackgroundImage() {
        try {
            var stream = AppFrame.class.getResourceAsStream("/image.png");
            if (stream != null) {
                backgroundImage = ImageIO.read(stream);
                return;
            }
        } catch (Exception ignored) {
        }
        
        try {
            File file = new File(LOGIN_BG_PATH);
            if (file.exists()) {
                backgroundImage = ImageIO.read(file);
            }
        } catch (Exception ignored) {
        }
    }
    
    private void initFrame() {
        setTitle("📚 Study Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Main container with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(MAIN_BG);
        
        // Create login + signup panels
        loginPanel = new LoginPanelSimple(this::onAuthenticated, this::showSignUp);
        signUpPanel = new SignUpPanelSimple(this::onAuthenticated, this::showLogin);
        mainContentPanel.add(loginPanel, "LOGIN");
        mainContentPanel.add(signUpPanel, "SIGNUP");

        // Create main app panel
        JPanel appPanel = createMainAppPanel();
        mainContentPanel.add(appPanel, "APP");

        add(mainContentPanel);

        // Show login first
        cardLayout.show(mainContentPanel, "LOGIN");

        setVisible(true);
    }
    
    private JPanel createMainAppPanel() {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setBackground(MAIN_BG);
        
        // Side navigation
        navigationPanel = new SideNavigationPanel(this);
        mainContainer.add(navigationPanel, BorderLayout.WEST);
        
        // Content area with CardLayout
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(MAIN_BG);
        
        // Create all panels
        dashboardPanel = new DashboardHomePanel();
        subjectsPanel = new SubjectsPanel();
        tasksPanel = new TasksPanel();
        studySessionPanel = new StudySessionPanel();
        summaryPanel = new SummaryPanel();
        
        // Add panels to CardLayout
        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(subjectsPanel, "SUBJECTS");
        contentPanel.add(tasksPanel, "TASKS");
        contentPanel.add(studySessionPanel, "STUDY_SESSION");
        contentPanel.add(summaryPanel, "SUMMARY");
        
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        
        // Show dashboard first
        contentCardLayout.show(contentPanel, "DASHBOARD");
        
        return mainContainer;
    }
    
    private void onAuthenticated(User user) {
        this.currentUser = user;
        setTitle("📚 Study Planner — " + user.getUsername());
        cardLayout.show(mainContentPanel, "APP");
    }

    private void showSignUp() {
        signUpPanel.reset();
        cardLayout.show(mainContentPanel, "SIGNUP");
    }

    private void showLogin() {
        loginPanel.reset();
        cardLayout.show(mainContentPanel, "LOGIN");
    }
    
    @Override
    public void navigateTo(String section) {
        contentCardLayout.show(contentPanel, section);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppFrame());
    }
}
