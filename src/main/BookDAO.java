package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BookDAO {
	private final static String PATH = "resources/books.dat";
	
	
	// Read the file and return a matrix containing the data in the file.
	public static String[][] readMatrix(){
		Scanner scan = null;
		int cont = 0;
		
		try {
			scan = new Scanner(new File(PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(scan.hasNextLine()) {
			cont++;
			scan.nextLine();
		}
		scan.close();
		
		String[][] mat = new String[cont][4];
		
		try {
			scan = new Scanner(new File(PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int i = 0;
		while(scan.hasNextLine()) {
			String[] txt = scan.nextLine().split("\\|");
			mat[i][0] = txt[0];
			mat[i][1] = txt[1];
			mat[i][2] = txt[2];
			mat[i][3] = txt[3];
			i++;
		}
		scan.close();
		
		return mat;
	}
	
	// Read the file and return an ArrayList containing Book objects.
	public static ArrayList<Book> readAll() {
		Scanner scan = null;
		ArrayList<Book> books = new ArrayList<Book>();
		
		try {
			scan = new Scanner(new File(PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(scan.hasNextLine()) {
			String[] txt = scan.nextLine().split("\\|");
			books.add(new Book(txt[0], txt[1], txt[2], txt[3]));
		}
		scan.close();
		return books;
	}
	
	// Write a book to the file.
	public static void addNewBook(Book b) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(PATH), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fw.write(b.toString() + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Clear the file and add all the books from the ArrayList.
	public static void updateAllBooks(ArrayList<Book> books) {
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Book b : books) {
			try {
				fw.write(b.toString() + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Return the amount of books in the file.
	public static int getSize() {
		return readMatrix().length;
	}
}
