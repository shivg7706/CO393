import java.util.Scanner;
import java.util.ArrayList;


class Cycle {

    private int nodes, edges;
    private int [][] adjacencyMatrix;
    private boolean [] visited;
    ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
    private  boolean [] finalCycles;

    public Cycle() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the no. of nodes: ");
        nodes = in.nextInt();
        System.out.print("Enter the no. of Edges: ");
        edges = in.nextInt();

        adjacencyMatrix = new int [nodes][nodes];
        visited = new boolean [nodes];

        for (int i = 0; i < nodes; i++) {
            visited[i] = false;
        }

        System.out.println("Enter the details of each edges <Start Node> <End Node>");

        for(int i = 0; i < edges; i++) {            
            int start, end;
            start = in.nextInt();
            end = in.nextInt();
            adjacencyMatrix[start][end] = 1;
        }

    }

    public void start() {
        for (int i = 0; i < nodes; i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            dfs(i, i, temp);
            for (int j = 0; j < nodes; j++) {
                adjacencyMatrix[i][j] = 0;
                adjacencyMatrix[j][i] = 0;
            }
        }
    }

    private void dfs(Integer start, Integer curr, ArrayList<Integer> temp) {
        temp.add(curr);
        visited[curr] = true;
        for (int i = 0; i < nodes; i++) {
            if(adjacencyMatrix[curr][i] == 1) {
                if (i == start) {
                    cycles.add(new ArrayList<Integer>(temp));
                } else {
                    if (!visited[i]) {
                        dfs(start, i, temp);
                    }
                }
            }
        }

        if(temp.size() > 0) {
            temp.remove(temp.size() - 1);
        }
        visited[curr] = false;
    }

    public void printAll() {
        for (int i = 0; i < cycles.size(); i++) {
            for (int j = 0; j < cycles.get(i).size(); j++) {
                System.out.print(cycles.get(i).get(j) + " -> ");
            }
            System.out.println(cycles.get(i).get(0));
            System.out.println();
        }

    }

    public void removedChildren() {
        finalCycles = new boolean[cycles.size()];
        for (int i = 0; i < cycles.size(); i++) {
            finalCycles[i] = false;
        }

        for (int i = 0; i < cycles.size(); i++) {
            for (int j = i+1; j < cycles.size(); j++) {
                if(!finalCycles[i]) {
                    if ((cycles.get(i)).containsAll(cycles.get(j))) {
                        finalCycles[j] = true;
                    }
                    if ((cycles.get(j)).containsAll(cycles.get(i))) {
                        finalCycles[i] = true;
                    }
                }
            }
        }
        System.out.println("Filtered Cycles");
        for (int i = 0; i < cycles.size(); i++) {
            for (int j = 0; j < cycles.get(i).size(); j++) {
                if (!finalCycles[i]) {
                    System.out.print(cycles.get(i).get(j) + " -> ");
                }
            }
            if (!finalCycles[i]) {
                System.out.println(cycles.get(i).get(0));    
            }
            
        }

    }

}

public class Main {
    public static void main(String[] args) {
        Cycle c = new Cycle();
        c.start();
        c.printAll();
        c.removedChildren();
    }
}