package fyi.shamim.aiagents.service.impl;

import fyi.shamim.aiagents.config.AiAgentConfigData;
import fyi.shamim.aiagents.exception.AiAgentException;
import fyi.shamim.aiagents.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/25/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Set<String> ALLOWED_EXT = Set.of(
            "png", "jpg", "jpeg", "webp", "gif", "svg", "pdf", "drawio", "puml"
    );

    private static final Map<String, String> CONTENT_TYPE_TO_EXT = Map.of(
            "image/png", "png",
            "image/jpeg", "jpg",
            "image/webp", "webp",
            "image/gif", "gif",
            "image/svg+xml", "svg",
            "application/pdf", "pdf"
    );

    private final Path root;

    public FileStorageServiceImpl(AiAgentConfigData aiAgentConfigData) throws IOException {
        this.root = Path.of(aiAgentConfigData.getUploadDir());
        Files.createDirectories(root);
    }

    @Override
    public String save(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new AiAgentException("File must not be empty!");
        }

        String ext = inferExtension(file);

        if (ext == null || !ALLOWED_EXT.contains(ext)) {
            throw new AiAgentException("Unsupported file type: " + file.getContentType());
        }

        String id = UUID.randomUUID().toString();
        String fileName = "%s.%s".formatted(id, ext);
        Path path = root.resolve(fileName).normalize();

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new AiAgentException("Couldn't save the file: " + file.getName(), e);
        }

    }

    @Override
    public Path resolvePath(String fileName) {
        return root.resolve(fileName);
    }

    private String inferExtension(MultipartFile file) {

        String originalName = file.getOriginalFilename();

        if (originalName != null && originalName.contains(".")) {
            return originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
        }

        return CONTENT_TYPE_TO_EXT.get(file.getContentType());
    }

}
