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
     *Add java docs for each class and method in each class
     *
     */

    /**
     * CSV FILE FORMAT
     *
     * first list all states, then followed by a morphism followed by 2 states
     * Eg:
     * A1,A2,A3, f1,A1,A2, f2,A2,A2,f3,A2,A3
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
    public static void main(String[] args) {
        readFile();
        formatLines();
        Graph g = new Graph(states,morphisms);
        //check 1. check if all the states have identites.
        if(g.hasIdentities()){
            //check if it obeys the associativity law
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





}