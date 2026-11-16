import java.time.LocalDate;
import java.util.List;

public class GrudTest {

    public static void main(String[] args) {

        StudyPlanner planner = new StudyPlanner();

        LocalDate date = LocalDate.of(2026, 11, 16);

        // CREATE
        System.out.println("===== CREATE =====");

        planner.addRecord(
                date,
                "Java Study",
                "Learn CRUD operations"
        );

        planner.addRecord(
                date,
                "Database Study",
                "Practice SQL"
        );


        System.out.println("\n===== READ =====");

        List<StudyRecord> records = planner.findByDate(date);

        for (StudyRecord record : records) {
            System.out.println("ID: " + record.getId());
            System.out.println("Title: " + record.getTitle());
            System.out.println("Content: " + record.getContent());
        }

        System.out.println("\n===== UPDATE =====");

        if (!records.isEmpty()) {
            String id = records.get(0).getId();

            boolean updated = planner.updateRecord(
                    id,
                    date,
                    "Java Programming",
                    "Updated study content"
            );

            System.out.println("Update result: " + updated);
        }

        // SEARCH
        System.out.println("\n===== SEARCH =====");

        List<StudyRecord> searchResults =
                planner.search("Java", null, null);

        for (StudyRecord record : searchResults) {
            System.out.println("Found: " + record.getTitle());
        }

        // DELETE
        System.out.println("\n===== DELETE =====");

        if (!records.isEmpty()) {
            String id = records.get(0).getId();

            boolean deleted = planner.deleteRecord(id);

            System.out.println("Delete result: " + deleted);
        }

        System.out.println("\n===== FINAL RECORDS =====");

        List<StudyRecord> remaining =
                planner.findByDate(date);

        for (StudyRecord record : remaining) {
            System.out.println(record.getTitle());
        }

        System.out.println("\n===== TEST FINISHED =====");
    }
}
