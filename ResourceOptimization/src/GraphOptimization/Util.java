package GraphOptimization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Util {

	public List<Vertex> vertices = new ArrayList<Vertex>();

	
	  public static Graph createNoEdgeGraphFromVertices(int verticesCount) { Graph
	  graph = new Graph(); for (int i = 0; i < verticesCount; i++) { String label =
	  "v" + i; graph.addVertex(label);
	  
	  }
	  
	  return graph; }

	public static Graph createGraphFromAdjacencyMatrix(int[][] adjacencyMatrix) {
		// first I initialize the graph with only the vertices Graph graph =
		Graph aGraph = createNoEdgeGraphFromVertices(adjacencyMatrix[0].length);
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			// if the value in the matrix is 0, there is no edge linking the vertex with //
			// position i and the vertex with position j
			/*
			 * j starts from 0 and not from i+: this means that a given non upper triangular
			 * matrix will identify a graph with possibly more than one edge linking two
			 * same vertices
			 */
			for (int j = i + 1; j < adjacencyMatrix.length && j != i; j++) {
				int weight = adjacencyMatrix[i][j];
				if (weight > 0) {
					aGraph.addEdge(aGraph.vertices.get(i), aGraph.vertices.get(j), weight, "e" + i + j);
				}
			}
		}
		return aGraph;
	}

	


	 
	public static int[][] getMatrixFromGraph(Graph aGraph) {
		int[][] matrix = new int[aGraph.vertices.size()][aGraph.vertices.size()];

		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix.length; column++) {
				if (column != row) {
					Edge aEdge = aGraph.getEdge(aGraph.getVertex(row), aGraph.getVertex(column));
					if (aEdge != null) {
						matrix[row][column] = aEdge.weight;
					} 
				}else {
					matrix[row][column] = 0; //I'm treating diagonal values as 0
				}

			}

		}
		return matrix;
	}

	public static void exportMatrixToCSV(int[][] aMatrix, String path) {

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path));

				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
			for (int[] row : aMatrix) {
				StringBuilder sb = new StringBuilder();
				for (int element : row) {
					sb.append(element + "\t ");
				}
				csvPrinter.printRecord(sb.toString());
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int[][] getAdjacencyMatrixFromCSV(String path, boolean hasHeader) {
		//if hasHeader is true, the input csv has an header as row and an header as column. TODO: change this
		File file = new File(path);
		FileInputStream fileIS;
		BufferedReader br = null;
		try {
			fileIS = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fileIS));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int seq = 0;
		String[] header = null;
		int dim = 0;
		String cvsSplitBy = ";|\\|";
		int startFromIndex = 0;
		int[][] matrix = null;
		try {
			String line = br.readLine();
			String[] firstRow = line.split(cvsSplitBy);
			if(hasHeader) {
				dim = firstRow.length -1;
				header = firstRow;
			} else {
				dim = firstRow.length;
			}
			/*
			 * Note: the row.length is the number of the columns in the input csv file. This means that the 
			 * input file can have both the first column and row as headers or it can have none of the first column and row as headers
			 * but it cannot have only one between the first column and the first row as header.
			 * 	
			 * In the input csv file of a given graph,
			 * seq is the (number of rows of the matrix +1) if the file contains an header or the number of rows if it does not
			 * i is the (number of columns of the matrix +1) if the file contains an header or the number of columns if it does not
			*/
			matrix = new int[dim][dim];
			while (line != null) {
				if (seq == 0 && hasHeader) {
					startFromIndex = 1;
					seq++;
					line = br.readLine();
				} else {
					String[] row = line.replaceAll(";;", ";0;").split(cvsSplitBy); //this avoids blank spaces in the csv to generate NumberFormatException
					
					for (int i = startFromIndex; i < row.length; i++) {
						Integer currentRow = hasHeader ? seq-1 : seq;
						Integer currentColumn = hasHeader ? i-1 : i;
						if(currentRow.equals(currentColumn)) {
							matrix[currentRow][currentColumn] = 0;  
						} else {
						matrix[currentRow][currentColumn] = Integer.parseInt(row[i]);
						}
					}
					seq++;
					line = br.readLine();
				}
			}
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException io1) {
					io1.printStackTrace();
				}
			}

		}

		return matrix;
	}
	
	/*
	 * If the matrix is incomplete, I add a very big value to fill it. 
	 */
	private static int[][] completeMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (j != i && matrix[i][j] < 1) {
					matrix[i][j] = 1000;
				}

			}
		}
		return matrix;
	}

	public static Graph getMSTFromAdjacencyMatrix(int[][] matrix, String mode) {
		completeMatrix(matrix);
		Graph completeGraph = new Graph(matrix);
		Graph spanningTree = new Graph();
		ArrayList<Vertex> vertices = (ArrayList<Vertex>) completeGraph.vertices;
		List<Edge> addedEdges = new ArrayList<Edge>();
		List<Vertex> subSet1 = new ArrayList<Vertex>();
		List<Vertex> subSet2 = new ArrayList<Vertex>();
		switch (mode) {

		case "Prim":
			spanningTree.vertices = vertices;
			/*
			 * 1) Select a random vertex among all the vertex of the complete graph. This will be called 'firstVertex and so I'll have two 
			 * subsets of vertices: one (subSet1) containing firstVertex and the other one (subSet2) containing all the other vertices.
			 * 2) Find the minimum weight's edge linking a vertex in subSet1 to a vertex in subSet2 or vice-versa.
			 * 3) Add the found edge to the list of edges that will be part of the final spanning tree (addedEdges).
			 * 4) Move the vertex (of the edge that has been found) in subSet2 to subSet1.
			 * 
			 * Steps 2-4 will be repeated for (#vertices-1) iterations. Note that subSet1 will always contain a 
			 * list of vertices that are connected by the edges in addedEdges.
			 * 5)return the spanning tree with addedEdges.
			 */
			Edge edgeToAdd = null;
			int randomIndex = ThreadLocalRandom.current().nextInt(0, vertices.size()-1);
			Vertex firstVertex = completeGraph.getVertex(randomIndex);
			
			subSet1.add(firstVertex);
			for (Vertex vertex : vertices) {
				if(vertices.indexOf(vertex) != randomIndex) {
					subSet2.add(vertex);
				}
			}
			while (addedEdges.size() != (vertices.size() - 1)) {
				Vertex vertexToMoveFromSubset2ToSubset1 = null;
				for (Vertex vertex : subSet1) {
					for (Vertex vertex2 : subSet2) {
						
						Edge currentEdge = completeGraph.getEdge(vertex, vertex2).weight<completeGraph.getEdge(vertex2, vertex).weight 
								? completeGraph.getEdge(vertex, vertex2)
										: completeGraph.getEdge(vertex2, vertex);
						if (edgeToAdd == null) {
							edgeToAdd = currentEdge;
							vertexToMoveFromSubset2ToSubset1 = vertex2;
						} else if (currentEdge.weight < edgeToAdd.weight) {
							edgeToAdd = currentEdge;
							vertexToMoveFromSubset2ToSubset1 = vertex2;
						}
					}
				}
				addedEdges.add(edgeToAdd);
				subSet1.add(vertexToMoveFromSubset2ToSubset1);
				subSet2.remove(vertexToMoveFromSubset2ToSubset1);
				edgeToAdd = null;

			}

			spanningTree.edges = addedEdges;
			return spanningTree;

		case "Kruskal":
			/*
			 * 1) Sort the edges by the weight (ascending)
			 *  while (#edges in the spanning tree < #vertices-1)
			 * 2) Add the first edge to the spanning tree graph, if this doesn't create a cycle.
			 */
			spanningTree.vertices = vertices;
			
			Collections.sort(completeGraph.edges);
			
			while (addedEdges.size()<completeGraph.vertices.size()-1) {
				for (Edge currentEdge : completeGraph.edges) {
					Vertex[] currentVertices = currentEdge.vertices;
					//The next line verifies that I'm not creating a cycle by adding the current edge
					if(!(subSet1.contains(currentVertices[0]) && subSet1.contains(currentVertices[1]))) {
						addedEdges.add(currentEdge);
						if(!subSet1.contains(currentVertices[0])) {
							subSet1.add(currentVertices[0]);
						}
						if(!subSet1.contains(currentVertices[1])) {
							subSet1.add(currentVertices[1]);
						}
					}
					
				}
			}
			spanningTree.edges = addedEdges;
			return spanningTree;

		default:
			return null;
		}
	}
}