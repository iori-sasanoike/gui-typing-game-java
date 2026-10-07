import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ModelTest {

	@Test
	void プレイ中の時間経過のテスト() {
		Model model = new Model();

		// 1. まずENTERキーを入力して、タイトル画面からプレイ中(PLAYING_STATE)へ切り替える
		model.processKeyTyped("ENTER");

		// 2. その状態で時間を進める
		int initialTime = model.getTime();
		model.processTimeElapsed(100);

		// 3. プレイ中なら、時間経過イベントによって時刻が1増加するか
		assertEquals(initialTime + 1, model.getTime());
	}

	@Test
	void タイトル画面での難易度変更テスト() {
		Model model = new Model();

		// 1キーを入力するとEASYになるか
		model.processKeyTyped("1");
		assertEquals(Model.EASY, model.getDifficulty());

		// 3キーを入力するとHARDになるか
		model.processKeyTyped("3");
		assertEquals(Model.HARD, model.getDifficulty());

		// 2キーを入力するとNORMALになるか
		model.processKeyTyped("2");
		assertEquals(Model.NORMAL, model.getDifficulty());
	}

	@Test
	void タイトル画面からゲーム開始への遷移テスト() {
		Model model = new Model();

		// 初期状態はタイトル画面(TITLE_STATE)であるか
		assertEquals(Model.TITLE_STATE, model.getState());

		// ENTERキーを押すと、プレイ中(PLAYING_STATE)へ画面が切り替わるか
		model.processKeyTyped("ENTER");
		assertEquals(Model.PLAYING_STATE, model.getState());
	}

	@Test
	void バックスペースキーの安全テスト() {
		Model model = new Model();

		// 文字が何もない空の状態でバックスペースを押しても、
		// エラー（例外）で強制終了にならず、バッファが空のまま維持されるか
		model.processKeyTyped("BS");
		model.processKeyTyped("BACK_SPACE");
		assertEquals("", model.getTypedChar());
	}
}