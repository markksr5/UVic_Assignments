
public class A3test{

    boolean stack_works = true;
    private static String results [] = new String[18];

    // for (int i = 0; i < results.length; i++){
    //     results[i] = "ABCD";
    // }

    private static String tests [] = {"Node.java testing -- ",
                                    "StackEmptyException.java testing -- ",
                                    "Stack Constructor -- ",
                                    "isEmpty() method -- ",
                                    "push() method -- ",
                                    "peek() method -- ",
                                    "pop() method -- ",
                                    "popAll() method -- ",
                                    "Test 1     -- ",
                                    "Test 2     -- ",
                                    "Test 3     -- ",
                                    "Test 4     -- ",
                                    "Test 5     -- ",
                                    "Test 6     -- ",
                                    "Test 7     -- ",
                                    "Test 8     -- ",
                                    "Test 9     -- ",
                                    "Test 10    -- "
        };

    public static void main(String [] args){
        Node_test();
        StackEE_test();
        StringStack_test();
        ArithExpression_test();
        results_summary();
    }

    public static void Node_test(){

        System.out.println("===============================================");
        System.out.println("Testing Node.java");
        System.out.println("===============================================\n");

        try {
            Node n1 = new Node();
        } catch (Exception e){
            System.out.println("Node Testing FAILED. Node object could not be created");
            results[0] = "FAILED";
            return;
        }

        System.out.println("Node.java testing PASSED\n");
        results[0] = "PASSED";
    }

    public static void StackEE_test(){

        System.out.println("===============================================");
        System.out.println("Testing StackEmptyException.java");
        System.out.println("===============================================\n");

        try {
            StackEmptyException s1 = new StackEmptyException();
        } catch (Exception e){
            System.out.println("StackEmptyException.java FAILED. StackEmptyException object could not be created");
            results[1] = "FAILED";
        }

        try {
            StackEmptyException s1 = new StackEmptyException("error");
        } catch (Exception e){
            System.out.println("StackEmptyException.java FAILED. StackEmptyException object could not be created");
            results[1] = "FAILED";
        }

        System.out.println("StackEmptyException.java testing PASSED\n");
        results[1] = "PASSED";
    }


