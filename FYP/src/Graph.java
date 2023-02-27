import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Graph {
    List<State> states;
    List<Morphisms> morphisms;
    HashMap<State,List<Morphisms>> mapA;
    HashMap<State,List<Morphisms>> mapB;
    HashMap<State,Morphisms> identities;
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


    public boolean checkAssociativity(State a, State b, State c){
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
                        return true;
                    }
                }
            }
        }
        return false;
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
        Morphisms[][] table = new Morphisms[morphisms.size()][morphisms.size()];
        for(int i=0;i< morphisms.size();i++){
            Morphisms row = morphisms.get(0);
            for( int j=0;j< morphisms.size();j++){
                Morphisms col = morphisms.get(0);
                //cycle through the morphisms and fill up the table.
                State rowStateA = row.stateA;
                State rowStateB = row.stateB;
                State colStateA = col.stateA;
                State colStateB = col.stateB;
                if(rowStateA!= colStateA){
                    table[i][j] = null;
                    break;
                }
                if(rowStateB == colStateB){








                }








            }
        }

    }


}
