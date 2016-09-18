import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class Initiate {

	public static void main(String[] args) {
		Initiate x = new Initiate();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame mainMenu = new JFrame();
		mainMenu.setSize(1200,720);
		mainMenu.setLocation(dim.width/2-1200/2, dim.height/2-720/2);
		mainMenu.setUndecorated(true);
		mainMenu.getContentPane().setBackground((new Color(51,155,253)));
		mainMenu.setLayout(null);
		mainMenu.setVisible(true);

		JFrame window = new JFrame();
		window.setSize(1200,720);
		window.setLocation(dim.width/2-1200/2, dim.height/2-720/2);
		window.setUndecorated(true);
		window.getContentPane().setBackground((new Color(51,155,253)));
		window.setLayout(null);
		window.setVisible(false);
		x.mainMenu(mainMenu, x, window);

	}

	public void mainMenu(final JFrame menuWindow, Initiate x, final JFrame window) {
		menuWindow.setVisible(true);
		menuWindow.getContentPane().removeAll();//Removes all components on this JFrame
		Font font = new Font("Courier New", Font.PLAIN, 30);
		Font titleFont = new Font("Verdana", Font.BOLD, 50);

		JLabel programTitle = new JLabel("Gilbert's Program");
		programTitle.setFont(titleFont);
		programTitle.setSize(550,300);
		programTitle.setLocation(350,50);
		programTitle.setVisible(true);

		JButton closeButton = new JButton("Close the Program");
		closeButton.setSize(200,20);
		closeButton.setLocation(500, 600);
		closeButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); //Terminates the program
			}
		});
		closeButton.setVisible(true);



		JButton ticTacToeButton = new JButton("Play");
		ticTacToeButton.setSize(200,20);
		ticTacToeButton.setLocation(500, 400);
		ticTacToeButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				window.getContentPane().removeAll();
				x.startGame(menuWindow, window, x);//Accesses the startGame method in the BoggleGame class
				menuWindow.setVisible(false);
			}
		});
		ticTacToeButton.setVisible(true);


		menuWindow.add(programTitle);
		menuWindow.add(ticTacToeButton);
		menuWindow.add(closeButton);
		menuWindow.setVisible(true);
		menuWindow.repaint();
	}//end of mainMenu method

	public void startGame (JFrame menuWindow, JFrame window, Initiate x) {
		window.setVisible(true);

		window.setLayout(null);
		JButton backButton = new JButton("Main Menu");
		backButton.setSize(100,20);
		backButton.setLocation(10,10);

		backButton.setVisible(true);
		window.add(backButton);
		String[][] board = {{" ", " ", " "},
				{" ", " ", " "},{" ", " ", " "}};
		String[] columnNames = {"1","2","3"};

		
		TableModel model = new DefaultTableModel(board, columnNames);
		JTable grid = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		backButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				x.clearAll(grid, window, x, menuWindow);
			}
		});
		TableColumnModel columnModel = grid.getColumnModel();
		grid.setSize(650,650);
		grid.setRowHeight(216);
		grid.setSelectionBackground(new Color(0,205,255));
		grid.setSelectionForeground(new Color(0,68,255));
		grid.setCellSelectionEnabled(true);
		grid.getTableHeader().setReorderingAllowed(false);
		grid.setShowGrid(true);
		grid.setFont(new Font("Verdana", Font.BOLD, 150));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );

		for (int i = 0; i < 3; i++){
			columnModel.getColumn(i).setPreferredWidth(100);
			columnModel.getColumn(i).setCellRenderer(centerRenderer);
		}
		grid.setBackground(new Color(51,155,253));
		grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		grid.doLayout();
		grid.setBorder(BorderFactory.createLineBorder(Color.black));
		grid.setGridColor(Color.black);
		grid.setLocation(275,35);
		grid.setVisible(true);
		Counter counter = new Counter();
		grid.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				counter.updateCount();

				int xCoord = grid.getSelectedRow();
				int yCoord = grid.getSelectedColumn();
				int[] compMove = new int[1];
				int xCoordComp = 0, yCoordComp = 0;
				if (grid.getValueAt(xCoord, yCoord) == " ") {

					grid.setValueAt("X",xCoord,yCoord);//If the current cell selected is no blank, we set it to blank

					if (x.win(grid, xCoord, yCoord, "X")){
						JFrame winWindow = new JFrame();
						JOptionPane.showMessageDialog(winWindow, ("You Won!"));
						x.clearAll(grid, window, x, menuWindow);
					}
					else {
						if(counter.turns != 5) {

							compMove = x.computerChoice(grid, xCoord, yCoord, xCoordComp, yCoordComp);
							xCoordComp = compMove[0];
							yCoordComp = compMove[1];
							grid.setValueAt("O",xCoordComp,yCoordComp);
							
							if (x.win(grid, xCoordComp, yCoordComp, "O")){
								JFrame winWindow = new JFrame();
								JOptionPane.showMessageDialog(winWindow, ("You Lost!"));
								x.clearAll(grid, window, x, menuWindow);
							}
						}
						
						else {
							JFrame winWindow = new JFrame();
							JOptionPane.showMessageDialog(winWindow, ("You Tied!"));
							x.clearAll(grid, window, x, menuWindow);
						}
					}
				}
			}
		});

		window.add(grid);


	}
	
	public void clearAll (JTable grid, JFrame window, Initiate x, JFrame menuWindow) {
		for (int i = 0; i < 3; i ++) {
			for (int j = 0; j < 3; j ++) {
				grid.setValueAt(" ", i, j);
			}
		}
		x.mainMenu(menuWindow, x, window);
	}

	public int[] computerChoice (final JTable grid, int xCoord, int yCoord, int xCoordComp, int yCoordComp) {
		
		//========================================================
		
				for (int x = 0; x <3; x++) {
					int check1 = 0;
					for (int y = 0; y < 3; y++) {
						if (grid.getValueAt(x, y) == "O"){

							check1++;

							if(check1 == 2) {
								for(int c = 0; c < 3; c++) {
									if (grid.getValueAt(x, c) == " ") {

										int[] computerMove = {x,c};
										return computerMove;
									}
								}

							}
						}
					}
				}

				for (int y = 0; y <3; y++) {
					int check1 = 0;
					for (int x = 0; x < 3; x++) {
						if (grid.getValueAt(x, y) == "O"){

							check1++;

							if(check1 == 2) {
								for(int c = 0; c < 3; c++) {
									if (grid.getValueAt(c, y) == " ") {

										int[] computerMove = {c,y};
										return computerMove;
									}
								}

							}
						}
					}
				}
				int check1 = 0;
				for (int y = 0; y <3; y++) {


					if (grid.getValueAt(y, y) == "O"){

						check1++;

						if(check1 == 2) {
							for(int c = 0; c < 3; c++) {
								if (grid.getValueAt(c, c) == " ") {

									int[] computerMove = {c,c};
									return computerMove;
								}
							}

						}

					}
				}
				check1 = 0;
				for (int y = 0; y <3; y++) {


					if (grid.getValueAt(y, 2-y) == "O"){

						check1++;

						if(check1 == 2) {
							for(int c = 0; c < 3; c++) {
								if (grid.getValueAt(c, 2-c) == " ") {

									int[] computerMove = {c,2-c};
									return computerMove;
								}
							}

						}

					}
				}
				//========================================================



		for (int x = 0; x <3; x++) {
			int check = 0;
			for (int y = 0; y < 3; y++) {
				if (grid.getValueAt(x, y) == "X"){

					check++;

					if(check == 2) {
						for(int c = 0; c < 3; c++) {
							if (grid.getValueAt(x, c) == " ") {

								int[] computerMove = {x,c};
								return computerMove;
							}
						}

					}
				}
			}
		}

		for (int y = 0; y <3; y++) {
			int check = 0;
			for (int x = 0; x < 3; x++) {
				if (grid.getValueAt(x, y) == "X"){

					check++;

					if(check == 2) {
						for(int c = 0; c < 3; c++) {
							if (grid.getValueAt(c, y) == " ") {

								int[] computerMove = {c,y};
								return computerMove;
							}
						}

					}
				}
			}
		}
		int check = 0;
		for (int y = 0; y <3; y++) {


			if (grid.getValueAt(y, y) == "X"){

				check++;

				if(check == 2) {
					for(int c = 0; c < 3; c++) {
						if (grid.getValueAt(c, c) == " ") {

							int[] computerMove = {c,c};
							return computerMove;
						}
					}

				}

			}
		}
		check = 0;
		for (int y = 0; y <3; y++) {


			if (grid.getValueAt(y, 2-y) == "X"){

				check++;

				if(check == 2) {
					for(int c = 0; c < 3; c++) {
						if (grid.getValueAt(c, 2-c) == " ") {

							int[] computerMove = {c,2-c};
							return computerMove;
						}
					}

				}

			}
		}
		
		
		Random rn = new Random();
		do {
			xCoordComp = rn.nextInt(3);
			yCoordComp = rn.nextInt(3);
			if (grid.getValueAt(xCoordComp, yCoordComp) == " "){
				break;
			}
		}while(true);

		int[] computerMove = {xCoordComp,yCoordComp};
		return computerMove;
	}

	public boolean win (final JTable grid, int xCoord, int yCoord, String turn) {
		if(xCoord == 0 && yCoord ==0) {
			if(grid.getValueAt(xCoord, yCoord+1).equals(turn) && grid.getValueAt(xCoord, yCoord+2).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord+1, yCoord).equals(turn) && grid.getValueAt(xCoord+2, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord+1, yCoord+1).equals(turn) && grid.getValueAt(xCoord+2, yCoord+2).equals(turn)){
				return true;
			}
		}

		if(xCoord == 1 && yCoord == 0) {

			if(grid.getValueAt(xCoord-1, yCoord).equals(turn) && grid.getValueAt(xCoord+1, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord, yCoord+1).equals(turn) && grid.getValueAt(xCoord, yCoord+2).equals(turn)){
				return true;
			}
		}

		if(xCoord == 2 && yCoord == 0) {
			if(grid.getValueAt(xCoord, yCoord+1).equals(turn) && grid.getValueAt(xCoord, yCoord+2).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord-1, yCoord).equals(turn) && grid.getValueAt(xCoord-2, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord-1, yCoord+1).equals(turn) && grid.getValueAt(xCoord-2, yCoord+2).equals(turn)){
				return true;
			}
		}

		if(xCoord == 0 && yCoord == 1) {
			if(grid.getValueAt(xCoord+1, yCoord).equals(turn) && grid.getValueAt(xCoord+2, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord, yCoord+1).equals(turn) && grid.getValueAt(xCoord, yCoord-1).equals(turn)){
				return true;
			}
		}

		if(xCoord == 1 && yCoord == 1) {
			if(grid.getValueAt(xCoord-1, yCoord).equals(turn) && grid.getValueAt(xCoord+1, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord, yCoord+1).equals(turn) && grid.getValueAt(xCoord, yCoord-1).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord+1, yCoord+1).equals(turn) && grid.getValueAt(xCoord-1, yCoord-1).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord+1, yCoord-1).equals(turn) && grid.getValueAt(xCoord-1, yCoord+1).equals(turn)){
				return true;
			}
		}

		if(xCoord == 2 && yCoord == 1) {
			if(grid.getValueAt(xCoord-1, yCoord).equals(turn) && grid.getValueAt(xCoord-2, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord, yCoord+1).equals(turn) && grid.getValueAt(xCoord, yCoord-1).equals(turn)){
				return true;
			}
		}

		if(xCoord == 0 && yCoord == 2) {
			if(grid.getValueAt(xCoord+1, yCoord).equals(turn) && grid.getValueAt(xCoord+2, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord, yCoord-1).equals(turn) && grid.getValueAt(xCoord, yCoord-2).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord+1, yCoord-1).equals(turn) && grid.getValueAt(xCoord+2, yCoord-2).equals(turn)){
				return true;
			}
		}
		if(xCoord == 1 && yCoord == 2) {
			if(grid.getValueAt(xCoord-1, yCoord).equals(turn) && grid.getValueAt(xCoord+1, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord, yCoord-1).equals(turn) && grid.getValueAt(xCoord, yCoord-2).equals(turn)){
				return true;
			}
		}
		if(xCoord == 2 && yCoord == 2) {
			if(grid.getValueAt(xCoord-1, yCoord).equals(turn) && grid.getValueAt(xCoord-2, yCoord).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord, yCoord-1).equals(turn) && grid.getValueAt(xCoord, yCoord-2).equals(turn)){
				return true;
			}
			if(grid.getValueAt(xCoord-1, yCoord-1).equals(turn) && grid.getValueAt(xCoord-2, yCoord-2).equals(turn)){
				return true;
			}
		}

		return false;
	}



}

class Counter {
	int turns;
	public void Counter() {
		turns = 0;
	}
	public void updateCount() {
		turns++;
	}
}
