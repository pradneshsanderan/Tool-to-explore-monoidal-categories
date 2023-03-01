import java.util.Stack;

public class State {
    String name;
    boolean identity;
    Morphisms identityMorphism;

    /**
     * the constunctor of the State class
     * @param name the name of the state
     */
    State(String name){
        this.name = name;
        this.identity = false;
    }

    /**
     *
     * @return true if the state has an identity morphism
     */
    public boolean hasIdentity(){
        return  identity;
    }
}
