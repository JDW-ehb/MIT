package ehb.be.mit.dto;

import ehb.be.mit.models.Objective;

import java.util.List;

public record UserView(
        String username,
        List<Objective> objectives,
        String dateLabel,
        boolean today,
        String profileImage
) {}
