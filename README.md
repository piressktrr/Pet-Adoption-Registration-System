## Pet Adoption Registration System
A CLI-based system for registering pets in a veterinary clinic so they can be adopted. Built purely in Java for learning purposes, inspired by the original project by Lucas Carrilho (https://github.com/karilho/desafioCadastro)

## Prerequisites

Java 17 or higher
IntelliJ IDEA (recommended, not required)

## Features

Register a new pet using a form read from a .txt file; 
List all registered pets; 
Search pets by multiple criteria (name, sex, age, weight, breed, address or animal type); 
Update a registered pet's data; 
Delete a registered pet; 
Data in a txt, working as a simple file-based database; 

## Setup

Fork this repository and clone it to your machine:

bashgit clone https://github.com/your-username/Pet-Adoption-Registration-System.git

Open the project in IntelliJ IDEA
Run the Main class

## How to Use
Once the program starts, the terminal will display a menu with 6 options:
1. Register a new pet
2. Update a registered pet's data 
3. Delete a registered pet 
4. List all registered pets 
5. Search pets by criteria 
6. Exit

Registering a pet: the terminal will prompt a series of questions loaded from formulario.txt, asking for the pet's name, type, sex, address, age, weight and breed. After submission, a .txt file is created inside the petsCadastrados/ directory.

Searching pets: the search always starts by animal type (CACHORRO or GATO). After the results are shown, you can optionally refine the search with a second criteria.

Updating and deleting: both operations show a numbered list of registered pets. You select the pet by its index and proceed.

## Project Structure
src/

├── Interface/       # CLI layer — user input, menus and display.

├── Service/         # Business rules and validations.

├── Repository/      # File I/O — reading and writing pet files.

├── models/          # Pet, PetDTO, PetDTOAtualizar, Endereco, enums.

├── exceptions/      # Custom exceptions.

└── Main.java
petsCadastrados/     # Generated at runtime — stores one .txt file per pet.

formulario.txt       # Questions displayed during pet registration.


## Business Rules

Name: first name and last name are required, only letters (A–Z, accented characters allowed), no numbers or special characters

Animal type: only CACHORRO or GATO (Dog/Cat)

Sex: MASCULINO, FEMININO, MACHO or FEMEA (Masculine/Feminine)

Age: maximum 20 years; values between 0.1 and 0.9 are interpreted as months and converted to years

Weight: between 0.5 kg and 60 kg

Breed: no numbers or special characters

Any field left blank (except name) is stored as NÃO INFORMADO (NOT_INFORMED) 
