import java.util.*;
public class SequencePhenotype implements Phenotype {

    // constants
    @SuppressWarnings("unchecked")
    public final String[] NUCLEOBASES = new String[]{"A", "C", "G", "T"};

    // fields
    private String sequence;

    // constructor
    public SequencePhenotype() {
        int index = random();
        this.sequence = NUCLEOBASES[index];
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

        // find closest phenotype in history
        double closestDistance = 100.0;
        if (history.length > 0) {
            for (int i = 0; i < history.length; i++) {
                double thisDistance = distance(history[i]);
                if (thisDistance < closestDistance) {
                    closestDistance = thisDistance;
                }
                if (thisDistance < 0.01) {
                    break;
                }
            }
        }

        double risk = closestDistance * Parameters.smithConversion;
        double minRisk = 1.0 - Parameters.homologousImmunity;
        risk = Math.max(minRisk, risk);
        risk = Math.min(1.0, risk);

        return risk;

    }

    // returns a mutated copy, original SequencePhenotype is unharmed
    public Phenotype mutate() {
        int index = random();
        return new SequencePhenotype(this.sequence + NUCLEOBASES[index]);
    }

    public String toString() {
        return this.sequence;
    }

    private int random() {
        Random random = new Random();
        return random.nextInt(0, NUCLEOBASES.length - 1);
    }
}