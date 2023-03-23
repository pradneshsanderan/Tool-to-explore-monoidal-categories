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

    public static String path = "C:\\Users\\pradn\\Desktop\\School\\Year_4\\FYP\\cat1.csv";
    public static String tensorPath = "C:\\Users\\pradn\\Desktop\\School\\Year_4\\FYP\\tensor1.csv";
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
        TensorTable tensorTable = formatTensor();
        Table table = createTable();

        //check category properties



        //check monoidal properties
        boolean assoc = checkAssociativity(tensorTable);
        boolean check2 = check2(tensorTable,table);
        boolean domain = checkDomain(tensorTable,table);
        boolean codomain = checkCodomain(tensorTable);
        boolean idenMonoidal = checkIndetitesMonoidal(tensorTable);
        boolean pass = assoc && check2 && domain && codomain && idenMonoidal;

        if(pass){
            System.out.println("It is a valid monoidal category");
        }
        else {
            System.out.println("It is NOT a valid monoidal category");
            if(!assoc){
                System.out.println("It failed the associativity test");
            }
            if(!check2){
                System.out.println("It failed the check2 test");
            }
            if(!domain){
                System.out.println("It failed the domain test");
            }
            if(!codomain){
                System.out.println("It failed the codomain test");
            }
            if(!idenMonoidal){
                System.out.println("It failed the identity test");
            }
        }

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
                   if(!curLine[g].equals(titlesCol[g]) || !curLine[g].equals("-")){
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
                        if(!c2[k].equals(titlesRow[l]) || !c2[k].equals("-")){
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
                t[i][j] = morphismNames.getOrDefault(currLine[j], null);

            }
        }
        Table table = new Table(t);
        table.row = row;
        table.col = col;
        return table;
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
        for(int i=1;i<readLines.size();i++){
            String row = "f" + (i-1);
            String[] curr = readLines.get(i).split(",");
            for(int j =1;j<curr.length;j++){
                String n = curr[j];
                String col = "f" + (j-1);

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
                            rechecks.add(new int[]{i+1,j+1});
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
                            rechecks.add(new int[]{i+1,j+1});
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
                                rechecks.add(new int[]{i+1,j+1});
                                continue;
                            }

                        }else {
                            System.out.println("state A not added yet");
                            if(statesA.containsKey(row)){
                                statesA.put(n,statesA.get(row));
                                continue;
                            }
                            else{
                                rechecks.add(new int[]{i+1,j+1});
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
                                rechecks.add(new int[]{i+1,j+1});
                                continue;
                            }

                        }

                        Bset = true;
                    }
                    if(!ASet && !Bset){
                        //need to recheck later
                        rechecks.add(new int[]{i+1,j+1});
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

    public static TensorTable formatTensor(){
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
        TensorTable t = new TensorTable(tensorTable);
        t.tensorRow = tensorRow;
        t.tensorCol = tensorCol;
        return t;
    }

    public static Morphisms getTensor(String row, String col, TensorTable tensorTable){
        return tensorTable.getMorphism(row,col);
    }






// =================================================================================== CHECK MONOIDAL PROPERTIES =============================================================================================


    //(f * g) * h == f * (g * h)
    // assume the table has no blanks or "-"
    public static boolean checkAssociativity(TensorTable tensortable){
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
    public static boolean check2(TensorTable tensorTable, Table t){
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


                        if(!left.name.equals(right.name)){
                            return false;
                        }

                    }
                }
            }
        }
        return true;
    }


//    dom(f * g) == dom(f) * dom(g)
//    instead of using the objects, we can use the identity of the objects so that its morphism multiplication instead of object multiplication
//     *  dom(f) * dom(g) is object multiplication. use id(dom f) * id(dom g) instead
    public static boolean checkDomain(TensorTable tensorTable, Table table){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                Morphisms f = morphisms.get(i);
                Morphisms g = morphisms.get(j);
                //recheck if we return the dom of the right side or not
                State lefttSide = tensorTable.getMorphism(f.name,g.name).stateA;
                State rightSide = tensorTable.getMorphism(f.stateA.getIdentityMorphism().name,g.stateA.getIdentityMorphism().name).stateA;

                if(!lefttSide.name.equals(rightSide.name)){
                    return false;
                }
            }
        }




        return true;
    }


    public static boolean checkCodomain(TensorTable tensorTable){
        for(int i=0;i<morphisms.size();i++){
            for(int j=0;j<morphisms.size();j++){
                Morphisms f = morphisms.get(i);
                Morphisms g = morphisms.get(j);
                //recheck if we return the dom of the right side or not
                State lefttSide = tensorTable.getMorphism(f.name,g.name).stateB;
                State rightSide = tensorTable.getMorphism(f.stateB.getIdentityMorphism().name,g.stateB.getIdentityMorphism().name).stateB;

                if(!lefttSide.name.equals(rightSide.name)){
                    return false;
                }
            }
        }
        return true;
    }


    //id(A) * id(B) = id(A *B)

    public static boolean checkIndetitesMonoidal(TensorTable tensorTable){
        for(int i=0;i<identityNames.size();i++){
            for( int j=0;j<identityNames.size();j++){

                Morphisms a = morphismNames.get(identityNames.get(i));
                Morphisms b = morphismNames.get(identityNames.get(j));

                Morphisms lhs = tensorTable.getMorphism(a.name,b.name);


                int  rowIndex = tensorTable.getRowIndex(lhs.name);
                int colIndex =tensorTable.getColIndex(lhs.name);

                Morphisms[] line = tensorTable.t[rowIndex];


                //check the row
                for(int k=0;k<line.length;k++){
                    if(!line[k].name.equals(tensorColTitles.get(k))){
                        return false;
                    }

                }

                //check col
                for(int k=0;k<tensorTable.t.length;k++){
                    if(!tensorTable.t[k][colIndex].name.equals(tensorRowTitles.get(k))){
                        return false;
                    }
                }
                // we dont have to check the exact row and col of the identity because the above checks has done that



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
