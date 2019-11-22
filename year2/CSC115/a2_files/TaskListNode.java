class TaskListNode {
    Task task;
    TaskListNode next;

    TaskListNode(Task task) {
        this.task = task;
        this.next = null;
    }

    TaskListNode(Task task, TaskListNode next) {
        this.task = task;
        this.next = next;
    }
}
