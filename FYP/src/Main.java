import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;


/**
 * Finite monoidal categories generalise finite monoids, and can be defined by giving their multiplication table.
 * They are interesting from many perspectives, including programming semantics and quantum theory, but in this
 * project we'll forget that and regard them as combinatorial objects, purely in terms of their multiplication table.
 * This has applications in cryptography and is interesting in its own right. We want to explore which multiplication
 * tables are possible. If I give you a multiplication table, can you check whether it defines a legal monoidal category?
 * If I give you a multiplication table that is only partially filled, can you check whether it can be completed to a
 * legal monoidal category? In this project you will write a tool with a graphical UI that implements these questions,
 * either for finite monoidal categories or for finite 2-categories. A partial tool exists, written in Haskell and
 * using a SAT-solver backend, that could be extended, but you can also restart from scratch and use a different
 * platform. If all goes well we could add features to check for example whether two elements of a given
 * multiplication table are adjoint.
 */
public class Main {

    public static String path = "C:\\Users\\pradn\\Desktop\\School\\Year_4\\FYP\\cat1.csv";
    public  static List<String> readLines = new ArrayList<>();
    public static List<State> states = new ArrayList<>();
    public  static List<Morphisms> morphisms = new ArrayList<>();
    public static  List<String> identityNames = new ArrayList<>();
    public static HashMap<String,String> statesA = new HashMap<>();
    public static HashMap<String,String> stateB = new HashMap<>();
    public static List<int[]> rechecks = new ArrayList<>();

/**
 * TODO change the program to take a multiplication table instead.
 */


    /**
     * TODO: THE MONOIDAL PART
     * A monoidal category is a category equipped with a tensor product and a unit object, subject to the following properties:
     *
     *      1)Associativity: For any objects A, B, and C in the category, there is a natural isomorphism (A ⊗ B) ⊗ C ≅ A ⊗ (B ⊗ C).
     *
     *      2)Unit: There exists a unit object I such that for any object A in the category, there are natural isomorphisms A ⊗ I ≅ A and I ⊗ A ≅ A.
     *
     *      3)Coherence: The associativity and unit isomorphisms are required to satisfy certain coherence conditions involving
     *      higher-order associativity and commutativity isomorphisms.
     *
     *      4)Compatibility with functors: Given two monoidal categories (C, ⊗, I) and (D, ⊙, J),
     *      a monoidal functor F : C → D is a functor that preserves the tensor product and unit object,
     *      i.e. F(A ⊗ B) = F(A) ⊙ F(B) and F(I) = J, and also preserves the coherence isomorphisms.
     *
     *      5)Symmetry (for a symmetric monoidal category): There is a natural isomorphism A ⊗ B ≅ B ⊗ A, for any objects A and B.
     *
     *      6)Braiding (for a braided monoidal category): There is a natural isomorphism A ⊗ B ≅ B ⊗ A, together with coherence conditions.
     *
     *      7)Duals (for a monoidal category with duals): For any object A in the category, there exist objects A^* and εA,
     *      together with natural isomorphisms A ⊗ A^* ≅ I and εA ⊗ A ≅ I, where I is the unit object,
     *      such that A^* is the dual of A and εA is the counit of A. The dual and counit satisfy certain coherence conditions
     *
     *
     *
     * In a monoidal category, the tensor product is a bifunctor, which means that it takes two objects and returns another object.
     * So, to check if a monoidal category has a bifunctor, we need to check if the tensor product satisfies the following conditions:
     *
     *      1) It is a functor in each variable separately, meaning that for any two morphisms f: A → A' and g: B → B' in the category,
     *      there is a corresponding morphism f ⊗ g: A ⊗ B → A' ⊗ B' in the category.
     *
     *      2)It is associative up to natural isomorphism, meaning that there is a natural isomorphism associating
     *      ((A ⊗ B) ⊗ C) with (A ⊗ (B ⊗ C)) for all objects A, B, and C in the category.
     *
     *      3) It has a unit object I, such that there is a natural isomorphism between (A ⊗ I) and A,
     *      and between (I ⊗ A) and A, for all objects A in the category.
     *
     *
     */


    /**
     * the main method of the class. it first reads the file stated in the path, calls the method to format the lines.
     * it then first checks if the category has any identities. then if there is a valid solution.
     * if a valid solution exists, then it prints out the table. otherwise it prints
     * "The category has no solutions"
     * @param args
     */
    public static void main(String[] args) {
        readFile();
        formatIdentities();
    }

    /**
     * Reads a CSV file containing the states and morphisms and adds each line to a list of Strings.
     * the file is taken from its path which is stored in the path variable.
     * the format of the contents in the file is as follows.
     * first list all states, then followed by a morphism followed by 2 states
     *      * Eg:
     *      * A1,A2,A3, \n f1,A1,A2,\n  f2,A2,A2,\n f3, A2,A3
     */
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

    /**
     * Formats the String lines stored by readFile in readLines
     * the method splits the first line into different states and creates a new state object.
     * the states are stored in the states list.
     * for the morphisms, the method splits the string into an array of size 3 and creates a new morphism.
     * it then stores each morphism in the morphisms list
     */
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

//TODO ASSUME VALID IDENTITY

    /**
     * Formats the String lines stored by readFile in readLines
     * the method splits the first line into different states and creates a new state object.
     * the states are stored in the states list.
     * for the morphisms, the method splits the string into an array of size 3 and creates a new morphism.
     * it then stores each morphism in the morphisms list
     */
    public static void formatMultTable(){
        //Get Identities first
        formatIdentities();
        //if size of states is 0 here then we can conclude that it is not a valid category.

    }


