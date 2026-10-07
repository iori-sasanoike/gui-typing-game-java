import java.util.Random;

public class FallingWord {
	private String text; // お題の文字列
	private int x, y; // 現在の座標
	private int speed; // 落下速度
	private boolean isEnergy;
	
	private static Random rand = new Random();

	// コンストラクタ（初期状態をセットする）
	public FallingWord(String text, boolean isEnergy, int y, int speed) {
        this.text = text;
        this.isEnergy = isEnergy; // ★追加
        this.x = rand.nextInt(500) + 50; 
        this.y = y;
        this.speed = speed;
    }

	// 落下処理（自分自身のy座標を増やす）
	public void update() {
		y += speed;
	}

	// 画面外に出たかの判定
	public boolean isOutOfScreen(int screenHeight) {
		return y > screenHeight;
	}

	public String getText() {
		return text;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	
	public boolean isEnergy() {
        return isEnergy;
    }
}
