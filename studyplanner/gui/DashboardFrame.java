package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;
import com.studyplanner.service.PlannerServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * DASHBOARD FRAME - Main menu after login
 */
public class DashboardFrame extends JFrame implements ActionListener {

    private final PlannerService plannerService;

    private StyledButton btnAddSubject;
    private StyledButton btnAddSession;
    private StyledButton btnAddTask;
    private StyledButton btnViewData;
    private StyledButton btnExit;

    public DashboardFrame() {
        this.plannerService = new PlannerServiceImpl();

        UITheme.configureFrame(
                this,
                "Study Planner Dashboard",
                new Dimension(820, 560),
                new Dimension(760, 520),
                JFrame.EXIT_ON_CLOSE
        );

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        JPanel root = UITheme.createRootPanel();

        JPanel titlePanel = UITheme.createTransparentPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        titlePanel.add(UITheme.createTitleLabel("STUDY PLANNER DASHBOARD"), BorderLayout.CENTER);
        root.add(titlePanel, BorderLayout.NORTH);

        JPanel centerWrapper = UITheme.createTransparentPanel(new GridBagLayout());
        JPanel buttonsPanel = UITheme.createTransparentPanel(null);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        btnAddSubject = UITheme.primaryButton("Add Subject");
        btnAddSession = UITheme.primaryButton("Add Study Session");
        btnAddTask = UITheme.primaryButton("Add Task");
        btnViewData = UITheme.secondaryButton("View Data");
        btnExit = UITheme.dangerButton("Exit App");

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

    private void configureMenuButton(StyledButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(360, 54));
        button.setPreferredSize(new Dimension(360, 54));
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddSubject) {
            new AddSubjectForm(plannerService);
        } else if (e.getSource() == btnAddSession) {
            new AddSessionForm(plannerService);
        } else if (e.getSource() == btnAddTask) {
            new AddTaskForm(plannerService);
        } else if (e.getSource() == btnViewData) {
            new ViewDataFrame(plannerService);
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }
}

