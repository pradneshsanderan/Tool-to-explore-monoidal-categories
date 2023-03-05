import java.util.*;

public class Graph {
    List<State> states = new ArrayList<>();
    List<Morphisms> morphisms = new ArrayList<>();
    HashMap<String,List<Morphisms>> mapA = new HashMap<>();
    HashMap<String,List<Morphisms>> mapB = new HashMap<>();
    HashMap<String,Morphisms> identities = new HashMap<>();
    HashMap<Coordinates,List<Morphisms>> combinations = new HashMap<>();
    List<Coordinates> comboList = new ArrayList<>();

    /**
     * the constructor for the class
     * @param states a list of states that got inputted
     * @param morphisms a list of morphisms that got inputted
     */
    Graph(List<State> states,List<Morphisms> morphisms){
        this.states = states;
        this.morphisms = morphisms;
        setMapA();
        setMapB();
    }


    /**
     * the method reads through the list of morphisms and states and adds each state to a hashmap
     * where the key is the states name and the value is a list of morphisms that have said state as the domain
     * the method also sets the identity of a state to true and adds it to the identities list if it recognises it as
     * an identity.
     */
    private void setMapA(){
        for (Morphisms curr : morphisms) {
            if (mapA.containsKey(curr.stateA.name)) {
                List<Morphisms> a = mapA.get(curr.stateA.name);
                a.add(curr);
                mapA.remove(curr.stateA.name);
                mapA.put(curr.stateA.name, a);
            } else {
                List<Morphisms> a = new ArrayList<>();
                mapA.put(curr.stateA.name, a);
            }

            if(curr.stateB.name.equals( curr.stateA.name)){
                curr.stateB.identity = true;
                curr.stateA.identity =true;
                curr.stateB.identityMorphism = curr;
                identities.put(curr.stateA.name,curr);
                identities.put(curr.stateB.name,curr);

            }
        }
    }


    /**
     * the method reads through the list of morphisms and states and adds each state to a hashmap
     * where the key is the states name and the value is a list of morphisms that have said state as the codomain

     */
    private void setMapB(){
        for (Morphisms curr : morphisms) {
            if (mapB.containsKey(curr.stateB.name)) {
                List<Morphisms> a = mapB.get(curr.stateB.name);
                a.add(curr);
                mapB.remove(curr.stateB.name);
                mapB.put(curr.stateB.name, a);
            } else {
                List<Morphisms> a = new ArrayList<>();
                mapB.put(curr.stateB.name, a);
            }
        }
    }


    /**
     *
     * @return a list of states in the category
     */
    public List<State> getStates(){
        return states;
    }

    /**
     *
     * @return a list of morphisms in the category
     */
    public List<Morphisms> getMorphisms(){
        return morphisms;
    }

    /**
     *
     * @param s that state that we would like to get the morphisms for
     * @return a list of morphisms that have state s as a domain
     */
    public List<Morphisms> getOutgoingMorphisms(State s){
        return mapA.get(s.name);
    }

    /**
     *
     * @param s that state that we would like to get the morphisms for
     * @return a list of morphisms that have state s as a codomain
     */
    public List<Morphisms> getIncomingMorphisms(State s){
        return mapB.get(s.name);
    }

    /**
     *
     * @param s that state that we would like to get the morphism for
     * @return the identity morphism for the state
     */
    public Morphisms getIdentity(State s){
        return identities.get(s.name);
    }

    /**
     *  a new object that is used to return 2 values in the checkAssociativity() method
     */
    private class AssocRet{
        private boolean assoc;
        private List<Morphisms> morph;
    }

    public class Coordinates{
        public int row;
        public int col;
    }

    /**
     *
     * @return true if each state in the category has an identity
     */
    public boolean hasIdentities(){
        return identities.size()==states.size();
    }


    /**
     * checks the associativity between 3 states. if there is a morphism between state a and state b, and there
     * is a morphism between state b and state c then there must be a morphism between state a and state c for the
     * category to be valid
     * @param a A state
     * @param b A state
     * @param c A state
     * @return true and the morphism connecting state c and a if it is valid. false and null otherwise.
     */
    public AssocRet checkAssociativity(State a, State b, State c){
        boolean firstTwo = false;
        AssocRet ret = new AssocRet();
        List<Morphisms> retList = new ArrayList<>();

        ret.assoc = false;
        List<Morphisms> morpA = mapA.get(a.name);

        for (Morphisms curr : morpA) {
            if (curr.stateB.name.equals(b.name)) {
                firstTwo = true;
                break;
            }
        }
        boolean secondTwo = false;
        if(firstTwo){
            List<Morphisms> morphB = getOutgoingMorphisms(b);
            for (Morphisms curr : morphB) {
                if (curr.stateB.name.equals(c.name)) {
                    secondTwo = true;
                    break;
                }
            }
            if(secondTwo){
                for (Morphisms curr : morpA) {
                    if (curr.stateB.name.equals(c.name)) {
                        retList.add(curr);
                        ret.assoc = true;
                    }
                }
            }
        }
        ret.morph = retList;
        return ret;
    }

