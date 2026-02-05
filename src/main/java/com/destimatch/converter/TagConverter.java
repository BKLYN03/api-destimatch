package com.destimatch.converter;

import com.destimatch.common.api.response.TagResponse;
import com.destimatch.entity.TagEntity;

public class TagConverter {

    public static TagResponse toResponse(TagEntity tagEntity) {
        if (tagEntity == null)
            return null;

        return new TagResponse(
                tagEntity.id.toString(),
                tagEntity.getName(),
                tagEntity.getCategory(),
                tagEntity.getDescription(),
                tagEntity.getColorHex()
        );
    }
}
