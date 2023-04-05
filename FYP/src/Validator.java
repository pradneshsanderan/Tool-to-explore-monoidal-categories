public class Validator {

    /**
     * TODO: THE MONOIDAL PART
     * A monoidal category is a category equipped with a tensor product and a unit object, subject to the following properties:
     *  PROPERTIES TO CHECK:::
     *
     *  dom(f * g) == dom(f) * dom(g)
     *  instead of using the objects, we can use the identity of the objects so that its morphism multiplication instead of object multiplication
     *  dom(f) * dom(g) is object multiplication. use id(dom f) * id(dom g) instead
     *
     *  codomain(f * g) == codomain(f) * codomain(g)
     *
     *  (f * g) * h == f * (g * h)
     *
     *  (k . h) * (g . f) == (k * g) . (h * f) -> if cod(h) = dom(k) then cod(f) = dom(g)
     *  if k.h does not exist then just skip
     *
     *
     *  id(A) * id(B) = id(A *B)
     *  take 2 ids, get tensor value. then check if the tensor value is a valid id
     *
     *
     *
     *  the last one. go through every id. there must exist one id where the property holds for multiple fs
     *
     */

        //(m1 . m2) . m3 = m1 . (m2.m3)
        public static boolean checkAssoc(Table table){

            for(int i = 0; i< Main.morphisms.size(); i++){
                for(int j = 0; j< Main.morphisms.size(); j++){
                    Morphisms m1 = Main.morphisms.get(i);
                    Morphisms m2 = Main.morphisms.get(j);
                    if( table.getMorphism(m1.name,m2.name) == null){
                        continue;
                    }

                    for(int k = 0; k< Main.morphisms.size(); k++){
                        Morphisms m3 = Main.morphisms.get(k);
                        if(table.getMorphism(m2.name,m3.name)==null){
                            continue;
                        }

                        Morphisms l1 = table.getMorphism(m1.name,m2.name);
                        Morphisms r1 = table.getMorphism(m2.name,m3.name);
                        if(table.getMorphism(l1.name, m3.name) == null || table.getMorphism(m1.name,r1.name) == null){
                            continue;
                        }

                        if(!table.getMorphism(l1.name, m3.name).equals(table.getMorphism(m1.name,r1.name))){
                            return false;
                        }
                    }
                }
            }



            return true;
        }

    public static boolean checkComp(Table table){
            for(int i = 0; i< Main.morphisms.size(); i++){
                for(int j = 0; j< Main.morphisms.size(); j++){
                    if(table.getMorphism(Main.morphisms.get(i).name, Main.morphisms.get(j).name) == null){
                        continue;
                    }

                    String A1 = Main.statesA.get(Main.morphisms.get(i).name);
                    String B1 = Main.stateB.get(Main.morphisms.get(i).name);
                    String A2 = Main.statesA.get(Main.morphisms.get(j).name);
                    String B2 = Main.stateB.get(Main.morphisms.get(j).name);

                    if(!A1.equals(A2) && !A2.equals(B2) && !A1.equals(B2) ){

                        boolean found = false;
                        innerLoop:
                        for(int k = 0; k< Main.morphisms.size(); k++){
                            Morphisms curr = Main.morphisms.get(k);
                            String aState = Main.statesA.get(curr.name);
                            String bstate = Main.stateB.get(curr.name);

                            if(aState.equals(A1) && bstate.equals(B2)){
                                found = true;
                                break innerLoop;
                            }




                        }
                        if(!found){
                            return false;
                        }





                    }
                }
            }




            return true;
        }

    /**
     *
     * @param tensortable
     * @return
     */
    public static boolean checkAssociativity(Table tensortable){
        for(int i = 0; i< Main.morphisms.size(); i++){
            for(int j = 0; j< Main.morphisms.size(); j++){
                for(int k = 0; k< Main.morphisms.size(); k++){
                    Morphisms a = Main.morphisms.get(i);
                    Morphisms b = Main.morphisms.get(j);
                    Morphisms c = Main.morphisms.get(k);

                    //Left side
                    Morphisms ab = Main.getTensor(a.name,b.name,tensortable);
                    Morphisms abc = Main.getTensor(ab.name,c.name,tensortable);

                    // Right side

                    Morphisms bc = Main.getTensor(b.name,c.name,tensortable);
                    Morphisms bca = Main.getTensor(a.name,bc.name,tensortable);

                    if (!abc.name.equals(bca.name)){
                        return false;
                    }








                }
            }
        }
        return true;
    }

    /**
     *Property To Check:
     * (k . h) * (g . f) == (k * g) . (h * f) -> if cod(h) = dom(k) then cod(f) = dom(g)
     *
     * if the morphism product of the category is "-" then we ignore and more on.
     *
     *
     * @param tensorTable a table representing the tensortable of the category
     * @param t a table representing the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean check2(Table tensorTable, Table t){
        for(int i = 0; i< Main.morphisms.size(); i++){
            for(int j = 0; j< Main.morphisms.size(); j++){
                Morphisms km = Main.morphisms.get(i);
                Morphisms hm = Main.morphisms.get(j);
                if(t.getMorphism(km.name,hm.name) == null){
                    continue;
                }

                for(int k = 0; k< Main.morphisms.size(); k++){
                    for(int l = 0; l< Main.morphisms.size(); l++){

                        Morphisms gm = Main.morphisms.get(k);
                        Morphisms fm = Main.morphisms.get(l);
                        if(t.getMorphism(gm.name,fm.name)== null){
                            continue;
                        }
                        Morphisms kh = t.getMorphism(km.name,hm.name);
                        Morphisms gf = t.getMorphism(gm.name,fm.name);
                        Morphisms left = tensorTable.getMorphism(kh.name, gf.name);


                        Morphisms kg = tensorTable.getMorphism(km.name,gm.name);
                        Morphisms hf = tensorTable.getMorphism(hm.name,fm.name);

                        Morphisms right = t.getMorphism(kg.name,hf.name);
                        if(right == null){
                            continue;
                        }


                        if(!left.name.equals(right.name)){
                            return false;
                        }

                    }
                }
            }
        }
        return true;
    }

    /**
     *Property to Check:
     * dom(f * g) == dom(f) * dom(g)
     *
     * Instead of using the objects, we can use the identity of the objects so that its morphism multiplication instead of object multiplication
     * dom(f) * dom(g) is object multiplication. use id(dom f) * id(dom g) instead
     *
     *
     * @param tensorTable a table representing the tensortable of the category
     * @param table a table representing the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean checkDomain(Table tensorTable, Table table){
        for(int i = 0; i< Main.morphisms.size(); i++){
            for(int j = 0; j< Main.morphisms.size(); j++){
                Morphisms f = Main.morphisms.get(i);
                Morphisms g = Main.morphisms.get(j);
                //recheck if we return the dom of the right side or not
                Morphisms lefttSide1 = tensorTable.getMorphism(f.name,g.name);
                State lefttSide = Main.getState(Main.statesA.get(tensorTable.getMorphism(f.name,g.name).name));
                State rightSide = (Main.getState(Main.statesA.get(tensorTable.getMorphism(f.stateA.getIdentityMorphism().name,g.stateA.getIdentityMorphism().name).name)));

                if(!lefttSide.name.equals(rightSide.name)){
                    return false;
                }
            }
        }




        return true;
    }

    /**
     * Property to check:
     *  codom(f * g) == codom(f) * codom(g)
     *  instead of using the objects, we can use the identity of the objects so that its morphism multiplication instead of object multiplication
     *  codom(f) * codom(g) is object multiplication. use id(codom f) * id(codom g) instead
     *
     *
     *
     * @param tensorTable a table representing the tensortable of the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean checkCodomain(Table tensorTable){
        for(int i = 0; i< Main.morphisms.size(); i++){
            for(int j = 0; j< Main.morphisms.size(); j++){
                Morphisms f = Main.morphisms.get(i);
                Morphisms g = Main.morphisms.get(j);
                //recheck if we return the dom of the right side or not
                State lefttSide = Main.getState(Main.stateB.get(tensorTable.getMorphism(f.name,g.name).name));
                State rightSide = Main.getState(Main.stateB.get(tensorTable.getMorphism(f.stateB.getIdentityMorphism().name,g.stateB.getIdentityMorphism().name).name));

                if(!lefttSide.name.equals(rightSide.name)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * Property to check:
     * id(A) * id(B) = id(A *B)
     *
     *
     * checks if the category satisfies the monoidal identity property
     * TODO check if the rhs means that the product of the lhs must just satisfy the identity properties of not(Current Undestanding)
     *
     *
     * Identity Morphism Properties:
     *     Maps an object onto itself
     *     each object can only have one
     *     left and right associative
     *
     * @param tensorTable a table representing the tensortable of the category
     * @return boolean if the category satisfies the property or not
     */
    public static boolean checkIndetitesMonoidal(Table tensorTable){
        for(int i = 0; i< Main.identityNames.size(); i++){
            for(int j = 0; j< Main.identityNames.size(); j++){
                System.out.println(Main.identityNames.get(j));

                Morphisms a = Main.morphismNames.get(Main.identityNames.get(i));
                Morphisms b = Main.morphismNames.get(Main.identityNames.get(j));
                System.out.println("MOr a : "+ a.name);
                System.out.println("Mor b : "+ b.name);


                Morphisms lhs = tensorTable.getMorphism(a.name,b.name);
                if(!Main.identityNames.contains(lhs.name)){
                    return false;
                }



            }
        }
        return true;
    }

    public static boolean checkUniqueIden(Table tensorTable){
        for(int i = 0; i< Main.identityNames.size(); i++){
            String currIden = Main.identityNames.get(i);
            boolean valid = true;
            for(int j = 0; j< Main.morphisms.size(); j++){

                String currMorphism = Main.morphisms.get(j).name;
                if(!tensorTable.getMorphism(currIden,currMorphism).name.equals(currMorphism)){
                    valid = false;
                }
                if(!tensorTable.getMorphism(currMorphism,currIden).name.equals(currMorphism)){
                    valid = false;
                }



            }
            if(valid){
                return true;
            }



        }
        return false;
    }
}
