package com.keax.uploadimage.domain.ports.in;

import com.keax.course.domain.model.Course;
import com.keax.uploadimage.domain.model.ImageFile;

public interface UploadImageCourseUseCase {

    Course uploadImageCourse(Long courseId, ImageFile image);
}
