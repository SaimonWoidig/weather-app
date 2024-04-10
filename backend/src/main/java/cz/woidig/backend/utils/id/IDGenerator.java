package cz.woidig.backend.utils.id;

public interface IDGenerator {
    String generateID();

    String generateID(int length);
}
