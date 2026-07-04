package fyi.shamim.aiagents.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/25/26
 * Email: mdshamim723@gmail.com
 */

public interface FileStorageService {

    String save(MultipartFile file);

    Path resolvePath(String fileName);

}
