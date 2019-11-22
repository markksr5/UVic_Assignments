public class GradStudent extends Student
{
    final static int PHD = 1;
    final static int MSC = 2;
    final static int MA = 3;

    private String supervisor;
    private int    program;


    // Purpose:
    //  Initialize this instance of GradStudent. Make sure to use
    //  the superclass constructor with an appropriate call
    //  to "super(...)".
    //
    public GradStudent (String id, String name, double credits,
        String supervisor, int program)
    {
      super(id, name, credits);
      this.supervisor = supervisor;
      this.program = program;
    }


    // Purpose:
    //  Assign the supervisor of this grad student to the
    //  value passed in as a parameter.
    //
    public void setSupervisor(String supervisor)
    {
		  this.supervisor = supervisor;
    }

   
    // Purpose:
    //  Return the name of the supervisor for this grad student.
    //
    public String getSupervisor()
    {
		  return supervisor;
        //return "abcdefghijklmnopqrstvuwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }


    // Purpose:
    //  Change the program in which the grad student is registered
    //  to that given as a parameter.
    //
    public void setProgram(int program)
    {
		  this.program = program;
    }

   
    // Purpose:
    //  Return the program in which the grad student is registered.
    //
    public int getProgram()
    {
		  return program;
        //return -1;
    }


    // Purpose:
    //  Return a String representation of this Grad Student
    // 
    // Returns:
    //  id:name:credits:supervisor:program
    //  
    // Examples:
    //  GradStudent p = new GradStudent("V0222", "Kristy Clark", 1.5,
    //      PHD, "Dr. Weaver");
    //  
    //  p.toString() returns  V0222:Kristy Clark:1.5:Dr. Weaver:PHD
    // 
    //  Note: The strings for program are "PHD", "MSC" and "MA".
    //
    public String toString()
    {
      if (program == PHD) {
        return super.getId() + ":" + super.getName() + ":" + super.getCredits() + ":" + supervisor + ":PHD";
      }
      else if (program == MSC) {
        return super.getId() + ":" + super.getName() + ":" + super.getCredits() + ":" + supervisor + ":MSC";
      }
      else if (program == MA) {
        return super.getId() + ":" + super.getName() + ":" + super.getCredits() + ":" + supervisor + ":MA";
      }
      else {
        return "invalid program";
      }
          //return "No soup for you.";
      }
}
