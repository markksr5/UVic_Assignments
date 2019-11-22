public class Course 
{
    private static final int INITIAL_SIZE = 2;

    private String    code;
    private String    instructor;
    private int       size;
    private Student[] roster;

    //
    // Purpose:
    //  Initialize a new instance of Course. The initial size of
    //  the array *must* be the same as INITIAL_SIZE (i.e., you are
    //  *not* permitted to create an array that is the maximum size
    //  needed to pass all tests in A1test.java).
    //
    public Course(String code, String instructor)
    {
		this.code = code;
		this.instructor = instructor;
		this.size = 0;
		this.roster = new Student[INITIAL_SIZE];
    }


    //
    // Purpose:
    //  Return the course code used when creating this class instance.
    //
    public String getCode() {
        return code;
		//return "XXXXXXXXX";
    }    


    //
    // Purpose:
    //  Return the instructor used when creating this class instance.
    //
    public String getInstructor() {
        return instructor;
		//return "YYYYYYYY";
    }

    
    //
    // Purpose:
    //  return the number of students in the roster
    //
    // Returns:
    //  the number of students stored in the roster array
    //
    // Examples:
    //
    // If roster in c is {"V0333:Alice Miller:18", "V0211:Bob Smith:4.5"}
    // then c.getSize() returns 2.
    // 
    // If roster in c is {} then c.getSize() returns 0.
    //
    public int getSize() {
        if (size >= 0) {
			    return size;
		    }
		  return -1;
    }


    //
    // Purpose:
    //  return the Student in the roster at position index
    //
    // Pre-Conditions:
    //  for a Course c
    //  index >= 0 AND
    //  index < c.size
    //
    // Examples:
    //
    // If roster in c is {"V0333:Alice Miller:18", "V0211:Bob Smith:4.5"}
    // then:
    //   c.at(0) returns "V0333:Alice Miller:18"
    //   c.at(1) returns "V0211:Bob Smith:4.5"
    //   the result of calling c.at(3) is undefined
    //
    public Student at (int index)
    {
		  return roster[index];

        // NOTE NOTE NOTE
        //
        // This one line of code here needs to be removed. It's
        // only purpose is so the tester works. 
        //
        // Note: You must NOT allocate a new Student in this method.

        //return null;
    }


    //
    // Purpose:
    //  drop (remove) the Student at position index in the roster
    //
    // Pre-Conditions:
    //  for a Course c where
    //      index >= 0 AND
    //      index <0 c.size
    //
    // If roster in c is {"V0333:Alice Miller:18", "V0211:Bob Smith:4.5"}
    // then after c.drop(0), roster is {"V0211:Bob Smith:4.5"}
    //
    public void drop (int index)
    {
      if (index >= 0 && index < roster.length) {
        for (int i = index; i < size-1; i++) {
          roster[i] = roster[i+1];
        }
        roster[--size] = null;
      }
    }



    //
    // Purpose:
    //  enroll (add) the Student into the course by adding the Student
    //  object at the end of the current roster
    //
    // Comments:
    //
    //  The array you allocated to store Students may
    //  get full, but you are still required to add this
    //  Student (until the JVM runs out of memory!)
    //
    //  This means that you should check to see if the array
    //  is currently full.  If it is, allocate a new array
    //  that is twice as big, then copy the values over
    //  and update the storage reference to be the new array
    //  Finally, insert the new student.
    //
    // Examples:
    //
    // If roster in c is {"V0333:Alice Miller:18"} and s 
    // is "V0211:Bob Smith:4.5", then the value of roster after 
    // c.enroll(s) is {"V0333:Alice Miller:18", "V0211:Bob Smith:4.5"}.
    //
    public void enroll (Student s)
    {
		if (size >= roster.length) {
			Student[] container = new Student[roster.length*2];
			for (int i = 0; i < size; i++) {
				container[i] = roster[i];
			}
			roster=container;
		}
		roster[size++]=s;
		//System.out.println("size = " + size);
    }


    //
    // Purpose:
    //  return the index where S# is in the roster, -1 otherwise
    //
    // Pre-Conditions:
    //  none
    //
    // Returns:
    //  position of s in the roster - an integer between 0 and size() - 1
    //  -1 if s is not in the roster
    //
    // Examples:
    //
    // If roster in c is {"V0333:Alice Miller:18", "V0211:Bob Smith:4.5"}
    //
    //  c.search("V0333") returns 0
    //  
    //  String t = "V0211"
    //  c.search(t) returns 1
    //
    //  c.search("V0444") returns -1
    //
    public int search (String snum)
    {
        for (int i = 0; i < size; i++) {
			  if (roster[i].getId().equals(snum)) {
				  return i;
			  }
		}
		return -1;
    }
}
