import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FallingWordTest {
	@Test
	void 落下単語の生成テスト() {
		// ※もしFallingWordにコンストラクタ（引数）が必要なエラーが出たら、
		// new FallingWord("test", 100, 100, 5, false); のように適当な値を入れてください！
		FallingWord fw = new FallingWord("test", false, 100, 5);
		assertNotNull(fw);
	}

	@Test
	void 落下単語のメソッド呼び出しテスト() {
		FallingWord fw = new FallingWord("test", false, 100, 5); // 引数エラーが出たら適当な数字や文字を入れる

		fw.getText();
		fw.getX();
		fw.getY();
//		 fw.getSpeed();
		fw.isEnergy();
	}
}