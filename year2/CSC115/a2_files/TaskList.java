/**
 * TaskList.java
 * Created for CSC 115 Assignment Two.
 */

/**
 * An interface implemented by all TaskList classes. Operations
 * are meant to support a task simulator (i.e., task arrivals,
 * scheduling/dispatching, deletion).
 */
public interface TaskList {
    /** 
     * Examines the task list; if there are no tasks
     * returns true, otherwise false.
     * @return whether or not the list contains tasks
     */
    public boolean isEmpty();


    /** 
     * Purpose:
     *   Returns the number of tasks currently stored in the
     *   task list.
     *
     * Returns:
     *   Either by retrieving some value or by examining some
     *   data structure, returns the number of tasks that are
     *   stored in the list.
     *
     * Examples:
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.getSize() returns 3.
     *
     *   If tl is { } then tl.getSize() returns 0.
     */
    public int  getLength();


    /**
     * Purpose:
     *
     *   Returns the task at the head of the task list, and removes
     *   that task from the list.  If there are no tasks in the list,
     *   then the null value is returned.
     *
     * Returns:
     *   Either an instance of Task or the null value.
     *
     * Examples:
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.removeHead() returns (10, 100) and afterwards tl
     *   is equal to { (5, 31), (4, 102) }.
     *
     *   If tl is { } then tl.removeHead() returns null and there
     *   is no change to tl.
     */
    public Task removeHead();


    /**
     * Purpose:
     *   If there is a task in the list corresponding the
     *   number passed in, then that task is removed
     *   from the list. Otherwise there is no change in the list.
     *
     * Precondition:
     *   No two tasks in the list have the same task number.
     *
     * Return:
     *   A reference to the Task if there was one removed,
     *   null otherwise.
     *
     * Examples:
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.remove(31) returns (5, 31) and the value of tl
     *   afterwards is { (10, 100), (4, 102) }
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.remove(10, 0) returns null and tl is unchanged.
     *
     *   If tl is { } then all calls of tl.remove() will result in
     *   the value null being returned.
     */
    public Task remove(int number);


    /** 
     * Purpose:
     *   Given an existing task list, accept a new Task and
     *   insert into the list such that tasks are in decreasing
     *   priority order from head to tail, and all tasks
     *   having the same priority are in the list in the order
     *   of insertion (i.e., tasks inserted later are closer to
     *   the tail than tasks inserted earlier). Note that no    
     *   two tasks may have the same task number.
     *
     * Returns:
     *   true if the task was inserted
     *   false if there was another task already in the list with
     *      the same task number
     *
     * Examples:
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.insert(5, 291) returns true and the value of tl
     *   afterwards is { (10, 100), (5, 31), (5, 291), (4, 102) }
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.insert(2, 99) returns true and the value of tl
     *   afterwards is { (10, 100), (5, 31), (4, 102), (2, 99) }.
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.insert(2, 31) returns false and the value of tl
     *   afterwards is unchanged.
     */
    public boolean insert(int priority, int number);


    /**
     * Purpose:
     *   Given an existing task list, return the task at position
     *   pos, where pos == 0 is the same as the task at the head, and
     *   pos == tl.getSize() -1 is the same as the task at the tail.
     *
     *  Returns:
     *    Task at position i in the list, null if pos is < 0 or
     *    pos >= length of list.
     *
     * Examples:
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.retrieve(0) is (10, 100).
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.retrieve(1) is (5, 31).
     *
     *   If tl is { (10, 100), (5, 31), (4, 102) } then
     *   tl.retrieve(3) returns null.
     *
     *   If tl is {}, then tl.retrieve() returns null for
     *   all values passed in as a parameter.
     */ 
    public Task retrieve(int pos);
}
