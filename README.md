# Tool to Explore Finite Monoidal Categories

## Overview

This repository contains a tool designed to validate the multiplication tables of **strict finite monoidal categories**. The tool offers a graphical user interface (GUI) for users to input multiplication tables, extract necessary information, validate category properties, and check monoidal properties.

This project was developed to address the lack of existing tools in this niche area of research and has applications in mathematics, cryptography, and programming language design.

---

## Features

- **Validation of Multiplication Tables**:
  - Checks if a given multiplication table represents a valid strict monoidal category.
  - Validates both category and monoidal properties.
- **User-Friendly GUI**:
  - Allows users to input multiplication tables easily.
  - Displays validation results interactively.
- **Extensive Testing**:
  - Robust error handling and clear feedback for invalid inputs.
- **Built from Scratch**:
  - Designed and implemented without relying on prior tools, ensuring full control over functionality.

---

## Methodology

### Category Validation
1. **Input**: Multiplication tables for the category and tensor product in CSV format.
2. **Validation Steps**:
   - **Identity Morphisms**: Ensures each object has a unique identity morphism.
   - **Composition**: Checks that compositions of morphisms are valid.
   - **Associativity**: Verifies that morphism compositions satisfy associativity.

### Monoidal Property Validation

The tool checks six key properties:

1. **Domain Property**: $ \text{dom}(f \otimes g) = \text{dom}(f) \otimes \text{dom}(g) $
2. **Codomain Property**: $ \text{cod}(f \otimes g) = \text{cod}(f) \otimes \text{cod}(g) $
3. **Associativity**: $ (f \otimes g) \otimes h = f \otimes (g \otimes h) $
4. **Distributivity of Tensor over Composition**:
   $ (k \cdot h) \otimes (g \cdot f) = (k \otimes g) \cdot (h \otimes f) $
5. **Tensor of Identities**: $ \text{id}(A) \otimes \text{id}(B) = \text{id}(A \otimes B) $
6. **Identity Property**: $ \exists I: \text{id}_I \otimes \text{id}_A = \text{id}_A = \text{id}_A \otimes \text{id}_I $


### Implementation
- **Programming Language**: Java, chosen for its object-oriented capabilities and extensive libraries.
- **GUI Framework**: Java Swing, providing an intuitive interface for input and output.
- **Code Architecture**: 
  - `Main` class: Entry point for the tool.
  - `Table`, `State`, `Morphisms`: Represent key components of the category.
  - `Validator`: Contains methods for validating category and monoidal properties.
  - `CategoryValidatorGUI`: Manages the graphical user interface.

---

## Results

- Successfully validates strict monoidal categories and provides detailed feedback for invalid inputs.
- User-friendly interface that simplifies interaction with complex mathematical concepts.
- Robust performance with error handling and clear diagnostics.

---

## Installation and Usage

### Prerequisites
- Java Development Kit (JDK) installed on your system.

### Steps
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/monoidal-category-tool.git
2. Compile and run the tool:
   javac Main.java
   java Main
3. Input the size of the multiplication tables and fill in the values via the GUI.
4. Click "Validate" to check the category and monoidal properties.

---

### Screenshots
<img  src="./Screenshot 2023-12-13 215432.jpg"/>
The GUI queries the user for the size of the tables

<img src="./Screenshot 2023-12-13 215458.jpg"/>
The GUI displays 2 tables for the user to fill up. The top table is to be filled up with the category multiplication table and the bottom table is to be filled up with the tensor product table.

<img src="./Screenshot 2023-12-13 215525.jpg" />
The tool takes the tables as input and validates them based on the category properties and monoidal properties. It then outputs the results below.

---

### Future Work
- Extend functionality to handle free monoidal categories.
- Enhance the GUI with additional features like file import/export.
- Optimize validation algorithms for larger categories.

---

### Contributors
- Pradnesh Sanderan

---

### References
- "Category Theory for Computer Science" by Barr and Wells.
- "Category Theory for Programmers" by Bartosz Milewski.
- "An Invitation to Applied Category Theory" by Fong and Spivak.
- "DisCoPy: Monoidal Categories in Python" by Giovanni de Felice et al.
