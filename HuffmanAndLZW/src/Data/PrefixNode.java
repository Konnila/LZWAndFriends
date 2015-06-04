package Data;

public class PrefixNode {
    private char character;
    private int amount;

    public PrefixNode(char character, int amount) {
        this.character = character;
        this.amount = amount;
    }

    public PrefixNode(char character) {
        this.character = character;
    }
    

    public char getCharacter() {
        return character;
    }

    public int getAmount() {
        return amount;
    }
    
    public void increment() {
        amount++;
    }
    
}
