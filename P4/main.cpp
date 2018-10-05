#include<bits/stdc++.h>

using namespace std;

class MST {

private:
    int n, edges;
    int **matrix;
    int *parent;
    int *key;
    bool *setOfMST;

public:
    MST() {

        cout << "Enter the no. of nodes: ";
        cin >> n;
        cout << "Enter the no. of edges: ";
        cin >> edges;
        parent = new int [n];
        key = new int [n];
        setOfMST = new bool [n];
        matrix = new int *[n];
        for (int i = 0; i < n; i++) {
            matrix[i] = new int [n];
        }

        for (int i = 0; i < n; i++) {
            for(int j = 0; j < n ; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    void init() {

        int start, end, weight;
        cout << "Enter the content of Graph: \n";
        for(int i = 0; i < edges; i++) {
            cin >> start >> end >> weight;
            matrix[start - 1][end - 1] = weight;
            matrix[end - 1][start - 1] = weight;
        }
    }

    int getMinKey() {

        int min = INT_MAX;
        int minimum;

        for (int i = 0; i < n; i++) {
            if(!setOfMST[i] && key[i] < min) {
                min = key[i];
                minimum = i;
            }
        }
        return minimum;
    }

    void findMinimumSpanningTree() {
        
        for(int i = 0; i < n; i++) {
            key[i] = INT_MAX;
            setOfMST[i] = false;
        }

        // Starting from fisrt node for can be aby node
        key[0] = 0;
        parent[0] = -1;

        for(int i = 0; i < n; i++) {
            int k = getMinKey();

            setOfMST[k] = true;
            
            for(int j = 0; j < n; j++) {
                if(matrix[i][j]) {
                    if(!setOfMST[j]) {
                        if(matrix[i][j] < key[j]) {
                            parent[j] = i;
                            key[j] = matrix[i][j];
                        }
                    }
                }
            }
        }
    }

    void printAll() {
        int sum = 0;
        for (int i = 1; i < n; i++) {
            
            cout << "Edge: " <<parent[i]+1 << " - " << i+1 << "  Weight: " << matrix[i][parent[i]];
            cout << endl;
            sum += matrix[i][parent[i]];

        }
        cout << "Mimimum spannning cost" << sum;
    }

};

int main() {
    MST m;
    m.init();
    m.findMinimumSpanningTree();
    m.printAll();
}
