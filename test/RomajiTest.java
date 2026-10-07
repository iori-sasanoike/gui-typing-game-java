import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RomajiTest {

	@Test
	void ローマ字からひらがなへの変換テスト() {
		Romaji romaji = new Romaji();

		// 変換が正しく行われるかテスト
		assertEquals("ね", romaji.convert("ne"));
		assertEquals("お", romaji.convert("o"));
		assertEquals("ち", romaji.convert("chi"));

		// 小さい「っ」などの特殊な変換もテスト
		assertEquals("っ", romaji.convert("xtsu"));
		assertEquals("ん", romaji.convert("nn"));
	}

	@Test
	void 変換できない文字列はそのまま残るかテスト() {
		Romaji romaji = new Romaji();

		// 変換途中のアルファベットがそのまま返ってくるか
		assertEquals("n", romaji.convert("n"));
		assertEquals("k", romaji.convert("k"));
	}
}