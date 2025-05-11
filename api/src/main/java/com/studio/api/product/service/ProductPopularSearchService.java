package com.studio.api.product.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;

@PropertySource("classpath:application.yml")
@Service
@RequiredArgsConstructor
public class ProductPopularSearchService {

    private final StringRedisTemplate redisTemplate;
    private static final String POPULAR_SEARCH = "popular_search";

    @Value("${popular-search.ttl-hours}")
    private int ttlHours;


    // 검색어 누적
    public void incrementSearchCount(String keyword) {
        Set<String> allKeywords = redisTemplate.opsForZSet().range(POPULAR_SEARCH, 0, -1);
        boolean keywordExists = false;
        String normalizedKeyword = keyword.toLowerCase();

        for (String storedKeyword : allKeywords) {
            if (storedKeyword.equalsIgnoreCase(normalizedKeyword)) { // 정확히 일치하는 경우만 증가
                redisTemplate.opsForZSet().incrementScore(POPULAR_SEARCH, storedKeyword, 1);
                keywordExists = true;
                break; // 찾았으면 종료
            }
        }

        if (!keywordExists) {
            redisTemplate.opsForZSet().add(POPULAR_SEARCH, keyword, 1); // 초깃값 1로 등록
        }
    }

    @Async
    public void incrementSearchCountAsync(String keyword) {
        incrementSearchCount(keyword);
    }


    // 인기 검색어 가져오기
    public Set<String> getTopSearchKeywords(int topN) {
        return redisTemplate.opsForZSet().reverseRange("popular_search", 0, topN - 1);
    }

    // 검색어 TTL 설정
    public void setSearchKeywordTTL() {
        redisTemplate.expire("popular_search", Duration.ofHours(ttlHours));  // TTL 설정 (24시간)
    }

    // 검색어의 검색 횟수 가져오기
    public List<Double> getSearchCount(List<String> keywords) {
        Set<TypedTuple<String>> allEntries = redisTemplate.opsForZSet().rangeWithScores(POPULAR_SEARCH, 0, -1);

        // ZSet에서 모든 엔트리 중에서 부분 일치하는 것만 찾아서 카운트 반환
        return allEntries.stream()
            .filter(entry -> keywords.stream().anyMatch(keyword -> entry.getValue().toLowerCase().contains(keyword.toLowerCase())))
            .map(entry -> entry.getScore())
            .collect(Collectors.toList());
    }



}