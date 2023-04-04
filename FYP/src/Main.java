import java.io.BufferedReader;
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

    public static String path = "C:\\Users\\pradn\\Desktop\\School\\Year_4\\FYP\\partfilled.csv";
    public static String tensorPath = "C:\\Users\\pradn\\Desktop\\School\\Year_4\\FYP\\testtens1.csv";
    public  static List<String> readTensor = new ArrayList<>();
    public  static List<Morphisms> morphisms = new ArrayList<>();
    public static List<Tensor> tensorList = new ArrayList<>();
    public  static List<String> readLines = new ArrayList<>();
    public static HashMap<String,Morphisms> morphismNames = new HashMap<>();
    public static List<State> states = new ArrayList<>();
    public static List<String> tensorRowTitles = new ArrayList<>();
    public static List<String> tensorColTitles = new ArrayList<>();
    public static  List<String> identityNames = new ArrayList<>();
    public static HashMap<String,String> statesA = new HashMap<>();
    public static HashMap<String,String> stateB = new HashMap<>();
    public static List<int[]> rechecks = new ArrayList<>();

/**
 * TODO change the program to take a multiplication table instead.
 */
    /**
     * the main method of the class. it first reads the file stated in the path, calls the method to format the lines.
     * it then first checks if the category has any identities. once it finds the identities if they exist, it then creates
     * the morphisms, creates a category table and tensor table and then chekcs the category for the monoidal properties.
     * @param args
     */
    public static void main(String[] args) {
        readFile();
//        if(readLines.get(0).split(",")[0].equals("*")){
            formatIdentities();
            formatMorphs();
            createMorphs();
            Table tensorTable = formatTensor();
            if(!validTensor(tensorTable)){
                System.out.println("the tensor table is invalid");
                return;
            }
            Table table = createTable();
            boolean filled = solvetable(table,tensorTable);
            if(filled){
                System.out.println("it works!!!");

            }
            else {
                System.out.println("WE FUCKED UPPPPPPPPP !!!!!!!!!!!!!!!!!!!!!!!!!");
            }

//        }
//        formatIdentities();
//        if(identityNames.size()==0){
//            System.out.println("It is not  a valid monoidal category");
//            return;
//        }
//        formatMorphs();
//        createMorphs();
//        System.out.println("domne");
//        Table tensorTable = formatTensor();
//        if(!validTensor(tensorTable)){
//            System.out.println("the tensor table is invalid");
//            return;
//        }
//        Table table = createTable();
//        boolean catAssoc = checkAssoc(table);
//        boolean catComp = checkComp(table);
//        if(!catComp || !catAssoc){
//            System.out.println("Invalid category");
//            return;
//        }
//        System.out.println("Printin tables");
//        tensorTable.printTable(tensorColTitles,tensorRowTitles);
////        //check monoidal properties
//        boolean assoc = checkAssociativity(tensorTable);
//        boolean check2 = check2(tensorTable,table);
//        boolean domain = checkDomain(tensorTable,table);
//        boolean codomain = checkCodomain(tensorTable);
//        boolean idenMonoidal = checkIndetitesMonoidal(tensorTable);
//        boolean uniqueIden = checkUniqueIden(tensorTable);
//        boolean pass = assoc && check2 && domain && codomain && idenMonoidal && uniqueIden;
//        if(pass){
//            System.out.println("It is a valid monoidal category");
//        }
//        else {
//            System.out.println("It is NOT a valid monoidal category");
//            if(!assoc){
//                System.out.println("It failed the associativity test");
//            }
//            if(!check2){
//                System.out.println("It failed the check2 test");
//            }
//            if(!domain){
//                System.out.println("It failed the domain test");
//            }
//            if(!codomain){
//                System.out.println("It failed the codomain test");
//            }
//            if(!idenMonoidal){
//                System.out.println("It failed the identity test");
//            }
//            if(!uniqueIden){
//                System.out.println("It failed the unique identity test");
//            }
//        }

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

//========================================================================= FORMAT CATEGORY ===============================================================================================================

    /**
     * Identity Morphism Properties:
     * Maps an object onto itself
     * each object can only have one
     * left and right associative
     *
     * the method iterates through the lines read from the csv file and that was stored in readLines arraylist.
     * it finds morphisms that might satisfy the identity morphism properties and then further checks if it is both
     * left and right associative. if it is then it is an identity morphism
     * the identity morphism is then added to the list of morphisms, identityNames and MorphismNames
     */
    public static void formatIdentities(){
        int stateCounter =0;
        List<String> potentialIden = new ArrayList<>();
        String[] titlesCol = readLines.get(0).split(",");
        String[] titlesRow = new String[titlesCol.length];
        for(int i=0;i<titlesRow.length;i++){
            String[] cur = readLines.get(i).split(",");
            titlesRow[i] = cur[0];
        }
        for(int i=1;i<readLines.size();i++){
            String row = titlesRow[i];
            String[] curr = readLines.get(i).split(",");
            for(int j=1;j<curr.length;j++){
                String col = titlesCol[j];
                if(row.equals(col) && curr[j].equals(row)){
                    potentialIden.add(curr[j]);
                }
            }
        }
        boolean comp1 = true;
        boolean comp2 = true;
        //assume that a morphism can only be a potential identity once
        for(int i=0;i<potentialIden.size();i++){
            String potIden = potentialIden.get(i);
            innerloop:
            for(int j=0;j<titlesRow.length;j++){
                if(!titlesRow[j].equals(potIden)){
                    continue;
                }
                String[] curLine = readLines.get(j).split(",");

               for(int g=1;g<curLine.length;g++){
                   if(!curLine[g].equals(titlesCol[g]) && !curLine[g].equals("-") && !curLine[g].equals("*")){
                       comp1 = false;
                       break innerloop;
                   }
               }
            }
            loop2:
            if(comp1){
                for(int k=0;k<titlesCol.length;k++){
                    if(!titlesCol[k].equals(potIden)){
                        continue ;
                    }
                    for(int l=1;l<readLines.size();l++){
                        String[] c2 = readLines.get(l).split(",");
                        if(!c2[k].equals(titlesRow[l]) && !c2[k].equals("-") && !c2[k].equals("*")){
                            comp2 = false;
                            break loop2;
                        }
                    }
                }
            }
            if(comp1 && comp2){
                State s = new State(Integer.toString(stateCounter));
                Morphisms m = new Morphisms(s,s,potIden);
                s.identity = true;
                s.identityMorphism = m;
                states.add(s);
                statesA.put(potIden,s.name);
                stateB.put(potIden,s.name);
                identityNames.add(potIden);
                morphisms.add(m);
                morphismNames.put(m.name,m);
                stateCounter++;
            }
        }
    }

    /**
     * we assume that if the diagonal has a value then it must be an identity. otherwise it is "-"
     */
//    public static void formatIdentities(){
//        for(int i=1;i<readLines.size();i++){
//            String[] curr = readLines.get(i).split(",");
//            if(!curr[i+1].equals("-")){
//                State s = new State(Integer.toString(i));
//                Morphisms m = new Morphisms(s,s,curr[i+1]);
//                s.identity = true;
//                s.identityMorphism = m;
//                states.add(s);
//                statesA.put(curr[i+1],s.name);
//                stateB.put(curr[i+1],s.name);
//                identityNames.add(curr[i+1]);
//                morphisms.add(m);
//            }
//        }
//    }

    /**
     * the method takes the inputted csv file and creates a table object of type Table to represent the
     * category table.
     *
     * @return
     */
    public static Table createTable(){
        String[] labels = readLines.get(0).split(",");
        HashMap<String,Integer> row = new HashMap<>();
        HashMap<String,Integer> col = new HashMap<>();


        for(int i=1;i<labels.length;i++){
            col.put(labels[i],i-1);
        }
        Morphisms[][] t = new Morphisms[morphisms.size()][morphisms.size()];
        for(int i=1;i<readLines.size();i++){
            String[] currLine = readLines.get(i).split(",");

            row.put(currLine[0],i-1);
            for( int j=1;j<currLine.length;j++){
                if(currLine[j].equals("*")){
                    Morphisms n= new Morphisms(null,null,"*");
                    t[i-1][j-1] = n;
                } else if (currLine[j].equals("-")) {
                    t[i-1][j-1] = null;
                } else {
                    t[i-1][j-1] = morphismNames.get(currLine[j]);
                }


            }
        }
        Table table = new Table(t);
        table.row = row;
        table.col = col;
        return table;
    }


    /**
     *
     */
    private static void createMorphs() {
        while (rechecks.size()!=0){
            doRechecks();
        }
        Set<String> stateAKeys = statesA.keySet();
        Set<String> stateBKeys = stateB.keySet();
        if(stateBKeys.size()!= stateAKeys.size()){
            System.out.println("state a keys do not match state b keys");
//            exit(0);
        }
        for(String key : stateAKeys){
            if(!stateB.containsKey(key)){
                System.out.println("state a keys do not match state b keys 2");
//                exit(0);
            }
            if(!identityNames.contains(key)){
                Morphisms m = new Morphisms(getState(statesA.get(key)),getState(stateB.get(key)),key);
                morphismNames.put(m.name,m);
                morphisms.add(m);
            }

        }
    }

    /**
     *
     */
    public static void formatMorphs(){
        for(int i=1;i<readLines.size();i++){
            String row = String.valueOf((i-1));
            String[] curr = readLines.get(i).split(",");
            for(int j =1;j<curr.length;j++){
                String n = curr[j];
                String col = String.valueOf((j-1));

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


    /**
     *
     */
    public static void doRechecks(){
        System.out.println("got rechecks");
        List<int[]> rechecksdupl = new ArrayList<>();
        for(int i=0;i<rechecks.size();i++){
            int[] curr = rechecks.get(i);
            int row_i = curr[0];
            int col_j = curr[1];
            String[] currSplit = readLines.get(row_i).split(",");

            System.out.println(Arrays.toString(currSplit));
            System.out.println(col_j);
            String n = currSplit[col_j];
            String row = String.valueOf(row_i);
            String col = String.valueOf(col_j);


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

    /**
     *
     * @param name the name of the state we want
     * @return a state object that has the name of the state we inputted
     */
    public static  State getState(String name){

        for(State s : states){
            String ss = s.name;
            if(ss.equals(name)){
                return s;
            }
        }
        return null;
    }

    /**
     * the method takes the inputted tensor table and forms a table to represent it.
     * it also adds the row titles and colums titles to the lists tensorRowTitles and tensorColTitles
     *
     * @return a tensorTable object representing the tensor table
     */


    //===============================================================================================FORMAT TENSORS=================================================================================================
    public static Table formatTensor(){
        System.out.println("reading tensor");
        System.out.println(readTensor);
        Morphisms[][] tensorTable = new Morphisms[readTensor.size()-1][readTensor.size()-1];
        HashMap<String,Integer> tensorRow = new HashMap<>();
        HashMap<String, Integer> tensorCol = new HashMap<>();
        String[] colLabels = readTensor.get(0).split(",");
        for( int i=1;i<colLabels.length;i++){
            tensorCol.put(colLabels[i],i-1);
            tensorColTitles.add(colLabels[i]);
        }
        for( int i=1;i<readTensor.size();i++){
            String[] currLine = readTensor.get(i).split(",");
            tensorRow.put(currLine[0],i-1);
            tensorRowTitles.add(currLine[0]);
            for(int j=1;j<currLine.length;j++){
                tensorTable[i-1][j-1] = new Morphisms(null,null,currLine[j]);
                Tensor n = new Tensor(new Morphisms(null,null,currLine[0]),new Morphisms(null,null,colLabels[j]),new Morphisms(null,null,currLine[j]));
                tensorList.add(n);
            }
        }
        Table t = new Table(tensorTable);
        t.row = tensorRow;
        t.col = tensorCol;
        return t;
    }

    /**
     *
     * @param row the name of the row in the table
     * @param col the name of the col in the table
     * @param tensorTable table representing the tensortable
     * @return the morphism queried about from the tensor table
     */
    public static Morphisms getTensor(String row, String col, Table tensorTable){
        return tensorTable.getMorphism(row,col);
    }
    public static boolean validTensor(Table tensorTable){
        for(int i=0;i<tensorTable.table.length;i++){
            for (int j=0;j<tensorTable.table[i].length;j++){

                if(tensorTable.table[i][j].name.equals("-")){
                    return false;
                }


            }
        }
        return true;
    }


//=====================================================================================  CHECK CATEGORY PROPS ===============================================================================================================
    //(m1 . m2) . m3 = m1 . (m2.m3)
    public static boolean checkAssoc(Table table){

        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j< morphisms.size();j++){
                Morphisms m1 = morphisms.get(i);
                Morphisms m2 = morphisms.get(j);
                if( table.getMorphism(m1.name,m2.name) == null){
                    continue;
                }

                for(int k=0;k<morphisms.size();k++){
                    Morphisms m3 = morphisms.get(k);
                    if(table.getMorphism(m2.name,m3.name)==null){
                        continue;
                    }

                    Morphisms l1 = table.getMorphism(m1.name,m2.name);
                    Morphisms r1 = table.getMorphism(m2.name,m3.name);
                    if(table.getMorphism(l1.name, m3.name) == null || table.getMorphism(m1.name,r1.name) == null){
                        continue;
                    }

                    if(!table.getMorphism(l1.name, m3.name).equals(table.getMorphism(m1.name,r1.name))){
                        return false;
                    }
                }
            }
        }



        return true;
    }


    public static boolean checkComp(Table table){
        for(int i=0;i<morphisms.size();i++){
            for(int j =0;j<morphisms.size();j++){
                if(table.getMorphism(morphisms.get(i).name,morphisms.get(j).name) == null){
                    continue;
                }

                String A1 = statesA.get(morphisms.get(i).name);
                String B1 = stateB.get(morphisms.get(i).name);
                String A2 = statesA.get(morphisms.get(j).name);
                String B2 = stateB.get(morphisms.get(j).name);

                if(!A1.equals(A2) && !A2.equals(B2) && !A1.equals(B2) ){

                    boolean found = false;
                    innerLoop:
                    for(int k=0;k<morphisms.size();k++){
                        Morphisms curr = morphisms.get(k);
                        String aState = statesA.get(curr.name);
                        String bstate = stateB.get(curr.name);

                        if(aState.equals(A1) && bstate.equals(B2)){
                            found = true;
                            break innerLoop;
                        }




                    }
                    if(!found){
                        return false;
                    }





                }
            }
        }




        return true;
    }




// =================================================================================== CHECK MONOIDAL PROPERTIES =============================================================================================




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

    //(f * g) * h == f * (g * h)
    // assume the table has no blanks or "-"

    /**
     *
     * @param tensortable
     * @return
     */
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


//

    /**
     *Property To Check:
     * (k . h) * (g . f) == (k * g) . (h * f) -> if cod(h) = dom(k) then cod(f) = dom(g)
     *
     * if the morphism product of the category is "-" then we ignore and more on.
     *
     *
     * @param tensorTable a table representing the tensortable of the category
     * @param t a table representing the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean check2(Table tensorTable, Table t){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                Morphisms km = morphisms.get(i);
                Morphisms hm = morphisms.get(j);
                if(t.getMorphism(km.name,hm.name) == null){
                    continue;
                }

                for(int k=0;k<morphisms.size();k++){
                    for(int l=0;l<morphisms.size();l++){

                        Morphisms gm = morphisms.get(k);
                        Morphisms fm = morphisms.get(l);
                        if(t.getMorphism(gm.name,fm.name)== null){
                            continue;
                        }
                        Morphisms kh = t.getMorphism(km.name,hm.name);
                        Morphisms gf = t.getMorphism(gm.name,fm.name);
                        Morphisms left = tensorTable.getMorphism(kh.name, gf.name);


                        Morphisms kg = tensorTable.getMorphism(km.name,gm.name);
                        Morphisms hf = tensorTable.getMorphism(hm.name,fm.name);

                        Morphisms right = t.getMorphism(kg.name,hf.name);
                        if(right == null){
                            continue;
                        }


                        if(!left.name.equals(right.name)){
                            return false;
                        }

                    }
                }
            }
        }
        return true;
    }




    /**
     *Property to Check:
     * dom(f * g) == dom(f) * dom(g)
     *
     * Instead of using the objects, we can use the identity of the objects so that its morphism multiplication instead of object multiplication
     * dom(f) * dom(g) is object multiplication. use id(dom f) * id(dom g) instead
     *
     *
     * @param tensorTable a table representing the tensortable of the category
     * @param table a table representing the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean checkDomain(Table tensorTable, Table table){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                Morphisms f = morphisms.get(i);
                Morphisms g = morphisms.get(j);
                //recheck if we return the dom of the right side or not
                Morphisms lefttSide1 = tensorTable.getMorphism(f.name,g.name);
                State lefttSide = getState(statesA.get(tensorTable.getMorphism(f.name,g.name).name));
                State rightSide = (getState(statesA.get(tensorTable.getMorphism(f.stateA.getIdentityMorphism().name,g.stateA.getIdentityMorphism().name).name)));

                if(!lefttSide.name.equals(rightSide.name)){
                    return false;
                }
            }
        }




        return true;
    }


    /**
     * Property to check:
     *  codom(f * g) == codom(f) * codom(g)
     *  instead of using the objects, we can use the identity of the objects so that its morphism multiplication instead of object multiplication
     *  codom(f) * codom(g) is object multiplication. use id(codom f) * id(codom g) instead
     *
     *
     *
     * @param tensorTable a table representing the tensortable of the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean checkCodomain(Table tensorTable){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                Morphisms f = morphisms.get(i);
                Morphisms g = morphisms.get(j);
                //recheck if we return the dom of the right side or not
                State lefttSide = getState(stateB.get(tensorTable.getMorphism(f.name,g.name).name));
                State rightSide = getState(stateB.get(tensorTable.getMorphism(f.stateB.getIdentityMorphism().name,g.stateB.getIdentityMorphism().name).name));

                if(!lefttSide.name.equals(rightSide.name)){
                    return false;
                }
            }
        }
        return true;
    }




    /**
     *
     * Property to check:
     * id(A) * id(B) = id(A *B)
     *
     *
     * checks if the category satisfies the monoidal identity property
     * TODO check if the rhs means that the product of the lhs must just satisfy the identity properties of not(Current Undestanding)
     *
     *
     * Identity Morphism Properties:
     *     Maps an object onto itself
     *     each object can only have one
     *     left and right associative
     *
     * @param tensorTable a table representing the tensortable of the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean checkIndetitesMonoidal(Table tensorTable){
        for(int i=0;i<identityNames.size();i++){
            for( int j=0;j<identityNames.size();j++){
                System.out.println(identityNames.get(j));

                Morphisms a = morphismNames.get(identityNames.get(i));
                Morphisms b = morphismNames.get(identityNames.get(j));
                System.out.println("MOr a : "+ a.name);
                System.out.println("Mor b : "+ b.name);


                Morphisms lhs = tensorTable.getMorphism(a.name,b.name);
                if(!identityNames.contains(lhs.name)){
                    return false;
                }



            }
        }
        return true;
    }

    //there exists ID(a) where f * ID(a)  = f  = ID(a) * f for every morphism f

    public static boolean checkUniqueIden(Table tensorTable){
        for(int i=0;i<identityNames.size();i++){
            String currIden = identityNames.get(i);
            boolean valid = true;
            for(int j=0;j<morphisms.size();j++){

                String currMorphism = morphisms.get(j).name;
                if(!tensorTable.getMorphism(currIden,currMorphism).name.equals(currMorphism)){
                    valid = false;
                }
                if(!tensorTable.getMorphism(currMorphism,currIden).name.equals(currMorphism)){
                    valid = false;
                }



            }
            if(valid){
                return true;
            }



        }
        return false;
    }



//================================================================================================ Table filler =====================================================================================================================
    public static boolean validEntry(Table table, Table tensorTable){
        boolean check1 = checkAssocHalf(table);
        boolean check2 = checkCompHalf(table);
        boolean check3 = check22(tensorTable,table);
        boolean check4 = checkUniqueIden(tensorTable);
        boolean check5 = checkIndetitesMonoidal(tensorTable);
        boolean check6 = checkCodomain(tensorTable);
        boolean check7 = checkDomain(tensorTable,table);
        boolean check8 = checkAssociativity(tensorTable);
        return check1 && check2 && check3 && check4 && check5 && check6 && check7 && check8;
    }



    public static void checker(Table t){
        for(int i=0;i<t.table.length;i++){
            for(int j=0;j<t.table[i].length;j++){


                if(t.table[i][j] ==null){
                    System.out.println("I: "+i);
                    System.out.println("J: "+ j);
                }
            }
        }
    }
    public static boolean solvetable(Table table, Table tensorTable){
        System.out.println("j");
        checker(table);
        Set<String> morphNames = morphismNames.keySet();
        List<String> possentries = new ArrayList<>(morphNames);
        possentries.add("-");
        for(int i=0;i<table.table.length;i++){
            for(int j=0;j<table.table[i].length;j++){
                if(table.table[i][j]!= null && table.table[i][j].name.equals("*")){
                    for(int k=0;k<possentries.size();k++){
                        Table t = table.copier();
                        String curr = possentries.get(k);
                        if(curr.equals("-")){
                            t.table[i][j] = null;
                        }
                        else {
                            Morphisms n = new Morphisms(getState(statesA.get(curr)),getState(stateB.get(curr)),curr);
                            t.table[i][j] = n;
                        }
                        if(validEntry(t,tensorTable)){
                            if(curr.equals("-")){
                                table.table[i][j] = null;
                            }
                            else {
                                Morphisms n = new Morphisms(getState(statesA.get(curr)),getState(stateB.get(curr)),curr);
                                table.table[i][j] = n;
                            }

                            if(solvetable(table,tensorTable)){
                                System.out.println(table.table[1][1]);
                                System.out.println("next");
                                System.out.println(table.table[2][2]);
                                return true;
                            }else {
                                Morphisms nn= new Morphisms(null,null,"*");
                                table.table[i][j] = nn;
                            }
                        }
                    }
                    return false;
                }

            }
        }


        return true;
    }




    public static boolean checkAssocHalf(Table table){

        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j< morphisms.size();j++){
                Morphisms m1 = morphisms.get(i);
                Morphisms m2 = morphisms.get(j);
                if( table.getMorphism(m1.name,m2.name) == null  || table.getMorphism(m1.name,m2.name).name.equals("*")){
                    continue;
                }

                for(int k=0;k<morphisms.size();k++){
                    Morphisms m3 = morphisms.get(k);
                    if(table.getMorphism(m2.name,m3.name)==null || table.getMorphism(m2.name,m3.name).name.equals("*")){
                        continue;
                    }

                    Morphisms l1 = table.getMorphism(m1.name,m2.name);
                    Morphisms r1 = table.getMorphism(m2.name,m3.name);
                    if(table.getMorphism(l1.name, m3.name) == null || table.getMorphism(m1.name,r1.name) == null ||
                            table.getMorphism(l1.name, m3.name).name.equals("*") || table.getMorphism(m1.name,r1.name).name.equals("*")){
                        continue;
                    }

                    if(!table.getMorphism(l1.name, m3.name).equals(table.getMorphism(m1.name,r1.name))){
                        return false;
                    }
                }
            }
        }



        return true;
    }


    public static boolean checkCompHalf(Table table){
        for(int i=0;i<morphisms.size();i++){
            for(int j =0;j<morphisms.size();j++){
                if(table.getMorphism(morphisms.get(i).name,morphisms.get(j).name) == null ||table.getMorphism(morphisms.get(i).name,morphisms.get(j).name).name.equals("*") ){
                    continue;
                }

                String A1 = statesA.get(morphisms.get(i).name);
                String B1 = stateB.get(morphisms.get(i).name);
                String A2 = statesA.get(morphisms.get(j).name);
                String B2 = stateB.get(morphisms.get(j).name);

                if(!A1.equals(A2) && !A2.equals(B2) && !A1.equals(B2) ){

                    boolean found = false;
                    innerLoop:
                    for(int k=0;k<morphisms.size();k++){
                        Morphisms curr = morphisms.get(k);
                        String aState = statesA.get(curr.name);
                        String bstate = stateB.get(curr.name);

                        if(aState.equals(A1) && bstate.equals(B2)){
                            found = true;
                            break innerLoop;
                        }




                    }
                    if(!found){
                        return false;
                    }





                }
            }
        }




        return true;
    }

    /**
     *Property To Check:
     * (k . h) * (g . f) == (k * g) . (h * f) -> if cod(h) = dom(k) then cod(f) = dom(g)
     *
     * if the morphism product of the category is "-" then we ignore and more on.
     *
     *
     * @param tensorTable a table representing the tensortable of the category
     * @param t a table representing the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean check22(Table tensorTable, Table t){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                Morphisms km = morphisms.get(i);
                Morphisms hm = morphisms.get(j);
                if(t.getMorphism(km.name,hm.name) == null || t.getMorphism(km.name,hm.name).name.equals("*")){
                    continue;
                }

                for(int k=0;k<morphisms.size();k++){
                    for(int l=0;l<morphisms.size();l++){

                        Morphisms gm = morphisms.get(k);
                        Morphisms fm = morphisms.get(l);
                        if(t.getMorphism(gm.name,fm.name)== null || t.getMorphism(gm.name,fm.name).name.equals("*")){
                            continue;
                        }
                        Morphisms kh = t.getMorphism(km.name,hm.name);
                        Morphisms gf = t.getMorphism(gm.name,fm.name);
                        Morphisms left = tensorTable.getMorphism(kh.name, gf.name);


                        Morphisms kg = tensorTable.getMorphism(km.name,gm.name);
                        Morphisms hf = tensorTable.getMorphism(hm.name,fm.name);

                        Morphisms right = t.getMorphism(kg.name,hf.name);
                        if(right == null || right.name.equals("*")){
                            continue;
                        }


                        if(!left.name.equals(right.name)){
                            return false;
                        }

                    }
                }
            }
        }
        return true;
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
