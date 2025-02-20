import java.util.*;

public class MasterMind_Bot {

    private static final int MAX_GUESSES = 10;
    private static final String[] COLORS = {"Black", "White", "Red", "Yellow", "Blue", "Green"};

    /**
     * Print the title for the mastermind bot
     */
    private static void printTitle(){
        System.out.println("""
                
                MasterMind Bot by Patrick form Turing
                The feedback format should be "BBW" and "NULL".
                "B" represents Black and "W" represents white. It should be all capital.
                =====================================================================================""");

    }

    /**
     * Generate all combinations for master mind
     * @return  A List<String[]> contains all possible combinations
     */
    private static List<String[]> generateAllCombo(){
        List<String[]> result = new ArrayList<>();
        for (String c1 : COLORS){
            for (String c2 : COLORS){
                for (String c3 : COLORS){
                    for (String c4 : COLORS){
                        result.add(new String[]{c1, c2, c3, c4});
                    }
                }
            }
        }
        return result;
    }

    private static String getInput(Scanner in, int count, String[] guess){
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.printf("Guess count: %d%n", count);
        System.out.println("Guess:" + Arrays.toString(guess));
        System.out.print("Feedback: ");
        return in.nextLine();
    }

    /**
     * Print something when the bot get the answer
     * @param answer  The answer of the mastermind
     */
    private static void printResult(int guessTime, String answer){
        if (guessTime < MAX_GUESSES) {
            System.out.printf("""
                    =====================================================================================
                    You lose! I know the correct answer is %s
                    """, answer);
        } else {
            System.out.println("""
                    =====================================================================================
                    I don't know the correct answer.'""");
        }
    }

    /**
     * Update the possible combos List<int[]> by the given feedback
     *
     * @param posCombo  The previous possible combination List<String[]>
     * @param tempGuess     The guess the put outputs this turn
     * @param feedback      The feedback player gave the bot
     */
    private static List<String[]> update(List<String[]> posCombo, String[] tempGuess, String feedback){
        // Special Case: NULL
        if (feedback.equals("NULL")) {
            Iterator<String[]> iterator = posCombo.iterator();
            while (iterator.hasNext()) {
                String[] temp = iterator.next();
                boolean needRemove = false;

                for (String c : temp) {
                    for (String tabu : tempGuess) {
                        if (tabu.equals(c)) {
                            needRemove = true;
                            break;
                        }
                    }
                    if (needRemove) { break; }
                }
                if (needRemove) { iterator.remove(); }
            }
            return posCombo;
        }

        // Other cases:
        List<String[]> newPossibles = new ArrayList<>();
        for (String[] c: posCombo){
            String simFB = simulateFeedback(tempGuess, c);
            if (simFB.equals(feedback)) {
                newPossibles.add(c);
            }
        }
        return newPossibles;
    }


    private static String simulateFeedback(String[] fakeGuess, String[] answer){

        // Initialization
        int black = 0, white = 0;
        String[] guessCopy = fakeGuess.clone();
        String[] answerCopy = answer.clone();

        // Check Black
        for (int i = 0; i < 4; i++){
            if (guessCopy[i].equals(answer[i])){
                black++;
                guessCopy[i] = null;
                answerCopy[i] = null;
            }
        }

        // Check White
        for (int i = 0; i < 4; i++){
            if (guessCopy[i] != null){
                for (int j = 0; j < 4; j++){
                    if (answerCopy[j] != null && answerCopy[j].equals(guessCopy[i])){
                        white++;
                        answerCopy[j] = null;
                        break;
                    }
                }
            }
        }

        // Build the simulated feedback and return
        return "B".repeat(Math.max(0, black)) +
                "W".repeat(Math.max(0, white));
    }


    public static void main(String[] args) {

        // Initialization
        int guessCount = 0;
        String feedback = "";
        Scanner in = new Scanner(System.in);
        List<String[]> allCombo = generateAllCombo();
        List<String[]> possibleCombo = new ArrayList<>(allCombo);

        // Print the title
        printTitle();

        // First Guess ------------------------------------------------------------
        String[] tempGuess = {COLORS[0], COLORS[0], COLORS[1], COLORS[1]};
        guessCount++;

        System.out.printf("Guess count: %d%n", guessCount);
        System.out.println("Guess:" + Arrays.toString(tempGuess));
        System.out.print("Feedback: ");
        feedback = in.nextLine();

        // Loop --------------------------------------------------------------------
        while (guessCount < MAX_GUESSES){
            // Check if win
            if (Objects.equals(feedback, "BBBB")){
                break;
            } else {
                // Update the possible combos
                possibleCombo = update(possibleCombo, tempGuess, feedback);
            }

            // After updating
            if (possibleCombo.isEmpty()) {
                System.out.println("No guesses found.");    return;
            } else if (possibleCombo.size() == 1) {
                tempGuess = possibleCombo.getFirst();
                break;
            } else {
                guessCount++;
                // Guess and take the input
                int num = possibleCombo.size() / 2;
                tempGuess = possibleCombo.get(num);
                feedback = getInput(in, guessCount, tempGuess);

            }
        }

        // Print the result
        printResult(guessCount, Arrays.toString(tempGuess));
    }
}