import java.util.Stack;

public class State {
    String name;
    boolean identity;

    State(String name){
        this.name = name;
        this.identity = false;
    }
    public boolean hasIdentity(){
        return  identity;
    }
}