    public static void StringStack_test(){

        StringStack st;
        boolean push_test = true;
        boolean pop_test = true;
        boolean peek_test = true;
        boolean popAll_test = true;

        System.out.println("===============================================");
        System.out.println("Testing StringStack.java");
        System.out.println("===============================================\n");

        // Create Stack
        try {
            st = new StringStack();
        } catch (Exception e){
            System.out.println("1) StringStack object could not be created \n" + e);
            System.out.println("\nEnd of StringStack testing");
            results[2] = "FAILED";
            return;
        }

        System.out.println("2) Stack is created. Constructor testing PASSED");
        results[2] = "PASSED";

        // Test isEmpty when stack is empty. Should return true
        if (!st.isEmpty()){
            System.out.println("3) isEmpty() method returns false when stack is empty");
            results[3] = "FAILED";
        }

        // push something onto stack
        try {
            st.push("A");
        } catch (Exception e) {
            System.out.println("4) push() method throws an Exception \n" + e);
            System.out.println("End of StringStack testing");
            results[4] = "FAILED";
            return;
        }

        // Test isEmpty method when stack is not empty. should return false
        if (st.isEmpty()){
            System.out.println("5) isEmpty() method returns true when stack is not empty");
            results[3] = "FAILED";
        } else {
            System.out.println("6) isEmpty() method testing PASSED");
            results[3] = "PASSED";
        }

        // Test peek and push method
        String pk;

        try {
            pk = st.peek();

            if (!pk.equals("A")){
                peek_test = false;
                System.out.println("7) String returned by peek() method is incorrect.");
                results[5] = "FAILED";
            }

        } catch (Exception e) {
            System.out.println("8) peek() method throws an exception \n" + e);
            results[5] = "FAILED";
            peek_test = false;
        }


        if (peek_test == true){

            try {
                st.push("B");
            } catch (Exception e) {
                System.out.println("9) push() method throws an Exception \n" + e);
                results[4] = "FAILED";
                push_test = false;
            }

            try {
                pk = st.peek();

                if (!pk.equals("B")){
                    System.out.println("10) String returned by peek() method is incorrect. Push() method not working properly");
                    results[4] = "FAILED";
                    push_test = false;
                    System.out.println("\nEnd of StringStack testing");
                    return;
                }

            } catch (Exception e) {
                System.out.println("11) peek() method throws an exception");
                results[5] = "FAILED";
                peek_test = false;
            }
        }



        // Test pop method
        String pp;
        try {
            pp = st.pop();
            if (!pp.equals("B")){
                System.out.println("12) pop() method does not return the expected string");
                results[6] = "FAILED";
                pop_test = false;
            }
            pp = st.pop();
            if (!pp.equals("A")){
                System.out.println("13) pop() method does not return the expected string");
                results[6] = "FAILED";
                pop_test = false;
            }

        } catch (Exception e){
            System.out.println("14) pop() method returns an Exception \n" + e);
            results[6] = "FAILED";
            pop_test = false;
        }

        // Test popAll method
        st.push("C");
        st.push("D");

        try {
            st.popAll();
        } catch (Exception e){
            System.out.println("15) popAll() method generates exception");
            results[7] = "FAILED";
            popAll_test = false;
        }

        if (!st.isEmpty()){
            System.out.println("16) Error in popAll method");
            results[7] = "FAILED";
            popAll_test = false;
        }



        // test for StackEmptyException

        // if push fails
        if (push_test != false){

            if (popAll_test == true && peek_test == true && pop_test == true){

                st.popAll();
                pop_test = false;
                peek_test = false;
                try {
                    st.peek();
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
                } catch (StackEmptyException s){
                    results[5] = "PASSED" ;
                    peek_test = true;
                } catch (Exception e){
                    System.out.println("peek() method did not generate StackEmptyException when stack is empty");
                    peek_test = false;
                }

                try {
                    st.pop();
                } catch (StackEmptyException s){
                    results[6] = "PASSED";
                    pop_test = true;
                } catch (Exception e){
                    System.out.println("pop() method did not generate StackEmptyException when stack is empty");
                    pop_test = false;
                }

            } else if (pop_test == true && peek_test == true) {
                st.pop();
                st.pop();
                pop_test = false;
                peek_test = false;
                try {
                    st.peek();
                } catch (StackEmptyException s){
                    results[5] = "PASSED" ;
                    peek_test = true;
                } catch (Exception e){
                    System.out.println("peek() method did not generate StackEmptyException when stack is empty");
                    peek_test = false;
                }

                try {
                    st.pop();
                } catch (StackEmptyException s){
                    results[6] = "PASSED";
                    pop_test = true;
                } catch (Exception e){
                    System.out.println("pop() method did not generate StackEmptyException when stack is empty");
                    pop_test = false;
                }
            }
        }

        if (push_test == true){
            System.out.println("\npush() method testing PASSED");
            results[4] = "PASSED";
        } else {
            System.out.println("\npush() method testing FAILED");
            results[4] = "FAILED";
        }

        if (peek_test == true){
            System.out.println("peek() method testing PASSED");
            results[5] = "PASSED";
        } else {
            System.out.println("peek() method testing FAILED");
            results[5] = "FAILED";
        }

        if (pop_test == true){
            System.out.println("pop() method testing PASSED");
            results[6] = "PASSED";
        } else {
            System.out.println("pop() method testing FAILED");
            results[6] = "FAILED";
        }

        if (popAll_test == true){
            System.out.println("popAll() method testing PASSED");
            results[7] = "PASSED";
        } else {
            System.out.println("popAll() method testing FAILED");
            results[7] = "FAILED";
        }

        System.out.println("\nEnd of StringStack.java Testing");
        System.out.println("----------------------------------------------------------------\n");


    }

