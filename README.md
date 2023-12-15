# Tool To Explore Finite Monoidal Categories
## About
Category theory is the general theory of mathematical structures. Monoidal
categories is a subsection of category theory which generalises finite monoids. While
monoidal categories are more generally represented as a collection of objects, morphisms and a tensor product, if we regard them as a combinatorial object, they can be
represented purely in terms of a multiplication table. This tool's function is to validate whether a given multiplication table is a valid monoidal category or not. 

## Technology Used
  - Java
  - Java Swing

## How It Works
The tool first queries the user for the size
of the table before displaying 2 tables for the user to fill with the categories multiplication table and the tensor product table. The tool then checks if the category satisfies the category properties and monoidal properties before displaying the results.

## Future Work  
  - **User testing and evaluation:** Not much user testing was carried out due to time constraints.
A more thorough evaluation with looser time constraints would result in a more
user-friendly product.
  - **Updating the User Interface to React.JS:** Integrating React.JS as the user interface instead of Java Swing would allow the tool to be used as a web app and have a more responsive, interactive and aesthetically pleasing interface.
  - **Extending the project to accept and validate finite 2-Categories:** A finite 2-category is a category enriched over the category of categories, meaning that
for every pair of objects in the category, there is a category of morphisms between them. Besides the composition of morphisms, the finite 2-category has
the horizontal composition property which allows 2 parallel morphisms to be
composed, subject to certain coherence conditions such as the interchange law.
finite 2-Categories can be thought of as a more structured finite monoidal category hence it would make sense to extend the tool to accept and validate these categories also
  - **Extend the project to check whether two elements of a given multiplication
table are adjoint:** The project can be extended to check if 2 multiplication tables
are adjoint by checking if there exist 2 morphisms that satisfy the adjointness
properties. This could be done by implementing an algorithm or by using category-theoretic methods such as the Hom-Tensor adjunction theorem.
-  **Extending the project to complete partially filled tables:** The project can be extended to check if a partially filled table can be filled to be a valid category and if so, should be able to display the filled table.

## Diagrams
<img  src="./Screenshot 2023-12-13 215432.jpg"/>
The GUI queries the user for the size of the tables

<img src="./Screenshot 2023-12-13 215458.jpg"/>
The GUI displays 2 tables for the user to fill up. The top table is to be filled up with the category multiplication table and the bottom table is to be filled up with the tensor product table.

<img src="./Screenshot 2023-12-13 215525.jpg" />
The tool takes the tables as input and validates them based on the category properties and monoidal properties. It then outputs the results below.
