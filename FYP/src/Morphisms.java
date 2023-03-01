public class Morphisms {
    State stateA;
    State stateB;
    String name;

    /**
     * the contructor for the Morphisms calss
     * @param stateA the domain state
     * @param stateB the codomain state
     * @param name the name of the morphism
     */
    Morphisms(State stateA, State stateB, String name){
        this.name = name;
        this.stateA = stateA;
        this.stateB = stateB;
    }

    /**
     * return the codomain state
     * @return
     */
    public State getNextState(){
        return stateB;
    }

    /**
     * returns the domain state
     * @return
     */
    public State getOriginState(){
        return stateA;
    }

    /**
     * returns the name of the morphism
     * @return
     */
    public String getName(){
        return name;
    }

}
