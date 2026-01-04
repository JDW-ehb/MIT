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
            IUserRepository users,
            IObjectiveRepository objectives,
            ICategoryRepository categories,
            IAuditLogRepository logs,
            PasswordEncoder encoder
    ) {
        return args -> {

            if (users.count() > 0) return;

            // ---------- CATEGORIES ----------
            var work   = categories.save(new Category("Work"));
            var health = categories.save(new Category("Health"));
            var study  = categories.save(new Category("Study"));

            var today = LocalDate.now();
            var yesterday = today.minusDays(1);
            var lastWeek = today.minusWeeks(1);
            var lastYear = today.minusYears(1);

            // ---------- USERS ----------
            var alex = new User();
            alex.setUsername("alex");
            alex.setEmail("alex@demo-app.com");
            alex.setPasswordHash(encoder.encode("password"));
            alex.setRole("USER");
            alex.setCountry("BE");

            var maria = new User();
            maria.setUsername("maria");
            maria.setEmail("maria@demo-app.com");
            maria.setPasswordHash(encoder.encode("password"));
            maria.setRole("USER");
            maria.setCountry("NL");

            var daniel = new User();
            daniel.setUsername("daniel");
            daniel.setEmail("daniel@demo-app.com");
            daniel.setPasswordHash(encoder.encode("password"));
            daniel.setRole("USER");
            daniel.setCountry("FR");

            var sophie = new User();
            sophie.setUsername("sophie");
            sophie.setEmail("sophie@demo-app.com");
            sophie.setPasswordHash(encoder.encode("password"));
            sophie.setRole("USER");
            sophie.setCountry("DE");

            var liam = new User();
            liam.setUsername("liam");
            liam.setEmail("liam@demo-app.com");
            liam.setPasswordHash(encoder.encode("password"));
            liam.setRole("USER");
            liam.setCountry("US");

            users.saveAll(List.of(alex, maria, daniel, sophie, liam));

            // ---------- MIT HELPER ----------
            java.util.function.Function<Object[], Objective> mit = arr -> {
                var o = new Objective(
                        (String) arr[0],
                        (String) arr[1],
                        (ObjectiveStatus) arr[2],
                        (LocalDate) arr[3]
                );
                o.setCreatedAt((LocalDate) arr[4]);
                o.setType(ObjectiveType.MIT);
                return o;
            };

            // ---------- OBJECTIVES ----------
            var n1 = mit.apply(new Object[]{"Prepare exam notes","Summarise chapters for Computer Science exam", ObjectiveStatus.IN_PROGRESS, today.plusDays(3), today});
            var n2 = mit.apply(new Object[]{"Workout session","Strength & cardio training", ObjectiveStatus.TODO, today.plusDays(1), today});
            var n3 = mit.apply(new Object[]{"Weekly planning","Organise tasks and priorities", ObjectiveStatus.TODO, today.plusDays(2), today});

            var t1 = mit.apply(new Object[]{"Fix backend issue","Investigate API timeout errors", ObjectiveStatus.DONE, yesterday.plusDays(2), yesterday});
            var t2 = mit.apply(new Object[]{"Prepare presentation","Slides for project review meeting", ObjectiveStatus.IN_PROGRESS, yesterday.plusDays(1), yesterday});
            var t3 = mit.apply(new Object[]{"Code cleanup","Refactor and simplify service layer", ObjectiveStatus.TODO, yesterday.plusDays(3), yesterday});

            var t4 = mit.apply(new Object[]{"Database review","Check indexes and slow queries", ObjectiveStatus.DONE, lastWeek.plusDays(2), lastWeek});
            var t5 = mit.apply(new Object[]{"Study networking","Revise routing and security concepts", ObjectiveStatus.IN_PROGRESS, lastWeek.plusDays(3), lastWeek});
            var t6 = mit.apply(new Object[]{"Go for a run","5km light run", ObjectiveStatus.TODO, lastWeek.plusDays(1), lastWeek});

            var m1 = mit.apply(new Object[]{"Project research","Review documentation & architecture notes", ObjectiveStatus.IN_PROGRESS, today.plusWeeks(2), today});
            var m2 = mit.apply(new Object[]{"Team sync-up","Prepare points for meeting", ObjectiveStatus.DONE, today.plusWeeks(1), today});
            var m3 = mit.apply(new Object[]{"Read article","Cloud infrastructure best practices", ObjectiveStatus.TODO, today.plusDays(5), today});

            var s1 = mit.apply(new Object[]{"Patch dependencies","Update outdated libraries", ObjectiveStatus.TODO, today.plusWeeks(1), today});
            var s2 = mit.apply(new Object[]{"Review test coverage","Identify missing unit tests", ObjectiveStatus.IN_PROGRESS, today.plusDays(4), today});
            var s3 = mit.apply(new Object[]{"Workspace cleanup","Organise files and folders", ObjectiveStatus.DONE, today.plusDays(2), today});

            var o1 = mit.apply(new Object[]{"Cook dinner","Prepare a healthy meal", ObjectiveStatus.DONE, lastYear.plusDays(2), lastYear});
            var o2 = mit.apply(new Object[]{"Mentoring session","Help a classmate understand a project topic", ObjectiveStatus.IN_PROGRESS, lastYear.plusWeeks(2), lastYear});
            var o3 = mit.apply(new Object[]{"Study schedule","Plan study blocks for next semester", ObjectiveStatus.TODO, lastYear.plusWeeks(1), lastYear});

            // ---------- ASSIGN CATEGORIES BEFORE SAVE ----------
            n1.setCategory(study);
            n2.setCategory(health);
            n3.setCategory(work);

            t1.setCategory(work);
            t2.setCategory(work);
            t3.setCategory(work);
            t4.setCategory(work);
            t5.setCategory(study);
            t6.setCategory(health);

            m1.setCategory(work);
            m2.setCategory(work);
            m3.setCategory(study);

            s1.setCategory(work);
            s2.setCategory(work);
            s3.setCategory(health);

            o1.setCategory(health);
            o2.setCategory(work);
            o3.setCategory(study);

            // ---------- SAVE OBJECTIVES ----------
            objectives.saveAll(List.of(
                    n1,n2,n3,
                    t1,t2,t3,t4,t5,t6,
                    m1,m2,m3,
                    s1,s2,s3,
                    o1,o2,o3
            ));

            // ---------- ASSIGN OWNERS ----------
            alex.addObjective(n1); alex.addObjective(n2); alex.addObjective(n3);

            maria.addObjective(t1); maria.addObjective(t2); maria.addObjective(t3);
            maria.addObjective(t4); maria.addObjective(t5); maria.addObjective(t6);

            daniel.addObjective(m1); daniel.addObjective(m2); daniel.addObjective(m3);

            sophie.addObjective(s1); sophie.addObjective(s2); sophie.addObjective(s3);

            liam.addObjective(o1); liam.addObjective(o2); liam.addObjective(o3);

            users.saveAll(List.of(alex, maria, daniel, sophie, liam));

            logs.save(new AuditLog("Seeded demo data", alex));

            System.out.println("🌱 Demo data seeded — professional dataset created");
        };
    }
}
