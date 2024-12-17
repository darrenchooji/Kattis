import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class bread {
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        int N = reader.nextInt();       // number of breads

        HashMap<Integer, Integer> breadPositionMap = new HashMap<>();
        int[] sortedBreadPosition = new int[N];
        for (int i=0; i<N; ++i) breadPositionMap.put(reader.nextInt(), i);
        for (int i=0; i<N; ++i) 
            sortedBreadPosition[i] = breadPositionMap.get(reader.nextInt());
        
        long inversions = mergeSort(sortedBreadPosition, 0, N-1);
        pw.println(inversions%2==0 ? "Possible":"Impossible");
        pw.flush();
    }

    public static long mergeSort(int[] a, int low, int high) {
        if (low >= high) return 0;
        int mid = (high-low)/2+low;
        long inversions = 0;
        inversions += mergeSort(a, low, mid);
        inversions += mergeSort(a, mid+1, high);
        inversions += merge(a, low, mid, high);
        return inversions;
    }

    public static long merge(int[] a, int low, int mid, int high) {
        int N = high-low+1;
        int[] b = new int[N];
        int left = low, right = mid+1, bIdx = 0;
        long inversions = 0;
        while (left <= mid && right <= high) {
            if (a[left] <= a[right]) b[bIdx++] = a[left++];
            else {
                inversions += mid-left+1;
                b[bIdx++] = a[right++];
            }
        }
        while (left <= mid) b[bIdx++] = a[left++];
        while (right <= high) b[bIdx++] = a[right++];
        for (int k=0; k<N; ++k) a[low+k] = b[k];
        return inversions;
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
