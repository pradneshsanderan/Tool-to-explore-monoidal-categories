import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Graph {
    List<State> states = new ArrayList<>();
    List<Morphisms> morphisms = new ArrayList<>();
    HashMap<State,List<Morphisms>> mapA = new HashMap<>();
    HashMap<State,List<Morphisms>> mapB = new HashMap<>();
    HashMap<State,Morphisms> identities = new HashMap<>();
    Graph(List<State> states,List<Morphisms> morphisms){
        this.states = states;
        this.morphisms = morphisms;
        setMapA();
        setMapB();
    }





    private void setMapA(){
        for (Morphisms curr : morphisms) {
            if (mapA.containsKey(curr.stateA)) {
                List<Morphisms> a = mapA.get(curr.stateA);
                a.add(curr);
                mapA.remove(curr.stateA);
                mapA.put(curr.stateA, a);
            } else {
                List<Morphisms> a = new ArrayList<>();
                mapA.put(curr.stateA, a);
            }

            if(curr.stateB == curr.stateA){
                curr.stateB.identity = true;
                curr.stateB.identityMorphism = curr;
                identities.put(curr.stateA,curr);
                identities.put(curr.stateB,curr);

            }
        }
    }


    private void setMapB(){
        for (Morphisms curr : morphisms) {
            if (mapB.containsKey(curr.stateB)) {
                List<Morphisms> a = mapB.get(curr.stateB);
                a.add(curr);
                mapB.remove(curr.stateB);
                mapB.put(curr.stateB, a);
            } else {
                List<Morphisms> a = new ArrayList<>();
                mapB.put(curr.stateB, a);
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
        return mapA.get(s);
    }

    public List<Morphisms> getIncomingMorphisms(State s){
        return mapB.get(s);
    }
    public Morphisms getIdentity(State s){
        return identities.get(s);
    }
    private class AssocRet{
        private boolean assoc;
        private Morphisms morph;
    }


    public AssocRet checkAssociativity(State a, State b, State c){
        boolean firstTwo = false;
        List<Morphisms> morpA = getOutgoingMorphisms(a);
        for (Morphisms curr : morpA) {
            if (curr.stateB == b) {
                firstTwo = true;
                break;
            }
        }
        boolean secondTwo = false;
        if(firstTwo){
            List<Morphisms> morphB = getOutgoingMorphisms(b);
            for (Morphisms curr : morphB) {
                if (curr.stateB == c) {
                    secondTwo = true;
                    break;
                }
            }
            if(secondTwo){
                for (Morphisms curr : morpA) {
                    if (curr.stateB == c) {
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
            Morphisms row = morphisms.get(0);
            for( int j=0;j< morphisms.size();j++){
                Morphisms col = morphisms.get(0);
                //cycle through the morphisms and fill up the table.
                State rowStateA = row.stateA;
                State rowStateB = row.stateB;
                State colStateA = col.stateA;
                State colStateB = col.stateB;
                if(rowStateB==colStateA){
                    if(rowStateA == rowStateB && colStateA == colStateB){
                        //both the identity morphism
                        table[i][j] = row.name;
                    } else if (rowStateA!= rowStateB && colStateA!=colStateB) {
                        // we have 3 different states, and we need to check associativity law
                        AssocRet assoc = checkAssociativity(rowStateA,rowStateB,colStateB);
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
                        if(rowStateA==rowStateB){
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