    public static void ArithExpression_test(){

        System.out.println("===============================================");
        System.out.println("Testing ArithExpression.java");
        System.out.println("===============================================\n");

        String [] test = {"2 + 5",
                                    "2 + 5 - 2",
                                    "2 + 5 * 2",
                                    "(2 + 5) * 2",
                                    "(2 + 5) * 2^2",
                                    "(2 + 50 / 5) * 2 ^2 + 20 / 4 + 2",
                                    "a",                                             // non numeric input
                                    "(2 5) + 12",                                // erroneous input
                                    "2 2",
                                    "+ +"

            };
        double [] expected_results = {7, 5, 12, 14, 28, 55};
        String [] postFixToken_expected = {"2 5 + ",
                                                                    "2 5 + 2 - ",
                                                                    "2 5 2 * + ",
                                                                    "2 5 + 2 * ",
                                                                    "2 5 + 2 2 ^ * ",
                                                                    "2 50 5 / + 2 2 ^ * 20 4 / + 2 + ",
                                                                    "a ",
                                                                    "2 5 12 + ",
                                                                    "2 2 ",
                                                                    "+ + "
                                                                };
        boolean testing_results [] = new boolean[10];

        // initialize testing results to true
        for (int i = 0; i <testing_results.length; i++){
            testing_results[i]= true;
        }

        // TEST 1
        System.out.println("--------------------------------------");
        System.out.println("Test 1. Input Expression -- " + test[0]);
        System.out.println("--------------------------------------\n");
        try {
            ArithExpression a1 = new ArithExpression(test[0]);

            String correct_expression = postFixToken_expected[0];
            String student_expression = a1.getPostfixExpression();

            // trim both expressions
            correct_expression = correct_expression.trim();
            student_expression = student_expression.trim();

            if (!(student_expression.equals(correct_expression))){
                System.out.println("Test 1 FAILED -- PostFix Expression is incorrect");
                System.out.println("Expected post fix expression is " + correct_expression + " Expression produced is " + student_expression + "\n");
                results[8] = "FAILED";
                testing_results[0] = false;
            } else {
                double d = a1.evaluate();
                if (d != expected_results[0]){
                    System.out.println("Test 1 FAILED. input expression: " + test[0]);
                    System.out.println(" Expected results is -- " + expected_results[0] + "result produced is -- " + d + "\n");
                    testing_results[0] = false;
                }
            }
        } catch (Exception e){
            System.out.println("Test 1 FAILED. ArithExpression object was not created. Exception generated \n" + e);
            System.out.println("End of ArithExpression.java testing\n");
            testing_results[0] = false;
            return;
        }

        if (testing_results[0] == true) {
            System.out.println("Test 1 PASSED\n");
        }

        // TEST 2 to 6
        ArithExpression a2 = null;

        for (int i = 1; i < 6; i ++){
            System.out.println("--------------------------------------");
            System.out.println("Test " + (i + 1) + ". Input Expression -- " + test[i]);
            System.out.println("--------------------------------------\n");

            try {

                a2 = new ArithExpression(test[i]);

                boolean test_passed = true;

                String correct_expression = postFixToken_expected[i];
                String student_expression = a2.getPostfixExpression();

                // trim both expressions
                correct_expression = correct_expression.trim();
                student_expression = student_expression.trim();

                if (!(student_expression.equals(correct_expression))){
                    System.out.println("Test " + (i + 1) + " FAILED -- PostFix Expression is incorrect");
                    System.out.println("Expected post fix expression is " + correct_expression + " Expression produced is " + student_expression + "\n");
                    testing_results[i] = false;
                } else {
                    double d = a2.evaluate();

                    if (d != expected_results[i]){
                        System.out.println("Test " + (i + 1) + " FAILED. input expression: " + test[i]);
                        System.out.println(" Expected results is -- " + expected_results[0] + "result produced is -- " + d + "\n");
                        testing_results[i] = false;
                    }
                }
            } catch (Exception e){
                System.out.println("Test " + (i + 1) + " generates exception ---- " + e);
                testing_results[i] = false;
                System.out.println("Test " + (i + 1) + " FAILED");
            }

            if (testing_results[i] == true) {
                System.out.println("Test " + (i + 1) + " PASSED\n");
            }
        }

        // TEST 7 -- "a"
        System.out.println("--------------------------------------");
        System.out.println("Test 7. Input Expression -- " + test[6]);
        System.out.println("--------------------------------------\n");

        try {
            a2 = new ArithExpression(test[6]);

            testing_results[6] = false;

            String correct_expression = postFixToken_expected[6];
            String student_expression = a2.getPostfixExpression();

            // trim both expressions
            correct_expression = correct_expression.trim();
            student_expression = student_expression.trim();

            if (!(student_expression.equals(correct_expression))){
                System.out.println("Test 7 FAILED -- PostFix Expression is incorrect");
                System.out.println("Expected post fix expression is " + correct_expression + " Expression produced is " + student_expression + "\n");
            } else {
                try {
                    double d = a2.evaluate();
                } catch (InvalidExpressionException e){
                    System.out.println(e);
                    testing_results[6] = true;
                }
            }
        } catch (Exception e){
            System.out.println("Test 7 generates exception ---- " + e);
            testing_results[6] = false;
            System.out.println("Test 7 FAILED");
        }

        if (testing_results[6] == true) {
            System.out.println("Test 7 PASSED\n");
        }

        // TEST 8 -- incorrect expression
        System.out.println("--------------------------------------");
        System.out.println("Test 8. Input Expression -- " + test[7]);
        System.out.println("--------------------------------------\n");

        try {
            a2 = new ArithExpression(test[7]);

            testing_results[7] = false;

            String correct_expression = postFixToken_expected[7];
            String student_expression = a2.getPostfixExpression();

            // trim both expressions
            correct_expression = correct_expression.trim();
            student_expression = student_expression.trim();

            if (!(student_expression.equals(correct_expression))){
                System.out.println("Test 8 -- PostFix Expression is incorrect");
                System.out.println("Expected post fix expression is " + correct_expression + " Expression produced is " + student_expression + "\n");
            } else {
                try {
                    double d = a2.evaluate();
                } catch (InvalidExpressionException e){
                    System.out.println(e);
                    testing_results[7] = true;
                }
            }
        } catch (Exception e){
            System.out.println("Test 8 generates exception ---- " + e);
            testing_results[7] = false;
            System.out.println("Test 8 FAILED");
        }


        if (testing_results[7] == true) {
            System.out.println("Test 8 PASSED\n");
        }
    //
        // TEST 9 -- incorrect expression "2 2"
        System.out.println("--------------------------------------");
        System.out.println("Test 9. Input Expression -- " + test[8]);
        System.out.println("--------------------------------------\n");

        try{
            a2 = new ArithExpression(test[8]);

            testing_results[8] = false;

            String correct_expression = postFixToken_expected[8];
            String student_expression = a2.getPostfixExpression();

            // trim both expressions
            correct_expression = correct_expression.trim();
            student_expression = student_expression.trim();

            if (!(student_expression.equals(correct_expression))){
                System.out.println("Test 9 -- PostFix Expression is incorrect");
                System.out.println("Expected post fix expression is " + correct_expression + " Expression produced is " + student_expression + "\n");
            } else {
                try {
                    double d = a2.evaluate();
                } catch (InvalidExpressionException e){
                    System.out.println(e);
                    testing_results[8] = true;
                }
            }
        } catch (Exception e){
            System.out.println("Test 9 generates exception ---- " + e);
            testing_results[8] = false;
            System.out.println("Test 9 FAILED");
        }


        if (testing_results[8] == true) {
            System.out.println("Test 9 PASSED\n");
        }

        // TEST 10 -- incorrect expression "++"
        System.out.println("--------------------------------------");
        System.out.println("Test 10. Input Expression -- " + test[9]);
        System.out.println("--------------------------------------\n");

        try {
            a2 = new ArithExpression(test[9]);

            testing_results[9] = false;

            String correct_expression = postFixToken_expected[9];
            String student_expression = a2.getPostfixExpression();

            // trim both expressions
            correct_expression = correct_expression.trim();
            student_expression = student_expression.trim();

            if (!(student_expression.equals(correct_expression))){
                System.out.println("Test 10 -- PostFix Expression is incorrect");
                System.out.println("Expected post fix expression is " + correct_expression + " Expression produced is " + student_expression + "\n");
            } else {
                try {
                   double d = a2.evaluate();
                } catch (InvalidExpressionException e){
                    System.out.println(e);
                    testing_results[9] = true;
                }
            }
        } catch (Exception e){
            System.out.println("Test 10 generates exception ---- " + e);
            testing_results[9] = false;
            System.out.println("Test 10 FAILED");
        }


        if (testing_results[9] == true) {
            System.out.println("Test 10 PASSED\n");
        }

        //System.out.println(testing_results.length);

        int a= 10;

        for (int i = 0; i < testing_results.length; i++){

            if (testing_results[i] == true){
                results[i + 8] = "PASSED";
            } else {
                results[i + 8] = "FAILED";
            }
        }

        System.out.println("===============================================");
        System.out.println("End of ArithExpression.java Testing");
        System.out.println("===============================================\n");
    }

    public static void results_summary(){

        System.out.println("Summary of testing results");
        System.out.println("===============================================\n");
        System.out.println(tests[0] + results[0]);
        System.out.println(tests[1] + results[1]);
        System.out.println("\nStringStack.java tests ---- ");

        for (int x = 2; x < 8; x ++){
            System.out.println("   " + tests[x] + results[x]);
        }

        System.out.println("\n ArithExpression.java tests ---- ");

        for (int x = 8; x < 18; x ++){
            System.out.println("   " + tests[x] + results[x]);
        }
    }
}
