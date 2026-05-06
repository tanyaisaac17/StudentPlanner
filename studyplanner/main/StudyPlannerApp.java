package com.studyplanner.main;

import com.studyplanner.gui.AppFrame;
import com.studyplanner.model.Record;

public class StudyPlannerApp {
    
    public static void main(String[] args) {
        displayWelcomeBanner();
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                new AppFrame();
                System.out.println("✓ Single-window application launched!\n");
            } catch (Exception e) {
                System.err.println("❌ Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Display welcome banner
     * Provides information about the application
     */
    private static void displayWelcomeBanner() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 📚 STUDENT STUDY PLANNER 📚                     ║");
        System.out.println("║           Desktop Application for Study Management              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  ✓ Add and manage subjects");
        System.out.println("  ✓ Track study sessions with hours");
        System.out.println("  ✓ Create and manage to-do tasks");
        System.out.println("  ✓ View all data from MySQL database");
        System.out.println("  ✓ Persistent data storage");
        System.out.println();
        System.out.println("Technologies:");
        System.out.println("  • Java 17/21");
        System.out.println("  • Java Swing for GUI");
        System.out.println("  • MySQL database");
        System.out.println("  • JDBC for connectivity");
        System.out.println();
        System.out.println("Database Configuration:");
        System.out.println("  • Database: study_planner");
        System.out.println("  • User: root");
        System.out.println("  • Location: localhost:3306");
        System.out.println();
    }
    
    
    
}
