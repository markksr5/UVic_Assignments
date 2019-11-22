public class Student
{
    private String id;
    private String name;
    private double credits;

    // Purpose:
    //  Initialize this instance of Student, but where all
    //  instance variables have values of null or zero.
    //
    public Student ()
    {
      id = "";
      name = "";
      credits = 0;
    }



    // Purpose:
    //  Initialize this instance of Student. Set the number of
    //  credits to zero.
    //
    public Student (String id, String name)
    {
      this.id = id;
      this.name = name;
      credits = 0;
    }


    // Purpose:
    //  Initialize this instance of Student.
    //
    public Student (String id, String name, double credits)
    {
      this.id = id;
      this.name = name;
      this.credits = credits;
    }


    // Purpose:
    //  Change the credits associated with this Student
    //
    public void setCredits (double credits)
    {
		  this.credits = credits;
    }


    // Purpose:
    //  Return the credits associated with this Student
    //
    public double getCredits()
    {
		  return credits;
        //return -1.0;
    }


    // Purpose:
    //  Change the name associated with this student.
    //  
    public void setName (String name)
    {
		  this.name = name;
    }


    // Purpose:
    //  Return the name associated with this student.
    //
    public String getName()
    {
		  return name;
        //return "ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuvwxyz";
    }


    // Purpose:
    //  Return the ID associated with this student.
    //
    public String getId()
    {
		  return id;
        //return "ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuvwxyz";
    }


    // Purpose:
    //  Compare this instance of Student to other; return true if
    //  they are the same, false otherwise.
    //  
    //  We consider two Students to be "equal" if their IDs are
    //  the same. We do *not* consider the name or number of credits.
    //
    // Pre-conditions:
    //  other is not null
    //
    // Returns:
    //  true    if this instance's id is the same as other's id
    //  false   otherwise
    //
    // Examples:
    //
    //  Student p = new Student("V0314", "Donald Trump", 1.5);
    //  Student q = new Student("V0272", "Justin Trudeau");
    //  Student r = new Student("V0314", "Donald J. Trump", 1.5);
    //  
    //  p.equals(q) - returns false
    //  p.equals(r) - returns true
    public boolean equals (Student other)
    {
      if (this.id.equals(other.id)) {
        return true;
      }
      else {
        return false;
      }
    }


    // Purpose:
    //  Return a String representation of this Student
    // 
    // Returns:
    //  id:name:credits
    //  
    // Examples:
    //  Student p = new Student("V0314", "Donald Trump", 1.5);
    //  
    //  p.toString() returns    V0314:Donald Trump:1.5
    //
    public String toString()
    {
		  return id + ":" + name + ":" + credits;
        //return "ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuvwxyz";
    }
}
