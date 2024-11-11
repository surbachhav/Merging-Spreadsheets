package cs2110;
import java.io.*;
import java.util.*;

public class CsvJoin {

    /**
     * This method takes a rectangular table with at least 1 row
     * and prints it in the format of a valid CSV file.
     * @param table
     */
    private static void formatTable(Seq<Seq<String>> table){
        for (Seq<String> row : table) {
            Iterator<String> iterate = row.iterator();
            if (iterate.hasNext()) {
                System.out.print(iterate.next());
            }
            while (iterate.hasNext()) {
                System.out.print("," + iterate.next());
            }
            System.out.println();
        }
    }

    /**
     * This method checks the following preconditions on the program arguments.
     *  1. Two csv files must be given in String[] args. If not, an error is printed and the main method is exited.
     *  2. Both csv files must be rectangular. If they are not, an error is printed and the main method is exited.
     *  If the preconditions are met, the method converts each file into a table, left outer joins
     *  them, and prints the output table. If the method cannot convert each file into a table,
     *  an exception an error is printed and the main method is excited.
     * @param args
     * @throws IOException
     *
     *
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2){
            System.err.println("Usage: cs2110.CsvJoin <left_table.csv> <right_table.csv>");
            System.exit(1);
        }

        String lFile = args[0];
        String rFile = args[1];

        Seq<Seq<String>> lTable = null;
        Seq<Seq<String>> rTable = null;

        try {
            lTable = csvToList(lFile);
            rTable = csvToList(rFile);

        } catch (IOException e) {
            System.err.println("Error: Could not read input tables.");
            System.exit(1);
        }

        if (!checkPreconditions(lTable, rTable)) {
            System.err.println("Error: Input tables are not rectangular.");
            System.exit(1);
        }

        Seq<Seq<String>> finalTable = join(lTable, rTable);
        formatTable(finalTable);

    }

    /**
     * This method checks that two tables 'left' and 'right' are rectangular and have at least 1 row
     * in length.
     * @param left
     * @param right
     * @return false if one or more of the LinkedSeq are not rectangular or not at least 1 row in length.
     * Otherwise, true is returned.
     */
    public static boolean checkPreconditions(Seq<Seq<String>> left, Seq<Seq<String>> right){

        if (left.get(0).size() < 1 && right.get(0).size() < 1){
            return false;
        }

        int check = left.get(0).size();
        for (Seq<String> row: left){
            if (row.size() != check){
                return false;
            }
        }

        check = right.get(0).size();
        for (Seq<String> row: right){
            if (row.size() != check){
                return false;
            }
        }

        return true;
    }

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String file) throws IOException{
        Seq<Seq<String>> list = new LinkedSeq<>();

        File f = new File(file);
        if (!f.exists() || !f.isFile()){
            throw new IOException();
        }

        // Read each line
        Reader in = new FileReader(file);
        Scanner lines = new Scanner(in);
        int index = 0;

        while (lines.hasNextLine()) {

            list.append(new LinkedSeq<>());

            String row = lines.nextLine();
            String[] elements = row.split(",", -1);

            for (String e : elements) {
                list.get(index).append(e);
            }

            index++;
        }

        return list;
    }

    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right){
        Seq<Seq<String>> joinTable = new LinkedSeq<>();

        for (Seq<String> leftRow : left) {
            boolean matchFound = false;
            String lElement = null;

            Iterator<String> iterateLeft = leftRow.iterator();
            if (iterateLeft.hasNext()) {
                lElement = iterateLeft.next();
            }

            for (Seq<String> rightRow : right) {
                String rElement = null;

                Iterator<String> iterateRight = rightRow.iterator();
                if (iterateRight.hasNext()) {
                    rElement = iterateRight.next();
                }

                if (lElement != null && lElement.equals(rElement)) {
                    matchFound = true;
                    Seq<String> joinTableNewRow = new LinkedSeq<>();

                    Iterator<String> newIterateLeft = leftRow.iterator();
                    while (newIterateLeft.hasNext()) {
                        joinTableNewRow.append(newIterateLeft.next());
                    }

                    while (iterateRight.hasNext()) {
                        joinTableNewRow.append(iterateRight.next());
                    }

                    joinTable.append(joinTableNewRow);
                }
            }

            if (!matchFound) {
                Seq<String> joinTableNewRow = new LinkedSeq<>();

                Iterator<String> newIterateLeft = leftRow.iterator();
                while (newIterateLeft.hasNext()) {
                    joinTableNewRow.append(newIterateLeft.next());
                }

                for (int i = 0; i < right.get(0).size()-1; i++) {
                    joinTableNewRow.append("");
                }

                joinTable.append(joinTableNewRow);
            }
        }

        return joinTable;
    }

}