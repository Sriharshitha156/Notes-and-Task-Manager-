package com.studyopedia;

class TaskLinkedList {
    TaskNode head;

    void addTask(String name) {
        TaskNode newNode = new TaskNode(name);
        if (head == null) {
            head = newNode;
        } else {
            TaskNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    void removeTask(String name) {
        if (head == null) return;

        if (head.taskName.equals(name)) {
            head = head.next;
            return;
        }

        TaskNode prev = head;
        TaskNode curr = head.next;

        while (curr != null) {
            if (curr.taskName.equals(name)) {
                prev.next = curr.next;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    void renameTask(String oldName, String newName) {
        TaskNode temp = head;
        while (temp != null) {
            if (temp.taskName.equals(oldName)) {
                temp.taskName = newName;
                return;
            }
            temp = temp.next;
        }
    }

    void setTaskDone(String name, boolean done) {
        TaskNode temp = head;
        while (temp != null) {
            if (temp.taskName.equals(name)) {
                temp.isDone = done;
                return;
            }
            temp = temp.next;
        }
    }
}
