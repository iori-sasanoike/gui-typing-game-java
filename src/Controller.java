import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.Timer;

public class Controller implements ActionListener, KeyListener, MouseListener {

    private static final int DELAY = 100; // msec
    private Model model;
    private Timer timer;
    private List<String> pressedKeys = new ArrayList<>();
    private boolean enableKeyRollover = false;

    public Controller(Model model) {
        // モデルを保持（イベントの通知先）
        this.model = model;
        timer = new Timer(DELAY, this);
    }

    public void start() {
        timer.start();
    }

    // キーの同時押しの許可・不許可モードの設定
    public void setKeyRollover(boolean b) { enableKeyRollover = b; }
    // キーの同時押しの許可・不許可モードの取得
    public boolean getKeyRollover() { return enableKeyRollover; }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.processTimeElapsed(DELAY);
    }

    private String getKeyName(KeyEvent e) {
        // 押されたキーを文字列表現に変換
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:         return "UP";
        case KeyEvent.VK_DOWN:       return "DOWN";
        case KeyEvent.VK_LEFT:       return "LEFT";
        case KeyEvent.VK_RIGHT:      return "RIGHT"; 
        case KeyEvent.VK_SHIFT:      return "SHIFT";
        case KeyEvent.VK_CONTROL:    return "CTRL";
        case KeyEvent.VK_ALT:        return "ALT";            
        case KeyEvent.VK_ESCAPE:     return "ESC";
        case KeyEvent.VK_ENTER:      return "ENTER";
        case KeyEvent.VK_BACK_SPACE: return "BS";
        default: return "" + e.getKeyChar();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String name = getKeyName(e);
        // 複数キーの同時押しを認めない場合
        if (!enableKeyRollover) {
            model.processKeyTyped(name);
            return;
        }
        // 押しっぱなしにした場合に同じキーが複数回登録されることを防ぐ
        if (!pressedKeys.contains(name)) 
            pressedKeys.add(getKeyName(e));        
        String s = pressedKeys.stream().collect(Collectors.joining("+"));
        model.processKeyTyped(s);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(getKeyName(e));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        model.processMousePressed(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }
}
