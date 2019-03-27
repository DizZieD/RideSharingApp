package rsa.quad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class LeafTrie<T extends HasPoint> extends Trie<T>{
	List<T> leaf;
	
	LeafTrie(double bottomRightX, double bottomRightY, double topLeftX, double topLeftY) {
		super(bottomRightX, bottomRightY, topLeftX, topLeftY);
		this.leaf = new ArrayList<T>();;
	}

	@Override
	void collectAll(Set<T> points) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void collectNear(double x, double y, double radius, Set<T> points) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void delete(T point) {
		// TODO Auto-generated method stub
		
	}

	@Override
	T find(T point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Trie<T> insert(T point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Trie<T> insertReplace(T point) {
		// TODO Auto-generated method stub
		return null;
	}
}
