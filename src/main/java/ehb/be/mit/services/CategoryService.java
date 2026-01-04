package ehb.be.mit.services;

import ehb.be.mit.models.Category;
import ehb.be.mit.repositories.ICategoryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {

    private final ICategoryRepository categories;

    public CategoryService(ICategoryRepository categories) {
        this.categories = categories;
    }

    /**
     * Resolves a category based on either:
     *  - a provided UUID, or
     *  - a new name to create (case-insensitive lookup first)
     *
     * Returns null if neither are provided.
     */
    public Category resolve(UUID id, String newName) {

        // Case 1 — user typed a new category name
        if (newName != null && !newName.isBlank()) {

            var trimmed = newName.trim();

            return categories.findByNameIgnoreCase(trimmed)
                    .orElseGet(() -> categories.save(new Category(trimmed)));
        }

        // Case 2 — category selected from dropdown
        if (id != null) {
            return categories.findById(id).orElse(null);
        }

        // Case 3 — no category info provided
        return null;
    }

    public Iterable<Category> findAll() {
        return categories.findAll();
    }
}
