package com.studyplanner.util;

import com.studyplanner.db.DBConnection;

/**
 * DATABASE CLEANER - UTILITY TO CLEAR ALL DATA
 */
public class DatabaseCleaner {

    public static void main(String[] args) {
        try {
            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║              🗑️  DATABASE CLEANER - REMOVING ALL DATA 🗑️           ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

            DBConnection db = DBConnection.getInstance();
            db.clearAllData();

            System.out.println("\n✓ Database cleanup completed!\n");

        } catch (Exception e) {
            System.err.println("\n❌ Error during database cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
