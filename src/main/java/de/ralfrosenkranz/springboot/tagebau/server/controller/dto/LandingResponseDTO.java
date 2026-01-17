package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.util.List;

public class LandingResponseDTO {
    private Hero hero;
    private List<ProductCardDTO> topProducts;
    private List<CategoryViewDTO> categories;

    public Hero getHero() { return hero; }
    public void setHero(Hero hero) { this.hero = hero; }
    public List<ProductCardDTO> getTopProducts() { return topProducts; }
    public void setTopProducts(List<ProductCardDTO> topProducts) { this.topProducts = topProducts; }
    public List<CategoryViewDTO> getCategories() { return categories; }
    public void setCategories(List<CategoryViewDTO> categories) { this.categories = categories; }

    public static class Hero {
        private String kicker;
        private String title;
        private String text;
        public String getKicker() { return kicker; }
        public void setKicker(String kicker) { this.kicker = kicker; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
