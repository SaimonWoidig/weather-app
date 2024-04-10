package cz.woidig.backend.utils.id;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public final class NanoIDGenerator implements IDGenerator {
    private final Random random;
    private final char[] alphabet;
    private final int idLength;

    public NanoIDGenerator(Random random, int idLength) {
        this(random, NanoIdUtils.DEFAULT_ALPHABET, idLength);
    }

    public NanoIDGenerator(Random random) {
        this(random, NanoIdUtils.DEFAULT_SIZE);
    }

    @Override
    public String generateID() {
        return NanoIdUtils.randomNanoId(random, alphabet, idLength);
    }

    @Override
    public String generateID(int length) {
        return NanoIdUtils.randomNanoId(random, alphabet, length);
    }
}
