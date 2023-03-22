import java.util.HashMap;

public class TensorTable {
    Morphisms[][] t ;
    public  HashMap<String, Integer> tensorCol = new HashMap<>();
    public HashMap<String,Integer> tensorRow = new HashMap<>();



    TensorTable(Morphisms[][] t){
        this.t = t;
    }

   public Morphisms getMorphism(String mophA, String morphB){
        int rowIndex = tensorRow.get(mophA);
        int colIndex = tensorCol.get(morphB);
        return t[rowIndex][colIndex];
   }
//
//   public void setMap(HashMap<String,Integer> tensorRow,HashMap<String, Integer> tensorCol){
//        this.tensorCol = tensorCol;
//        this.tensorRow = tensorRow;
//   }
}
