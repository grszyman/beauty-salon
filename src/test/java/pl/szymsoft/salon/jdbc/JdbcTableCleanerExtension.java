package pl.szymsoft.salon.jdbc;

import lombok.Builder;
import lombok.Singular;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Set;
import java.util.function.Supplier;

@Builder
public class JdbcTableCleanerExtension implements BeforeEachCallback {

    private final Supplier<String> url;
    private final Supplier<String> username;
    private final Supplier<String> password;
    @Singular
    private final Set<String> tables;

    @Override
    public void beforeEach(ExtensionContext context) {
        var dataSource = new SingleConnectionDataSource(url.get(), username.get(), password.get(), false);
        var jdbcTemplate = new JdbcTemplate(dataSource);
        tables.stream()
                .map(table -> "DELETE FROM " + table)
                .forEach(jdbcTemplate::execute);
    }
}
