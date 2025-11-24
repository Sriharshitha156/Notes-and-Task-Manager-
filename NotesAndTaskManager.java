package com.studyopedia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class NotesAndTaskManager {

    private JFrame frame;
    private JTabbedPane tabbedPane;

   
    private JPanel notesButtonPanel;
    private JScrollPane notesButtonScroll;
    private JTextArea notesInputArea;
    private HashMap<Integer, String> notesMap;
    private int noteCounter = 0;
    private int selectedNoteId = -1;

    
    private JPanel tasksPanel;
    private TaskLinkedList taskList;

    
    private JButton addButton, updateButton, deleteButton;

    public NotesAndTaskManager() {

        frame = new JFrame("Notes & Task Manager");

        notesMap = new HashMap<>();
        taskList = new TaskLinkedList();

        createTabs();
        createButtonsPanel();

        frame.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        leftPanel.add(addButton);
        leftPanel.add(updateButton);
        leftPanel.add(deleteButton);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createTabs() {
        tabbedPane = new JTabbedPane();

        
        notesButtonPanel = new JPanel();
        notesButtonPanel.setLayout(new BoxLayout(notesButtonPanel, BoxLayout.Y_AXIS));

        notesButtonScroll = new JScrollPane(
                notesButtonPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        JSeparator divider = new JSeparator(SwingConstants.HORIZONTAL);

        JLabel writeLabel = new JLabel("Write Here:");
        writeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        notesInputArea = new JTextArea();
        notesInputArea.setLineWrap(true);
        notesInputArea.setWrapStyleWord(true);

        JScrollPane inputScroll = new JScrollPane(
                notesInputArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        JPanel writePanel = new JPanel(new BorderLayout());
        writePanel.add(writeLabel, BorderLayout.NORTH);
        writePanel.add(inputScroll, BorderLayout.CENTER);

        JPanel notesTab = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 0.40;
        notesTab.add(notesButtonScroll, gbc);

        gbc.gridy = 1; gbc.weighty = 0.02;
        notesTab.add(divider, gbc);

        gbc.gridy = 2; gbc.weighty = 0.58;
        notesTab.add(writePanel, gbc);

        // TASK TAB
        tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));

        JScrollPane tasksScroll = new JScrollPane(tasksPanel);

        JPanel tasksTab = new JPanel(new BorderLayout());
        tasksTab.add(tasksScroll, BorderLayout.CENTER);

        tabbedPane.addTab("Notes", notesTab);
        tabbedPane.addTab("Tasks", tasksTab);
    }

    private void createButtonsPanel() {

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) handleNotesAdd();
            else handleTasksAdd();
        });

        updateButton.addActionListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) handleNotesUpdate();
            else handleTasksUpdate();
        });

        deleteButton.addActionListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) handleNotesDelete();
            else handleTasksDelete();
        });
    }

    private void handleNotesAdd() {
        String text = notesInputArea.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Write something first!");
            return;
        }

        noteCounter++;
        final int id = noteCounter;

        notesMap.put(id, text);

        JButton btn = new JButton("Note " + id);

        btn.addActionListener(ev -> {
            selectedNoteId = id;
            notesInputArea.setText(notesMap.get(id));
        });

        notesButtonPanel.add(btn);
        notesButtonPanel.revalidate();
        notesButtonPanel.repaint();

        notesInputArea.setText("");
    }

    private void handleNotesUpdate() {
        if (selectedNoteId == -1) {
            JOptionPane.showMessageDialog(frame, "Select a note first!");
            return;
        }
        String txt = notesInputArea.getText().trim();
        notesMap.put(selectedNoteId, txt);
        JOptionPane.showMessageDialog(frame, "Note Updated!");
    }

    private void handleNotesDelete() {
        if (selectedNoteId == -1) {
            JOptionPane.showMessageDialog(frame, "Click a note to delete.");
            return;
        }

        notesMap.remove(selectedNoteId);

        Component[] comps = notesButtonPanel.getComponents();
        for (Component c : comps) {
            if (c instanceof JButton b && b.getText().equals("Note " + selectedNoteId)) {
                notesButtonPanel.remove(b);
                break;
            }
        }

        notesButtonPanel.revalidate();
        notesButtonPanel.repaint();

        notesInputArea.setText("");
        selectedNoteId = -1;
    }

    private void handleTasksAdd() {
        String name = JOptionPane.showInputDialog(frame, "Enter Task:");
        if (name == null || name.trim().isEmpty()) return;

        taskList.addTask(name);

        JCheckBox cb = new JCheckBox(name);
        cb.addItemListener(e -> taskList.setTaskDone(name, cb.isSelected()));

        tasksPanel.add(cb);
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    private void handleTasksUpdate() {
        Component[] comps = tasksPanel.getComponents();
        for (Component c : comps) {
            if (c instanceof JCheckBox cb && cb.isSelected()) {
                String old = cb.getText();
                String newName = JOptionPane.showInputDialog(frame, "Rename:", old);
                if (newName != null && !newName.trim().isEmpty()) {
                    taskList.renameTask(old, newName);
                    cb.setText(newName);
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Tick a task to rename.");
    }

    private void handleTasksDelete() {
        Component[] comps = tasksPanel.getComponents();
        for (int i = comps.length - 1; i >= 0; i--) {
            Component c = comps[i];
            if (c instanceof JCheckBox cb && cb.isSelected()) {
                taskList.removeTask(cb.getText());
                tasksPanel.remove(cb);
            }
        }
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }
}
