
import java.util.ArrayList;
import java.util.List;

import SimpleEdition.TCPClient;
import SimpleEdition.StreamReader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class GUI extends Application {
	public static final int size = 20; 
	public static final int scene_height = size * 20 + 100;
	public static final int scene_width = size * 20 + 200;

	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right,hero_left,hero_up,hero_down;


	public static Player me;
	//public static Player lars;
	public static Player steffen;
	public static Player line;

	public static List<Player> players = new ArrayList<Player>();

	private Label[][] fields;
	private TextArea scoreList;

	private TCPClient tcpClient = new TCPClient("10.10.139.198", 6666,"Steffen"); //Steffen
	//private TCPClient tcpClient = new TCPClient("10.10.138.207", 6666); //Lars
//	private TCPClient tcpClient = new TCPClient("10.10.131.77", 6666); //Line
	//private TCPClient tcpClient = new TCPClient("10.10.138.206", 6666, "Line"); //Local

	private JavaFxReadThread javaFxReadThread;

	private  String[] board = {    // 20x20
			"wwwwwwwwwwwwwwwwwwww",
			"w        ww        w",
			"w w  w  www w  w  ww",
			"w w  w   ww w  w  ww",
			"w  w               w",
			"w w w w w w w  w  ww",
			"w w     www w  w  ww",
			"w w     w w w  w  ww",
			"w   w w  w  w  w   w",
			"w     w  w  w  w   w",
			"w ww ww        w  ww",
			"w  w w    w    w  ww",
			"w        ww w  w  ww",
			"w         w w  w  ww",
			"w        w     w  ww",
			"w  w              ww",
			"w  w www  w w  ww ww",
			"w w      ww w     ww",
			"w   w   ww  w      w",
			"wwwwwwwwwwwwwwwwwwww"
	};

	
	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {

		javaFxReadThread = new JavaFxReadThread(tcpClient.getClientReadThread());
		javaFxReadThread.start();

		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	
			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();
			
			GridPane boardGrid = new GridPane();

			image_wall  = new Image(getClass().getResourceAsStream("Image/wall4.png"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("Image/heroRight.png"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("Image/heroLeft.png"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("Image/heroUp.png"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("Image/heroDown.png"),size,size,false,false);

			fields = new Label[20][20];
			for (int j=0; j<20; j++) {
				for (int i=0; i<20; i++) {
					switch (board[j].charAt(i)) {
					case 'w':
						fields[i][j] = new Label("", new ImageView(image_wall));
						break;
					case ' ':					
						fields[i][j] = new Label("", new ImageView(image_floor));
						break;
					default: throw new Exception("Illegal field value: "+board[j].charAt(i) );
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);
			
			grid.add(mazeLabel,  0, 0);
			grid.add(scoreLabel, 1, 0); 
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);
						
			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				String msg="Move,";
				switch (event.getCode()) {
				case UP:
					msg+="0,-1,up,"+ tcpClient.getPlayerName();
					playerColisonDetection(0,-1);
					break;
				case DOWN:
					msg+="0,1,down,"+ tcpClient.getPlayerName();
					playerColisonDetection(0,1);
					break;
				case LEFT:
					msg+="-1,0,left,"+ tcpClient.getPlayerName();
					playerColisonDetection(-1,0);
					break;
				case RIGHT:
					msg+="1,0,right,"+ tcpClient.getPlayerName();
					playerColisonDetection(1,0);
					break;
				default:
					break;
				}
				if (msg != null) {
					tcpClient.write(msg);
				}
			});
            // Setting up standard players
			
			/*lars = new Player("Lars",9,4,"up");
			players.add(lars);
			fields[9][4].setGraphic(new ImageView(hero_up));*/

			steffen = new Player("Steffen",14,16,"up");
			players.add(steffen);
			fields[14][16].setGraphic(new ImageView(hero_up));

			line = new Player("Line",13,15,"up");
			players.add(line);
			fields[13][15].setGraphic(new ImageView(hero_up));

//			me=lars;
			me=steffen;
			//me=line;

			scoreList.setText(getScoreList());
		} catch(Exception e) {
			e.printStackTrace();
		}

		javaFxReadThread = new JavaFxReadThread(tcpClient.getClientReadThread());
		javaFxReadThread.start();
	}

	public void updatePoint(int point,Player player) {
		player.addPoints(point);
		scoreList.setText(getScoreList());
	}


	public void playerColisonDetection(int delta_x, int delta_y) {
		Player player = me;
		int x = player.getXpos(),y = player.getYpos();
		if (board[y+delta_y].charAt(x+delta_x)=='w') {
			tcpClient.write("Point,-1," + player.name);
		} else {
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
				tcpClient.write("Point,10," + player.name);
				tcpClient.write("Point,-10," + p.name);
			} else {
				tcpClient.write("Point,1," + player.name);
			}
		}
	}

	public void playerMoved(int delta_x, int delta_y, String direction, Player player) {
		player.direction = direction;
		int x = player.getXpos(),y = player.getYpos();

		if (board[y+delta_y].charAt(x+delta_x)=='w') {
			//Do nothing
		}

		else {
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
//				Do nothing
			} else {
				fields[x][y].setGraphic(new ImageView(image_floor));
				x+=delta_x;
				y+=delta_y;

				if (direction.equals("right")) {
					fields[x][y].setGraphic(new ImageView(hero_right));
				};
				if (direction.equals("left")) {
					fields[x][y].setGraphic(new ImageView(hero_left));
				};
				if (direction.equals("up")) {
					fields[x][y].setGraphic(new ImageView(hero_up));
				};
				if (direction.equals("down")) {
					fields[x][y].setGraphic(new ImageView(hero_down));
				};

				player.setXpos(x);
				player.setYpos(y);
			}
		}
	}


	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : players) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}

	public Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}

	public class JavaFxReadThread extends Thread {
		private final StreamReader streamReader;

		public JavaFxReadThread(StreamReader streamReader) {
			this.streamReader = streamReader;
		}

		@Override
		public void run() {
			while (true) {
				String msg = this.streamReader.read();

				Player currentPlayer = null;
				if (msg != null) {


					String[] splitted = msg.split(",");

					if (splitted[0].equals("Move") && splitted.length == 5) {
						/*if (splitted[4].equals("Lars")) {
							currentPlayer = lars;
						} else*/ if (splitted[4].equals("Line")) {
							currentPlayer = line;
						}else if (splitted[4].equals("Steffen")) {
							currentPlayer = steffen;
						}

						Player finalCurrentPlayer = currentPlayer;
						Platform.runLater(() -> {
							playerMoved(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), splitted[3], finalCurrentPlayer);
						});

					} else if (splitted[0].equals("Point")) {
						/*if (splitted[2].equals("Lars")) {
							currentPlayer = lars;
						} else*/ if (splitted[2].equals("Line")) {
							currentPlayer = line;
						}else if (splitted[2].equals("Steffen")) {
							currentPlayer = steffen;
						}
						Player finalCurrentPlayer = currentPlayer;
						Platform.runLater(() -> {
							updatePoint(Integer.parseInt(splitted[1]), finalCurrentPlayer);
						});
					}
				}
			}
		}
	}
}

