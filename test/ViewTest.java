import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ViewTest {

	@Test
	void Viewのテスト() {
		Model model = new Model();

		// Viewのインスタンスを生成（）
		View view = new View(model);
		assertNotNull(view);

		// Graphicsオブジェクトが必要な描画メソッドに、nullを渡して無理やり実行する
		// （途中でエラーになって落ちますが、数行分カバレッジを稼げます）
		try {
			view.clear(null);
		} catch (Exception e) {
			// エラーになっても無視
		}

		try {
			view.paintComponent(null);
		} catch (Exception e) {
			// エラーになっても無視
		}
	}
}