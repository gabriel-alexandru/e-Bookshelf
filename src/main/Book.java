package main;

public class Book {

	private String title, author, genre;
	private int id;
	
	public Book(String id, String title, String author, String genre) {
		super();
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.id = Integer.valueOf(id);
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getGenre() {
		return genre;
	}
	
	@Override
	public String toString() {
		return "" + id + "|" + title + "|" + author + "|" + genre;
	}
}
