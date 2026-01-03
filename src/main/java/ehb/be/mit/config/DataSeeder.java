package ehb.be.mit.config;

import ehb.be.mit.models.*;
import ehb.be.mit.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UserRepository users,
            ObjectiveRepository objectives,
            CategoryRepository categories,
            AuditLogRepository logs,
            PasswordEncoder encoder
    ) {
        return args -> {

            if (users.count() > 0) return;

            var work = categories.save(new Category("Work"));
            var health = categories.save(new Category("Health"));
            var study = categories.save(new Category("Study"));

            var today = LocalDate.now();
            var yesterday = today.minusDays(1);
            var lastWeek = today.minusWeeks(1);
            var lastYear = today.minusYears(1);

            // ---- USERS ----
            var neo = new User();
            neo.setUsername("neo");
            neo.setEmail("neo@matrix.io");
            neo.setPasswordHash(encoder.encode("password"));
            neo.setRole("USER");
            neo.setCountry("BE");

            var trinity = new User();
            trinity.setUsername("trinity");
            trinity.setEmail("trinity@matrix.io");
            trinity.setPasswordHash(encoder.encode("password"));
            trinity.setRole("USER");
            trinity.setCountry("FR");

            var morpheus = new User();
            morpheus.setUsername("morpheus");
            morpheus.setEmail("morpheus@matrix.io");
            morpheus.setPasswordHash(encoder.encode("password"));
            morpheus.setRole("USER");
            morpheus.setCountry("NL");

            var smith = new User();
            smith.setUsername("smith");
            smith.setEmail("smith@matrix.io");
            smith.setPasswordHash(encoder.encode("password"));
            smith.setRole("USER");
            smith.setCountry("US");

            var oracle = new User();
            oracle.setUsername("oracle");
            oracle.setEmail("oracle@matrix.io");
            oracle.setPasswordHash(encoder.encode("password"));
            oracle.setRole("USER");
            oracle.setCountry("GR");

            // Persist empty users first (so they have IDs)
            users.saveAll(List.of(neo, trinity, morpheus, smith, oracle));


            // ---- OBJECTIVES ----
            var n1 = new Objective("Learn Spring Boot","Finish backend app",
                    ObjectiveStatus.IN_PROGRESS, today.plusDays(3));
            n1.setCreatedAt(today);

            var n2 = new Objective("Go to the gym","Leg day 🦵",
                    ObjectiveStatus.TODO, today.plusDays(1));
            n2.setCreatedAt(today);

            var n3 = new Objective("Buy coffee","Fuel the code",
                    ObjectiveStatus.TODO, today.plusDays(2));
            n3.setCreatedAt(today);

            // TRINITY — YESTERDAY
            var t1 = new Objective("Patch vulnerabilities", "Security matters",
                    ObjectiveStatus.DONE, yesterday.plusDays(2));
            t1.setCreatedAt(yesterday);

            var t2 = new Objective("Coffee refill", "Again ☕",
                    ObjectiveStatus.IN_PROGRESS, yesterday.plusDays(1));
            t2.setCreatedAt(yesterday);

            var t3 = new Objective("Debug production", "🔥",
                    ObjectiveStatus.TODO, yesterday.plusDays(3));
            t3.setCreatedAt(yesterday);

            // TRINITY — LAST WEEK
            var t4 = new Objective("Network audit", "Deep dive",
                    ObjectiveStatus.DONE, lastWeek.plusDays(2));
            t4.setCreatedAt(lastWeek);

            var t5 = new Objective("Code review", "Be kind",
                    ObjectiveStatus.IN_PROGRESS, lastWeek.plusDays(3));
            t5.setCreatedAt(lastWeek);

            var t6 = new Objective("Yoga", "Stretch stress away",
                    ObjectiveStatus.TODO, lastWeek.plusDays(1));
            t6.setCreatedAt(lastWeek);

            // MORPHEUS — TODAY
            var m1 = new Objective("Find The One", "Search continues",
                    ObjectiveStatus.IN_PROGRESS, today.plusWeeks(2));
            m1.setCreatedAt(today);

            var m2 = new Objective("Teach Kung Fu", "Woah",
                    ObjectiveStatus.DONE, today.plusWeeks(1));
            m2.setCreatedAt(today);

            var m3 = new Objective("Meditate", "Clear mind",
                    ObjectiveStatus.TODO, today.plusDays(5));
            m3.setCreatedAt(today);

            // SMITH — TODAY
            var s1 = new Objective("Eliminate anomalies", "Order must be restored",
                    ObjectiveStatus.TODO, today.plusWeeks(1));
            s1.setCreatedAt(today);

            var s2 = new Objective("Upgrade Agent AI", "Efficiency programs",
                    ObjectiveStatus.IN_PROGRESS, today.plusDays(4));
            s2.setCreatedAt(today);

            var s3 = new Objective("Practice monologues", "Mr. Anderson…",
                    ObjectiveStatus.DONE, today.plusDays(2));
            s3.setCreatedAt(today);

            // ORACLE — LAST YEAR
            var o1 = new Objective("Bake cookies", "They already will be",
                    ObjectiveStatus.DONE, lastYear.plusDays(2));
            o1.setCreatedAt(lastYear);

            var o2 = new Objective("Guide The One", "But gently",
                    ObjectiveStatus.IN_PROGRESS, lastYear.plusWeeks(2));
            o2.setCreatedAt(lastYear);

            var o3 = new Objective("Water plants", "Even prophecies need oxygen",
                    ObjectiveStatus.TODO, lastYear.plusWeeks(1));
            o3.setCreatedAt(lastYear);

            objectives.saveAll(List.of(
                    n1,n2,n3,
                    t1,t2,t3,t4,t5,t6,
                    m1,m2,m3,
                    s1,s2,s3,
                    o1,o2,o3
            ));

            // (no saveAll yet — key difference!)

            // ---- RELATIONSHIPS ----
            neo.addObjective(n1);
            neo.addObjective(n2);
            neo.addObjective(n3);

            trinity.addObjective(t1);
            trinity.addObjective(t2);
            trinity.addObjective(t3);
            trinity.addObjective(t4);
            trinity.addObjective(t5);
            trinity.addObjective(t6);

            morpheus.addObjective(m1);
            morpheus.addObjective(m2);
            morpheus.addObjective(m3);

            smith.addObjective(s1);
            smith.addObjective(s2);
            smith.addObjective(s3);

            oracle.addObjective(o1);
            oracle.addObjective(o2);
            oracle.addObjective(o3);

            // ---- SAVE USERS AGAIN (cascade writes objectives) ----
            users.saveAll(List.of(neo, trinity, morpheus, smith, oracle));

            logs.save(new AuditLog("Seeded demo data", neo));

            System.out.println("🌱 Demo data seeded");
        };
    }

}
