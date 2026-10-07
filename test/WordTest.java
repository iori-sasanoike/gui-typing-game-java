import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class WordTest {
	@Test
	void 単語の生成テスト() {

		Word word = new Word("test", false);
		assertNotNull(word);
	}

	void 単語のメソッド呼び出しテスト() {
		Word word = new Word("test", false);

		// ⚠️ Wordクラスの中にあるメソッドを呼び出す
		word.getWord();
		word.isEnergy();
	}
}