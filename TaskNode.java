package com.studyopedia;

class TaskNode {
    String taskName;
    boolean isDone;
    TaskNode next;

    TaskNode(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
        this.next = null;
    }
}
