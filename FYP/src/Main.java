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
    public static String tensorPath = "C:\\Users\\pradn\\Desktop\\School\\Year_4\\FYP\\tensor1.csv";
    public  static List<String> readTensor = new ArrayList<>();
    public  static List<Morphisms> morphisms = new ArrayList<>();
    public static List<Tensor> tensorList = new ArrayList<>();
    public  static List<String> readLines = new ArrayList<>();
    public static HashMap<String,Integer> tensorRow = new HashMap<>();
    public static HashMap<String,Morphisms> morphismNames = new HashMap<>();
    public static HashMap<String, Integer> tensorCol = new HashMap<>();
    public static HashMap<String,Integer> catRow = new HashMap<>();
    public static HashMap<String, Integer> catCol = new HashMap<>();
    public static Morphisms[][] catTable = new Morphisms[morphisms.size()][morphisms.size()];
    public static List<State> states = new ArrayList<>();
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
     *  PROPERTIES TO CHECK:::
     *
     *  dom(f * g) == dom(f) * dom(g)
     *  instead of using the objects, we can use the identity of the objects so that its morphism multiplication instead of object multiplication
     *  dom(f) * dom(g) is object multiplication. use id(dom f) * id(dom g) instead
     *
     *  codomain(f * g) == codomain(f) * codomain(g)
     *
     *  (f * g) * h == f * (g * h)
     *
     *  (k . h) * (g . f) == (k * g) . (h * f) -> if cod(h) = dom(k) then cod(f) = dom(g)
     *  if k.h does not exist then just skip
     *
     *
     *  id(A) * id(B) = id(A *B)
     *  take 2 ids, get tensor value. then check if the tensor value is a valid id
     *
     *
     *
     *  the last one. go through every id. there must exist one id where the property holds for multiple fs
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
        formatMorphs();
        createMorphs();
        Table tensorTable = formatTensor();
        Table table = createTable();
    }




    //===========================================================================================READ THE CSVS============================================================================================
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
        String line2 = "";
        try{
            BufferedReader br = new BufferedReader( new FileReader(path));
            while((line = br.readLine())!= null){
                readLines.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            BufferedReader br1 = new BufferedReader( new FileReader(tensorPath));
            while((line2 = br1.readLine())!= null){
                readTensor.add(line2);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

//========================================================================= FORMAT INPUTS ===============================================================================================================


    /**
     * we assume that if the diagonal has a value then it must be an identity. otherwise it is "-"
     */
    public static void formatIdentities(){
        for(int i=0;i<readLines.size();i++){
            String[] curr = readLines.get(i).split(",");
            if(!curr[i].equals("-")){
                State s = new State(Integer.toString(i));
                Morphisms m = new Morphisms(s,s,curr[i]);
                s.identity = true;
                s.identityMorphism = m;
                states.add(s);
                statesA.put(curr[i],s.name);
                stateB.put(curr[i],s.name);
                identityNames.add(curr[i]);
                morphisms.add(m);
            }
        }
    }
    public static Table createTable(){
        Morphisms[][] t = new Morphisms[morphisms.size()][morphisms.size()];
        for(int i=0;i<readLines.size();i++){
            String[] currLine = readLines.get(i).split(",");
            for( int j=0;j<currLine.length;j++){
                t[i][j] = morphismNames.getOrDefault(currLine[j], null);

            }
        }
        return new Table(t);
    }


    private static void createMorphs() {
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
            if(!identityNames.contains(key)){
                Morphisms m = new Morphisms(getState(statesA.get(key)),getState(stateB.get(key)),key);
                morphismNames.put(m.name,m);
                morphisms.add(m);
            }

        }
    }

    public static void formatMorphs(){
        for(int i=0;i<readLines.size();i++){
            String row = "f" + i;
            String[] curr = readLines.get(i).split(",");
            for(int j =0;j<curr.length;j++){
                String n = curr[j];
                String col = "f" + j;

                if(row.equals(col) || n.equals("-")){
                    System.out.println("in identites"+ row+" "+col);
                    continue;
                }
                if(identityNames.contains(row) && col.equals(n)){
                    //start m = row
                    if(statesA.containsKey(n)){
                        if(!statesA.containsKey(row) || !statesA.get(n).equals(statesA.get(row))){
                            System.out.println("This is an invalid category 1");
                            continue;
                        }
                    }
                    else {
                        if(statesA.containsKey(row)){
                            System.out.println("in identites + col, not null"+ row+" "+col);
                            statesA.put(n,statesA.get(row));
                            continue;
                        }
                        else {
                            System.out.println("in identites + col, null"+ row+" "+col);
                            rechecks.add(new int[]{i,j});
                            continue;
                        }

                    }
                } else if (row.equals(n) && identityNames.contains(col)) {
                    //end m = identity
                    if(stateB.containsKey(n)){
                        if(!stateB.containsKey(col) || !stateB.get(n).equals(stateB.get(col))){
                            System.out.println("This is an invalid category 2");
                            exit(0);
                            continue;
                        }
                    }
                    else {
                        if(stateB.containsKey(col)){
                            System.out.println("in row + identites , not null"+ row+" "+col);
                            stateB.put(n,stateB.get(col));
                            continue;
                        }
                        else {
                            System.out.println("in row + identites , null"+ row+" "+col);
                            rechecks.add(new int[]{i,j});
                            continue;
                        }
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
                            if(statesA.containsKey(row)){
                                System.out.println("in row + col ,not null"+ row+" "+col);
                                if(!statesA.get(n).equals(statesA.get(row))){
                                    System.out.println("row: " + row);
                                    System.out.println("col: "+ col);
                                    System.out.println(statesA.get(n));
                                    System.out.println(statesA.get(row));
                                    System.out.println("This is an invalid category 4");
                                    exit(0);

                                }
                            }
                            else {
                                System.out.println("in row + col ,null"+ row+" "+col);
                                rechecks.add(new int[]{i,j});
                                continue;
                            }

                        }else {
                            System.out.println("state A not added yet");
                            if(statesA.containsKey(row)){
                                statesA.put(n,statesA.get(row));
                                continue;
                            }
                            else{
                                rechecks.add(new int[]{i,j});
                                continue;
                            }

                        }
                        ASet = true;
                    }
                    if(stateB.containsKey(col)){
                        //m end = col end
                        if(stateB.containsKey(n)){
                            System.out.println("state b added");

                            if(!stateB.get(n).equals(stateB.get(col))){
                                System.out.println("This is an invalid category 5");
                                exit(0);
                                continue;
                            }


                        }else{
                            System.out.println("state b not added");
                            if(stateB.containsKey(col)){
                                stateB.put(n,stateB.get(col));
                                continue;
                            }
                            else{
                                rechecks.add(new int[]{i,j});
                                continue;
                            }

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
                            System.out.println(morphisms.size());
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
            String ss = s.name;
            if(ss.equals(name)){
                return s;
            }
        }
        return null;
    }

    public static Table formatTensor(){
        Morphisms[][] tensorTable = new Morphisms[readTensor.size()-1][readTensor.size()-1];
        String[] colLabels = readTensor.get(0).split(",");
        for( int i=1;i<colLabels.length;i++){
            tensorCol.put(colLabels[i],i-1);
        }
        for( int i=1;i<readTensor.size();i++){
            String[] currLine = readTensor.get(i).split(",");
            tensorRow.put(currLine[0],i-1);
            for(int j=1;j<currLine.length;j++){
                tensorTable[i-1][j-1] = new Morphisms(null,null,currLine[j]);
                Tensor n = new Tensor(new Morphisms(null,null,currLine[0]),new Morphisms(null,null,colLabels[j]),new Morphisms(null,null,currLine[j]));
                tensorList.add(n);
            }
        }
        return new Table(tensorTable);
    }

    public static Morphisms getTensor(String row, String col, Table tensorTable){
        int rowIndex = tensorRow.get(row);
        int colIndex = tensorCol.get(col);
        return tensorTable.getMorphism(rowIndex,colIndex);
    }






// =================================================================================== CHECK MONOIDAL PROPERTIES =============================================================================================


    //(f * g) * h == f * (g * h)
    // assume the table has no blanks or "-"
    public static boolean checkAssociativity(Table tensortable){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                for( int k=0;k<morphisms.size();k++){
                    Morphisms a = morphisms.get(i);
                    Morphisms b = morphisms.get(j);
                    Morphisms c = morphisms.get(k);

                    //Left side
                    Morphisms ab = getTensor(a.name,b.name,tensortable);
                    Morphisms abc = getTensor(ab.name,c.name,tensortable);

                    // Right side

                    Morphisms bc = getTensor(b.name,c.name,tensortable);
                    Morphisms bca = getTensor(a.name,bc.name,tensortable);

                    if (!abc.name.equals(bca.name)){
                        return false;
                    }








                }
            }
        }
        return true;
    }


//    (k . h) * (g . f) == (k * g) . (h * f) -> if cod(h) = dom(k) then cod(f) = dom(g)
    public static boolean check2(Table tensorTable, Table t){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                if(t.getMorphism(i,j)== null){
                    continue;
                }

                for(int k=0;k<morphisms.size();k++){
                    for(int l=0;l<morphisms.size();l++){
                        if(t.getMorphism(k,l)== null){
                            continue;
                        }
                        Morphisms kh = t.getMorphism(i,j);
                        Morphisms gf = t.getMorphism(k,l);
                        Morphisms left = tensorTable.getMorphism(tensorRow.get(kh.name),tensorCol.get(gf.name));

                        //should be inputting a morphism not a number. just as above

                        Morphisms kg = tensorTable.getMorphism()






                    }
                }
            }
        }
    }























    //    /**
//     * Formats the String lines stored by readFile in readLines
//     * the method splits the first line into different states and creates a new state object.
//     * the states are stored in the states list.
//     * for the morphisms, the method splits the string into an array of size 3 and creates a new morphism.
//     * it then stores each morphism in the morphisms list
//     */
//    public static void formatLines(){
//        //split the states and add them to the list
//        String[] stateArr = readLines.get(0).split(",");
//        for (String s : stateArr) {
//            State a = new State(s);
//            states.add(a);
//        }
//        for(int i=1;i<readLines.size();i++){
//            String currLine = readLines.get(i);
//            String[] splittedLine = currLine.split(",");
//            State a = new State(splittedLine[1]);
//            State b = new State(splittedLine[2]);
//            Morphisms m = new Morphisms(a,b,splittedLine[0]);
//            morphisms.add(m);
//        }
//    }
//
//    //TODO ASSUME VALID IDENTITY
//
//    /**
//     * Formats the String lines stored by readFile in readLines
//     * the method splits the first line into different states and creates a new state object.
//     * the states are stored in the states list.
//     * for the morphisms, the method splits the string into an array of size 3 and creates a new morphism.
//     * it then stores each morphism in the morphisms list
//     */
//    public static void formatMultTable(){
//        //Get Identities first
//        formatIdentities();
//        //if size of states is 0 here then we can conclude that it is not a valid category.
//
//    }







}
