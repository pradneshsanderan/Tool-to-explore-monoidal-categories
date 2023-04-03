public class Tensor {
    Morphisms morphismsA;
    Morphisms morphismsB;
    Morphisms ret;


    /**
     * The constructor for the tensor class
     * @param morphismsA row morphism
     * @param morphismsB col morphism
     * @param ret tensor product of the two morphisms
     */
    Tensor(Morphisms morphismsA,Morphisms morphismsB,Morphisms ret){
        this.morphismsA = morphismsA;
        this.morphismsB = morphismsB;
        this.ret = ret;
    }
}
