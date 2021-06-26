package in.ongrid.kshitijroy.model.dto;

import in.ongrid.kshitijroy.model.entity.Category;

import java.util.*;

public class CategoryResponseDTO {
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
