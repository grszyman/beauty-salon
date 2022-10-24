package pl.szymsoft.salon.root;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value
@Builder(toBuilder = true)
@JsonInclude(NON_EMPTY)
class FileProcessingResult {

    @NonNull
    String requestParam;

    @Nullable
    String originalFilename;

    int successes;

    @Singular
    List<LineError> lineErrors;

    @Nullable
    String fileError;

    public int getProcessed() {
        return successes + lineErrors.size();
    }

    public int getErrors() {
        return lineErrors.size();
    }

    record LineError(String message, Object value) {
    }
}
