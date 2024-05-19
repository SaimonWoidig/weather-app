package cz.woidig.backend.service.id;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Service;

@Service
public class NanoIdGenerationService implements IdGenerationService {
    @Override
    public String generateId() {
        return NanoIdUtils.randomNanoId();
    }
}
