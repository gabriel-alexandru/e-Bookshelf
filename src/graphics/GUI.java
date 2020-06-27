package graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import main.Book;
import main.BookDAO;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

public class GUI {

	private JFrame frame;
	private JLayeredPane layeredPane;
	private JButton addMenu, removeMenu, 
					searchMenu, viewMenu, 
					backButton, addButton, 
					removeButton, searchButton;
	private JPanel mainPanel, addPanel,
					removePanel, searchPanel,
					viewPanel;
	private JLabel authorLabel, titleLabel, 
					genreLabel, removeLabel, searchLabel;
	private JTextField authorField, titleField, 
						genreField, removeField, 
						searchField;
	private JTable bookList;
	private JScrollPane scroll;
	private GridBagLayout layout;

	public GUI() {
		int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame = new JFrame("e-Bookshelf");
		frame.setBounds((windowWidth / 2) - 125, (windowHeight / 2) - 300, 250, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int width = frame.getSize().width;
		int height = frame.getSize().height;
		int textFieldDim = 15;
		frame.setResizable(false);
		
		// Setup the layeredPane and add it to the frame.
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, width, height);
		frame.getContentPane().add(layeredPane);
		layout = new GridBagLayout();
		setLayout();
		
		// Setup the mainPanel and add it to the layeredPane.
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBounds(0, 0, width, height);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Main menu"));
		layeredPane.add(mainPanel);
		
		// Setup the TextField and the Label for the add form.
		authorLabel = new JLabel("Author");
		authorField = new JTextField(textFieldDim);
		titleLabel = new JLabel("Title");
		titleField = new JTextField(textFieldDim);
		genreLabel = new JLabel("Genre");
		genreField = new JTextField(textFieldDim);
		
		// Setup the TextField and the Label for the remove form.
		removeLabel = new JLabel("ID to remove");
		removeField = new JTextField(textFieldDim);
		
		// Setup the TextField and the Label for the search form.
		searchLabel = new JLabel("Insert the exact title, author or genre");
		searchField = new JTextField(textFieldDim);
		
		//Setup the column names and the values for the Table.
		String[] columnNames = {"ID", "Title", "Author", "Genre"};
		String[][] data = BookDAO.readMatrix();
		bookList = new JTable(data, columnNames);
		
		
		// Setup the add button in the menu and add it to the main panel. 
		addMenu = new JButton("Add a book");
		addMenu.setPreferredSize(new Dimension(50, 15));
		addMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButtonFromPanel(backButton);
				GridBagConstraints backButtonConts = new GridBagConstraints();
				backButtonConts.fill = GridBagConstraints.HORIZONTAL;
				backButtonConts.insets = new Insets(0, 0, 0, 5);
				backButtonConts.gridx = 3;
				backButtonConts.gridy = 5;
				addPanel.add(backButton, backButtonConts);
				addPanel.setSize(250, 200);
				frame.setSize(250, 200);
				switchPanel(addPanel);
			}
		});
		addToMainPanel(addMenu);
		
		// Setup the remove button in the menu and add it to the main panel. 
		removeMenu = new JButton("Remove a book");
		removeMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButtonFromPanel(backButton);
				removePanel.setSize(250, 150);
				frame.setSize(250, 150);
				
				GridBagConstraints backButtonConst = new GridBagConstraints();
				backButtonConst.insets = new Insets(0, 0, 5, 5);
				backButtonConst.gridx = 3;
				backButtonConst.gridy = 2;
				removePanel.add(backButton, backButtonConst);
				switchPanel(removePanel);
			}
		});
		addToMainPanel(removeMenu);
		
		// Setup the search button in the menu and add it to the main panel. 
		searchMenu = new JButton("Search a book");
		searchMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButtonFromPanel(backButton);
				searchPanel.setSize(300, 150);
				frame.setSize(300, 150);
				searchPanel.add(backButton);
				switchPanel(searchPanel);
			}
		});
		addToMainPanel(searchMenu);
		
		// Setup the view button in the menu and add it to the main panel. 
		viewMenu = new JButton("View books");
		viewMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButtonFromPanel(backButton);
				updateBookTable(BookDAO.readMatrix(), columnNames);
				viewPanel.setSize(600, 600);
				frame.setSize(600, 600);
				viewPanel.add(backButton);
				switchPanel(viewPanel);
			}
		});
		addToMainPanel(viewMenu);
		
		// Setup the back button available in every panel other than the main panel. 
		backButton = new JButton("Back");
		backButton.setLocation(50, height - 50);
		backButton.setVisible(true);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButtonFromPanel(backButton);
				switchPanel(mainPanel);
				frame.setSize(250, 200);
				clearField();
			}
		});
		
		// SETUP the add panel. Implements the actionListener for the button
		// and does the "add" algorithm.
		addPanel = new JPanel();
		
		addButton = new JButton("Add book");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// The algorithm add a book with the given title, author and genre.
				// Then calculates the id for that book and add it to the booklist.
				
				String author = authorField.getText();
				String title = titleField.getText();
				String genre = genreField.getText();
				String id = "" + (BookDAO.getSize() + 1);
				boolean isIn = false;
				
				// Check if the book to add is already in the list.
				for(Book b : BookDAO.readAll()) {
					if(author.toLowerCase().equals(b.getAuthor().toLowerCase()) && 
							title.toLowerCase().equals(b.getTitle().toLowerCase()) && 
							genre.toLowerCase().equals(b.getGenre().toLowerCase())) {
						isIn = true;
						break;
					}
				}
				
				// If it is not already in, then add it and show the user a message.
				if(author.length() != 0 && title.length() != 0 && genre.length() != 0 && !isIn) {
					BookDAO.addNewBook(new Book(id, title, author, genre));
					
					JFrame message = new JFrame("Message!");
					JPanel messPanel = new JPanel();
					JLabel messLabel = new JLabel("Book added correctly");
					JButton messButton = new JButton("OK");
					messButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							message.setVisible(false);
						}
					});
					messPanel.add(messLabel);
					messPanel.add(messButton);
					message.add(messPanel);
					
					message.setResizable(false);
					message.setVisible(true);
					message.setSize(250, 75);
					message.setLocation((windowWidth / 2) - 125, (windowHeight / 2) - 300);
					message.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					// If it is already in, then do not add it and show the user a message.
				} else if(isIn){
					JFrame message = new JFrame("Message!");
					JPanel messPanel = new JPanel();
					JLabel messLabel = new JLabel("Book is already in");
					JButton messButton = new JButton("OK");
					messButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							message.setVisible(false);
						}
					});
					messPanel.add(messLabel);
					messPanel.add(messButton);
					message.add(messPanel);
					
					message.setResizable(false);
					message.setVisible(true);
					message.setSize(250, 75);
					message.setLocation((windowWidth / 2) - 125, (windowHeight / 2) - 300);
					message.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					// If some fields are empty show the user a message.
				} else {
					JFrame message = new JFrame("Message!");
					JPanel messPanel = new JPanel();
					JLabel messLabel = new JLabel("Please insert all the fields!");
					JButton messButton = new JButton("OK");
					messButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							message.setVisible(false);
						}
					});
					messPanel.add(messLabel);
					messPanel.add(messButton);
					message.add(messPanel);
					
					message.setResizable(false);
					message.setVisible(true);
					message.setSize(250, 75);
					message.setLocation((windowWidth / 2) - 125, (windowHeight / 2) - 300);
					message.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}
				
				updateBookTable(BookDAO.readMatrix(), columnNames);
				
				clearField();
			}
		});
		addPanel.setBorder(BorderFactory.createTitledBorder("Add a book to the list"));
		addPanel.setLayout(layout);
		addToAddPanel();
		
		
		// SETUP the remove panel. Implements the actionListener for the button 
		// and does the "remove" algorithm.  
		
		removePanel = new JPanel();
		
		removeButton = new JButton("Remove book");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// The algorithm removes the book with the given id.
				// Then updates the file.
				int id = 0;
				try {
					id = Integer.valueOf(removeField.getText());
				} catch(NumberFormatException ex) {
					
				}
				ArrayList<Book> books = BookDAO.readAll();
				
				// Remove the book matching the id inserted by the user.
				if(id > 0 && id <= books.size()) {
					
					for(Book b : books) {
						if(b.getId() == id) {
							books.remove(b);
							break;
						}
					}
					
					// Repair the "hole" made by removing a book from the list.
					for(int i = 0; i < books.size(); i++) {
						int id_ = Integer.valueOf(books.get(i).getId());
						if(id_ != i+1) {
							books.get(i).setId(i+1);
						}
					}
					
					BookDAO.updateAllBooks(books);
					
					// Show the user a message with the result.
					JFrame message = new JFrame("Message!");
					JPanel messPanel = new JPanel();
					JLabel messLabel = new JLabel("Book removed correctly");
					JButton messButton = new JButton("OK");
					messButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							message.setVisible(false);
						}
					});
					messPanel.add(messLabel);
					messPanel.add(messButton);
					message.add(messPanel);
					
					message.setResizable(false);
					message.setVisible(true);
					message.setSize(250, 75);
					message.setLocation((windowWidth / 2) - 125, (windowHeight / 2) - 300);
					message.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					
					// If the id inserted is not a valid id then show the user a messge.
				} else {
					JFrame message = new JFrame("Message!");
					JPanel messPanel = new JPanel();
					JLabel messLabel = new JLabel("No such ID!");
					JButton messButton = new JButton("OK");
					messButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							message.setVisible(false);
						}
					});
					messPanel.add(messLabel);
					messPanel.add(messButton);
					message.add(messPanel);
					
					message.setResizable(false);
					message.setVisible(true);
					message.setSize(250, 75);
					message.setLocation((windowWidth / 2) - 125, (windowHeight / 2) - 300);
					message.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}
				
				updateBookTable(BookDAO.readMatrix(), columnNames);
				clearField();
			}
		});
		removePanel.setBorder(BorderFactory.createTitledBorder("Remove a book from the list"));
		removePanel.setLayout(layout);
		addToRemovePanel();
		
		
		// Setup the search panel. Implements the actionListener for the button
		// and execute the search algorithm.
		searchPanel = new JPanel();
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Book> books, result;
				result = new ArrayList<Book>();
				books = BookDAO.readAll();
				String txt = searchField.getText().toLowerCase();
				
				if(txt.length() >0) {
				
					for(Book b : books) {
						if(txt.equals(b.getAuthor().toLowerCase()) 
								|| txt.equals(b.getGenre().toLowerCase())
								|| txt.equals(b.getTitle().toLowerCase())) {
							result.add(b);
						}
					}
					String[][] mat = new String[result.size()][4];
					for(int i = 0; i < result.size(); i++) {
						Book b = result.get(i);
						mat[i][0] = "" + b.getId();
						mat[i][1] = b.getTitle();
						mat[i][2] = b.getAuthor();
						mat[i][3] = b.getGenre();
					}
					
					viewPanel.add(backButton);
					updateBookTable(mat, columnNames);
					viewPanel.setSize(600, 600);
					frame.setSize(600, 600);
					switchPanel(viewPanel);
				}
				clearField();
			}
		});
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search a book in the list"));
		addToSearchPanel();

		// Setup the ScrollPane for the view panel and add the table to it.
		scroll = new JScrollPane(bookList);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(595, 500));
	    
		viewPanel = new JPanel();
		viewPanel.setLayout(new FlowLayout());
		viewPanel.setBorder(BorderFactory.createTitledBorder("View books in the list"));
		addToViewPanel();
		
		frame.setVisible(true);
	}
	
	private void switchPanel(JPanel panel) {
		layeredPane.removeAll();
		layeredPane.add(panel);
		layeredPane.repaint();
		layeredPane.revalidate();
	}
	
	private void removeButtonFromPanel(JButton button) {
		addPanel.remove(button);
		removePanel.remove(button);
		searchPanel.remove(button);
		viewPanel.remove(button);
	}
	
	// Setup the Constraints for the layout and add objects to the add panel.
	private void addToAddPanel() {
		GridBagConstraints authorLabelConst = new GridBagConstraints();
		authorLabelConst.insets = new Insets(0, 0, 5, 5);
		authorLabelConst.gridx = 1;
		authorLabelConst.gridy = 1;
		addPanel.add(authorLabel, authorLabelConst);
		
		GridBagConstraints authorFieldConst = new GridBagConstraints();
		authorFieldConst.fill = GridBagConstraints.HORIZONTAL;
		authorFieldConst.insets = new Insets(0, 0, 5, 5);
		authorFieldConst.gridx = 3;
		authorFieldConst.gridy = 1;
		addPanel.add(authorField, authorFieldConst);
		
		GridBagConstraints titleLabelConst = new GridBagConstraints();
		titleLabelConst.insets = new Insets(0, 0, 5, 5);
		titleLabelConst.gridx = 1;
		titleLabelConst.gridy = 2;
		addPanel.add(titleLabel, titleLabelConst);
		
		GridBagConstraints titleFieldConst = new GridBagConstraints();
		titleFieldConst.fill = GridBagConstraints.HORIZONTAL;
		titleFieldConst.insets = new Insets(0, 0, 5, 5);
		titleFieldConst.gridx = 3;
		titleFieldConst.gridy = 2;
		addPanel.add(titleField, titleFieldConst);
		
		GridBagConstraints genreLabelConst = new GridBagConstraints();
		genreLabelConst.insets = new Insets(0, 0, 5, 5);
		genreLabelConst.gridx = 1;
		genreLabelConst.gridy = 3;
		addPanel.add(genreLabel, genreLabelConst);
		
		GridBagConstraints genreFieldConst = new GridBagConstraints();
		genreFieldConst.fill = GridBagConstraints.HORIZONTAL;
		genreFieldConst.insets = new Insets(0, 0, 5, 5);
		genreFieldConst.gridx = 3;
		genreFieldConst.gridy = 3;
		addPanel.add(genreField, genreFieldConst);
		
		GridBagConstraints addButtonConst = new GridBagConstraints();
		addButtonConst.fill = GridBagConstraints.HORIZONTAL;
		addButtonConst.insets = new Insets(0, 0, 0, 5);
		addButtonConst.gridx = 1;
		addButtonConst.gridy = 5;
		addPanel.add(addButton, addButtonConst);
	}
	
	// Setup the Constraints for the layout and add objects to the remove panel.
	private void addToRemovePanel() {
		GridBagConstraints removeLabelConst = new GridBagConstraints();
		removeLabelConst.insets = new Insets(0, 0, 5, 5);
		removeLabelConst.gridx = 1;
		removeLabelConst.gridy = 1;
		removePanel.add(removeLabel, removeLabelConst);
		
		GridBagConstraints removeFieldConst = new GridBagConstraints();
		removeFieldConst.fill = GridBagConstraints.HORIZONTAL;
		removeFieldConst.insets = new Insets(0, 0, 5, 5);
		removeFieldConst.gridx = 3;
		removeFieldConst.gridy = 1;
		removePanel.add(removeField, removeFieldConst);
		
		GridBagConstraints removeButtonConst = new GridBagConstraints();
		removeButtonConst.insets = new Insets(0, 0, 5, 5);
		removeButtonConst.gridx = 1;
		removeButtonConst.gridy = 2;
		removePanel.add(removeButton, removeButtonConst);
	}
	
	// Add objects to the search panel.
	private void addToSearchPanel() {
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
	}
	
	// Add objects to the view panel.
	private void addToViewPanel() {
		viewPanel.add(scroll);
	}
	
	// Add objects to the main panel and setup a rigid area to separate the objects.
	private void addToMainPanel(JButton b) {
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPanel.add(b);
	}
	
	// Update the table and set the cell not editable. 
	private void updateBookTable(String[][] data, String[] column) {
		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel(data, column) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		bookList.setModel(model);
	}
	
	// Clear the TextField
	private void clearField() {
		authorField.setText("");
		titleField.setText("");
		genreField.setText("");
		removeField.setText("");
		searchField.setText("");
	}
	
	// Setup the columns and rows value for the GridBagLayout.
	private void setLayout() {
		layout.columnWidths = new int[] {15, 30, 0, 30, 0, 30, 15};
		layout.rowHeights = new int[] {0, 0, 30, 0, 15};
		layout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
	}

}
