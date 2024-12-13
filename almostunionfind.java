import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class almostunionfind {
    public static void main(String[] args) {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        while (true) {
            try {
                // n: number of integers, m: number of commands
                int n = reader.nextInt(), m = reader.nextInt();
                UnionFind ufds = new UnionFind(n+1);
                for (int i=0; i<m; ++i) {
                    int command = reader.nextInt();
                    if (command == 3) {
                        int p = reader.nextInt();
                        pw.println(ufds.sizeOfSet(p)+" "+ufds.sumOfSet(p));
                    } else {
                        int p = reader.nextInt();
                        int q = reader.nextInt();
                        if (command == 1) ufds.unionSet(p, q);
                        else ufds.moveSet(p, q);
                    }
                }
            } catch (Exception e) { break; }
        }
        pw.flush();
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

class UnionFind {                                             
    private ArrayList<Integer> p, p2, rank, setSize;
    private ArrayList<Long> setSum;
  
    public UnionFind(int N) {
        p = new ArrayList<Integer>(N);
        p2 = new ArrayList<Integer>(N);
        rank = new ArrayList<Integer>(N);
        setSize = new ArrayList<Integer>(N);
        setSum = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            p.add(i); p2.add(i); rank.add(0);
            setSize.add(1); setSum.add((long)i);
        }
    }
  
    public int findSet(int i) {
        int x = p2.get(i);
        int y = p.get(x);
        while (x != y) {
            p.set(x, p.get(y));
            x = p.get(x);
            y = p.get(x);
        }
        return x;
    }
  
    public Boolean isSameSet(int i, int j) { return findSet(i) == findSet(j); }

    public void moveSet(int i, int j) {
        if (!isSameSet(i, j)) {
            int x = findSet(i), y = findSet(j);
            setSize.set(x, setSize.get(x)-1);
            setSize.set(y, setSize.get(y)+1);
            setSum.set(x, setSum.get(x)-i);
            setSum.set(y, setSum.get(y)+i);
            p2.set(i, y);
        }
    }
  
    public void unionSet(int i, int j) { 
        if (!isSameSet(i, j)) { 
            int x = findSet(i), y = findSet(j);
            if (rank.get(x) > rank.get(y)) { 
                p.set(y, x); 
                setSize.set(x, setSize.get(x) + setSize.get(y)); 
                setSum.set(x, setSum.get(x) + setSum.get(y));
            } else { 
                p.set(x, y); 
                setSize.set(y, setSize.get(y) + setSize.get(x));
                setSum.set(y, setSum.get(y) + setSum.get(x));
                if (rank.get(x) == rank.get(y)) 
                    rank.set(y, rank.get(y) + 1); 
            } 
        } 
    }
    
    public int sizeOfSet(int i) { return setSize.get(findSet(i)); }
    public long sumOfSet(int i) { return setSum.get(findSet(i)); }
}