/*
 * UVic CSC 115
 * Fall 2018
 * Author: Mike Zastre (zastre@uvic.ca)
 *
 * A2test.java
 *
 * A test program for assignment 2.
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

public class A2test
{
    public static int       testCount = 1;
    public static boolean   testArraySolution = false;
    final public static int STRESS_TEST_SIZE = 2000;
    public static TaskList  testList;

    static String[] sim1_in = { "TA 2 1", "TA 11 2", "TA 9 3", "TA 6 5",
        "TA 1 9", "TA 5 13", "TA 12 16", "TA 11 21", "TA 14 25", "TA 8 29", 
        "TA 11 30", "SC", "SC", "SC", "SC", "TA 5 21", "SC", 
        "TA 5 30", "TA 4 32", "SC", "ST" };
    static int[] sim1_out = { 25, 16, 2, 21, 30, 3, 8 };

    static String[] sim2_in = { "TA 14 3", "TA 5 6", "TA 12 11", 
        "TA 11 16", "TA 3 19", "TA 3 23", "TA 14 24", "TA 1 26", 
        "TA 6 31", "TA 7 33", "ST", "SC", 
        "TA 4 3", "SC", "SC", "TD 26", 
        "SC", "SC", "SC", "TD 6", "SC", "TA 3 3", 
        "TA 12 4", "ST", "TD 3", "TA 5 3", "TD 3", 
        "SC", "TD 23", "TA 1 23", "ST" };
    static int[] sim2_out = { 10, 3, 24, 11, 16, 33, 31, 3, 4, 4, 2 };

    static String[] sim3_in = { "TA 6 1", "TA 0 6", "TA 8 7", 
        "TA 14 8", "TA 5 13", "TA 0 18", "TA 8 21", 
        "TA 14 24", "TA 6 25", "TA 3 30", "ST", 
        "TD 25", "SC", "TD 18", "SC", "SC", 
        "TA 15 7", "TD 30", "TA 4 30", "SC", 
        "SC", "TA 6 21", "SC", "TA 7 1", "TD 6", 
        "TA 4 6", "TD 21", "TD 1", "TD 30", 
        "SC", "TA 15 13", "TD 13", "TA 6 13", 
        "TA 10 14", "TD 13", "TA 8 13", 
        "SC", "SC", "SC", "TA 4 6", "TA 9 10", 
        "TD 6", "TA 14 6", "TA 9 8", "TA 8 11", 
        "SC", "TA 3 6", "TA 13 7", "SC", 
        "SC", "ST" };
    static int[] sim3_out = { 10, 8, 24, 7, 7, 21, 1, 13, 
        14, 13, 6, 6, 7, 10, 3 };

    static String[] sim4_in = { "TA 11 10", "TA 0 12", "TA 11 15", 
        "TA 13 19", "TA 5 23", "TA 3 24", "TA 10 27", 
        "TA 4 29", "TA 7 34", "TA 1 3", "TA 9 8", 
        "TD 12", "TA 8 12", "SC", "TA 2 19", 
        "TD 19", "SC", "TA 1 10", "TA 1 14", "SC", 
        "SC", "TA 8 27", "TA 12 32", "TD 14", 
        "TA 8 14", "SC", "SC", "SC", "SC", "TA 11 27", 
        "TD 27", "TA 2 27", "TA 13 28", "TA 1 33", 
        "TD 23", "TA 2 23", "TA 5 25", "SC", "SC", 
        "TA 11 14", "TD 24", "TD 34", "SC", "SC", "SC", 
        "SC", "TA 9 27", "TA 5 32", "TA 2 35", "TA 1 36", 
        "TA 15 5", "TA 11 8", "SC", "SC", "SC", "SC", 
        "SC", "TD 10", "TA 4 10", "SC", "SC", "SC", 
        "TA 4 3", "TA 9 7", "ST", "TA 0 11", "TA 11 12", 
        "TA 8 17", "SC", "ST", "TD 36", "TA 11 36", 
        "TD 3", "TD 7", "SC", "ST" };
    static int[] sim4_out = { 19, 10, 15, 27, 32, 8, 12, 27, 28, 
        14, 14, 25, 29, 27, 5, 8, 27, 32, 
        23, 10, 35, 3, 4, 12, 6, 36, 3 };


    public static void displayResults (boolean passed)
    { /* There is some dipsy-doodling going on here to extract the
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


    public static void resetTestList() {
        if (testArraySolution) {
            testList = new TaskListArray();
        } else {
            testList = new TaskListLL();
        }
    }


    public static boolean taskSimulator(String[] events, int[] scheduled) {
        int in, out;

        resetTestList();

        for (in = 0, out = 0; 
                in < events.length && out < scheduled.length; 
                in++)
        {
            String[] event = events[in].split(" ", -1);
            if (event[0].equals("ST")) {
                int length = testList.getLength();
                if (length != scheduled[out]) {
                    System.out.println("@ " + in + " expected ["
                        + scheduled[out] + "] but got ["
                        + length + "] instead");
// System.out.println("DEBUG " + testList.toString());
                    return false;
                }
                out++;
            } else if (event[0].equals("TA")) {
                int priority = Integer.parseInt(event[1]);
                int number   = Integer.parseInt(event[2]);
                testList.insert(priority, number);
            } else if (event[0].equals("TD")) {
                int number = Integer.parseInt(event[1]);
                testList.remove(number);
// System.out.println("DEBUG TD " + number + ": " + testList.toString());
            } else if (event[0].equals("SC")) {
                Task t = testList.removeHead();
                if (t == null) {
                    System.out.println("@ " + in + " expected "
                        + scheduled[out] + " but got null instead");
// System.out.println("DEBUG " + testList.toString());
                    return false; 
                }
                if (t.getNumber() != scheduled[out]) {
                    System.out.println("@ " + in + " expected "
                        + scheduled[out] + " but got "
                        + t.getNumber() + " instead");
// System.out.println("DEBUG " + testList.toString());
                    return false;
                }
                out++;
            } else {
                System.out.println("@ " + in + " the operation '"
                    + event[0] + "' makes no sense.");
                return false;
            }
        }

        if (in == events.length && out == scheduled.length) {
            return true;
        } else {
            return false;
        }
    }


    public static void taskTest() {
        System.out.println("Testing of 'Task' class (basic)");
        Task t1, t2;

        t1 = new Task(10, 200);
        t2 = new Task(12, 201);

        displayResults(t1.getPriority() == 10 && t1.getNumber() == 200);
        displayResults(t2.getPriority() == 12 && t2.getNumber() == 201);
        displayResults(!t1.equals(t2));

        t1 = new Task(10, 200);
        t2 = new Task(10, 200);
        displayResults(t1.equals(t2));
        displayResults(t1.compareTo(t2) == 0);

        t1 = new Task(10, 200);
        t2 = new Task(12, 201);
        displayResults(t1.compareTo(t2) == -1);
        displayResults(t2.compareTo(t1) == 1);
    }


    public static void taskListBasicTest() {
        System.out.println("Testing of 'TaskList' class (basic)");
        
        Task t1, t2;

        resetTestList();
        displayResults(testList.getLength() == 0);
        displayResults(testList.isEmpty());

        displayResults(testList.insert(10, 200));
        displayResults(testList.getLength() == 1);

        t1 = testList.retrieve(0);
        displayResults(t1 != null);
        displayResults(t1.getPriority() == 10 && t1.getNumber() == 200);
        displayResults(testList.getLength() == 1);

        t1 = testList.remove(200);
        displayResults(t1 != null);
        displayResults(t1.getPriority() == 10 && t1.getNumber() == 200);
        displayResults(testList.getLength() == 0);

        t1 = testList.remove(200);
		System.out.println(t1);
        displayResults(t1 == null);
        displayResults(testList.getLength() == 0);

        resetTestList();
        displayResults(testList.insert(10, 200));
        t1 = testList.removeHead();
        displayResults(t1 != null);
        displayResults(t1.getPriority() == 10 && t1.getNumber() == 200);
        displayResults(testList.getLength() == 0);
    }


    public static void taskListGrowthTest() {
        System.out.println("Testing of 'TaskList' class (growth)");
        
        Task t1, t2, t3;

        resetTestList();
        testList.insert(10, 200);
        testList.insert(12, 150);
        testList.insert(14, 250);
        displayResults(testList.getLength() == 3);
        t1 = testList.retrieve(0);
        t2 = testList.retrieve(1);
        t3 = testList.retrieve(2);
        displayResults(t1 != null && t2 != null && t3 != null);
        displayResults(t1.getNumber() == 250 && t2.getNumber() == 150 
            && t3.getNumber() == 200);

        testList.insert(16, 80);
        testList.insert(18, 84);
        testList.insert(20, 78);
        displayResults(testList.getLength() == 6);

        t1 = testList.removeHead();
        displayResults(testList.getLength() == 5);
        displayResults(t1 != null && t1.getNumber() == 78);
    }


    public static void taskListUniqueTest() {
        System.out.println("Testing of 'TaskList' class (unique)");
        
        Task t1, t2, t3;

        resetTestList();
        testList.insert(10, 200);
        testList.insert(12, 150);
        testList.insert(14, 250);
        displayResults(testList.getLength() == 3);

        displayResults(testList.insert(16, 80) == true);
        displayResults(testList.insert(16, 80) == false);
        displayResults(testList.insert(20, 80) == false);
        displayResults(testList.insert(5, 80) == false);
    
        displayResults(testList.getLength() == 4);
    
    }


    public static void taskListOrderingTest() {
        System.out.println("Testing of 'TaskList' class (ordering)");
        
        Task t1, t2, t3;

        resetTestList();
        testList.insert(12, 611);
        testList.insert(14, 314);
        testList.insert(10, 401);
        displayResults(testList.getLength() == 3);
        t1 = testList.retrieve(0);
        t2 = testList.retrieve(1);
        t3 = testList.retrieve(2);
        displayResults(t1 != null && t1.getNumber() == 314);
        displayResults(t2 != null && t2.getNumber() == 611);
        displayResults(t3 != null && t3.getNumber() == 401);

        resetTestList();
        testList.insert(14, 314);
        testList.insert(10, 401);
        testList.insert(12, 611);
        testList.insert(12, 613);
        testList.insert(12, 614);
        displayResults(testList.getLength() == 5);
        t1 = testList.retrieve(4);
        displayResults(t1 != null && t1.getNumber() == 401);
        t1 = testList.retrieve(3);
        displayResults(t1 != null && t1.getNumber() == 614);
        t1 = testList.retrieve(1);
        displayResults(t1 != null && t1.getNumber() == 611);
    }


    public static void main (String[] args)
    {
        if (args.length > 0) {
            testArraySolution = true;
        }

        try
        {   
/* Disabled for now...
            taskTest();
*/
            taskListBasicTest();
            taskListGrowthTest();
            taskListUniqueTest();
            taskListOrderingTest();
            
            System.out.println("Running task simulation 1");
            displayResults(taskSimulator(sim1_in, sim1_out));

            System.out.println("Running task simulation 2");
            displayResults(taskSimulator(sim2_in, sim2_out));

            System.out.println("Running task simulation 3");
            displayResults(taskSimulator(sim3_in, sim3_out));

            System.out.println("Running task simulation 4");
            displayResults(taskSimulator(sim4_in, sim4_out));
        }
        catch (Exception e)
        {
            System.out.println("Your code threw an Exception.");
            System.out.println("Perhaps this stack trace will help:");
            e.printStackTrace(System.out);
        }
    }
}
