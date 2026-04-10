package java.io.github.hirannor.hexadocs.infrastructure.aggregate;

import java.util.List;

public interface Evented {

    List<DomainEvent> events();


    void clearEvents();
}