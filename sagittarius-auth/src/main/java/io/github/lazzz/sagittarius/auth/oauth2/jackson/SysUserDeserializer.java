package io.github.lazzz.sagittarius.auth.oauth2.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import io.github.lazzz.sagittarius.auth.model.SysUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Set;

/**
 * 序列化配置
 *
 * @author Lazzz
 * @date 2025/10/12 16:01
 **/
public class SysUserDeserializer extends JsonDeserializer<SysUserDetails> {

    private static final TypeReference<Set<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<Set<SimpleGrantedAuthority>>() {
    };

    @Override
    public SysUserDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Set<? extends GrantedAuthority> authorities = mapper.convertValue(readJsonNode(jsonNode, "authorities"), SIMPLE_GRANTED_AUTHORITY_SET);
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        Long userId = jsonNode.get("userId").asLong();
        String username = jsonNode.get("username").asText();
        String password = passwordNode.asText();
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
        boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
        boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
        boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();
        return new SysUserDetails(userId, username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}

