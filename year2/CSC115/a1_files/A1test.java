/*
 * UVic CSC 115
 * Fall 2018
 * Author: Mike Zastre (zastre@uvic.ca)
 *
 * A1test.java
 *
 * A test program for assignment 1.
 *
 * This program contains a list of "programmatic" test cases (i.e.,
 * code that exercises parts of a solution and checks the solution's
 * results for correctness). Much of the construction of test cases is
 * ad-hoc -- that is, as the instructor thought of them, he/she added
 * a test case.
 *
 * You should therefore treat this file as something other than a set
 * of exhaustive test cases. (Systematic software testing is a topic
 * for a course after CSC 115.)  Rather it is meant to give you some
 * confidence that you are on the right track with completing the
 * assignment.
 *
 * If your code fails a test case, then do the following:
 *
 * (1) Look at the what code precedes the line number of the failed
 *     test case.
 *
 * (2) Determine from reading that code what the test intends to
 *     accomplish.
 *
 * (3) Only then try to determine what it is about *your* own code
 *     that has led the the failed test. Try to determine which of
 *     your methods has the error. Just because this tester reports your
 *     code failing from a particular method (for example, a "get()"
 *     method) does *not* necessarily mean the error is in that
 *     method.
 */

public class A1test
{
    public static int       testCount = 1;
    public static boolean   testArraySolution = false;
    final public static int STRESS_TEST_SIZE = 2000;


    public static void displayResults (boolean passed)
    {
        /* There is some dipsy-doodling going on here to extract the
         * line number. The idea for doing this is borrowed from:
         *
         *     http://bit.ly/2q3n2uI
         */

        if (passed)
        {
            System.out.println ("Passed test: " + testCount);
        } else {
            Thread me = Thread.currentThread();
            System.out.println ("Failed test: " 
                + testCount + " at line " 
                + me.getStackTrace()[2].getLineNumber()
            );
            System.exit(1);
        }
        testCount++;
    }


    public static void studentTest() {
        System.out.println("Testing of 'Student' class (basic)");
        Student s, t;

        s = new Student("V001", "Vin Diesel");
        displayResults(s.getId().equals("V001"));
        displayResults(s.getName().equals("Vin Diesel"));
        displayResults(s.getCredits() == 0);
   
        s.setCredits(12);
        displayResults(s.getCredits() == 12);
    
        s = new Student("V001", "Vin Diesel", 10.5);
        displayResults(s.getId().equals("V001"));
        displayResults(s.getName().equals("Vin Diesel"));
        displayResults(s.getCredits() == 10.5);
   
        s.setName("I am Groot");
        displayResults(s.getName().equals("I am Groot"));
 
        s.setCredits(12);
        displayResults(s.getCredits() == 12);

        s = new Student("V010", "Vladimir Putin", 21);
        t = new Student("V010", "Vlad Putin", 24);
        displayResults(s.equals(t)); 
        displayResults(t.equals(s)); 
        displayResults(s.toString().equals("V010:Vladimir Putin:21.0"));
        displayResults(t.toString().equals("V010:Vlad Putin:24.0"));

        s = new Student("V099", "Donald Trump", 9.5);
        t = new Student("V909", "Donald Trump", 9.5);
        displayResults(!s.equals(t)); 
        displayResults(!t.equals(s)); 
    }


    public static void gradStudentTest() {
        System.out.println("Testing of 'GradStudent' class (basic)");

        GradStudent g, h;
    
        g = new GradStudent("V256", "Fred Flintstone", 6,
            "Dr. Rubble", GradStudent.PHD);
        displayResults(g.getId().equals("V256"));
        displayResults(g.getName().equals("Fred Flintstone"));
        displayResults(g.getCredits() == 6);
        displayResults(g.getSupervisor().equals("Dr. Rubble"));
        displayResults(g.getProgram() == GradStudent.PHD);
   
        g.setProgram(GradStudent.MSC);
        displayResults(g.getProgram() == GradStudent.MSC);
 
        g.setSupervisor("Mr. Montgomery Burns");
        displayResults(g.getSupervisor().equals("Mr. Montgomery Burns"));

        g = new GradStudent("V314", "Bob Dylan", 5, "Woody Guthrie",
            GradStudent.MA);
        h = new GradStudent("V314", "Robert Zimmerman", 7,
            "Woody Guthrie", GradStudent.MA);
        displayResults(g.equals(h)); 
        displayResults(h.equals(g)); 
        displayResults(g.toString().equals(
            "V314:Bob Dylan:5.0:Woody Guthrie:MA")
        );
        displayResults(h.toString().equals(
            "V314:Robert Zimmerman:7.0:Woody Guthrie:MA")
        );

        g = new GradStudent("V555", "Jerry Seinfeld", 3.5, 
            "Mort Sahl", GradStudent.MA);
        h = new GradStudent("V777", "Jerry Lewis", 12.5,
            "Dean Martin", GradStudent.PHD);
        displayResults(!g.equals(h)); 
        displayResults(!h.equals(g)); 
    }


