import java.util.Random;

public class Dictionary {
	private Word[] NormalWords;
	private Word[] EnergyWords;
	private Random rand = new Random();

	public Dictionary() {
		// レポートにかかわる日本語（20個）
		// 第二引数は isEnergy なので、通常のお題は false にします
		NormalWords = new Word[] { new Word("れぽーと", false), new Word("かだい", false), new Word("ていしゅつ", false),
				new Word("しめきり", false), new Word("ぶんしょう", false), new Word("ふぁいる", false), new Word("ほぞん", false),
				new Word("いんさつ", false), new Word("ぱそこん", false), new Word("しりょう", false), new Word("ぶんけん", false),
				new Word("いんよう", false), new Word("まとめ", false), new Word("こうさつ", false), new Word("じっけん", false),
				new Word("けっか", false), new Word("もくじ", false), new Word("こうせい", false), new Word("てつや", false),
				new Word("ねおち", false), new Word("たんい", false), new Word("じゅぎょう", false), new Word("だいがく", false),
				new Word("きょうじゅ", false), new Word("はっぴょう", false), new Word("ぷれぜん", false), new Word("きまつてすと", false),
				new Word("ちゅうかん", false), new Word("さいしけん", false), new Word("せいせき", false), new Word("ひょうか", false),
				new Word("けんさく", false), new Word("ぶらうざ", false), new Word("こぴー", false), new Word("ぺーすと", false),
				new Word("きーぼーど", false), new Word("まうす", false), new Word("がめん", false), new Word("ばっくあっぷ", false),
				new Word("ごじだつじ", false), new Word("ていせい", false), new Word("へんしゅう", false), new Word("もじすう", false) };

		// エナジードリンクの商品名（5個）
		// こちらはエネルギー回復アイテムなので、true にします
		EnergyWords = new Word[] { new Word("もんすたーえなじー", true), new Word("れっどぶる", true), new Word("ぞーん", true),
				new Word("りあるごーるど", true), new Word("おろなみんしー", true), new Word("つばさをさずける", true) };
	}

	public Word getRandomNormalWord() {
		int index = rand.nextInt(NormalWords.length);
		return NormalWords[index];
	}

	public Word getRandomEnergyWord() {
		int index = rand.nextInt(EnergyWords.length);
		return EnergyWords[index];
	}
}
