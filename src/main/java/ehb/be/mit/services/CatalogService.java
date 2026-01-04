package ehb.be.mit.services;

import ehb.be.mit.dto.UserView;
import ehb.be.mit.models.Objective;
import ehb.be.mit.models.Category;
import ehb.be.mit.models.ObjectiveType;
import ehb.be.mit.repositories.IUserRepository;
import ehb.be.mit.repositories.ICategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final IUserRepository users;
    private final ICategoryRepository categories;

    public CatalogService(IUserRepository users,
                          ICategoryRepository categories) {
        this.users = users;
        this.categories = categories;
    }

    public List<UserView> loadUsersWithObjectives() {

        var today = LocalDate.now();
        var sameYear = DateTimeFormatter.ofPattern("MMM d");
        var otherYear = DateTimeFormatter.ofPattern("MMM d yyyy");

        return users.findAll()
                .stream()
                .flatMap(u ->
                        u.getObjectives()
                                .stream()

                                .filter(o -> o.getType() == ObjectiveType.MIT)

                                .collect(Collectors.groupingBy(Objective::getCreatedAt))
                                .entrySet()
                                .stream()
                                .map(entry -> {
                                    LocalDate date = entry.getKey();

                                    var objs = entry.getValue()
                                            .stream()
                                            .limit(3)   // still capped at 3
                                            .toList();

                                    boolean isToday = date.equals(today);

                                    String label =
                                            isToday ? "Today" :
                                                    date.getYear() == today.getYear()
                                                            ? date.format(sameYear)
                                                            : date.format(otherYear);

                                    String avatar =
                                            (u.getProfileImage() == null || u.getProfileImage().isBlank())
                                                    ? "/images/avatars/default.png"
                                                    : u.getProfileImage();

                                    return new UserView(
                                            u.getUsername(),
                                            objs,
                                            label,
                                            isToday,
                                            avatar
                                    );
                                })
                )
                .toList();

    }

    public List<Category> loadAllCategories() {
        return categories.findAll();
    }
}
