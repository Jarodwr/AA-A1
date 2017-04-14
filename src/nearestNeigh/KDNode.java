package nearestNeigh;

public class KDNode {
	
	public KDNode parent;
	public Point value;
	public KDNode left;
	public KDNode right;
	
	KDNode(Point point, KDNode parent) {
		this.parent = parent;
		this.value = point;
		this.left = null;
		this.right = null;
	}
}
