package com.spboot.app.encoder;

import cn.hutool.crypto.SecureUtil;
import com.jntoo.db.utils.StringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Md5 密码加密技术
 */
public class Md5PasswordEncoder implements PasswordEncoder {

    /**
     * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or
     * greater hash combined with an 8-byte or greater randomly generated salt.
     *
     * @param rawPassword
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return SecureUtil.md5(rawPassword.toString());
    }

    /**
     * Verify the encoded password obtained from storage matches the submitted raw
     * password after it too is encoded. Returns true if the passwords match, false if
     * they do not. The stored password itself is never decoded.
     *
     * @param rawPassword     the raw password to encode and match
     * @param encodedPassword the encoded password from storage to compare with
     * @return true if the raw password, after encoding, matches the encoded password from
     * storage
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (StringUtil.isNullOrEmpty(rawPassword)) {
            return false;
        }
        if (StringUtil.isNullOrEmpty(encodedPassword)) {
            return false;
        }
        return encode(rawPassword).equals(encodedPassword);
    }
}
