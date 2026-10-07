import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DictionaryTest {
	@Test
	void 辞書クラスの生成テスト() {
		Dictionary dict = new Dictionary();
		assertNotNull(dict); // 無事に作られたかチェック
	}
	
	void 辞書のメソッド呼び出しテスト() {
		Dictionary dict = new Dictionary();
		
		// ⚠️ Dictionaryクラスの中にあるメソッドを呼び出す
		 dict.getRandomNormalWord(); // メソッド名は実際のコードに合わせてください！
		 dict.getRandomEnergyWord();
	}
}