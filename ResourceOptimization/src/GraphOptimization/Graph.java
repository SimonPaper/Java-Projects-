package GraphOptimization;

import java.util.ArrayList;
import java.util.List;

public class Graph implements IGraph {

	public List<Vertex> vertices = new ArrayList<Vertex>();
	public List<Edge> edges = new ArrayList<Edge>();

	public Graph() {

	}

	public Graph(int verticesCount) {

		for (int i = 0; i < verticesCount; i++) {
			String label = "v" + i;
			this.addVertex(label);

		}

	}

	public Graph(int[][] adjacencyMatrix) {
		// First, i add all the vertices in the graph
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			String label = "v" + i;
			this.addVertex(label);
		}
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			// if the value in the matrix is 0, there is no edge linking the vertex positioned in i and the vertex positioned in j
			/*
			 * Note: a given non upper triangular matrix could identify a graph with possibly
			 * more than one edge linking two same vertices. 
			 */
		
			for (int j = 0; j < adjacencyMatrix.length; j++) {
				if(j != i) {
					int weight = adjacencyMatrix[i][j];
					if (weight > 0) {
						Edge newEdge = this.addEdge(this.vertices.get(i), this.vertices.get(j), weight, "e" + i + j);
						this.vertices.get(i).edges.add(newEdge);
						this.vertices.get(j).edges.add(newEdge);
	
					}
				}
			}
		}
	}

	public Vertex addVertex(String label) {
		Vertex newVert = new Vertex(label);
		this.vertices.add(newVert);
		return newVert;
	}

	public Vertex getVertex(int ind) {
		return this.vertices.get(ind);
	}

	public void removeVertex(Vertex v) {
		vertices.remove(v);
	}

	public Edge addEdge(Vertex v1, Vertex v2, int weight, String label) {
		Edge newEdge = new Edge(v1, v2, weight, label);
		this.edges.add(newEdge);
		return newEdge;
	}

	public Edge getEdge(Vertex v1, Vertex v2) {
		int indV1 = this.vertices.indexOf(v1);
		int indV2 = this.vertices.indexOf(v2);
		for (Edge edge : edges) {
			int itInd0 = this.vertices.indexOf(edge.vertices[0]);
			int itInd1 = this.vertices.indexOf(edge.vertices[1]);
			//this or condition means that no attention is payed towards the direction of the edge, as if it was an 
			if ((indV1 == itInd0 && indV2 == itInd1) /*|| (indV1 == itInd1 && indV2 == itInd0)*/) {
				return edge;
			}
		}
		return null;

	}

	public void removeEdge(Vertex v1, Vertex v2) {
		Vertex[] verteces = new Vertex[2];
		verteces[0] = v1;
		verteces[1] = v2;
		edges.remove(this.getEdge(v1, v2));

	}

}
