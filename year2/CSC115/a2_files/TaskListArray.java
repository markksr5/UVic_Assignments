public class TaskListArray implements TaskList {
    private int numTasks;
    private Task[] tasks;
    final public static int INITIAL_SIZE = 2;

    public TaskListArray() {
        tasks = new Task[INITIAL_SIZE];
        numTasks = 0;
    }


    public int getLength() {
        return numTasks;
    }


    public boolean isEmpty() {
        return (numTasks == 0);
    }


    public Task removeHead() {
        if (numTasks == 0) {
            return null;
        }

        Task result = tasks[0];

        numTasks--;
        for (int i = 0; i < numTasks; i++) {
            tasks[i] = tasks[i+1];
        }

        return result;
    }

    
    public Task remove(int number) {
        int pos = -1;

        for (int i = 0; i < numTasks; i++) {
            if (tasks[i].getNumber() == number) {
                pos = i;
                break;
            }
        }

        if (pos == -1) {
            return null;
        }

        Task result = tasks[pos];
        numTasks--;
        for (int i = pos; i < numTasks; i++) {
            tasks[i] = tasks[i+1];
        }

        return result;
    }


    public boolean insert(int priority, int number) {
        if (exists(number)) {
            return false;
        }

        Task t = new Task(priority, number);

        if (numTasks == tasks.length) {
            Task[] temp = new Task[tasks.length * 2];
            for (int i = 0; i < numTasks; i++) {
                temp[i] = tasks[i];
            }
            tasks = temp;
        }

        int insertPos = -1;

        for (int i = 0; i < numTasks; i++) {
            if (t.compareTo(tasks[i]) == 1) {
                insertPos = i;
                break;
            }            
        }

        /* Either we insert the item as the end of the list,
         * or we insert the item before some other tasks in the
         * list. 
         */
        if (insertPos == -1) {
            tasks[numTasks] = t;
            numTasks++;
        } else {
            numTasks++;
            for (int i = numTasks - 1; i > insertPos; i--) {
                tasks[i] = tasks[i-1];
            } 
            tasks[insertPos] = t;
        }

        return true;
    }


    public Task retrieve(int pos) {
        if (pos < numTasks) {
            return tasks[pos];
        } else {
            return null; 
        }
    }

    private boolean exists(int number) {
        for (int i = 0; i < numTasks; i++) {
            if (tasks[i].getNumber() == number) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        String result = "{";
        String separator = "";

        for (int i = 0; i < numTasks; i++) {
            result += separator + tasks[i].toString();
            separator = ", ";
        }
        result += "}";

        return result;
    }
}
