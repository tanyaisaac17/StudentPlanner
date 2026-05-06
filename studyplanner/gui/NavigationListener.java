package com.studyplanner.gui;

import com.studyplanner.service.PlannerService;

/**
 * NAVIGATION LISTENER INTERFACE
 * 
 * This interface allows any panel to request screen navigation
 * and access the shared PlannerService without coupling to MainFrame.
 * 
 * DESIGN PATTERN: Observer Pattern for navigation
 * - Panels don't know about MainFrame directly
 * - Panels notify NavigationListener of navigation requests
 * - MainFrame implements this interface to handle navigation
 */
public interface NavigationListener {
    
    /**
     * Request navigation to a specific screen
     * @param screenName Screen identifier (use MainFrame.SCREEN_NAME constants)
     */
    void navigateTo(String screenName);
    
    /**
     * Get the shared PlannerService instance
     * @return PlannerService for database operations
     */
    PlannerService getPlannerService();
}
