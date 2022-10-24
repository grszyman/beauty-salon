package pl.szymsoft.salon.root;

import com.opencsv.CSVReaderHeaderAwareBuilder;
import pl.szymsoft.salon.root.EntitiesAndProcessingResult.EntitiesAndProcessingResultBuilder;
import pl.szymsoft.salon.root.FileProcessingResult.LineError;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.StreamSupport.stream;

@Builder
class CsvEntityReader<T> {

    @NonNull
    private final String[] headers;
    @NonNull
    private final Function<String[], T> entityExtractor;

    EntitiesAndProcessingResult<T> readFrom(String requestParam, MultipartFile file) {

        var results = EntitiesAndProcessingResult.<T>builder()
                .requestParam(requestParam)
                .originalFilename(file.getOriginalFilename());
        try (
                var reader = new BufferedReader(new InputStreamReader(file.getInputStream(), UTF_8));
                var csvReader = new CSVReaderHeaderAwareBuilder(reader).build()
        ) {
            stream(csvReader.spliterator(), true)
                    .forEach(values -> Try.ofSupplier(() -> entityExtractor.apply(values))
                            .onFailure(ex -> addLineErrorToResult(results, values, ex))
                            .onSuccess(results::entity));

        } catch (IOException e) {
            results.fileError(e.getMessage());
        }
        return results.build();
    }

    private void addLineErrorToResult(EntitiesAndProcessingResultBuilder<T> results, String[] values, Throwable ex) {
        results.lineError(new LineError(ex.getMessage(), values));
    }
}
