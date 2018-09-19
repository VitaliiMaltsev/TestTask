import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QualityUnitTestApp {

    //regex pattern for Waiting Lines
    private static String patternC =
            "\\s*C ((\\d+)(\\.\\d+)?) (\\*|(((\\d+)?(\\.\\d+)?)(\\.\\d+)?)) ([PN]) (\\d{2}\\.\\d{2}\\.\\d{4})\\s(\\d+)\\s*";

    //regex pattern for Query Lines
    private static String patternD =
            "\\s*D ((\\d+)(\\.\\d+)?) (\\*|(((\\d+)?(\\.\\d+)?)(\\.\\d+)?)) ([PN]) (\\d{2}\\.\\d{2}\\.\\d{4})-?(\\d{2}\\.\\d{2}\\.\\d{4})?\\s*";

    private static Pattern regexC = Pattern.compile(patternC);
    private static Pattern regexD = Pattern.compile(patternD);

    public static void main(String[] args) {
        calculateAverageTime();
    }

    private static void calculateAverageTime() {
        // Input Line from your task
        String inputLineFromProgrammerTasken = "7\n" +
                "C 1.1 8.15.1 P 15.10.2012 83\n" +
                "C 1 10.1 P 01.12.2012 65\n" +
                "C 1.1 5.5.1 P 01.11.2012 117\n" +
                "D 1.1 8 P 01.01.2012-01.12.2012\n" +
                "C 3 10.2 N 02.10.2012 100\n" +
                "D 1 * P 08.10.2012-20.11.2012\n" +
                "D 3 10 P 01.12.2012\n";

        //My Input Line with different variations and specially made errors for testing
        String testinputLine = "7\n" +
                "C 1.1 8.15.1 P 15.10.2012 83\n" +
                "D 3 10 N 03.09.2012-12.12.2013\n" +
                "C 1 10.1 P 01.12.2012 65\n" +
                "C 1.1 5.5.1 P 01.11.2012 117\n" +
                "C 1.1 5.5.1 P 01.11.2012 508\n" +
                "D 1.1 8 P 01.01.2012-01.12.2012\n" +
                "fljhnvlojnlvrkenvlervn"+
                "C 3 10.2 N 02.10.2012 54\n" +
                "C 3 10.2 N 02.10.2015 100\n" +
                "D 1 * P 08.10.2012-20.11.2012\n" +
                "D 3 10 P 01.12.2012\n" +
                "D 3 * N 01.12.2012-01.07.2018\n" +
                "Output:\n" +
                "83\n" +
                "100";

        Matcher matcherC = regexC.matcher(inputLineFromProgrammerTasken);
        Matcher matcherD = regexD.matcher(inputLineFromProgrammerTasken);

        while (matcherD.find()) {

            int result = 0;
            int matchFound = 0;
            int tempWaitingTime = 0;

            matcherC.reset();
            while (matcherC.find()) {

                String service_idVar_idForQuery = matcherD.group(1);
                String service_idForQuery = matcherD.group(2);
                String question_idCategory_idSubcategory_idForQuery = matcherD.group(4);
                String question_idCategory_idForQuery = matcherD.group(6);
                String question_idForQuery = matcherD.group(7);
                String answerTypeForQuery = matcherD.group(10);
                String service_idVar_idForWaitingLine = matcherC.group(1);
                String service_idForWaitingLine = matcherC.group(2);
                String answerTypeForWaitingLine = matcherC.group(10);
                String question_idCategory_idSubcategory_idForWaitingLine = matcherC.group(4);
                String question_idCategory_idForWaitingLine = matcherC.group(6);
                String question_idForWaitingLine = matcherC.group(7);

                if (service_idVar_idForQuery.equals(service_idVar_idForWaitingLine) ||
                        service_idVar_idForQuery.equals(service_idForWaitingLine) ||
                        service_idForQuery.equals(service_idForWaitingLine)) {

                    if (answerTypeForQuery.equals(answerTypeForWaitingLine)) {

                        if (question_idCategory_idSubcategory_idForQuery.equals("*") ||
                                question_idCategory_idSubcategory_idForQuery.equals(question_idCategory_idSubcategory_idForWaitingLine) ||
                                question_idCategory_idSubcategory_idForQuery.equals(question_idCategory_idForWaitingLine) ||
                                question_idCategory_idSubcategory_idForQuery.equals(question_idForWaitingLine) ||
                                question_idCategory_idForQuery.equals(question_idCategory_idForWaitingLine) ||
                                question_idCategory_idForQuery.equals(question_idForWaitingLine) ||
                                question_idForQuery.equals(question_idForWaitingLine)) {

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                            LocalDate date_from = LocalDate.parse(matcherD.group(11), formatter);
                            LocalDate responseDate = LocalDate.parse(matcherC.group(11), formatter);
                            LocalDate date_to = LocalDate.parse(matcherD.group(12), formatter);

                            if (responseDate == date_from ||
                                    responseDate.isBefore(date_to) && responseDate.isAfter(date_from)) {

                                matchFound++;
                                int currentTime = Integer.parseInt(matcherC.group(12));
                                tempWaitingTime = tempWaitingTime + currentTime;
                                result = (tempWaitingTime) / matchFound;

                            }
                        }
                    }
                }
            }
            System.out.println("===========================================");

            if (result != 0) {
                System.out.println("Current average waiting time: " + result + " min");
            } else {
                System.out.println("---No matches found for Current Query---");
            }
        }
        System.out.println("===========================================");
    }
}

