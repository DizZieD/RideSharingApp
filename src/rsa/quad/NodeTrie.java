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
		for (Trie<T> trie : tries.values()) {
			trie.collectAll(points);
		}
	}

	@Override
	void collectNear(double x, double y, double radius, Set<T> points) {
		for (Trie<T> trie : tries.values()) {
			if(trie.overlaps(x, y, radius))
				trie.collectNear(x, y, radius, points);
		}
	}

	@Override
	void delete(T point) {
		tries.get(quadrantOf(point)).delete(point);;
	}

	@Override
	T find(T point) {
		return tries.get(quadrantOf(point)).find(point);
	}

	@Override
	Trie<T> insert(T point) {
		return tries.get(quadrantOf(point)).insert(point);
	}

	@Override
	Trie<T> insertReplace(T point) {
		return tries.get(quadrantOf(point)).insert(point);
	}
	
	Trie.Quadrant quadrantOf(T point){
		double halfX = (topLeftX+bottomRightX)/2;
		double halfY = (topLeftY+bottomRightY)/2;
		
		if(halfX >= point.getX())
		{
			if(halfY >= point.getY())
				return Trie.Quadrant.NE;
			return Trie.Quadrant.SE;
		}
		if(halfY >= point.getY())
			return Trie.Quadrant.NW;
		return Trie.Quadrant.SW;
	}
}