    /**
     * checks if there is a morphism going from state a to state b
     * @param a A state
     * @param b A state
     * @return true if there is a morphism going from state a to state b
     */
    public AssocRet twoStateALink(State a,State b){
        List<Morphisms> morpA = getOutgoingMorphisms(a);
        List<Morphisms> combos = new ArrayList<>();
        AssocRet ret = new AssocRet();
        ret.assoc =false;
        for (Morphisms curr : morpA) {
            if (curr.stateB.name.equals(b.name) && !combos.contains(curr)) {
                combos.add(curr);
                if(combos.size()>1){
                    ret.assoc =true;
                }

            }
        }
        ret.morph =combos;
        return ret;
    }
    private String[][] copier(String[][] s){
        String[][] ret = new String[s.length][s.length];
        for(int i=0;i<s.length;i++){
            for(int j=0;j<s.length;j++){
                ret[i][j] = s[i][j];
            }
        }
        return ret;
    }


    /**
     * fills up the multiplication table row by row. it first checks if the codomain of the first state
     * is the same as the domain of the second one. if it is not, it sets the string to "-". if there is,
     * it first checks if it is an identity morphism,then if the domain of the fist, domain of the second and codomain
     * of the second is different( then it calls checkAssociativity()) to check if its associative. if its not, it breaks
     * and sets the first entry to ERROR to tell the main function that there is no solution) else it can be a morphism
     * followed by identity or the other way around. then we just add the morphism to the table
     * @return a table of the names of the morphisms representing a valid solution
     */
    public String[][] returnTable(){
        String[][] table = new String[morphisms.size()][morphisms.size()];
        outerloop:
        for(int i=0;i< morphisms.size();i++){
            Morphisms row = morphisms.get(i);
            for( int j=0;j< morphisms.size();j++){
                Morphisms col = morphisms.get(j);
                //cycle through the morphisms and fill up the table.
                String rowStateA = row.stateA.name;
                String rowStateB = row.stateB.name;
                String colStateA = col.stateA.name;
                String colStateB = col.stateB.name;
                if(rowStateB.equals(colStateA)){
                    if(rowStateA .equals( rowStateB) && colStateA.equals( colStateB)){
                        //both the identity morphism
                        table[i][j] = row.name;
                    } else if (!rowStateA.equals(rowStateB) && !colStateA.equals(colStateB)) {
                        // we have 3 different states, and we need to check associativity law
                        AssocRet assoc = checkAssociativity(row.stateA,row.stateB,col.stateB);
                        if(assoc.assoc){
                            table[i][j] = assoc.morph.get(0).name;
                            List<Morphisms> combos = new ArrayList<>(assoc.morph);
                            boolean hasCol = false;
                            boolean hasRow = false;
                            for(int f=0;f<combos.size();f++){
                                if(combos.get(f).name.equals(col.name) ){
                                    hasCol =true;
                                    break ;
                                }
                            }
                            for(int f=0;f<combos.size();f++){
                                if(combos.get(f).name.equals(row.name)){
                                    hasRow =true;
                                    break ;
                                }
                            }
                            if(hasCol){
                                table[i][j] = col.name;
                            } else if (hasRow) {
                                table[i][j] = row.name;
                            }
                            else{
                                Coordinates c = new Coordinates();
                                c.row = i;
                                c.col = j;
                                combinations.put(c,combos);
                                comboList.add(c);
                            }
                        }
                        else{
                            table[0][0]= "ERROR";
                            break outerloop;
                        }
                    }
                    else{
                        //identity followed by normal or normal followed by identity
                        // Identity followed by normal
                        AssocRet returnnedAssoc = twoStateALink(row.stateA,col.stateA);
                        if(rowStateA.equals(rowStateB)){
                            if(returnnedAssoc.assoc){
                                table[i][j] = col.name;
                                List<Morphisms> combos = new ArrayList<>(returnnedAssoc.morph);
                                boolean hasRow = false;
                                boolean hasCol = false;
                                for(int f=0;f<combos.size();f++){
                                    if(combos.get(f).name.equals(col.name)){
                                        hasCol =true;
                                        break ;
                                    }
                                }
                                for(int f=0;f<combos.size();f++){
                                    if(combos.get(f).name.equals(row.name)){
                                        hasRow =true;
                                        break ;
                                    }
                                }
                                if(hasRow) {
                                    table[i][j] = row.name;
                                }
                                if(!hasRow && !hasCol){
                                    Coordinates c = new Coordinates();
                                    c.row = i;
                                    c.col = j;
                                    combinations.put(c,combos);
                                    comboList.add(c);
                                }
                            }
                            else{
                                table[i][j] = col.name;
                            }
                         //normal followed by identity
                        }else{
                            if(returnnedAssoc.assoc){
                                table[i][j] = row.name;
                                List<Morphisms> combos = new ArrayList<>(returnnedAssoc.morph);
                                boolean hasCol = false;
                                boolean hasRow = false;
                                for(int f=0;f<combos.size();f++){
                                    if(combos.get(f).name.equals(col.name)){
                                        hasCol =true;
                                        break ;
                                    }
                                }
                                for(int f=0;f<combos.size();f++){
                                    if(combos.get(f).name.equals(row.name)){
                                        hasRow =true;
                                        break ;
                                    }
                                }
                                if(hasCol){
                                    table[i][j] = col.name;
                                }
                                if(!hasRow && !hasCol){
                                    Coordinates c = new Coordinates();
                                    c.row = i;
                                    c.col = j;
                                    combinations.put(c,combos);
                                    comboList.add(c);
                                }
                            }
                            else{
                                table[i][j] = row.name;
                            }
                        }
                    }
                } else{
                    table[i][j]= "-";
                }
            }
        }
        return table;

    }


