package com.studyopedia;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotesAndTaskManager::new);
    }
}
