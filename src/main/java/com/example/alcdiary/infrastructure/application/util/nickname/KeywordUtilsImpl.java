package com.example.alcdiary.infrastructure.application.util.nickname;

import com.example.alcdiary.application.util.keyword.KeywordUtils;
import com.example.alcdiary.application.util.keyword.dto.DeleteKeywordDto;
import com.example.alcdiary.application.util.keyword.dto.GetRandomKeywordListDto;
import com.example.alcdiary.application.util.keyword.dto.SaveKeywordDto;
import com.example.alcdiary.infrastructure.entity.Nickname;
import com.example.alcdiary.infrastructure.entity.NicknamePK;
import com.example.alcdiary.infrastructure.jpa.NicknameJpaRepository;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class KeywordUtilsImpl implements KeywordUtils {

    private final NicknameJpaRepository nicknameJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public String generateNickname() {
        Nickname first = nicknameJpaRepository
                .findByLocation(Nickname.ELocation.FIRST.toString())
                .get(0);
        Nickname second = nicknameJpaRepository
                .findByLocation(Nickname.ELocation.SECOND.toString())
                .get(0);
        String generatedNickname = first.getKeyword() + " " + second.getKeyword();
        String randomNickname = generatedNickname;
        int count = 0;
        while (userJpaRepository.findByNickname(randomNickname).isPresent() && count < 10) {
            randomNickname = generatedNickname;
            int randomNumber = 1 + (int) Math.floor(Math.random() * 999);
            randomNickname += randomNumber;
            count++;
        }
        return randomNickname;
    }

    @Override
    public GetRandomKeywordListDto getRandomKeywordList() {
        List<Nickname> firstKeywordList = nicknameJpaRepository.findByLocation(Nickname.ELocation.FIRST);
        List<Nickname> secondKeywordList = nicknameJpaRepository.findByLocation(Nickname.ELocation.SECOND);
        return new GetRandomKeywordListDto(
                firstKeywordList.stream().map(Nickname::getKeyword).collect(Collectors.toList()),
                secondKeywordList.stream().map(Nickname::getKeyword).collect(Collectors.toList())
        );
    }

    @Override
    public void saveKeyword(SaveKeywordDto dto) {
        Nickname nickname = Nickname.of(dto.getKeyword(), Nickname.ELocation.from(dto.getLocation()));
        nicknameJpaRepository.save(nickname);
    }

    @Override
    public void deleteKeyword(DeleteKeywordDto dto) {
        NicknamePK nicknamePK = new NicknamePK(dto.getKeyword(), Nickname.ELocation.from(dto.getLocation()));
        nicknameJpaRepository.deleteById(nicknamePK);
    }
}
