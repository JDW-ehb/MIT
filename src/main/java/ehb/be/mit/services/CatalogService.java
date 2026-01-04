package ehb.be.mit.services;

import ehb.be.mit.dto.UserView;
import ehb.be.mit.models.Objective;
import ehb.be.mit.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final UserRepository users;

    public CatalogService(UserRepository users) {
        this.users = users;
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
                                .collect(Collectors.groupingBy(Objective::getCreatedAt))
                                .entrySet()
                                .stream()
                                .map(entry -> {

                                    LocalDate date = entry.getKey();

                                    var objs = entry.getValue()
                                            .stream()
                                            .limit(3)
                                            .toList();

                                    boolean isToday = date.equals(today);

                                    String label =
                                            isToday ? "Today" :
                                                    date.getYear() == today.getYear()
                                                            ? date.format(sameYear)
                                                            : date.format(otherYear);

                                    // 👇 fallback avatar if missing
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
}
