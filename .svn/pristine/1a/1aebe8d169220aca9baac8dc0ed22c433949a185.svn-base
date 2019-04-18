package rsa.quad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class LeafTrie<T extends HasPoint> extends Trie<T>{
	List<T> points;
	
	/**
	 * Create a empty QuadTree Leaf with given (topLeftX, topLeftY) and (bottomRightX, bottomRightY)
	 * @param topLeftX - top left X coordinate of QuadTree Leaf
	 * @param topLeftY - top left Y coordinate of QuadTree Leaf
	 * @param bottomRightX - bottom right X coordinate of QuadTree Leaf
	 * @param bottomRightY - bottom right Y coordinate of QuadTree Leaf
	 */
	LeafTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		this.points = new ArrayList<T>();;
	}

	@Override
	void collectAll(Set<T> points) {
		for (T tmp : this.points){
        	points.add(tmp);
        }
	}

	@Override
	void collectNear(double x, double y, double radius, Set<T> points) {
		double radiusSquared = radius * radius;
		for (T tmp : this.points){
			if((tmp.getX() - x)*(tmp.getX() - x) + (tmp.getY() - y)*(tmp.getY() - y) <= radiusSquared)
        		points.add(tmp);
        }
	}

	@Override
	void delete(T point) {
		this.points.remove(point);
	}

	@Override
	T find(T point) {
		for (T tmp : this.points){
        	if(tmp.getX() == point.getX() && tmp.getY() == point.getY())
        		return tmp;
        }
		return null;
	}
	
	@Override
	Trie<T> insert(T point) {
		if(this.points.size() < LeafTrie.getCapacity()) {
			this.points.add(point);
			return this;
		}
		return split(point);
	}

	@Override
	Trie<T> insertReplace(T point) {
		Iterator<T> i = points.iterator();
		 while (i.hasNext()) {
	         T p = i.next();
	         if (p.getX() == point.getX() && p.getY() == point.getY()) {
	            i.remove();
	            this.points.add(point);
	            break;
	         }
	      }
		return this;
	}
	
	/**
	 * Turns LeafTrie into NodeTrie when the capacity is reached and insert given point
	 * @param point - to be inserted
	 * @return changed parent node
	 */
	private Trie<T> split(T point) {
		Trie<T> node = new NodeTrie<T>(this.topLeftX, this.topLeftY, this.bottomRightX, this.bottomRightY);
		List<T> oldPoints = new ArrayList<T>(this.points);
		this.points.clear();
		node = node.insert(point);
		for (T tmp : oldPoints){
			node = node.insert(tmp);
        }
		return node;
	}

	@Override
	public String toString() {
		return "LeafTrie [points=" + points + ", topLeftX=" + topLeftX + ", topLeftY=" + topLeftY + ", bottomRightX="
				+ bottomRightX + ", bottomRightY=" + bottomRightY + ", toString()=" + super.toString() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
	
}
