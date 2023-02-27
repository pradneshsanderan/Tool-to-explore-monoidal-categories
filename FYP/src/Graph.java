import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Graph {
    List<State> states = new ArrayList<>();
    List<Morphisms> morphisms = new ArrayList<>();
    HashMap<String,List<Morphisms>> mapA = new HashMap<>();
    HashMap<String,List<Morphisms>> mapB = new HashMap<>();
    HashMap<String,Morphisms> identities = new HashMap<>();
    Graph(List<State> states,List<Morphisms> morphisms){
        this.states = states;
        this.morphisms = morphisms;
        setMapA();
        setMapB();
    }





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


    public List<State> getStates(){
        return states;
    }

    public List<Morphisms> getMorphisms(){
        return morphisms;
    }

    public List<Morphisms> getOutgoingMorphisms(State s){
        return mapA.get(s.name);
    }

    public List<Morphisms> getIncomingMorphisms(State s){
        return mapB.get(s.name);
    }
    public Morphisms getIdentity(State s){
        return identities.get(s.name);
    }
    private class AssocRet{
        private boolean assoc;
        private Morphisms morph;
    }
    public boolean hasIdentities(){
        return identities.size()==states.size();
    }


    public AssocRet checkAssociativity(State a, State b, State c){
        boolean firstTwo = false;
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
                        AssocRet ret = new AssocRet();
                        ret.morph = curr;
                        ret.assoc = true;
                        return ret;
                    }
                }
            }
        }
        AssocRet ret = new AssocRet();
        ret.morph = null;
        ret.assoc = false;
        return ret;
    }

    public boolean twoStateALink(State a,State b){
        List<Morphisms> morpA = getOutgoingMorphisms(a);
        for (Morphisms curr : morpA) {
            if (curr.stateB == b) {
                return true;
            }
        }
        return false;
    }


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
//                System.out.println(rowStateA);
//                System.out.println(rowStateB);
//                System.out.println(colStateA);
//                System.out.println(colStateB);
                if(rowStateB.equals(colStateA)){
                    if(rowStateA .equals( rowStateB) && colStateA.equals( colStateB)){
                        //both the identity morphism
                        table[i][j] = row.name;
                    } else if (!rowStateA.equals(rowStateB) && !colStateA.equals(colStateB)) {
                        // we have 3 different states, and we need to check associativity law
                        AssocRet assoc = checkAssociativity(row.stateA,row.stateB,col.stateB);
                        if(assoc.assoc){
                            table[i][j] = assoc.morph.name;
                        }
                        else{
                            table[0][0]= "ERROR";
                            break outerloop;
                        }
                    }
                    else{
                        //identity followed by normal or normal followed by identity
                        // identity follwed by normal
                        if(rowStateA.equals(rowStateB)){
                            table[i][j] = col.name;

                         //normal followed by identity
                        }else{
                            table[i][j] = row.name;
                        }
                    }
                } else{
                    table[i][j]= "-";
                }
            }
        }
        return table;

    }




}