    public void printTables(){
        //comboList is a list of coordinates with multiples entries
        // combinations is a hashmap <Coordinate, List<Morphisms>>
        String[][] t =returnTable();
        if(t[0][0].equals("ERROR")){
            System.out.println("The category has no solutions");
            return;
        }
        int counter = 1;
        System.out.println("Table 0");
        System.out.println(Arrays.deepToString(t).replace("],","]\n"));
        System.out.println(" ");
        HashMap<Integer,String[][]> tables = new HashMap<Integer, String[][]>();
        tables.put(0,t);
        int tracker =0;
        int size =1;
        for(int i=0;i<comboList.size();i++){
            Coordinates currCoordinate = comboList.get(i);
            innerLoop:
            size = tables.size();
            for(int j=0;j<size;j++){
                String[][] currTable = copier(tables.get(j));
                List<Morphisms> morphisms = combinations.get(currCoordinate);
                for(int k=0;k<morphisms.size();k++){
                    Morphisms currMorphism = morphisms.get(k);
                    if(!currTable[currCoordinate.row][currCoordinate.col].equals(currMorphism.name)){
                        currTable[currCoordinate.row][currCoordinate.col]=currMorphism.name;
                        System.out.println("Table "+counter);
                        System.out.println(Arrays.deepToString(currTable).replace("],","]\n"));
                        System.out.println(" ");
                        counter++;
                        tracker++;
                        tables.put(tracker,currTable);
                    }
                }
            }
        }
    }





















//    public void returnCombinations(String[][] t){
//        List<String[][]> tableList = new ArrayList<>();
//        tableList.add(t);
//        /**for every coordinate*/
//        for(int i=0;i<comboList.size();i++){
//            Coordinates currCoordinate = comboList.get(i);
//            List<Morphisms> currList = combinations.get(currCoordinate);
//            int tableSize = tableList.size();
//            System.out.println(tableSize);
//            System.out.println(currCoordinate.row + "   " + currCoordinate.col);
//            List<String> added = new ArrayList<>();
//            added.add(t[currCoordinate.row][currCoordinate.col]);
//            /**for every morphism*/
//            for(int j=0;j<currList.size();j++){
//                Morphisms currMorphism = currList.get(j);
//                System.out.println("curr morphism "+ currMorphism.name);
//                /**for every table in the list*/
//                for(int k=0;k<tableSize;k++){
//                    if(!added.contains(currMorphism.name)){
//                        System.out.println("inside");
//                        String[][] tableA = tableList.get(k).clone();
//                        tableA[currCoordinate.row][currCoordinate.col] = currMorphism.name;
//                        tableList.add(tableA);
//                        added.add(currMorphism.name);
//                    }
//                }
//            }
//        }
//
//
//        for(int i=0;i<tableList.size();i++){
//
//
//            System.out.println("Table "+ i);
//            System.out.println(Arrays.deepToString(tableList.get(i)).replace("],","]\n"));
//            System.out.println(" ");
//
//        }
//    }



//
//    public String[][] returnTable(){
//        String[][] table = new String[morphisms.size()][morphisms.size()];
//        outerloop:
//        for(int i=0;i< morphisms.size();i++){
//            Morphisms row = morphisms.get(i);
//            for( int j=0;j< morphisms.size();j++){
//                Morphisms col = morphisms.get(j);
//                //cycle through the morphisms and fill up the table.
//                String rowStateA = row.stateA.name;
//                String rowStateB = row.stateB.name;
//                String colStateA = col.stateA.name;
//                String colStateB = col.stateB.name;
////                System.out.println(rowStateA);
////                System.out.println(rowStateB);
////                System.out.println(colStateA);
////                System.out.println(colStateB);
//                if(rowStateB.equals(colStateA)){
//                    if(rowStateA .equals( rowStateB) && colStateA.equals( colStateB)){
//                        //both the identity morphism
//                        table[i][j] = row.name;
//                    } else if (!rowStateA.equals(rowStateB) && !colStateA.equals(colStateB)) {
//                        // we have 3 different states, and we need to check associativity law
//                        AssocRet assoc = checkAssociativity(row.stateA,row.stateB,col.stateB);
//                        if(assoc.assoc){
//                            table[i][j] = assoc.morph.get(0).name;
//                            List<Morphisms> combos = new ArrayList<>(assoc.morph);
//                            boolean hasCol = false;
//                            boolean hasRow = false;
//                            for(int f=0;f<combos.size();f++){
//                                if(combos.get(f).name.equals(col.name) ){
//                                    hasCol =true;
//                                    break ;
//                                }
//                            }
//                            for(int f=0;f<combos.size();f++){
//                                if(combos.get(f).name.equals(row.name)){
//                                    hasRow =true;
//                                    break ;
//                                }
//                            }
//
//                            if(hasCol){
//                                table[i][j] = col.name;
//                            } else if (hasRow) {
//                                table[i][j] = row.name;
//                            }
////                            } else{
////                                Coordinates c = new Coordinates();
////                                c.row =i;
////                                c.col = j;
////                                combinations.put(c,combos);
////                                comboList.add(c);
////                            }
//
//                        }
//                        else{
//                            table[0][0]= "ERROR";
//                            break outerloop;
//                        }
//                    }
//                    else{
//                        //identity followed by normal or normal followed by identity
//                        //  follwed by normal
//                        AssocRet returnnedAssoc = twoStateALink(row.stateA,col.stateA);
//                        if(rowStateA.equals(rowStateB)){
//                            if(returnnedAssoc.assoc){
//                                List<Morphisms> combos = new ArrayList<>(returnnedAssoc.morph);
//                                boolean hasCol = false;
//                                boolean hasRow = false;
//                                for(int f=0;f<combos.size();f++){
//                                    if(combos.get(f).name.equals(col.name)){
//                                        hasCol =true;
//                                        break ;
//                                    }
//                                }
//                                for(int f=0;f<combos.size();f++){
//                                    if(combos.get(f).name.equals(row.name)){
//                                        hasRow =true;
//                                        break ;
//                                    }
//                                }
//                                if(hasCol){
//                                    table[i][j] = col.name;
//                                }else if (hasRow) {
//                                    table[i][j] = row.name;
//
//                                }
//                                else{
//                                    table[i][j] = col.name;
//                                }
////                                if(hasCol){
////                                    table[i][j] = col.name;
////                                }else if (hasRow) {
////                                    table[i][j] = row.name;
////
////                                }
////                                else{
////                                    Coordinates c = new Coordinates();
////                                    c.row =i;
////                                    c.col = j;
////                                    combinations.put(c,combos);
////                                    table[i][j] = combos.get(0).name;
////                                    comboList.add(c);
////                                }
//
//                            }
//                            else{
//                                table[i][j] = col.name;
//                            }
//                            /**
//                             * if the col is a certain morphism, it can only be that morphism
//                             */
//
//                            //normal followed by identity
//                        }else{
//                            if(returnnedAssoc.assoc){
//                                List<Morphisms> combos = new ArrayList<>(returnnedAssoc.morph);
//                                boolean hasCol = false;
//                                boolean hasRow = false;
//                                for(int f=0;f<combos.size();f++){
//                                    if(combos.get(f).name.equals(col.name)){
//                                        hasCol =true;
//                                        break ;
//                                    }
//                                }
//                                for(int f=0;f<combos.size();f++){
//                                    if(combos.get(f).name.equals(row.name)){
//                                        hasRow =true;
//                                        break ;
//                                    }
//                                }
//                                if(hasCol){
//                                    table[i][j] = col.name;
//                                }else if (hasRow) {
//                                    table[i][j] = row.name;
//
//                                }
//                                else{
//                                    table[i][j] = row.name;
//                                }
//
////                                if(hasCol){
////                                    table[i][j] = col.name;
////                                }else if (hasRow) {
////                                    table[i][j] = row.name;
////
////                                }
////                                else{
////                                    Coordinates c = new Coordinates();
////                                    c.row =i;
////                                    c.col = j;
////                                    combinations.put(c,combos);
////                                    table[i][j] = combos.get(0).name;
////                                    comboList.add(c);
////                                }
//                            }
//                            else{
//                                table[i][j] = row.name;
//                            }
//
//                        }
//                    }
//                } else{
//                    table[i][j]= "-";
//                }
//            }
//        }
//        return table;
//
//    }




}
