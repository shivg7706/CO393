#include <iostream>
#include <unistd.h>
#include <cmath>

using namespace std;

class SR {
private:
    int s, r, q, clock;

public:

    SR() {
        s = 0;
        r = 0;
        q = 0;
        clock = 0;
    }

    void solve(int s, int r, int clock) {
        this->s = s;
        this->r = r;
        this->clock = clock;
        
        if (clock) {
            if(!s && r) {
                this->q = 0;
            }else{
                if(s && !r)
                    this->q = 1;
            }
        }else{
            this->q = 0;
        }
    }

    void reset() {
        this->q = 0;
    }
    int getQ() {
        return this->q;
    }
};


int main() {

    SR *ff = new SR [4];
    int currentBits[4];
    int decimal, clock = 1;

    while (clock == 1) {
        decimal = 0;
        for(int i = 0 ; i < 4; i++) {
            currentBits[i] = ff[i].getQ();
            cout << currentBits[i] << " ";
            decimal += currentBits[i] * pow(2.0, 3-i); 
        }
        cout << "Decimal: " << decimal;

        // S = BCD, R = C'D
        ff[0].solve(currentBits[1] && currentBits[2] && currentBits[3],
                     !currentBits[2] && currentBits[3], clock);
        // S = B'CD, R = BCD
        ff[1].solve(!currentBits[1] && currentBits[2] && currentBits[3],
                     currentBits[1] && currentBits[2] && currentBits[3], clock);
        // S = AC'D, R = CD
        ff[2].solve(!currentBits[0] && !currentBits[2] && currentBits[3],
                     currentBits[2] && currentBits[3], clock);
        // S = D' R = D
        ff[3].solve(!currentBits[3],
                     currentBits[3], clock);

        cout<<endl;
        usleep(500000);
        if(decimal == 9) {
            cout << "Do you want to count again: (0/1): ";
            cin >> clock;
        } 
    }

    for (int i = 0; i < 4; ++i) {
        ff[i].reset();
    }
    cout << "Happy Coding" << endl;

    return 0;
}
