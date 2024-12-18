import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;

public class chatter {
    static int n, r, a, b, c;
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        while (true) {
            try {
                n = sc.nextInt();   // represents both the number of people logged into the chat server and number of connections to make 
                r = sc.nextInt();   // random number seed used to connect users
                a = sc.nextInt();
                b = sc.nextInt();
                c = sc.nextInt();
            } catch (Exception e) { break; }

            UnionFind UF = new UnionFind(n);
            for (int i=0; i<n; ++i) {
                int x=0, y=0;
                while (x == y) {
                    r = (r*a+b)%c;
                    x = r%n;
                    r = (r*a+b)%c;
                    y = r%n;
                }
                UF.unionSet(x, y);
           }
            HashSet<Integer> parentSet = new HashSet<>();
            TreeMap<Integer, Integer> sizeFreqMap = new TreeMap<>();

            for (int i=0; i<n; ++i) {
                int p = UF.findSet(i);
                if (!parentSet.contains(p)) {
                    parentSet.add(p);
                    int size = UF.sizeOfSet(p);
                    sizeFreqMap.put(size, sizeFreqMap.getOrDefault(size, 0)+1);
                }
            }
            pw.print(UF.numDisjointSets()+" ");
            while (!sizeFreqMap.isEmpty()) {
                int size = sizeFreqMap.lastKey();
                int freq = sizeFreqMap.get(size);
                if (freq == 1) pw.print(size+" ");
                else pw.print(size+"x"+freq+" ");
                sizeFreqMap.remove(size);
            }
            pw.println();
            pw.flush();
        }
        sc.close();
    }

    static class UnionFind {                                             
        private ArrayList<Integer> p, rank, setSize;
        private int numSets;
      
        public UnionFind(int N) {
            p = new ArrayList<Integer>(N);
            rank = new ArrayList<Integer>(N);
            setSize = new ArrayList<Integer>(N);
            numSets = N;
            for (int i = 0; i < N; i++) {
                p.add(i);
                rank.add(0);
                setSize.add(1);
            }
        }
      
        public int findSet(int i) { 
            if (p.get(i) == i) return i;
            else {
                int ret = findSet(p.get(i)); p.set(i, ret);
                return ret;
            }
        }
      
        public Boolean isSameSet(int i, int j) { return findSet(i) == findSet(j); }
      
        public void unionSet(int i, int j) { 
            if (!isSameSet(i, j)) { 
                numSets--; 
                int x = findSet(i), y = findSet(j);
                if (rank.get(x) > rank.get(y)) { 
                    p.set(y, x); 
                    setSize.set(x, setSize.get(x) + setSize.get(y)); 
                } else { 
                    p.set(x, y); 
                    setSize.set(y, setSize.get(y) + setSize.get(x));
                    if (rank.get(x) == rank.get(y)) 
                        rank.set(y, rank.get(y) + 1); 
                } 
            } 
        }
    
        public int numDisjointSets() { return numSets; }
        public int sizeOfSet(int i) { return setSize.get(findSet(i)); }
    }       
}