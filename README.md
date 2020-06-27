# e-Bookshelf

During the Covid-19 pandemic I was asked to do a school project by my Information Technology teacher. I chose to do an electronic library to keep track of all the books I have.

### How does it work?

The software is written in Java and the graphic part is made using the Swing library.

---

In the main menu you can choose what action to perform:

 - Add a book
 - Remove a book
 - Search in the list
 - View the entire list

---

#### Adding a book

To add a book to your library, you need to insert the attributes of the book (title, author and genre).
Every time a book is added, the software assign it an ID.
***IT IS NOT POSSIBLE TO ADD A BOOK WITHOUT SOME OF THESE PARAMETERS.***

#### Removing a book

To remove a book from your library, you need to insert the ID of the book you want to remove. If the inserted ID is a valid one the book with the specified ID will be removed.

#### Search in the list

The search function need at least one parameter to search for one or more book.

**Example:** If I want to search a specified book I will insert its title, its author or its genre. If the parameter matches with a bunch of books, all of them will be displayed.

#### View the list

This will show you all of your books in a non-editable table.

---

### How is book information stored?

Once you add a book to the list, the software write the information of that book in a file at `resources/book.dat`.

Every book is represented by a line in the file.
The books information are separated with the pipe character `|`.
The parameter order is `ID|Title|Author|Genre`.

---

