import java.util.LinkedList;

public class Model {

	private View view;
	private Controller controller;

	// Sample instance variables:
	private int time;
	private String typedChar = "";
	private int mx;
	private int my;
	private boolean enableKeyRollover = false;

	private Romaji romajiConverter = new Romaji();
	private String displayString = ""; // 画面に表示するひらがな
	private String romajiBuffer = ""; // 未確定のローマ字を溜める

	private Dictionary dictionary = new Dictionary();
	private LinkedList<FallingWord> fallingWords = new LinkedList<>();

	// ★追加: 現在の文字数と目標文字数
	private int currentLength = 0;
	public final static int TARGET_LENGTH = 10000;

	public static final double ENERGY_DROP_RATE = 0.10;

	// ★追加: 状態を表す定数と、現在の状態を保持する変数
	public static final int PLAYING_STATE = 0;
	public static final int GAMEOVER_STATE = 1;
	public static final int GAMECLEAR_STATE = 2;
	public static final int TITLE_STATE = 3;
	public static final int RULE_STATE = 4;
	private int state = TITLE_STATE;
	private int previousState = TITLE_STATE;

	// ★追加: 難易度を表す定数と現在の難易度変数
	public static final int EASY = 0;
	public static final int NORMAL = 1;
	public static final int HARD = 2;
	private int difficulty = NORMAL;

	public Model() {
		view = new View(this);
		controller = new Controller(this);

	}

	public synchronized void processTimeElapsed(int msec) {
		// ★追加: プレイ中以外（クリアやゲームオーバー後）は時間を進めず、文字も降らせない
		if (state != PLAYING_STATE)
			return;
		time++;

		// ★追加：7時間（420分）経過したらゲームオーバーにする
		int gameMinute = (time * 6) / 10;
		if (gameMinute >= 420) {
			state = GAMEOVER_STATE;
			view.repaint();
			return; // ゲームオーバーになったらここで処理を終わる
		}

		// （0.1秒 × speed回）おきに新しいワードを生成
		int speed = 17; // デフォルト
		if (difficulty == EASY) {
			speed = 23; // ゆっくり
		} else if (difficulty == NORMAL) {
			speed = 17; // 普通
		} else if (difficulty == HARD) {
			speed = 14; // 速い
		}
		if (time % speed == 0) {
			Word newWord;

			if (Math.random() < ENERGY_DROP_RATE) {
				newWord = dictionary.getRandomEnergyWord();
			} else {
				newWord = dictionary.getRandomNormalWord();
			}

			// ★修正: FallingWordのコンストラクタに isEnergy() を追加で渡す
			fallingWords.add(new FallingWord(newWord.getWord(), newWord.isEnergy(), 0, 3));
		}
		// ★リスト内の文字をすべて落下させ、画面外のものを削除する
		LinkedList<FallingWord> nextWords = new LinkedList<>();
		for (FallingWord fw : fallingWords) {
			fw.update(); // 落下させる
			// 画面下部を超えていなければ、新しいリストに残す
			if (!fw.isOutOfScreen(720)) { // ※もし画面の高さを720などに変更している場合は、ここの数字も合わせます
				nextWords.add(fw);
			} else {
				// ★追加: 画面の一番下（最下層）まで落ちてしまったときの処理
				// それがエナジードリンク「以外（通常のお題）」だったらゲームオーバーにする
				if (!fw.isEnergy()) {
					state = GAMEOVER_STATE;
					view.repaint();
					return; // ゲームオーバーになったら以降の処理はせずにここで終わる
				}
				// エナジードリンクの場合は上の if ブロックをすり抜けるため、
				// ゲームオーバーにはならず、ただリストから消える（無視される）だけになります！
			}
		}
		fallingWords = nextWords; // リストを入れ替える（画面外に出たものは消える）

		view.repaint();
	}

