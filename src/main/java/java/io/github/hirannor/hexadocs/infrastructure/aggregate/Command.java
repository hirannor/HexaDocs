package java.io.github.hirannor.hexadocs.infrastructure.aggregate;

import java.time.Instant;

public interface Command {

    static Instant now() {
        return Instant.now();
    }

    CommandId id();

}