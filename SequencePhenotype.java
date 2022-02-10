import java.util.*;
import java.lang.*;
public class SequencePhenotype implements Phenotype {

    // constants
    @SuppressWarnings("unchecked")
    public final String[] NUCLEOBASES = new String[]{"A", "C", "G", "T"};

    // fields
    private String sequence;

    // constructor
    public SequencePhenotype() {
        this.sequence = "";
        Random rand = new Random();
        for (int i = 0; i < Parameters.sequence.length(); i++) {
            int indexN = rand.nextInt(0, this.NUCLEOBASES.length - 1);
            this.sequence += this.NUCLEOBASES[indexN];
        }
    }
    public SequencePhenotype(String sequence) {
        sequence = sequence.toUpperCase();
        for (int i = 0; i < sequence.length(); i++) {
            String sequenceChar = ("" + sequence.charAt(i));
            boolean contains = Arrays.stream(NUCLEOBASES).anyMatch(sequenceChar::equals);
            if (!contains) {
                throw new IllegalArgumentException(sequenceChar + " is not a valid nucleobase!");
            }
        }
        this.sequence = sequence;
    }

    public String getSequence() {
        return this.sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public double distance(Phenotype p) {
        // String validation
        if (!(p instanceof SequencePhenotype)) {
            throw new IllegalArgumentException("Parameter Phenotype p should be an instance of SequencePhenotype!");
        }

        SequencePhenotype seqP = (SequencePhenotype) p;

        String seq2 = seqP.getSequence();
        // equal length validation
        if (this.sequence.length() != seq2.length()) {
            System.out.println("s1 " + this.sequence);
            System.out.println("s2 " + seq2.toString());
            throw new IllegalArgumentException("Sequence lengths are not equal!");
        }

        // Calculates the hamming distance between this.sequence and seq2
        int hammingDistance = 0;
        for (int i = 0; i < this.sequence.length(); i++) {
            if (this.sequence.charAt(i) != seq2.charAt(i)) {
                hammingDistance++;
            }
        }

        return (double) hammingDistance;
    }

    // cross immunity between a virus phenotype and a host's immune history
    // here encoded more directly as risk of infection, which ranges from 0 to 1
    public double riskOfInfection(Phenotype[] history) {
        double risk = 1;
        for (Phenotype sequence : history) {
            SequencePhenotype seq = (SequencePhenotype) sequence;
            if(seq.getSequence().length() == this.sequence.length()) {
                risk *= this.distance(seq);
            }
        }
        return risk;
    }

    // returns a mutated copy, original SequencePhenotype is unharmed
    public Phenotype mutate() {
        Random rand = new Random();
        int index = rand.nextInt(0, this.sequence.length() - 1);
        int indexN = rand.nextInt(0, this.NUCLEOBASES.length - 1);

        // substitute a random index of sequence with a random nucleobase
        StringBuilder mutated = new StringBuilder(this.sequence);
        mutated.setCharAt(index, this.NUCLEOBASES[indexN].charAt(0));

        return new SequencePhenotype(mutated.toString());
    }

    public String toString() {
        return this.sequence;
    }

    private int random() {
        Random random = new Random();
        return random.nextInt(0, NUCLEOBASES.length - 1);
    }
}