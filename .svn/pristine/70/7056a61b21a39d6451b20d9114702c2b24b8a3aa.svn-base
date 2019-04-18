package rsa.quad;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class NodeTrie<T extends HasPoint> extends Trie<T>{
	Map<Trie.Quadrant, Trie<T>> tries;

	/**
	 * Create a empty QuadTree Node with given (topLeftX, topLeftY) and (bottomRightX, bottomRightY)
	 * @param topLeftX - top left X coordinate of QuadTree Leaf
	 * @param topLeftY - top left Y coordinate of QuadTree Leaf
	 * @param bottomRightX - bottom right X coordinate of QuadTree Leaf
	 * @param bottomRightY - bottom right Y coordinate of QuadTree Leaf
	 */
	NodeTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
		super(topLeftX, topLeftY, bottomRightX, bottomRightY);
		this.tries = new HashMap<Trie.Quadrant, Trie<T>>();
		double halfX = (topLeftX+bottomRightX)/2;
		double halfY = (topLeftY+bottomRightY)/2;
		this.tries.put(Trie.Quadrant.NE, new LeafTrie<T>(halfX,topLeftY,bottomRightX,halfY));
		this.tries.put(Trie.Quadrant.NW, new LeafTrie<T>(topLeftX,topLeftY,halfX,halfY));
		this.tries.put(Trie.Quadrant.SE, new LeafTrie<T>(halfX,halfY,bottomRightX,bottomRightY));
		this.tries.put(Trie.Quadrant.SW, new LeafTrie<T>(topLeftX,halfY,halfX,bottomRightY));
	}

	@Override
	void collectAll(Set<T> points) {
		for (Trie<T> trie : this.tries.values()) {
			trie.collectAll(points);
		}
	}

	@Override
	void collectNear(double x, double y, double radius, Set<T> points) {
		for (Trie<T> trie : this.tries.values()) {
			if(trie.overlaps(x, y, radius))
				trie.collectNear(x, y, radius, points);
		}
	}

	@Override
	void delete(T point) {
		this.tries.get(quadrantOf(point)).delete(point);;
	}

	@Override
	T find(T point) {
		return this.tries.get(quadrantOf(point)).find(point);
	}

	@Override
	Trie<T> insert(T point) {
		this.tries.put(quadrantOf(point), this.tries.get(quadrantOf(point)).insert(point));
		return this;
	}

	@Override
	Trie<T> insertReplace(T point) {
		this.tries.put(quadrantOf(point), this.tries.get(quadrantOf(point)).insert(point));
		return this;
	}
	
	Trie.Quadrant quadrantOf(T point){
		double halfX = (this.topLeftX+this.bottomRightX)/2;
		double halfY = (this.topLeftY+this.bottomRightY)/2;
		
		if(halfX >= point.getX())
		{
			if(halfY >= point.getY())
				return Trie.Quadrant.SW;
			return Trie.Quadrant.NW;
		}
		if(halfY >= point.getY())
			return Trie.Quadrant.SE;
		return Trie.Quadrant.NE;
	}

	@Override
	public String toString() {
		return "NodeTrie [tries=" + tries + ", topLeftX=" + topLeftX + ", topLeftY=" + topLeftY + ", bottomRightX="
				+ bottomRightX + ", bottomRightY=" + bottomRightY + ", toString()=" + super.toString() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
	
}
