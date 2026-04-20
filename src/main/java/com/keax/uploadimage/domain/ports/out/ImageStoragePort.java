package com.keax.uploadimage.domain.ports.out;

import com.keax.uploadimage.domain.model.ImageFile;

public interface ImageStoragePort {

    String upload(ImageFile imageFile, String folder);

}
