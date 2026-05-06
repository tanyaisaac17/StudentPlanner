package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * VIEW DATA FRAME - Summary + Subjects + Sessions + Tasks.
 */
public class ViewDataFrame extends JFrame implements ActionListener {

    private final PlannerService plannerService;

    private JTabbedPane tabbedPane;
    private JTextArea txtSummary;
    private JTextArea txtSubjects;
    private JTextArea txtSessions;
    private TaskManagerPanel taskManagerPanel;
    private StyledButton btnRefresh;
    private JLabel lblLoadingStatus;

    public ViewDataFrame(PlannerService plannerService) {
        this.plannerService = plannerService;

        UITheme.configureFrame(
                this,
                "Study Planner - View Data",
                new Dimension(980, 680),
                new Dimension(860, 580),
                JFrame.DISPOSE_ON_CLOSE
        );

        initializeComponents();
        loadDataInBackground();

        setVisible(true);
    }

    private void initializeComponents() {
        JPanel root = UITheme.createRootPanel();

        JPanel toolBar = UITheme.createCardPanel(new BorderLayout(10, 0));
        toolBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblLoadingStatus = UITheme.createNormalLabel("Loading data...");
        lblLoadingStatus.setForeground(UITheme.TEXT_MUTED);

        btnRefresh = UITheme.secondaryButton("Refresh Data");
        btnRefresh.setPreferredSize(new Dimension(180, 44));
        btnRefresh.addActionListener(this);

        toolBar.add(lblLoadingStatus, BorderLayout.WEST);
        toolBar.add(btnRefresh, BorderLayout.EAST);

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

        add(root);
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setMargin(new Insets(10, 10, 10, 10));
        UITheme.styleTextArea(area);
        return area;
    }

    private JComponent wrapTaskPanel(JComponent panel) {
        JPanel wrapper = UITheme.createTransparentPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private void loadDataInBackground() {
        btnRefresh.setEnabled(false);
        lblLoadingStatus.setText("Loading data...");

        Thread loadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadAllData();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        lblLoadingStatus.setText("Data loaded.");
                        btnRefresh.setEnabled(true);
                    }
                });
            }
        });

        loadingThread.start();
    }

    private void loadAllData() {
        StringBuilder summaryText = new StringBuilder();
        StringBuilder subjectsText = new StringBuilder();
        StringBuilder sessionsText = new StringBuilder();

        try {
            var subjects = plannerService.getAllSubjects();
            var sessions = plannerService.getAllSessions();
            var tasks = plannerService.getAllTasks();

            int totalHours = 0;
            for (var session : sessions) {
                totalHours += session.getHours();
            }

            int completedTasks = 0;
            for (var task : tasks) {
                if (task.isCompleted()) {
                    completedTasks++;
                }
            }
            int pendingTasks = tasks.size() - completedTasks;

            summaryText.append("SUMMARY\n");
            summaryText.append("================================\n\n");
            summaryText.append("Subjects: ").append(subjects.size()).append("\n");
            summaryText.append("Study Sessions: ").append(sessions.size()).append("\n");
            summaryText.append("Total Hours Spent: ").append(totalHours).append("\n");
            summaryText.append("Tasks Remaining: ").append(pendingTasks).append("\n");
            summaryText.append("Tasks Completed: ").append(completedTasks).append("\n\n");

            summaryText.append("HOURS BY SUBJECT\n");
            summaryText.append("================================\n");
            Map<String, Integer> hoursBySubject = new LinkedHashMap<>();
            for (var session : sessions) {
                hoursBySubject.put(session.getSubject(), hoursBySubject.getOrDefault(session.getSubject(), 0) + session.getHours());
            }
            if (hoursBySubject.isEmpty()) {
                summaryText.append("No sessions yet.\n");
            } else {
                for (var entry : hoursBySubject.entrySet()) {
                    summaryText.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" hours\n");
                }
            }

            subjectsText.append("================================\n");
            subjectsText.append("ALL SUBJECTS\n");
            subjectsText.append("================================\n\n");
            if (subjects.isEmpty()) {
                subjectsText.append("No subjects found.\n");
            } else {
                for (var subject : subjects) {
                    subjectsText.append("ID: ").append(subject.getId()).append("\n");
                    subjectsText.append("Name: ").append(subject.getName()).append("\n");
                    subjectsText.append("Credits: ").append(subject.getCredits()).append("\n");
                    subjectsText.append("Status: ").append(subject.getStatus().getDisplayName()).append("\n");
                    subjectsText.append("---\n\n");
                }
            }

            sessionsText.append("================================\n");
            sessionsText.append("ALL STUDY SESSIONS\n");
            sessionsText.append("================================\n\n");
            if (sessions.isEmpty()) {
                sessionsText.append("No study sessions found.\n");
            } else {
                for (var session : sessions) {
                    sessionsText.append("ID: ").append(session.getId()).append("\n");
                    sessionsText.append("Subject: ").append(session.getSubject()).append("\n");
                    sessionsText.append("Hours: ").append(session.getHours()).append("\n");
                    sessionsText.append("Status: ").append(session.getStatus().getDisplayName()).append("\n");
                    sessionsText.append("---\n\n");
                }
            }
        } catch (Exception e) {
            summaryText.append("Error loading summary: ").append(e.getMessage()).append("\n");
            subjectsText.append("Error loading subjects: ").append(e.getMessage());
            sessionsText.append("Error loading sessions: ").append(e.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtSummary.setText(summaryText.toString());
                txtSummary.setCaretPosition(0);

                txtSubjects.setText(subjectsText.toString());
                txtSubjects.setCaretPosition(0);

                txtSessions.setText(sessionsText.toString());
                txtSessions.setCaretPosition(0);

                taskManagerPanel.reload();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadDataInBackground();
        }
    }
}
