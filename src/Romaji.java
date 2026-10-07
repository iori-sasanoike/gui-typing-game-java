import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Romaji {
	private Map<String, String> dictionary = new HashMap<>();

	// ★1. コンストラクタを実装（クラスが作られた時に自動で辞書を読み込む）
	public Romaji() {
		loadDictionary("romaji.txt");
	}

	// ★2. 辞書ファイルを読み込むメソッド（授業資料「09 Java 補足.pdf」の基準に準拠）
	private void loadDictionary(String filename) {
		// try-with-resources構文で自動でファイルを閉じる
		try (InputStream is = getClass().getResourceAsStream(filename);
				BufferedReader reader = (is == null) ? null : new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

			if (reader == null) {
				System.out.println("辞書ファイルが見つかりません: " + filename);
				return;
			}

			String line;
			while ((line = reader.readLine()) != null) {
				// ファイル内のカンマ区切り、イコール区切りを分解してMapに格納
				String[] pairs = line.split(",");
				for (String pair : pairs) {
					String[] parts = pair.split("=");
					if (parts.length == 2) {
						dictionary.put(parts[0], parts[1]); // 例: ka -> か
					}
				}
			}
		} catch (IOException e) { // 明示的に入出力例外をキャッチ
			System.out.println("辞書ファイルの読み込みに失敗しました: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// 3. 変換ロジック（子音連続・小さい「つ」対応版）
	public String convert(String input) {
		// まずは辞書ファイル（Map）にそのまま登録があるか完全一致チェック (ka -> か)
		if (dictionary.containsKey(input)) {
			return dictionary.get(input);
		}

		// 小さい「つ」の判定
		if (input.length() >= 2) {
			char first = input.charAt(0);
			char second = input.charAt(1);

			if (first == second && first != 'n' && (first >= 'a' && first <= 'z')) {
				String remaining = input.substring(1);
				String convertedRemaining = dictionary.getOrDefault(remaining, remaining);

				if (!convertedRemaining.equals(remaining) || remaining.length() >= 2) {
					return "っ" + convertedRemaining;
				}
			}
		}

		// どちらでもなければ入力途中
		return input;
	}
}