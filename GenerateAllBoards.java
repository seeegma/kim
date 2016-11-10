import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class GenerateAllBoards {

	private class Vertex {
		public int carNum;
		public HashSet<Vertex> neighbors;

		public Vertex(int carNum, HashSet<Vertex> neighbors) {
			this.carNum = carNum;
			this.neighbors = neighbors;
		}
	}

	public int counter = 0;

	public HashSet<Vertex> carAdjacencyGraph() {
		NumBoards num = new NumBoards();
		HashSet<Vertex> vertexList = new HashSet<Vertex>();
		for(int i = 0;i<108;i++) {
			Board b = new Board(6,6);
			b.addCar(new Car(4,2,2,true));
			if(!b.addCar(num.carFromSeed(i))) {
				continue;
			}

			vertexList.add(new Vertex(i, new HashSet<Vertex>()));			
		}

		for(Vertex v : vertexList) {
			for (Vertex w : vertexList){
				Board b = new Board(6,6);
				b.addCar(new Car(4,2,2,true));
				b.addCar(num.carFromSeed(v.carNum));
				if (b.addCar(num.carFromSeed(w.carNum))) {
					v.neighbors.add(w);
				}
			}
		}
		return vertexList;
	}

	public void printHashSet(HashSet<Vertex> set) {
		String s = "{";
		for(Vertex v : set){
			s = s + v.carNum + ", ";
		}
		s = s+ "}";
		System.out.println(s);
	}

	public void bronKerbosch(HashSet<Vertex> r, HashSet<Vertex> p, HashSet<Vertex> x) {
		//printHashSet(r);
		//if (r.size() == 6) {
		//	return;
		//}
		
		if (r.size() == 3) {
			counter++;
			System.out.println(counter);
			//printHashSet(r);
		}
		HashSet<Vertex> pcheck = new HashSet<Vertex>(p);
		for(Vertex v : p) {
			if (!pcheck.contains(v)) {
				continue;
			}
			HashSet<Vertex> newr = new HashSet<Vertex>(r);
			HashSet<Vertex> newp = new HashSet<Vertex>(pcheck);
			HashSet<Vertex> newx = new HashSet<Vertex>(x);
			newr.add(v);
			newp.retainAll(v.neighbors);
			newx.retainAll(v.neighbors);
			bronKerbosch(newr,newp,newx);
			pcheck.remove(v);
			x.add(v);
		}
	}


	public static void main(String[] args) {
		GenerateAllBoards gen = new GenerateAllBoards();
		HashSet<Vertex> verts = gen.carAdjacencyGraph();
		gen.bronKerbosch(new HashSet<Vertex>(), verts, new HashSet<Vertex>());
	}

}