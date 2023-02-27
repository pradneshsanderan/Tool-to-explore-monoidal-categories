import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static String path = "C:\\Users\\pradn\\Desktop\\Book1.csv";
    public  static List<String> readLines = new ArrayList<>();
    public static List<State> states = new ArrayList<>();
    public  static List<Morphisms> morphisms = new ArrayList<>();



    /**
     * TODO
     *
     *  check if each state has an identity
     *  cycle through the morphisms just like in the table and check that they all follow the associativity law
     *
     *
     */

    /**
     * CSV FILE FORMAT
     *
     * first list all states, then followed by a morphism followed by 2 states
     * Eg:
     * A1,A2,A3, f1,A1,A2, f2,A2,A2,f3,A2,A3
     */
    public static void main(String[] args) {
        readFile();
        formatLines();
        Graph g = new Graph(states,morphisms);
        //check 1. check if all the states have identites.
        if(hasIdentity(g)){
            //check if it obeys the associativity law
            System.out.println("has identity");
            String[][] table = g.returnTable();
            if(table[0][0].equals("ERROR")){
                System.out.println("The category has no solutions");
            }
            else{
                System.out.println(Arrays.deepToString(table).replace("],","]\n"));
            }
        }
        else{
            System.out.println("The category has no solutions");
        }


    }

    public static void readFile(){
        String line = "";
        try{
            BufferedReader br = new BufferedReader( new FileReader(path));
            while((line = br.readLine())!= null){
                readLines.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void formatLines(){
        //split the states and add them to the list
        String[] stateArr = readLines.get(0).split(",");
        for (String s : stateArr) {
            State a = new State(s);
            states.add(a);
        }
        for(int i=1;i<readLines.size();i++){
            String currLine = readLines.get(i);
            String[] splittedLine = currLine.split(",");
            State a = new State(splittedLine[1]);
            State b = new State(splittedLine[2]);
            Morphisms m = new Morphisms(a,b,splittedLine[0]);
            morphisms.add(m);
        }


    }
    public static  boolean hasIdentity(Graph g) {
        List<State> states1 = g.states;
        for (State state : states1) {
            if (!state.hasIdentity()) {
                return false;
            }
        }
        return true;
    }




}