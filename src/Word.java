
public class Word {
	private String word;
	private boolean Energy;

	public Word(String w, boolean e) {
		this.word = w;
		this.Energy = e;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public boolean isEnergy() {
		return this.Energy;
	}
}
