import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class View extends JPanel {

	private Model model;

	// Sample instance variables:
	private Image image;
	private Image bgNight, bgDawn, bgMorning;
	private Image studentAwake, studentSleepy, studentSleep;

	public View(Model model) {
		this.model = model;

		// ★追加: 画像の読み込み
		bgNight = Toolkit.getDefaultToolkit().getImage(getClass().getResource("yoru.png"));
		bgDawn = Toolkit.getDefaultToolkit().getImage(getClass().getResource("asayake.png"));
		bgMorning = Toolkit.getDefaultToolkit().getImage(getClass().getResource("asa.png"));

		studentAwake = Toolkit.getDefaultToolkit().getImage(getClass().getResource("oki.png"));
		studentSleepy = Toolkit.getDefaultToolkit().getImage(getClass().getResource("hanbiraki.png"));
		studentSleep = Toolkit.getDefaultToolkit().getImage(getClass().getResource("neta.png"));

	}

	/**
	 * 画面を描画する
	 * 
	 * @param g 描画用のグラフィックスオブジェクト
	 */
	@Override
	public void paintComponent(Graphics g) {
		// 画面をいったんクリア
		clear(g);

		int state = model.getState();

		// ルール（ボスが来た）画面の描画
		if (state == Model.RULE_STATE) {
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
			g.setColor(Color.WHITE);
			g.drawString("【遊び方】", 50, 100);

			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
			g.drawString("・降ってくる単語をタイピングして消そう", 20, 150);
			// ★変更: 単語が落ちた時のペナルティを追記
			g.drawString("・単語が画面下まで落ちると寝落ち（ゲームオーバー）", 20, 200);
			// ★変更: エナジードリンクは無視できることを追記
			g.drawString("・エナジードリンク系（緑）は無視もOK！打てればボーナス(+100)", 20, 250);
			g.drawString("・7:00を迎えるとゲームオーバー", 20, 300);
			g.drawString("・1000文字達成でゲームクリア！", 20, 350);

			g.setColor(Color.YELLOW);
			g.drawString("Press ESC to Return", 50, 430); // 少し下にずらす

			getToolkit().sync();
			return;
		}

		/// ★追加・修正: タイトル画面の描画
		if (state == Model.TITLE_STATE) {
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
			g.setColor(Color.WHITE);
			// Xを150にして画面の真ん中に、Yを250に下げてバランスをとる
			g.drawString("深夜の寝落ち耐久レポート作成", 150, 250);

			// ★追加: 現在の難易度と変更方法の表示
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
			String diffStr = "NORMAL";
			int diff = model.getDifficulty();
			if (diff == Model.EASY)
				diffStr = "EASY";
			else if (diff == Model.HARD)
				diffStr = "HARD";
			g.setColor(Color.CYAN);
			g.drawString("難易度: " + diffStr, 300, 330);
			g.drawString(" (1:EASY, 2:NORMAL, 3:HARD で変更)", 180, 370);

			// スタートを促す文字の描画
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
			g.setColor(Color.YELLOW);
			g.drawString("Press ENTER to Start", 230, 450);

			getToolkit().sync();
			return; // タイトル画面の時はここで描画を終わらせる（他のUIを描画しない）
		}

		// --- ★追加: 背景の描画（時間によって切り替え） ---
		// 現在のゲーム内経過時間（分）を計算（Modelの getGameTimeString 内の計算と同じ）
		int gameMinute = (model.getTime() * 6) / 10;

		if (gameMinute >= 300) {
			// 5:00以降（夜明け）
			g.drawImage(bgMorning, 0, 0, 720, 720, this);
		} else if (gameMinute >= 180) {
			// 3:00以降（少し明るい）
			g.drawImage(bgDawn, 0, 0, 720, 720, this);
		} else {
			// それ以前（夜）
			g.drawImage(bgNight, 0, 0, 720, 720, this);
		}

		// --- ★追加: 学生の描画（文字の高さによって切り替え） ---
		int maxY = model.getMaxY();
		// 学生を表示するX座標, Y座標, 幅, 高さ（画像のサイズに合わせて調整してください！）
		int sx = 70, sy = 70, sw = 600, sh = 600;

		if (maxY > 500) {
			// 限界ラインギリギリ（目が閉じかけ）
			g.drawImage(studentSleep, sx, sy, sw, sh, this);
		} else if (maxY > 300) {
			// 少し下がってきた（半目）
			g.drawImage(studentSleepy, sx, sy, sw, sh, this);
		} else {
			// まだ余裕（起きている）
			g.drawImage(studentAwake, sx, sy, sw, sh, this);
		}

		// 描画する
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
		g.setColor(Color.MAGENTA);

//        g.drawString("Time: " + model.getTime(), 100, 150);
		// 入力中の文字は画面の下の方に配置
		g.drawString("Key Typed: " + model.getTypedChar(), 20, 660);
//        g.drawString("KeyRollover: " + model.getEnableKeyRollover(), 100, 250);
//        g.drawString("Mouse Pressed: " + model.getMX() + "," + model.getMY(), 100, 300);

		// 右上のタイマーを、幅720に収まるように 530 に変更
		g.drawString("Time: " + model.getGameTimeString(), 530, 50);

		// ★追加: 左上にレポートの文字数を表示 (現在の文字数 / 目標文字数)
		g.drawString("文字数: " + model.getCurrentLength() + " / " + Model.TARGET_LENGTH, 20, 50);

		// 画像の表示例
//        g.drawImage(image, model.getMX(), model.getMY(), this);

		// ★修正: リスト内の単語をすべてループして描画する
		for (FallingWord fw : model.getFallingWords()) {
			// エナジーワードなら黄色、そうでないなら白色に色をセットする
			if (fw.isEnergy()) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.YELLOW);
			}
			// セットした色で文字を描画
			g.drawString(fw.getText(), fw.getX(), fw.getY());
		}

		// ★追加: ゲーム状態に応じた大きな文字の表示
		if (state == Model.GAMEOVER_STATE) {
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
			g.setColor(Color.RED);
			// 画面の中央付近に表示
			g.drawString("GAME OVER", 190, 350);
		} else if (state == Model.GAMECLEAR_STATE) {
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
			g.setColor(Color.RED);
			g.drawString("GAME CLEAR", 190, 350);
		}

		// Linux でアニメーションをスムーズに描画するため（描画結果が勝手に間引かれることを防ぐ）
		getToolkit().sync();
	}

	/**
	 * 画面を黒色でクリア
	 * 
	 * @param g 描画用のグラフィックスオブジェクト
	 */
	public void clear(Graphics g) {
		Dimension size = getSize();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, size.width, size.height);
	}

}
