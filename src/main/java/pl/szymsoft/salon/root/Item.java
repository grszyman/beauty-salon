package pl.szymsoft.salon.root;

import lombok.Builder;

@Builder
record Item<T>(
        T value,
        String[] source) {
}
