import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class TileMap extends JPanel {

	public Map<Point, Integer> map;
	private Color[] color = { Color.black, Color.white , Color.blue, Color.green, Color.red};
	public static final int PREFERRED_GRID_SIZE_PIXELS = 60;

	public TileMap(Map<Point, Integer> map, int width, int height) {
		this.map = new HashMap<Point, Integer>(map);
		setBorder(BorderFactory.createEmptyBorder());

		setPreferredSize(new Dimension(PREFERRED_GRID_SIZE_PIXELS * width, PREFERRED_GRID_SIZE_PIXELS * height));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (Map.Entry<Point, Integer> entry : map.entrySet()) {
			g.setColor(color[entry.getValue()]);
			g.fillRect(PREFERRED_GRID_SIZE_PIXELS * (entry.getKey().x - 1),
					PREFERRED_GRID_SIZE_PIXELS * (entry.getKey().y - 1), PREFERRED_GRID_SIZE_PIXELS,
					PREFERRED_GRID_SIZE_PIXELS);
			g.setColor(Color.black);
			g.drawString("(" + entry.getKey().x + ", " + entry.getKey().y + ")",
					PREFERRED_GRID_SIZE_PIXELS * (entry.getKey().x - 1) + PREFERRED_GRID_SIZE_PIXELS / 4,
					PREFERRED_GRID_SIZE_PIXELS * (entry.getKey().y - 1) + PREFERRED_GRID_SIZE_PIXELS / 2);
		}

	}

	public void setMap(Map<Point, Integer> map) {
		this.map = map;
	}

}