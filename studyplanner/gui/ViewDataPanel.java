package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewDataPanel extends JPanel implements ActionListener {
    
    private NavigationListener navigationListener;
    private PlannerService plannerService;
    
    private JTabbedPane tabbedPane;
    private JTextArea txtSummary;
    private JTextArea txtSubjects;
    private JTextArea txtSessions;
    private TaskManagerPanel taskManagerPanel;
    private StyledButton btnRefresh;
    private JLabel lblLoadingStatus;
    
    /**
     * CONSTRUCTOR
     */
    public ViewDataPanel(NavigationListener navigationListener, PlannerService plannerService) {
        this.navigationListener = navigationListener;
        this.plannerService = plannerService;
        
        initializeComponents();
        loadDataInBackground();
    }
    
    /**
     * Initialize UI components
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UITheme.PRIMARY_BACKGROUND);
        
        // Top navigation bar
        TopNavigationBar navBar = new TopNavigationBar(navigationListener, "📊 View All Data");
        add(navBar, BorderLayout.NORTH);
        
        // Main content
        JPanel root = UITheme.createRootPanel();
        
        // Toolbar with refresh button
        JPanel toolBar = UITheme.createCardPanel(new BorderLayout(10, 0));
        toolBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblLoadingStatus = UITheme.createNormalLabel("Loading data...");
        lblLoadingStatus.setForeground(UITheme.TEXT_MUTED);
        
        btnRefresh = UITheme.secondaryButton("🔄 Refresh Data");
        btnRefresh.setPreferredSize(new Dimension(180, 44));
        btnRefresh.addActionListener(this);
        
        toolBar.add(lblLoadingStatus, BorderLayout.WEST);
        toolBar.add(btnRefresh, BorderLayout.EAST);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        UITheme.styleTabbedPane(tabbedPane);
        
        txtSummary = createTextArea();
        txtSubjects = createTextArea();
        txtSessions = createTextArea();
        taskManagerPanel = new TaskManagerPanel(plannerService);
        
        tabbedPane.addTab("Summary", new JScrollPane(txtSummary));
        tabbedPane.addTab("Subjects", new JScrollPane(txtSubjects));
        tabbedPane.addTab("Study Sessions", new JScrollPane(txtSessions));
        tabbedPane.addTab("Tasks", wrapTaskPanel(taskManagerPanel));
        
        JPanel tabsCard = UITheme.createCardPanel(new BorderLayout());
        tabsCard.setBackground(UITheme.PANEL_BACKGROUND_2);
        tabsCard.add(tabbedPane, BorderLayout.CENTER);
        
        root.add(toolBar, BorderLayout.NORTH);
        root.add(tabsCard, BorderLayout.CENTER);
        
        add(root, BorderLayout.CENTER);
    }
    
    /**
     * Create styled text area
     */
    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setMargin(new Insets(10, 10, 10, 10));
        UITheme.styleTextArea(area);
        return area;
    }
    
    /**
     * Wrap task panel in container
     */
    private JComponent wrapTaskPanel(JComponent panel) {
        JPanel wrapper = UITheme.createTransparentPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }
    
    /**
     * Load data in background thread
     */
    private void loadDataInBackground() {
        btnRefresh.setEnabled(false);
        lblLoadingStatus.setText("Loading data...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                loadAllData();
                return null;
            }
            
            @Override
            protected void done() {
                lblLoadingStatus.setText("✓ Data loaded");
                btnRefresh.setEnabled(true);
            }
        };
        
        worker.execute();
    }
    
    /**
     * Load all data from database
     */
    private void loadAllData() {
        try {
            // Load subjects, sessions, and tasks
            var subjects = plannerService.getAllSubjects();
            var sessions = plannerService.getAllSessions();
            var tasks = plannerService.getAllTasks();
            
            StringBuilder summary = new StringBuilder();
            summary.append("📊 STUDY PLANNER STATISTICS\n\n");
            summary.append("Total Subjects: ").append(subjects.size()).append("\n");
            summary.append("Total Study Sessions: ").append(sessions.size()).append("\n");
            summary.append("Total Tasks: ").append(tasks.size()).append("\n");
            long completedTasks = tasks.stream().filter(t -> t.isCompleted()).count();
            summary.append("Completed Tasks: ").append(completedTasks).append("\n");
            summary.append("\n📈 Quick Stats:\n");
            summary.append("- Review your subjects, sessions, and tasks below\n");
            summary.append("- Use navigation to add new items\n");
            summary.append("- Mark tasks as complete in the Tasks tab\n");
            
            txtSummary.setText(summary.toString());
            
            // Load subjects
            StringBuilder subjectsText = new StringBuilder();
            subjectsText.append("📚 SUBJECTS\n\n");
            for (var subject : subjects) {
                subjectsText.append("• ").append(subject.getName())
                    .append(" (").append(subject.getCredits()).append(" credits)\n");
            }
            if (subjects.isEmpty()) {
                subjectsText.append("No subjects added yet.\n");
            }
            txtSubjects.setText(subjectsText.toString());
            
            // Load sessions - use getAllSessions() instead of getAllStudySessions()
            StringBuilder sessionsText = new StringBuilder();
            sessionsText.append("⏱️ STUDY SESSIONS\n\n");
            for (var session : sessions) {
                sessionsText.append("• ").append(session.getSubject())
                    .append(" - ").append(session.getHours()).append(" hours\n");
            }
            if (sessions.isEmpty()) {
                sessionsText.append("No study sessions recorded yet.\n");
            }
            txtSessions.setText(sessionsText.toString());
            
        } catch (Exception e) {
            txtSummary.setText("Error loading data: " + e.getMessage());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadDataInBackground();
        }
    }
    
    /**
     * Apply custom table cell renderer with cyberpunk styling
     * 
     * This creates alternating row colors and proper text styling
     * for the cyberpunk theme.
     */
    static class CyberpunkTableCellRenderer extends DefaultTableCellRenderer {
        
        // Cyberpunk colors
        private static final Color ROW_COLOR = new Color(30, 41, 59);       // #1E293B
        private static final Color ALTERNATE_COLOR = new Color(36, 52, 71); // #243447
        private static final Color TEXT_COLOR = new Color(226, 232, 240);   // #E2E8F0
        private static final Color SELECTION_COLOR = new Color(99, 102, 241); // #6366F1
        private static final Color SELECTION_TEXT = Color.WHITE;
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Set font to Consolas for data display
            c.setFont(new Font("Consolas", Font.PLAIN, 13));
            
            if (isSelected) {
                c.setBackground(SELECTION_COLOR);
                c.setForeground(SELECTION_TEXT);
            } else {
                // Alternating row colors
                if (row % 2 == 0) {
                    c.setBackground(ROW_COLOR);
                } else {
                    c.setBackground(ALTERNATE_COLOR);
                }
                c.setForeground(TEXT_COLOR);
            }
            
            // Add padding
            setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
            
            return c;
        }
    }
    
    /**
     * Apply custom table header renderer
     */
    static class CyberpunkTableHeaderRenderer extends DefaultTableCellRenderer {
        
        private static final Color HEADER_BG = new Color(56, 189, 248);    // #38BDF8
        private static final Color HEADER_TEXT = Color.WHITE;
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            c.setBackground(HEADER_BG);
            c.setForeground(HEADER_TEXT);
            c.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            
            return c;
        }
    }
    
    /**
     * Style a JTable with cyberpunk colors
     */
    public static void styleTableWithCyberpunk(JTable table) {
        // Set table colors
        table.setBackground(new Color(15, 23, 42));              // #0F172A
        table.setForeground(new Color(226, 232, 240));           // #E2E8F0
        table.setSelectionBackground(new Color(99, 102, 241));   // #6366F1
        table.setSelectionForeground(Color.WHITE);
        
        // Increase row height for better readability
        table.setRowHeight(32);
        
        // Set cell renderer
        CyberpunkTableCellRenderer cellRenderer = new CyberpunkTableCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new CyberpunkTableHeaderRenderer());
        header.setBackground(new Color(56, 189, 248));           // #38BDF8
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        
        // Grid color
        table.setGridColor(new Color(51, 65, 85));
    }
}
