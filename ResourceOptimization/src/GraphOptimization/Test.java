package GraphOptimization;

public class Test {

	public static void main(String[] args) {
		String path = "C:...\\adjacency.csv";
		int[][] matrix = Util.getAdjacencyMatrixFromCSV(path, true);

		//Graph mstGraph = Util.getMSTFromAdjacencyMatrix(matrix, "Kruskal");
		Graph mstGraph = Util.getMSTFromAdjacencyMatrix(matrix, "Prim");
		int[][] mstMatrix = Util.getMatrixFromGraph(mstGraph);

		Util.exportMatrixToCSV(mstMatrix, "C:...\\adjacencyExported.csv");
		

	}

}
