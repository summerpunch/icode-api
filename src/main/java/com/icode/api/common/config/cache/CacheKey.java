package com.icode.api.common.config.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class CacheKey {
    private String key;
    private String keyOri;
}