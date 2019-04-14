package rsa.quad;

import java.util.HashSet;
import java.util.Set;

public class PointQuadtree<T extends HasPoint> extends java.lang.Object {
	private Trie<T> root;
	
	/**
	 * Create a QuadTree with given (topLeftX, topLeftY) and (bottomRightX, bottomRightY)
	 * @param topLeftX - top left X coordinate of QuadTree
	 * @param topLeftY - top left Y coordinate of QuadTree
	 * @param bottomRightX - bottom right X coordinate of QuadTree
	 * @param bottomRightY - bottom right Y coordinate of QuadTree
	 */
	public PointQuadtree(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		root = new LeafTrie<T> (topLeftX, topLeftY, bottomRightX, bottomRightY);
	}
	
	/**
	 * Delete given point from QuadTree, if it exists there
	 * @param point - to be deleted
	 */
	public void delete(T point) {
		this.root.delete(point);
	}
	
	/**
	 * Find a recorded point with the same coordinates of given point
	 * @param point - with requested coordinates
	 * @return recorded point, if found; null otherwise
	 */
	public T find(T point) {
		return this.root.find(point);
	}
	
	/**   
	 * Returns a set of points at a distance smaller or equal to radius from point with given coordinates.
	 * @param x - coordinate of point
	 * @param y - coordinate of point
	 * @param radius - from given point
	 * @return set of instances of type HasPoint
	 */
	public Set<T> findNear(double x, double y, double radius) {
		Set<T> points = new HashSet<T>();
		this.root.collectNear(x, y, radius, points);
		return points;
	}
	
	/**
	 * A set with all points in the QuadTree
	 * @return set of instances of type HasPoint 
	 */
	public Set<T> getAll() {
		Set<T> points = new HashSet<T>();
		this.root.collectAll(points);
		return points;
	}
	
	/**
	 * Insert given point in the QuadTree
	 * @param point - to be inserted 
	 */
	public void insert(T point) {
		/*System.out.println("a" + this.root.topLeftX);
		System.out.println("a" + this.root.topLeftY);
		System.out.println("a" + this.root.bottomRightX);
		System.out.println("a" + this.root.bottomRightY);*/
		if(this.root.topLeftX > point.getX() || this.root.topLeftY < point.getY() || this.root.bottomRightX < point.getX() || this.root.bottomRightY > point.getY())
			//System.out.println("THROW");
			throw new PointOutOfBoundException();
		
		this.root = this.root.insert(point);
	}
	
	/**
	 * Insert point, replacing existing point in the same position
	 * @param point - point to be inserted 
	 */
	public void insertReplace(T point) {
		this.root = this.root.insertReplace(point);
	}
}
