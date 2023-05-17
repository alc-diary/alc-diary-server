package com.alc.diary.application.auth.strategy;

import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyRequest;
import com.alc.diary.application.auth.strategy.dto.SocialLoginStrategyResponse;
import com.alc.diary.domain.auth.error.AuthError;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.enums.SocialType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AppleLoginService implements SocialLoginStrategy {

    private static final URL jwkSetUrl;
    private static final int CONNECTION_TIMEOUT = 5_000; // milliseconds
    private static final int READ_TIMEOUT = 5_000; // milliseconds
    private static final int SIZE_LIMIT = 10_240; // bytes
    private static final DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
    private static final DefaultResourceRetriever resourceRetriever =
            new DefaultResourceRetriever(CONNECTION_TIMEOUT, READ_TIMEOUT, SIZE_LIMIT);
    private static final JWKSource<SecurityContext> keySource;
    private static final JWSVerificationKeySelector<SecurityContext> keySelector;

    static {
        try {
            jwkSetUrl = new URL("https://appleid.apple.com/auth/keys");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        keySource = JWKSourceBuilder.create(jwkSetUrl, resourceRetriever)
                .build();
        keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
    }

    @Override
    public SocialLoginStrategyResponse login(SocialLoginStrategyRequest request) {
        String appleJwtToken = request.socialAccessToken();
        JWTClaimsSet claimSet = getJwtClaimsSet(appleJwtToken);
        validJwt(claimSet);

        return new SocialLoginStrategyResponse(
                SocialType.APPLE,
                claimSet.getSubject(),
                null,
                (String) claimSet.toJSONObject().get("email"),
                null,
                null
        );
    }

    private static JWTClaimsSet getJwtClaimsSet(String appleJwtToken) {
        JWTClaimsSet claimSet;
        try {
            claimSet = jwtProcessor.process(appleJwtToken, null);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return claimSet;
    }

    private static void validJwt(JWTClaimsSet claimSet) {
        Date now = new Date();
        if (claimSet.getExpirationTime().before(now)) {
            throw new DomainException(AuthError.EXPIRED_JWT);
        }
    }
}
