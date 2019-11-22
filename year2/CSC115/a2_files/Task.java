/**
 * Task.java
 * Created for CSC 115 Assignment Two.
 */

/**
 * Task objects contain the information necessary to represent
 * a task in a scheduling simulator, i.e., task priority
 * and task number.
 *
 * This class is not meant to be used as a linked-list node!
 */
public class Task {
    private int priority;
    private int number;


    Task(int priority, int number) {
        this.priority = priority;
        this.number   = number;
    }

    
    public int getNumber() {
        return number;
    }


    public int getPriority() {
        return priority;
    }


    public boolean equals(Task o) {
        if (!(o instanceof Task)) {
            return false;
        }

        Task t = (Task)o;
        return (priority == t.getPriority() &&
            number == t.getNumber());
    }


    /*
     * If the value of the integer stored in this task's priority
     * is less than the value of the integer stored in task t's
     * priority, then -1 is returned;
     *
     * otherwise if the value of the integer stored in this task's
     * priority is greater than the value of the ingeger stored in  
     * task t's priority, then 1 is returned;
     *
     * otherwise zero (0) is returned.
     */
    public int compareTo(Task t) {
        if (priority < t.getPriority()) {
            return -1;
        } else if (priority > t.getPriority()) {
            return 1;
        } else {
            return 0;
        }
    }


    public String toString() {
        return "(" + priority + ", " + number + ")";
    }
}
