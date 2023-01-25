package org.spsochnev.vacancies.collector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class VacancySource {

    private static final Logger LOG = LoggerFactory.getLogger(VacancySource.class);

    @Value("${vacancy.source.result.max.pages}")
    private long searchResultMaxPages;

    @Value("${vacancy.source.server.url}")
    private String serverURL;

    @Value("${vacancy.source.vacancies}")
    private String vacancies;

    @Value("${vacancy.source.search.params}")
    private String searchParams;

    @Value("${vacancy.source.page}")
    private String page;

    @Value("${vacancy.source.keyword.blacklist}")
    private List<String> keywordBlacklist;

    private HttpClient client = HttpClient.newHttpClient();

    private JSONParser parser = new JSONParser();

    public VacancySource() {
    }

    public List<Vacancy> getVacancies() {
        List<String> vacanciesListPages = getVacanciesListPages(serverURL + vacancies + searchParams);
        if (vacanciesListPages.isEmpty()) {
            LOG.info("No vacancies found");
        }
        return vacanciesListPages.stream().
                flatMap(page1 -> retrieveVacanciesFromPage(page1).stream()).
                collect(Collectors.toList());
    }

    private List<String> getVacanciesListPages(String searchURL) {
        String searchResult = getDataFromURL(searchURL);
        long pagesNum = getNumberOfPagesForSearchResult(searchResult);

        return LongStream.range(0, pagesNum).
                mapToObj(n -> getDataFromURL(searchURL + page + n)).
                collect(Collectors.toList());
    }

    private String getDataFromURL(String requestURL) {
        LOG.info("Getting info from: " + requestURL);
        URI requestURI = URI.create(requestURL);
        HttpRequest request = HttpRequest.newBuilder().
                uri(requestURI).
                build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
                thenApply(HttpResponse::body).
                join();
    }

    private long getNumberOfPagesForSearchResult(String searchResult) {
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(searchResult);
            long pagesNum = (Long) json.get("pages");
            return Math.min(pagesNum, searchResultMaxPages);
        } catch (ParseException e) {
            LOG.error("Error while parsing JSON occurred: " + e.getMessage());
            return -1;
        }
    }

    private List<Vacancy> retrieveVacanciesFromPage(String page) {
        List<Vacancy> vacPage = new ArrayList<>();
        JSONObject jsonPage;
        long pageNum = -1L;
        try {
            jsonPage = (JSONObject) parser.parse(page);
            List<Integer> idspage = getIdsFromItems(jsonPage);
            idspage.forEach(id -> vacPage.add(getVac(id)));
            pageNum = (Long) jsonPage.get("page");
        } catch (ParseException e) {
            LOG.error("Error while parsing JSON occurred: " + e.getMessage());
        }
        LOG.info("Page " + pageNum + " processed");
        return vacPage;
    }

    private List<Integer> getIdsFromItems(JSONObject wholeJson) {
        JSONArray items = (JSONArray) wholeJson.get("items");
        List<Integer> ids = new ArrayList<>();
        items.forEach(item -> {
            JSONObject it = (JSONObject) item;
            String name = (String) it.get("name");
            if (name != null) {
                if (isJavaDev(name)) {
                    String idObj = (String) it.get("id");
                    if (idObj != null) ids.add(Integer.parseInt(idObj));
                }
            }
        });
        return ids;
    }

    private boolean isJavaDev(String name) {
        name = name.toLowerCase();
        if (!name.contains("java")) return false;
        return keywordBlacklist.stream().noneMatch(name::contains);
    }

    private Vacancy getVac(Integer id) {
        return parseVacancy(getDataFromURL(serverURL + vacancies + "/" + id));
    }

    private Vacancy parseVacancy(String body) {
        Vacancy vac = new Vacancy();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(body);
        } catch (ParseException e) {
            LOG.error("Error while parsing JSON occurred: " + e.getMessage());
        }
        if (json != null) {
            Integer id = Integer.parseInt(json.get("id").toString());
            vac.setId(id);

            String name = (String) json.get("name");
            vac.setTitle(name);

            JSONObject salary = (JSONObject) json.get("salary");
            if (salary != null) {
                Long from = (Long) salary.get("from");
                if (from != null) {
                    if (from < 500) { // likely salary is in thousands, multiplying
                        from = from * 1000;
                    }
                    vac.setSalaryFrom(from.intValue());
                }

                Long to = (Long) salary.get("to");
                if (to != null) {
                    if (to < 500) { // likely salary is in thousands, multiplying
                        to = to * 1000;
                    }
                    vac.setSalaryTo(to.intValue());
                }

                String curr = (String) salary.get("currency");
                if (curr != null) {
                    vac.setCurrency(new Currency(curr));
                }
            }

            JSONObject exp = (JSONObject) json.get("experience");
            String expId = (String) exp.get("id");
            vac.setExperience(new Experience(expId));

            String descr = (String) json.get("description");
            vac.setDescription(descr);

            JSONArray items = (JSONArray) json.get("key_skills");
            Set<Skill> keySkills = new HashSet<>();
            items.forEach(item -> {
                JSONObject it = (JSONObject) item;
                String skill = (String) it.get("name");
                keySkills.add(new Skill(skill));
            });
            vac.setKeySkills(keySkills);
        }
        return vac;
    }

}
