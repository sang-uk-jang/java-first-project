import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.UUID;

public class StudyPlanner {

    private List<StudyRecord> records;

    public StudyPlanner() {
        records = new ArrayList<>();
    }

    public void addRecord(LocalDate date,
                          String title,
                          String content) {

        if (date == null || title == null || title.isBlank()) {
            System.out.println("Invalid record information.");
            return;
        }

        String id = UUID.randomUUID().toString();

        StudyRecord record =
                new StudyRecord(id, date, title, content);

        records.add(record);

        System.out.println("Study record added successfully.");
    }
    public List<StudyRecord> findByDate(LocalDate date) {

        List<StudyRecord> result = new ArrayList<>();

        for (StudyRecord record : records) {
            if (record.getDate().equals(date)) {
                result.add(record);
            }
        }

        return result;
    }
    public StudyRecord findById(String id) {

        for (StudyRecord record : records) {
            if (record.getId().equals(id)) {
                return record;
            }
        }

        return null;
    }

    public boolean updateRecord(String id, LocalDate date,
                                String title, String content) {

        if (date == null || title == null || title.isBlank()) {
            return false;
        }

        StudyRecord record = findById(id);

        if (record == null) {
            return false;
        }

        record.setDate(date);
        record.setTitle(title);
        record.setContent(content);

        return true;
    }

    public boolean deleteRecord(String id) {

        StudyRecord record = findById(id);

        if (record == null) {
            return false;
        }

        records.remove(record);

        return true;
    }

    public List<StudyRecord> search(String keyword,
                                    LocalDate startDate,
                                    LocalDate endDate) {

        List<StudyRecord> result = new ArrayList<>();

        for (StudyRecord record : records) {

            boolean matchesKeyword =
                    keyword == null || keyword.isBlank()
                            || record.getTitle().toLowerCase()
                            .contains(keyword.toLowerCase());

            boolean matchesDate =
                    (startDate == null || !record.getDate().isBefore(startDate))
                            && (endDate == null || !record.getDate().isAfter(endDate));

            if (matchesKeyword && matchesDate) {
                result.add(record);
            }
        }

        return result;
    }


}
