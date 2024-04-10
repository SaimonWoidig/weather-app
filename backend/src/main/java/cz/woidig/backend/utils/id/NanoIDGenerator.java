package cz.woidig.backend.utils.id;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public final class NanoIDGenerator implements IDGenerator {
    public static final char[] DEFAULT_ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '-', '_'
    };
    public static final int DEFAULT_LENGTH = 12;

    private final Random random;
    private final char[] alphabet;
    private final int idLength;

    public NanoIDGenerator(Random random, int idLength) {
        this(random, DEFAULT_ALPHABET, idLength);
    }

    public NanoIDGenerator(Random random) {
        this(random, DEFAULT_LENGTH);
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
