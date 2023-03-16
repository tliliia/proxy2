package com.tronina.proxy.service;

import com.tronina.proxy.mapper.SearchItemMapper;
import com.tronina.proxy.model.SearchResult;
import com.tronina.proxy.modelDto.SearchItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceStackExchangeImpl implements DataService {
    private static final String ORDER_PARAM = "order";
    private static final String SORT_PARAM = "sort";
    private static final String SITE_PARAM = "site";
    private static final String INTITLE_PARAM = "intitle";

    @Value("${baseurl}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    @Override
    public List<SearchItemDto> search(String query) {
        URI uri = buildURI(baseUrl, query);
        SearchResult result = restTemplate.getForObject(uri, SearchResult.class);
        return SearchItemMapper.INSTANCE.toDto(result.getItems());
    }

    private URI buildURI(String baseUrl, String query) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("/search")
                .queryParam(ORDER_PARAM, "desc")
                .queryParam(SORT_PARAM, "activity")
                .queryParam(SITE_PARAM, "stackoverflow")
                .queryParam(INTITLE_PARAM, query)
                .build().toUri();
    }
}
