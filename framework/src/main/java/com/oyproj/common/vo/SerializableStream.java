package com.oyproj.common.vo;

import com.oyproj.common.utils.Base64DecodeMultipartFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * 序列化的 input stream
 *
 * @author oywq3000
 * @since 2025-12-30
 */
@Data
@NoArgsConstructor
public class SerializableStream {
    private String base64;

    public SerializableStream(InputStream inputStream){
        this.base64 = Base64DecodeMultipartFile.inputStreamToStream(inputStream);

    }
}
