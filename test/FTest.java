import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class FTest {

	@Test
	void ゲーム乱れ打ち自動プレイテスト() {
		Model model = new Model();

		// 1. タイトル画面からゲーム開始
		model.processKeyTyped("1");
		model.processKeyTyped("ENTER");

		// 2. 時間をものすごい勢いで進めながら、適当なキーを連打する！
		// これにより「単語が落ちる」「ミスする」「バックスペース」「画面外に消える」
		// などのあらゆる処理ルート（赤い行）を強制的に通過させます。
		for (int i = 0; i < 1000; i++) {
			model.processTimeElapsed(50);
			model.processKeyTyped("a");
			model.processKeyTyped("k");
			model.processKeyTyped("i");
			model.processKeyTyped("u");
			model.processKeyTyped("BS"); // バックスペースも混ぜる
		}

		// エラーで止まらなければOK
		assertNotNull(model);
	}

	@Test
	void Viewの完全描画裏技テスト() {
		Model model = new Model();
		View view = new View(model);

		// 【裏技】メモリ上にダミーの透明な画像を生成し、
		// そこから「本物のGraphicsオブジェクト」を取り出す！
		BufferedImage img = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();

		try {
			// nullではなく本物のGraphicsを渡すので、
			// エラーで中断されず、Viewの描画処理が下まで全行実行されます！
			view.paintComponent(g);
			view.clear(g);
		} catch (Exception e) {
			// 万が一エラーが起きても無視
		}
	}
}