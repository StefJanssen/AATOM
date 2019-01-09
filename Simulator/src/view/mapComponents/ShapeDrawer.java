package view.mapComponents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import model.environment.position.Position;

/**
 * The {@link ShapeDrawer} class is used to draw shapes. It is built so that the
 * shapes are drawn correctly on the screen, also in case of zooming in. Each
 * {@link MapComponentView} relies on this class for visualization.
 * 
 * @author S.A.M. Janssen
 */
public class ShapeDrawer {

	/**
	 * Graphics.
	 */
	private static Graphics2D g2;
	/**
	 * The pixel ratio.
	 */
	private static double pixelRatio;
	/**
	 * The offset in the x direction.
	 */
	private static int xOffset;
	/**
	 * The offset in the y direction.
	 */
	private static int yOffset;

	/**
	 * Draws a circle.
	 * 
	 * @param color
	 *            The color.
	 * @param position
	 *            The center of the circle.
	 * @param radius
	 *            The radius.
	 */
	public static void drawCircle(Color color, Position position, double radius) {
		g2.setColor(color);
		g2.fill(new Ellipse2D.Double(pixelRatio * (position.x - radius) - xOffset,
				pixelRatio * (position.y - radius) - yOffset, pixelRatio * 2 * radius, pixelRatio * 2 * radius));
	}

	/**
	 * Draws a line.
	 * 
	 * @param color
	 *            The color.
	 * @param start
	 *            The start position.
	 * @param end
	 *            The end position.
	 */
	public static void drawLine(Color color, Position start, Position end) {
		g2.setColor(color);
		g2.draw(new Line2D.Double(pixelRatio * start.x - xOffset, pixelRatio * start.y - yOffset,
				pixelRatio * end.x - xOffset, pixelRatio * end.y - yOffset));
	}

	/**
	 * Draws a path.
	 * 
	 * @param color
	 *            The color.
	 * @param position
	 *            The start position.
	 * @param positions
	 *            The positions of the path.
	 */
	public static void drawPath(Color color, Position position, List<Position> positions) {
		// We are using try catch exceptions here, because from time to time
		// there is an exception popping up.
		try {
			g2.setColor(color);
			GeneralPath path = new GeneralPath(Path2D.WIND_EVEN_ODD, positions.size());
			path.moveTo(position.x * pixelRatio - xOffset, position.y * pixelRatio - yOffset);
			for (Position p : positions)
				path.lineTo(p.x * pixelRatio - xOffset, p.y * pixelRatio - yOffset);
			g2.draw(path);
		} catch (Exception e) {
		}
	}

	/**
	 * Draws a polygon.
	 * 
	 * @param color
	 *            The color.
	 * @param positions
	 *            The corners.
	 */
	public static void drawPolygon(Color color, List<Position> positions) {
		g2.setColor(color);
		Path2D path = new Path2D.Double();
		path.moveTo(positions.get(0).x * pixelRatio - xOffset, positions.get(0).y * pixelRatio - yOffset);

		for (int i = 1; i < positions.size(); ++i) {
			path.lineTo(positions.get(i).x * pixelRatio - xOffset, positions.get(i).y * pixelRatio - yOffset);
		}
		path.lineTo(positions.get(0).x * pixelRatio - xOffset, positions.get(0).y * pixelRatio - yOffset);
		g2.draw(path);
		g2.fill(path);
	}

	/**
	 * Draws a rectangle.
	 * 
	 * @param color
	 *            The color.
	 * @param position
	 *            The top left position.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 */
	public static void drawRectangle(Color color, Position position, double width, double height) {
		g2.setColor(color);
		g2.draw(new Rectangle2D.Double(pixelRatio * position.x - xOffset, pixelRatio * position.y - yOffset,
				pixelRatio * width, pixelRatio * height));
		g2.fill(new Rectangle2D.Double(pixelRatio * position.x - xOffset, pixelRatio * position.y - yOffset,
				pixelRatio * width, pixelRatio * height));
	}

	/**
	 * Gets the complement of a {@link Color}.
	 * 
	 * @param color
	 *            The color.
	 * @return The complement color.
	 */
	public static Color getComplementaryColor(Color color) {
		return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

	/**
	 * Gets a rectangle that can be used as a bounding box.
	 * 
	 * @param position
	 *            The top left position.
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @return The rectangle.
	 */
	public static Rectangle getRectangle(Position position, double width, double height) {
		return new Rectangle((int) (pixelRatio * (position.x - width) - xOffset),
				(int) (pixelRatio * (position.y - height) - yOffset), (int) (pixelRatio * 2 * width),
				(int) (pixelRatio * 2 * height));
	}

	/**
	 * Updates the variables.
	 * 
	 * @param g2
	 *            Graphics.
	 * @param pixelRatio
	 *            Ratio.
	 * @param xOffset
	 *            x offset.
	 * @param yOffset
	 *            y offset.
	 */
	public static void updateVariables(Graphics2D g2, double pixelRatio, int xOffset, int yOffset) {
		ShapeDrawer.g2 = g2;
		ShapeDrawer.pixelRatio = pixelRatio;
		ShapeDrawer.xOffset = xOffset;
		ShapeDrawer.yOffset = yOffset;
	}

	/**
	 * Private class to prevent object creation.
	 */
	private ShapeDrawer() {
	}

}
