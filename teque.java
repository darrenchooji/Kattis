import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class teque {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int N = Integer.parseInt(br.readLine());            // Number of operations for the teque
        Custom_Deque t = new Custom_Deque();
        for (int i=0; i<N; ++i) {
            String[] input = br.readLine().split(" ");
            int x = Integer.parseInt(input[1]);
            if (input[0].equals("push_front")) t.push_front(x);
            else if (input[0].equals("push_middle")) t.push_middle(x);
            else if (input[0].equals("push_back")) t.push_back(x);
            else pw.println(t.get(x));
        }
        pw.flush();
    }
}

class Custom_Deque {
    int[] front = new int[1_000_000];
    int[] back = new int[1_000_000];
    int frontIndex = 499_999, backIndex = 499_999;
    int frontSize = 0, backSize = 0;

    Custom_Deque() {}

    void push_front(int x) {
        if (frontSize == backSize) {
            front[--frontIndex] = x;
            frontSize++;
        } else {
            back[--backIndex] = front[frontIndex+frontSize-1];
            backSize++; 
            front[--frontIndex] = x;
        }
    }

    void push_middle(int x) {
        if (frontSize == backSize) {
            front[frontIndex+frontSize] = x;
            frontSize++;
        } else {
            back[--backIndex] = x;
            backSize++;
        }
    }

    void push_back(int x) {
        if (frontSize == backSize) {
            back[backIndex+backSize] = x;
            backSize++;
            front[frontIndex+frontSize] = back[backIndex++];
            frontSize++; backSize--;
        } else {
            back[backIndex+backSize] = x;
            backSize++;
        }
    }

    int get(int x) { 
        if (x < frontSize) {
            return front[frontIndex+x];
        } else {
            return back[backIndex+x-frontSize];
        }
     }
}