	public synchronized void processKeyTyped(String typed) {
		// ★追加・修正: Escキーが押されたときの「ボスが来た画面」の切り替え
		if (typed.equals("ESC")) {
			if (state == RULE_STATE) {
				// 既にルール画面なら、記憶しておいた元の状態に戻す
				state = previousState;
			} else {
				// ルール画面でなければ、今の状態を記憶してからルール画面へ遷移
				previousState = state;
				state = RULE_STATE;
			}
			view.repaint();
			return; // 状態を切り替えたらここで入力を終わらせる
		}

		// ★追加: ルール画面を開いている最中は、他のキー入力をすべて無視する
		if (state == RULE_STATE) {
			return;
		}
		// ★追加: タイトル画面の処理（Enterが押されたらゲームスタート）
		// ★追加・修正: タイトル画面の処理（難易度選択を追加）
		if (state == TITLE_STATE) {
			if (typed.equals("1")) {
				difficulty = EASY;
				view.repaint();
			} else if (typed.equals("2")) {
				difficulty = NORMAL;
				view.repaint();
			} else if (typed.equals("3")) {
				difficulty = HARD;
				view.repaint();
			} else if (typed.equals("ENTER") || typed.equals("\n")) {
				// ゲームを初期化してスタート
				time = 0;
				currentLength = 0;
				fallingWords.clear();
				displayString = "";
				romajiBuffer = "";
				typedChar = "";
				state = PLAYING_STATE;
				view.repaint();
			}
			return;
		}

		// ★修正: プレイ中以外（クリア後やゲームオーバー後）はキー入力を無視する
		if (state != PLAYING_STATE)
			return;
		// --- バックスペースの処理 ---
		if (typed.equals("BS")) {

			// 1. まず未確定のローマ字バッファが溜まっているなら、そこから1文字削る
			if (romajiBuffer.length() > 0) {
				romajiBuffer = romajiBuffer.substring(0, romajiBuffer.length() - 1);
			}
			// 2. ローマ字バッファが空なら、既に確定したひらがな側から1文字削る
			else if (displayString.length() > 0) {
				displayString = displayString.substring(0, displayString.length() - 1);
			}

			// 画面表示用の文字列を再合成
			typedChar = displayString + romajiBuffer;
			view.repaint();
			return; // ★重要: バックスペースの時は、下の通常入力処理に進ませずにここで終わらせる！
		}

		// ★追加: --- Enterキーが押されたときの正誤判定処理 ---
		if (typed.equals("ENTER") || typed.equals("\n")) {
			boolean isHit = false; // 正解したかどうかのフラグ

			// 降ってきている単語リストをループして、入力文字(displayString)と一致するものがあるか探す
			for (int i = 0; i < fallingWords.size(); i++) {
				FallingWord fw = fallingWords.get(i);
				if (fw.getText().equals(displayString)) {

					// ★修正: エナジードリンクなら+100、通常なら文字数×10
					if (fw.isEnergy()) {
						currentLength += 100;
						currentLength += fw.getText().length() * 100;
					} else {
						currentLength += fw.getText().length() * 100;
					}

					fallingWords.remove(i); // 画面から消す
					isHit = true;

					// ★追加：目標文字数に達したらゲームクリアにする
					if (currentLength >= TARGET_LENGTH) {
						state = GAMECLEAR_STATE;
					}

					break;
				}
			}

//			if (isHit) {
//				// 正解したときの処理（後で「到達文字数を増やす」処理をここに追加できます）
//				System.out.println("正解！お題を消去しました");
//			} else {
//				// 不正解のときの処理（企画書の「ミスしたら即ゲームオーバー」処理を後でここに追加できます）
//				System.out.println("ミス！");
//			}

			// 正誤判定が終わったら、入力状態をすべてリセットして次のタイピングに備える
			displayString = "";
			romajiBuffer = "";
			typedChar = "";
			view.repaint();
			return; // Enterの処理はここで終わらせる
		}

		// --- ここから下は通常のアルファベット入力処理 ---
		romajiBuffer += typed;

		// 変換を試みる
		String result = romajiConverter.convert(romajiBuffer);

		// 変換できた（Mapにキーが存在した）場合のみ表示を更新しバッファをクリア
		if (!result.equals(romajiBuffer)) {
			displayString += result;
			romajiBuffer = ""; // 変換できたらバッファを空にする
		}

		typedChar = displayString + romajiBuffer;
		view.repaint();
	}

	// ★追加: ゲーム内時間（0:00〜7:00）を計算して文字列で返すメソッド
	public String getGameTimeString() {
		// time は 0.1秒ごとに1増えます。70秒経過時は time = 700 となります。
		// 700回呼ばれた時に、7時間（420分）進めたいので、
		// 経過分数 = (time * 420) / 700 ＝ (time * 6) / 10 と計算できます。
		int gameMinute = (time * 6) / 10;

		int hour = gameMinute / 60;
		int minute = gameMinute % 60;

		// もし7時間（AM 7:00）を超えたら、それ以上進まないように固定する
		if (hour >= 7) {
			hour = 7;
			minute = 0;
		}

		// 分が1桁のときに "0" を補う（例：0:5 ではなく 0:05 にする）
		String minuteStr = "";
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = "" + minute;
		}

		return hour + ":" + minuteStr;
	}

	public synchronized void processMousePressed(int x, int y) {
		mx = x;
		my = y;
		view.repaint();
	}

	public void start() {
		controller.start();
	}

	public View getView() {
		return view;
	}

	public Controller getController() {
		return controller;
	}

	public int getTime() {
		return time;
	}

	public String getTypedChar() {
		return typedChar;
	}

	public int getMX() {
		return mx;
	}

	public int getMY() {
		return my;
	}

	public boolean getEnableKeyRollover() {
		return enableKeyRollover;
	}

	public LinkedList<FallingWord> getFallingWords() {
		return fallingWords;
	}

	public int getCurrentLength() {
		return currentLength;
	}

	public int getState() {
		return state;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getMaxY() {
		int maxY = 0;
		for (FallingWord fw : fallingWords) {
			if (fw.getY() > maxY) {
				maxY = fw.getY();
			}
		}
		return maxY;
	}

}
