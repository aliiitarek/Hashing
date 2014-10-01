import java.io.IOException;
import java.util.Random;


public class Test {
	public static void main(String[] args) throws IOException {
			DoubleHashing<Integer, Integer> hs = new DoubleHashing<>();
			Random r = new Random();
			int q = 10000000;
			while(q-- > 0) {
				int x = r.nextInt(2);
				int y = r.nextInt(10000);
				hs.put(x, y);
			}
			System.out.println("zebda bel kebda");
		}
}
