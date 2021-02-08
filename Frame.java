import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

public class Frame extends JFrame {

	private JPanel contentPane;
	private JTextField x;
	private JTextField y;
	private TileMap tileMap;
	private Map<Point, Integer> map;

	private final Point startPoint = new Point(1, 1);
	private final int WALL = 0;
	private final int ROAD = 1;
	private final int START = 2;
	private final int END = 3;
	private final int STEP = 4;

	private Frame thiss = this;
	private int mapWidth;
	private int mapHeight;

	/**
	 * Create the frame.
	 */
	public Frame() {
		ReadMap.openFile("src//map.txt");
		ReadMap.readMap();
		ReadMap.closeFile();
		map = new HashMap<Point, Integer>(ReadMap.getRecordMap());

		mapWidth = ReadMap.getMapWidth();
		mapHeight = ReadMap.getMapHeight();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 419, 316);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);

		JLabel lblxY = new JLabel("\u9078\u64C7\u8D77\u9EDE(x, y): ");
		panel.add(lblxY);

		x = new JTextField();
		x.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(x);
		x.setColumns(2);

		y = new JTextField();
		y.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(y);
		y.setColumns(2);

		JButton btnRun = new JButton("run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int pointX = Integer.parseInt(x.getText());
					int pointY = Integer.parseInt(y.getText());
					if (!startPoint.equals(new Point(pointX, pointY)) && pointX >= 1 && pointX <= mapWidth
							&& pointY >= 1 && pointY <= mapHeight) {
						setMap(new Point(Integer.parseInt(x.getText()), Integer.parseInt(y.getText())));
						backtracking();
					}else {
						JOptionPane.showMessageDialog(thiss, "請輸入有效座標");
						x.requestFocus();
						x.setText("");
						y.setText("");
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(thiss, "請輸入正整數");
					x.requestFocus();
					x.setText("");
					y.setText("");
					e.printStackTrace();
				}
			}
		});
		contentPane.add(btnRun, BorderLayout.SOUTH);

		tileMap = new TileMap(map, ReadMap.getMapWidth(), ReadMap.getMapHeight());
		contentPane.add(tileMap, BorderLayout.CENTER);
	}

	private void setMap(Point end) {
		tileMap.map = new HashMap<Point, Integer>(map);
		tileMap.map.put(startPoint, START);
		tileMap.map.put(end, END);
		tileMap.repaint();
	}

	private void backtracking() {
		Map<Point, Integer> mapForRun = new HashMap<Point, Integer>(tileMap.map);
		Stack<Point> path = new Stack<Point>();
		path.push(startPoint);
		boolean isNotFound = true;

		Point lastStep = new Point(0, 0);
		while (isNotFound) {
			int x = path.peek().x;
			int y = path.peek().y;
			Point[] ways = { new Point(x, y - 1), new Point(x + 1, y), new Point(x, y + 1), new Point(x - 1, y) };
			boolean noWay = true;
			for (Point way : ways) {
				if (mapForRun.containsKey(way) && !lastStep.equals(way)) {
					if (mapForRun.get(way) == END) {
						path.push(way);
						isNotFound = false;
						break;
					}
				}
			}
			if (isNotFound) {
				for (Point way : ways) {
					if (mapForRun.containsKey(way) && !lastStep.equals(way)) {
						if (mapForRun.get(way) == ROAD) {
							lastStep = path.peek();
							path.push(way);
							noWay = false;
							break;
						}
					}
				}
			}
			if (noWay) {
				mapForRun.put(path.pop(), WALL);
			}
		}
		ArrayList<Point> donePath = new ArrayList<Point>();
		while (!path.isEmpty()) {
			donePath.add(path.pop());
		}
		for (int i = 0; i < donePath.size(); i++) {
			int lastIndex = donePath.lastIndexOf(donePath.get(i));
			if (lastIndex >= 0) {
				donePath.subList(i, lastIndex).clear();
			}
		}
//		System.out.println(donePath.toString());
		Thread thread = new Thread(() -> {
			try {
				for (int i = donePath.size() - 2; i >= 0; i--) {
					tileMap.map.put(donePath.get(i), STEP);
					tileMap.repaint();
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		thread.start();
	}
}