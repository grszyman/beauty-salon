package pl.szymsoft.salon.root;

import pl.szymsoft.salon.root.FileProcessingResult.LineError;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@Value
class EntitiesAndProcessingResult<T> {

    List<T> entities;

    @NonNull
    FileProcessingResult result;

    @Builder
    private EntitiesAndProcessingResult(
            @Singular Collection<T> entities,
            @NonNull String requestParam,
            @Nullable String originalFilename,
            @Singular Collection<LineError> lineErrors,
            @Nullable String fileError
    ) {
        this.entities = List.copyOf(entities);
        result = FileProcessingResult.builder()
                .requestParam(requestParam)
                .originalFilename(originalFilename)
                .successes(entities.size())
                .lineErrors(lineErrors)
                .fileError(fileError)
                .build();
    }
}
