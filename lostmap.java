import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class lostmap {
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int n = reader.nextInt();
        ArrayList<IntegerTriple> EL = new ArrayList<>();
        for (int i=0; i<n; ++i) {
            for (int j=0; j<n; ++j) {
                int d = reader.nextInt();
                if (j >= i) continue;
                EL.add(new IntegerTriple(d, j, i));
            }
        }
        Collections.sort(EL);

        int num_taken = 0;
        UnionFind UF = new UnionFind(n);
        for (int i=0; i<EL.size(); ++i) {
            IntegerTriple front = EL.get(i);
            if (UF.isSameSet(front.second(), front.third())) continue;
            pw.println((front.second()+1)+" "+(front.third()+1));
            UF.unionSet(front.second(), front.third());
            ++num_taken;
            if (num_taken == n-1) break;
        }
        pw.flush();
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
    
    static class IntegerTriple implements Comparable<IntegerTriple> {
        Integer _first, _second, _third;
      
        public IntegerTriple(Integer f, Integer s, Integer t) {
          _first = f;
          _second = s;
          _third = t;
        }
      
        public int compareTo(IntegerTriple o) {
          if (!this.first().equals(o.first()))
            return this.first() - o.first();
          else if (!this.second().equals(o.second()))
            return this.second() - o.second();
          else
            return this.third() - o.third();
        }
      
        Integer first() { return _first; }
        Integer second() { return _second; }
        Integer third() { return _third; }
      
        public String toString() { return first() + " " + second() + " " + third(); }
    }

    static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public Reader(String file_name) throws IOException {
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public String readLine() throws IOException {
            byte[] buf = new byte[64]; // line length
            int cnt = 0, c;
            while ((c = read()) != -1) {
                if (c == '\n') {
                    if (cnt != 0) {
                        break;
                    }
                    else {
                        continue;
                    }
                }
                buf[cnt++] = (byte) c;
            }
            return new String(buf, 0, cnt);
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (neg) return -ret;
            return ret;
        }

        public long nextLong() throws IOException {
            long ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
            if (neg) return -ret;
            return ret;
        }

        public double nextDouble() throws IOException {
            double ret = 0, div = 1;
            byte c = read();
            while (c <= ' ') c = read();
            boolean neg = (c == '-');
            if (neg) c = read();

            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            if (c == '.') {
                while ((c = read()) >= '0' && c <= '9') {
                    ret += (c - '0') / (div *= 10);
                }
            }

            if (neg) return -ret;
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }

        public void close() throws IOException {
            if (din == null) return;
            din.close();
        }
    }
}