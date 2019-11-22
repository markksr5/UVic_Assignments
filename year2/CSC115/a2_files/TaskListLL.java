public class TaskListLL implements TaskList {
	private TaskListNode head;
	private int count;
	
    public TaskListLL() {
		this.head = null;
		this.count = 0;
    }


    public int getLength() {
		if (head == null) {
			return 0;
		}
		else if (count >= 0) {
			return count;
		}
        return -1;
    }


    public boolean isEmpty() {
		if (head == null) {
			return true;
		}
        return false;
    }


    public Task removeHead() {
        Task result = new Task(-1,-1);
		TaskListNode curr = head;
		
		if (this.isEmpty()) {
			return null;
		}
		head = head.next;
		this.count--;
		return curr.task;
    }

    
    public Task remove(int number) {
		
		//Task prevtask = new Task(-1, -1);
		TaskListNode curr = head;
		TaskListNode prev = head;
		boolean foundtask = false;
		if (head == null) {
			return null;
		}
		if (head.task.getNumber() == number) {
			prev.task = this.removeHead();
			foundtask = true;
		}
		else {
			curr = head.next;
			while (curr != null) {
			if (curr.task.getNumber() == number) {
				foundtask = true;
				break;
			}
			prev = curr;
			curr = curr.next;
			}
			if (foundtask == false) {
				return null;
			}
			prev.next = curr.next;
			this.count--;
		}
		return prev.task;
    }


    public boolean insert(int priority, int number) {
		//returns false if duplicate task number
		//returns true if inputted task
		
		Task newTask = new Task(priority, number);
		TaskListNode newNode = new TaskListNode(newTask);
		if (head == null) { //empty list
			head = newNode;
		}
		else {
			TaskListNode curr = head;
			
			while (curr != null) {
				if (curr.task.getNumber() == newNode.task.getNumber()) {
					return false;
				}
				curr = curr.next;
			}
			
			curr = head;
			if (head.task.getPriority() < newNode.task.getPriority()) {
				newNode.next = head;
				head = newNode;
				this.count++;
				return true;
			}
			TaskListNode prev = head;
			
			while (curr != null) {
				if (curr.task.getPriority() >= newNode.task.getPriority()) {
					prev = curr;
					if (curr.next != null) {
						curr = curr.next;
					}
					else {
						curr.next = newNode;
						this.count++;
						return true;
					}
				}
				else {
					break;
				}
			}
			
			newNode.next = curr;
			prev.next = newNode;
		}
		this.count++;
        return true;
    }


    public Task retrieve(int pos) {
		
		if (pos >= this.getLength() || pos < 0) {
			return null;
		}
		TaskListNode curr = head;
		for (int i = 0; i < pos; i++) {
			curr = curr.next;
		}
		
        return curr.task;
    }
}
