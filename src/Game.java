import javax.swing.JFrame;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Game extends JFrame {

    /** 初期ウィンドウサイズ（高さ） */
    public static final int WIN_HEIGHT = 720;
    /** 初期ウィンドウサイズ（幅） */
    public static final int WIN_WIDTH = 720;

    private Model model = null;

    public Game() {
        // Window タイトルの設定
        setTitle("GUI Sample");
        // Window を閉じるボタンを有効にする
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Window サイズの設定
        setSize(WIN_WIDTH, WIN_HEIGHT);

        // モデルの生成
        model = new Model();

        // この Window に view を登録
        setContentPane(model.getView());
        // キーボードイベントのリスナとして controller を登録
        addKeyListener(model.getController());
        // マウスイベントのリスナとして controller を登録
        model.getView().addMouseListener(model.getController());
        // この Window を表示
        setVisible(true);
    }

    public void start() {
        // ゲーム開始
        model.start();
    }

    /** 起動用 main 関数 */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
