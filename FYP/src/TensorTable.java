import java.util.HashMap;

public class TensorTable {
    Morphisms[][] t ;
    public  HashMap<String, Integer> tensorCol = new HashMap<>();
    public HashMap<String,Integer> tensorRow = new HashMap<>();



    TensorTable(Morphisms[][] t){
        this.t = t;
    }

    /**
     * constructor for the tensortable class
     * @param mophA row morphism
     * @param morphB col morphism
     * @return tensorproduct of the 2 morphisms from the table
     */
    public Morphisms getMorphism(String mophA, String morphB){
        int rowIndex = tensorRow.get(mophA);
        int colIndex = tensorCol.get(morphB);
        return t[rowIndex][colIndex];
   }


    /**
     *
     * @param row name of the morphism we want the row for
     * @return the index of the row with that morphism as the title
     */
    public int getRowIndex(String row){
        return tensorRow.get(row);
    }

    /**
     *
     * @param col name of the morphism we want the col for
     * @return the index of thr col with that morphism as the title
     */
    public int getColIndex(String col){
        return  tensorCol.get(col);
    }
}