    public static void courseTestBasic() {
        System.out.println("Testing of 'Course' class (basic)");
    
        Course c;
        Student s;
        GradStudent g;

        c = new Course("CSC 115", "Mike Zastre");
        displayResults(c.getCode().equals("CSC 115")); 
        displayResults(c.getInstructor().equals("Mike Zastre"));
        displayResults(c.getSize() == 0);

        s = new Student("V001", "Vin Diesel", 10.5);
        g = new GradStudent("V256", "Fred Flintstone", 6,
            "Dr. Rubble", GradStudent.PHD);
        displayResults(c.search(s.getId()) == -1);
        displayResults(c.search(g.getId()) == -1);
    }


    public static void courseTestMore() {
        System.out.println("Testing of 'Course' class (more)");

        Course c, d;
        Student s, t, u;

        c = new Course("CSC 123", "Kim Kardashian");
        s = new Student("V915", "Taylor Swift", 0);
        t = new Student("V031", "Tom Hiddleston", 30);
        c.enroll(s);
        c.enroll(t);
        displayResults(c.getSize() == 2);

        displayResults(c.search("V915") != -1);
        displayResults(c.search("V031") != -1);
        displayResults(c.search("V000") == -1);

        c.drop(0);
        displayResults(c.getSize() == 1);
        displayResults(c.search("V031") != -1);
        c.drop(0);
        displayResults(c.getSize() == 0);
        displayResults(c.search("V031") == -1);
    
        c = new Course("GRS 210", "Sally Sappho");
        d = new Course("CHEM 301", "Harry Hydrogen");
        s = new Student("V497", "John Oliver", 0);
        t = new Student("V633", "Samantha Bee", 30);
        c.enroll(s);
        c.enroll(t);
        d.enroll(t);
        d.enroll(s);
        u = c.at(1);
        u.setCredits(15);
        u = d.at(0);
        displayResults(u.getCredits() == 15);
    }


    public static void courseTestResize() {
        System.out.println("Testing of 'Course' class (resizing)");

        Course c;
        Student s, t, u, v, w;

        c = new Course("FOOD 101", "Julie Child");
        s = new Student("V212", "Ray Kroc", 0);
        t = new Student("V121", "Dave Thomas", 0.5);
        u = new Student("V003", "Nigella Lawson", 3.5);
        v = new Student("V003", "Mary Berry", 14);
        w = new Student("V888", "Graham Kerr", 12);
        c.enroll(s);
        c.enroll(t);
        c.enroll(u);
        c.enroll(v);
        c.enroll(w);
        displayResults(c.getSize() == 5);

        s = new Student("V321", "Clarissa Dickson Wright", 1.3);
        t = new Student("V573", "Jennifer Paterson", 9.5);
        u = new Student("V610", "James Barber", 4.5);
        v = new Student("V009", "Bruno Gerussi", 3);
        w = new Student("V903", "Jeff Smith", 0);
        c.enroll(s);
        c.enroll(t);
        c.enroll(u);
        c.enroll(v);
        c.enroll(w);
        displayResults(c.getSize() == 10);
       
        displayResults(c.search("V321") != -1);
        displayResults(c.search("V212") != -1);
        displayResults(c.search("V903") != -1); 

        c.drop(1);
        displayResults(c.search("V212") != -1);
        displayResults(c.search("V888") != -1);
    }


    public static void courseTestStress() {
        System.out.println("Testing of 'Course' class (stress)");

        Course  c;
        Student s;
        String  basename = "Dalek_";
        boolean passed;

        c = new Course("MATH 101", "Albert Einstein");
        for (int i = 0; i < STRESS_TEST_SIZE; i++) {
            if (i % 2 == 0) {
                c.enroll(new Student("V"+i, basename+i, 0));
            } else {
                c.enroll(new GradStudent("V"+i, basename+i, 0,
                    "Alan Turing", GradStudent.MA));
            }
        }
        displayResults(c.getSize() == STRESS_TEST_SIZE);

        passed = true;
        for (int i = 0; i < STRESS_TEST_SIZE; i++) {
            if (c.search("V"+i) == -1) {
                passed = false;
                break;
            }
        }
        displayResults(passed);

        passed = true;
        for (int i = STRESS_TEST_SIZE - 1; i > 0 ; i--) {
            s = c.at(0);
            c.drop(0);

            if (c.search(s.getId()) != -1) {
                passed = false;
                break;
            }

            if (c.getSize() != i) {
                passed = false;
                break;
            } 
        }
        displayResults(passed); 
    }


    public static void main (String[] args)
    {
        try
        {   
            studentTest();
            gradStudentTest();
            
            courseTestBasic();
            courseTestMore();
            courseTestResize();
            courseTestStress();
        }
        catch (Exception e)
        {
            System.out.println("Your code threw an Exception.");
            System.out.println("Perhaps this stack trace will help:");
            e.printStackTrace(System.out);
        }
    }
}
