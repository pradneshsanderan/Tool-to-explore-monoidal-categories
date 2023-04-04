import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Table {
    Morphisms[][] table;
    public HashMap<String, Integer> col = new HashMap<>();
    public HashMap<String,Integer> row = new HashMap<>();

    Table( Morphisms[][] table){
        this.table = table;
    }

    /**
     * constructor for the category table class
     * @param mophA row morphism
     * @param morphB col morphism
     * @return table value of the two morphisms
     */
    public Morphisms getMorphism(String mophA, String morphB){
        System.out.println("rpq");
        System.out.println(row.keySet());
        System.out.println("clq");
        System.out.println(col.keySet());
        int rowIndex = row.get(mophA);
        int colIndex = col.get(morphB);
        return table[rowIndex][colIndex];
    }

    /**
     *
     * @param row_ name of the morphism we want the row for
     * @return the index of the row with that morphism as the title
     */
    public int getRowIndex(String row_){
        return row.get(row_);
    }

    /**
     *
     * @param col_ name of the morphism we want the col for
     * @return the index of thr col with that morphism as the title
     */
    public int getColIndex(String col_){
        return  col.get(col_);
    }

    public void printTable(List<String> cols, List<String> rows){



        System.out.print("/");
        for(int i=0;i<cols.size();i++){
            System.out.print(cols.get(i));
        }
        int k=0;
        for(int i=0;i<table.length;i++){
            System.out.print(";");
            System.out.println(rows.get(k));
            for(int j=0;j<table[i].length;j++){
                System.out.print(".");
                System.out.print(table[i][j].name);
                System.out.print("-");
            }
            k++;
        }
    }

    public Table copier(){
        Morphisms[][] t = new Morphisms[table.length][table.length];
        for(int i=0;i<table.length;i++){
            for(int j=0;j<table[i].length;j++){
                t[i][j] = table[i][j];


            }
        }
        Table tt =  new Table(t);
        tt.col = this.col;
        tt.row = this.row;
        return tt;


    }




}