    public static void formatIdentities(){
        for(int i=0;i<readLines.size();i++){
            String[] curr = readLines.get(i).split(",");
            if(!curr[i].equals("-")){
                State s = new State(Integer.toString(i));
                Morphisms m = new Morphisms(s,s,curr[i]);
                s.identity = true;
                s.identityMorphism = m;
                states.add(s);
                identityNames.add(curr[i]);
                morphisms.add(m);
            }

        }
        formatMorphs();
        while (rechecks.size()!=0){
            doRechecks();
        }
        Set<String> stateAKeys = statesA.keySet();
        Set<String> stateBKeys = stateB.keySet();
        if(stateBKeys.size()!= stateAKeys.size()){
            System.out.println("state a keys do not match state b keys");
            exit(0);
        }
        for(String key : stateAKeys){
            if(!stateB.containsKey(key)){
                System.out.println("state a keys do not match state b keys 2");
                exit(0);
            }
            Morphisms m = new Morphisms(getState(key),getState(stateB.get(key)),key);
            morphisms.add(m);
        }
    }

    public static void formatMorphs(){
        for(int i=0;i<readLines.size();i++){
            String row = "f" + Integer.toString(i);
            String[] curr = readLines.get(i).split(",");
            for(int j =0;j<curr.length;j++){
                String n = curr[j];
                String col = "f" + Integer.toString(j);
                if(row.equals(col) || n.equals("-")){
                    continue;
                }
                if(identityNames.contains(row) && col.equals(n)){
                    //start m = row
                    if(statesA.containsKey(n)){
                        if(!statesA.get(n).equals(col)){
                            System.out.println("This is an invalid category 1");
                            exit(0);
                        }
                    }
                    else {
                        statesA.put(n,col);
                    }
                } else if (row.equals(n) && identityNames.contains(col)) {
                    //end m = identity
                    if(stateB.containsKey(n)){
                        if(!stateB.get(n).equals(col)){
                            System.out.println("This is an invalid category 2");
                            exit(0);
                        }
                    }
                    else {
                        stateB.put(n,col);
                    }
                }
                else if (identityNames.contains(col) && identityNames.contains(row)){
                    System.out.println("This is an invalid category3");
                    exit(0);
                }
                else {
                    boolean ASet = false;
                    boolean Bset = false;
                    if (statesA.containsKey(row)){
                        // m state A = row state A
                        if(statesA.containsKey(n)){
                            if(!statesA.get(n).equals(statesA.get(row))){
                                System.out.println("This is an invalid category 4");
                                exit(0);
                            }
                        }else {
                            statesA.put(n,statesA.get(row));
                        }
                        ASet = true;
                    }
                    if(stateB.containsKey(col)){
                        //m end = col end
                        if(stateB.containsKey(n)){
                            if(!stateB.get(n).equals(stateB.get(col))){
                                System.out.println("This is an invalid category 5");
                                exit(0);
                            }
                        }else{
                            stateB.put(n,stateB.get(col));
                        }

                        Bset = true;
                    }
                    if(!ASet && !Bset){
                        //need to recheck later
                        rechecks.add(new int[]{i,j});
                    }

                }
                //check if any rechecks needed. otherwise can continue

            }
        }
    }


    public static void doRechecks(){
        List<int[]> rechecksdupl = new ArrayList<>();
        for(int i=0;i<rechecks.size();i++){
            int[] curr = rechecks.get(i);
            int row_i = curr[0];
            int col_j = curr[1];
            String[] currSplit = readLines.get(row_i).split(",");
            String n = currSplit[col_j];
            String row = "f"+row_i;
            String col = "f"+col_j;


            if(row.equals(col) || n.equals("-")){
                continue;
            }
            if(identityNames.contains(row) && col.equals(n)){
                //start m = row
                if(statesA.containsKey(n)){
                    if(!statesA.get(n).equals(col)){
                        System.out.println("This is an invalid category 6");
                        exit(0);
                    }
                }
                else {
                    statesA.put(n,col);
                }
            } else if (row.equals(n) && identityNames.contains(col)) {
                //end m = identity
                if(stateB.containsKey(n)){
                    if(!stateB.get(n).equals(col)){
                        System.out.println("This is an invalid category 7");
                        exit(0);
                    }
                }
                else {
                    stateB.put(n,col);
                }
            }
            else if (identityNames.contains(col) && identityNames.contains(row)){
                System.out.println("This is an invalid category 8");
                exit(0);
            }
            else {
                boolean ASet = false;
                boolean Bset = false;
                if (statesA.containsKey(row)){
                    // m state A = row state A
                    if(statesA.containsKey(n)){
                        if(!statesA.get(n).equals(statesA.get(row))){
                            System.out.println("This is an invalid category 9");
                            exit(0);
                        }
                    }else {
                        statesA.put(n,statesA.get(row));
                    }
                    ASet = true;
                }
                if(stateB.containsKey(col)){
                    //m end = col end
                    if(stateB.containsKey(n)){
                        if(!stateB.get(n).equals(stateB.get(col))){
                            System.out.println("This is an invalid category 10");
                            exit(0);
                        }
                    }else{
                        stateB.put(n,stateB.get(col));
                    }

                    Bset = true;
                }
                if(!ASet && !Bset){
                    //need to recheck later
                    rechecksdupl.add(new int[]{row_i,col_j});
                }

            }
            //check if any rechecks needed. otherwise can continue
        }
        rechecks.clear();
        rechecks = rechecksdupl;
    }
    public static  State getState(String name){
        for(State s : states){
            if(s.name.equals(name)){
                return s;
            }
        }
        return null;
    }


}
