//Edgar Fu 2020

import java.io.*;
import java.util.*;

class BSTNode<T>
{	T key;
	BSTNode<T> left,right;
	BSTNode( T key, BSTNode<T> left, BSTNode<T> right )
	{	this.key = key;
		this.left = left;
		this.right = right;
	}
}

class Queue<T>
{	LinkedList<BSTNode<T>> queue;
	Queue() { queue =  new LinkedList<BSTNode<T>>(); }
	boolean empty() { return queue.size() == 0; }
	void enqueue( BSTNode<T>  node ) { queue.addLast( node ); }
	BSTNode<T>  dequeue() { return queue.removeFirst(); }
	
}

class BSTreeL6<T>
{
	private BSTNode<T> root;
	private int nodeCount;
	private boolean addAttemptWasDupe=false;

	public BSTreeL6()
	{
		nodeCount=0;
		root=null;
	}

	@SuppressWarnings("unchecked")
	public BSTreeL6( String infileName ) throws Exception
	{
		nodeCount=0;
		root=null;
		BufferedReader infile = new BufferedReader( new FileReader( infileName ) );
		while ( infile.ready() )
			add( (T) infile.readLine() );
		infile.close();
	}


	public BSTreeL6(  BSTreeL6<T> other )
	{
		nodeCount=0;
		root=null;


		addNodesInPrOrder( other.root );
	}
	private void addNodesInPrOrder( BSTNode<T> otherBSTNode )
	{
		if ( otherBSTNode == null ) return;
		this.add( otherBSTNode.key );
		this.addNodesInPrOrder( otherBSTNode.left );
		this.addNodesInPrOrder( otherBSTNode.right );
	}


	@SuppressWarnings("unchecked")
	public boolean add( T key )
	{	addAttemptWasDupe=false;
		root = addHelper( this.root, key );
		if (!addAttemptWasDupe) ++nodeCount;
		return !addAttemptWasDupe;
	}

	@SuppressWarnings("unchecked")
	private BSTNode<T> addHelper( BSTNode<T> root, T key )
	{
		if (root == null) return new BSTNode<T>(key,null,null);
		int comp = ((Comparable)key).compareTo( root.key );
		if ( comp == 0 )
			{ addAttemptWasDupe=true; return root; }
		else if (comp < 0)
			root.left = addHelper( root.left, key );
		else
			root.right = addHelper( root.right, key );

		return root;
    }

	public int size()
	{
		return nodeCount;
	}

	public int countNodes()
	{
		return countNodes( this.root );
	}
	private int countNodes( BSTNode<T> root )
	{
		if (root==null) return 0;
		return 1 + countNodes( root.left ) + countNodes( root.right );
	}

	public void printInOrder()
	{
		printInOrder( this.root );
		System.out.println();
	}
	private void printInOrder( BSTNode<T> root )
	{
		if (root == null) return;
		printInOrder( root.left );
		System.out.print( root.key + " " );
		printInOrder( root.right );
	}

	public void printPreOrder()
	{	printPreOrder( this.root );
		System.out.println();
	}
	private void printPreOrder( BSTNode<T> root )
	{	if (root == null) return;
		System.out.print( root.key + " " );
		printPreOrder( root.left );
		printPreOrder( root.right );
	}

	public void printPostOrder()
	{	printPostOrder( this.root );
		System.out.println();
	}
	private void printPostOrder( BSTNode<T> root )
	{	if (root == null) return;
		printPostOrder( root.left );
		printPostOrder( root.right );
		System.out.print( root.key + " " );
	}

	public void printLevelOrder()
	{	if (this.root == null) return;
		Queue<T> q = new Queue<T>();
		q.enqueue( this.root );
		while ( !q.empty() )
		{	BSTNode<T> n = q.dequeue();
			System.out.print( n.key + " " );
			if ( n.left  != null ) q.enqueue( n.left );
			if ( n.right != null ) q.enqueue( n.right );
		}
		System.out.println();
	}

  public int countLevels()
  {
    return countLevels( root ); 
  }
  private int countLevels( BSTNode root)
  {
    if (root==null) return 0;
    return 1 + Math.max( countLevels(root.left), countLevels(root.right) );
  }
  
  public int[] calcLevelCounts()
  {
    int levelCounts[] = new int[countLevels()];
    calcLevelCounts( root, levelCounts, 0 );
    return levelCounts;
  }
  private void calcLevelCounts( BSTNode root, int levelCounts[], int level )
  {
    if (root==null)return;
    ++levelCounts[level];
    calcLevelCounts( root.left, levelCounts, level+1 );
    calcLevelCounts( root.right, levelCounts, level+1 );
  }


	public BSTreeL6<T> makeBalancedCopyOf( )
	{
		ArrayList<T> keys = new ArrayList<T>();

		inOrderHelper(root, keys);

		BSTreeL6<T> balancedBST = new BSTreeL6<T>();

		addKeysInBalancedOrder(keys, 0, keys.size() - 1, balancedBST);

		return balancedBST;
	}

	private ArrayList<T> inOrderHelper(BSTNode<T> thisRoot, ArrayList<T> keys){
		if(thisRoot == null)
			return keys;

		inOrderHelper(thisRoot.left, keys);
		keys.add(thisRoot.key);
		inOrderHelper(thisRoot.right, keys);
		return keys;
	}

	private void addKeysInBalancedOrder (ArrayList<T> keys, int lo, int hi, BSTreeL6<T> balancedBST){
		int mid = (hi + lo) / 2;
		if(mid > hi || mid < lo)
			return;
		
		balancedBST.add(keys.get(mid));
		addKeysInBalancedOrder(keys, lo, mid - 1, balancedBST);
		addKeysInBalancedOrder(keys, mid + 1, hi, balancedBST);
	}

	public boolean equals( BSTreeL6<T> other ){
		BSTNode<T> thisRoot = this.root;
		BSTNode<T> otherRoot = other.root;
		return equals(thisRoot, otherRoot);
	}
	private boolean equals( BSTNode<T> thisRoot, BSTNode<T> otherRoot ){
		if((thisRoot == null && otherRoot == null) || thisRoot == otherRoot)
			return true;
		if((thisRoot == null && otherRoot != null) || (thisRoot != null && otherRoot == null))
			return false;
		if(!thisRoot.key.equals(otherRoot.key))
			return false;

		equals(thisRoot.left, otherRoot.right);
		equals(thisRoot.right, otherRoot.right);
		return true;
	}
}
