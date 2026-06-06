package supnum.projet.Library.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleBooksService {

    private final RestClient restClient;

    public GoogleBooksService() {
        this.restClient = RestClient.create("https://www.googleapis.com/books/v1");
    }

    public List<GoogleBook> searchBooks(String query, int maxResults) {
        try {
            JsonNode root = restClient.get()
                .uri("/volumes?q={query}&maxResults={max}", query, Math.min(maxResults, 40))
                .retrieve()
                .body(JsonNode.class);

            List<GoogleBook> results = new ArrayList<>();
            if (root != null && root.has("items")) {
                for (JsonNode item : root.get("items")) {
                    JsonNode info = item.get("volumeInfo");
                    if (info == null) continue;

                    GoogleBook book = new GoogleBook();
                    book.setGoogleId(item.get("id").asText());
                    book.setTitle(getText(info, "title"));
                    book.setSubtitle(getText(info, "subtitle"));
                    book.setAuthors(getList(info, "authors"));
                    book.setIsbn(extractIsbn(info));
                    book.setPublisher(getText(info, "publisher"));
                    book.setPublishedDate(getText(info, "publishedDate"));
                    book.setDescription(getText(info, "description"));
                    book.setPageCount(getInt(info, "pageCount"));
                    book.setLanguage(getText(info, "language"));
                    book.setThumbnail(extractThumbnail(info));
                    book.setCategories(getList(info, "categories"));

                    results.add(book);
                }
            }
            return results;
        } catch (Exception e) {
            return List.of();
        }
    }

    public GoogleBook findByIsbn(String isbn) {
        List<GoogleBook> results = searchBooks("isbn:" + isbn, 1);
        return results.isEmpty() ? null : results.getFirst();
    }

    private String getText(JsonNode node, String field) {
        return node.has(field) ? node.get(field).asText() : null;
    }

    private int getInt(JsonNode node, String field) {
        return node.has(field) ? node.get(field).asInt() : 0;
    }

    private List<String> getList(JsonNode node, String field) {
        List<String> list = new ArrayList<>();
        if (node.has(field)) {
            node.get(field).forEach(n -> list.add(n.asText()));
        }
        return list;
    }

    private String extractIsbn(JsonNode info) {
        if (info.has("industryIdentifiers")) {
            for (JsonNode id : info.get("industryIdentifiers")) {
                String type = id.get("type").asText();
                if ("ISBN_13".equals(type) || "ISBN_10".equals(type)) {
                    return id.get("identifier").asText();
                }
            }
        }
        return null;
    }

    private String extractThumbnail(JsonNode info) {
        if (info.has("imageLinks")) {
            JsonNode links = info.get("imageLinks");
            if (links.has("thumbnail")) {
                return links.get("thumbnail").asText().replace("http://", "https://");
            }
        }
        return null;
    }

    public static class GoogleBook {
        private String googleId;
        private String title;
        private String subtitle;
        private List<String> authors;
        private String isbn;
        private String publisher;
        private String publishedDate;
        private String description;
        private int pageCount;
        private String language;
        private String thumbnail;
        private List<String> categories;

        public String getGoogleId() { return googleId; }
        public void setGoogleId(String v) { this.googleId = v; }
        public String getTitle() { return title; }
        public void setTitle(String v) { this.title = v; }
        public String getSubtitle() { return subtitle; }
        public void setSubtitle(String v) { this.subtitle = v; }
        public List<String> getAuthors() { return authors; }
        public void setAuthors(List<String> v) { this.authors = v; }
        public String getIsbn() { return isbn; }
        public void setIsbn(String v) { this.isbn = v; }
        public String getPublisher() { return publisher; }
        public void setPublisher(String v) { this.publisher = v; }
        public String getPublishedDate() { return publishedDate; }
        public void setPublishedDate(String v) { this.publishedDate = v; }
        public String getDescription() { return description; }
        public void setDescription(String v) { this.description = v; }
        public int getPageCount() { return pageCount; }
        public void setPageCount(int v) { this.pageCount = v; }
        public String getLanguage() { return language; }
        public void setLanguage(String v) { this.language = v; }
        public String getThumbnail() { return thumbnail; }
        public void setThumbnail(String v) { this.thumbnail = v; }
        public List<String> getCategories() { return categories; }
        public void setCategories(List<String> v) { this.categories = v; }
    }
}
