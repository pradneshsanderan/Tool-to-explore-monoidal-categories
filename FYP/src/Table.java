import java.util.HashMap;

public class Table {
    Morphisms[][] table;
    public HashMap<String, Integer> col = new HashMap<>();
    public HashMap<String,Integer> row = new HashMap<>();

    Table( Morphisms[][] table){
        this.table = table;
    }

    public Morphisms getMorphism(String mophA, String morphB){
        int rowIndex = row.get(mophA);
        int colIndex = col.get(morphB);
        return table[rowIndex][colIndex];
    }




}
