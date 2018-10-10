#include <iostream>
#include <vector>
#include <tuple>
#include <graphics.h>
using namespace std;

int count = 0;
vector <tuple<int, char, char> > stack;
vector <int> peg1;
vector <int> peg2;
vector <int> peg3;

void press() {
    int in = 0;

    while (in == 0) {
        in = getchar();
    }
}

void draw() {

    for (int i = 1; i <= 3; ++i) {
        setcolor(WHITE);
        bar(i*150 - 60, 400 ,i*150 + 60 ,410);
        bar(i*150-2, 50, i*150+2, 400);
    }
    outtextxy(200, 10, "Restricted tower of Hanoi");
    outtextxy(125, 450, "Source");
    outtextxy(275, 450, "Auxiliary");
    outtextxy(425, 450, "Destination");

    //peg1
    for (int i = 0; i < peg1.size(); ++i) {
        setcolor(peg1[i]);
        bar(150 - peg1[i]*6, 385-15*i, 150 + peg1[i]*6, 395-15*i);
    }

    //peg2
    for (int i = 0; i < peg2.size(); ++i) {
        setcolor(peg2[i]);
        bar(300 - peg2[i]*6, 385-15*i, 300 + peg2[i]*6, 395-15*i);        
    }

    //peg3
    for (int i = 0; i < peg3.size(); ++i) {
        setcolor(peg3[i]);
        bar(450 - peg3[i]*6, 385-15*i, 450 + peg3[i]*6, 395-15*i);
    }

    delay(500);
    
}

void guiHanoi() {

    for(int i = 0; i < stack.size(); i++) {
        if(get<1>(stack[i]) == 'A') {
            peg1.pop_back();
        } else {
            if (get<1>(stack[i]) == 'B') {
                peg2.pop_back();
            } else {
                peg3.pop_back();
            }
        }

        if(get<2>(stack[i]) == 'A') {
            peg1.push_back(get<0>(stack[i]));
        } else {
            if (get<2>(stack[i]) == 'B') {
                peg2.push_back(get<0>(stack[i]));
            } else {
                peg3.push_back(get<0>(stack[i]));
            }
        }

        draw();
        if (i != stack.size()-1) {
            cleardevice();
        }
    }
    press();
    closegraph();
}

int toh(int n, char sp, char fp, char ap) {
    if(n == 0)
        return count;
    toh(n-1, sp, fp, ap);
    count++;
    stack.push_back(make_tuple(n, sp, ap));
    toh(n-1, fp, sp, ap);
    count++;
    stack.push_back(make_tuple(n, ap, fp));
    toh(n-1, sp, fp, ap);

}

int main() {
    int n;
    cout<<"Enter the no. of disks on first peg:";
    cin>>n;
    cout<<"The sequence Of Moves are :\n";
    cout << toh(n,'A','C','B') << endl;
    
    for (int i = n; i > 0 ; i--) {
        peg1.push_back(i);
    }

    int gd = DETECT,gm;
    initgraph(&gd, &gm, NULL);
    draw();
    guiHanoi();
    
    return 0;
}
