package com.keax.uploadimage.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageFile {

    private String originalName;
    private String contentType;
    private byte[] bytes;

    public boolean isEmpty() {
        return bytes == null || bytes.length == 0;
    }

}
