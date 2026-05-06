package com.studyplanner.util;

import javax.swing.*;
import java.awt.*;

/**
 * ICON UTILITY - Helper class for loading and managing icons
 * 
 * BEGINNER GUIDE:
 * This utility makes it easy to create icon buttons
 * 
 * HOW TO ADD ICONS:
 * 1. Create a folder: src/icons/
 * 2. Save PNG files there:
 *    - plus.png (16x16 or 24x24)
 *    - clock.png
 *    - checklist.png
 *    - table.png
 *    - arrow.png
 *    - power.png
 * 3. Update the getIcon() method paths below
 * 
 * ICON SIZES:
 * - Small: 16x16 (for toolbar icons)
 * - Medium: 24x24 (for buttons)
 * - Large: 32x32 (for main icons)
 */
public class IconUtil {
    
    // Icon folder path - UPDATE THIS IF YOU CHANGE FOLDER STRUCTURE
    private static final String ICON_FOLDER = "src/icons/";
    
    /**
     * Get icon by name
     * Returns null if file not found (will just show text)
     */
    public static ImageIcon getIcon(String iconName, int size) {
        try {
            String path = ICON_FOLDER + iconName + ".png";
            ImageIcon icon = new ImageIcon(path);
            
            // Resize icon to specified size
            Image scaledImage = icon.getImage().getScaledInstance(
                size, size, Image.SCALE_SMOOTH
            );
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconName);
            return null; // Icon not found - button will just show text
        }
    }
    
    /**
     * Get predefined icon sizes
     */
    public static ImageIcon getSmallIcon(String iconName) {
        return getIcon(iconName, 16);
    }
    
    public static ImageIcon getMediumIcon(String iconName) {
        return getIcon(iconName, 24);
    }
    
    public static ImageIcon getLargeIcon(String iconName) {
        return getIcon(iconName, 32);
    }
    
    /**
     * Create a button with icon and text
     * If icon not found, button shows just text
     */
    public static JButton createIconButton(String iconName, String text, int iconSize) {
        ImageIcon icon = getIcon(iconName, iconSize);
        JButton btn = new JButton(text);
        
        if (icon != null) {
            btn.setIcon(icon);
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setVerticalTextPosition(SwingConstants.CENTER);
            btn.setIconTextGap(8);
        }
        
        return btn;
    }
    
    /**
     * ALTERNATIVE: Use Unicode symbols instead of icons (no files needed)
     * This is useful if you don't have icon files
     */
    public static class UnicodeSymbols {
        public static final String PLUS = "➕";
        public static final String CLOCK = "⏱️";
        public static final String CHECKLIST = "✓";
        public static final String TABLE = "📊";
        public static final String ARROW = "←";
        public static final String POWER = "⏻";
        public static final String HOME = "🏠";
        public static final String ADD = "➕";
        public static final String REFRESH = "🔄";
    }
}
