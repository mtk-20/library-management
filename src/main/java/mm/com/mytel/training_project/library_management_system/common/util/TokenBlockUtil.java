package mm.com.mytel.training_project.library_management_system.common.util;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HexFormat;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlockUtil {

    private final ConcurrentHashMap<String, Long> blockToken = new ConcurrentHashMap<>();

    public void block(String token, Date expiration) {
        blockToken.put(hashToken(token), expiration.getTime());
    }

    public boolean isBlocked(String token) {
        String tokenHash = hashToken(token);
        Long expiration = blockToken.get(tokenHash);

        if (expiration == null) {
            return false;
        }

        if (expiration < System.currentTimeMillis()) {
            blockToken.remove(tokenHash);
            return false;
        }

        return true;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
