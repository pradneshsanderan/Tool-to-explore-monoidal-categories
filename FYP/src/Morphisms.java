public class Morphisms {
    State stateA;
    State stateB;
    String name;

    Morphisms(State stateA, State stateB, String name){
        this.name = name;
        this.stateA = stateA;
        this.stateB = stateB;
    }

    public State getNextState(){
        return stateB;
    }
    public State getOriginState(){
        return stateA;
    }
    public String getName(){
        return name;
    }

}
