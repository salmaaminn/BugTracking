import java.util.*;

// Represents the lifecycle state of a bug
enum Status {
    OPEN, IN_PROGRESS, CLOSED
}



// Stores information about a single bug report
class BugReport {
    private int id;
    private String description;
    private String severity;
    private Status status;

    // Constructor initializes bug as OPEN by default
    BugReport(int id, String description, String severity) {
        this.id = id;
        this.description = description;
        this.severity = severity;
        this.status = Status.OPEN;
    }

    // Getters
    int getId() { return id; }
    String getDescription() { return description; }
    String getSeverity() { return severity; }
    Status getStatus() { return status; }

    // Update bug status (OPEN → IN_PROGRESS → CLOSED)
    void setStatus(Status status) {
        this.status = status;
    }
}



// Manages bug storage, lookup, and updates
class BugTracker {
    private ArrayList<BugReport> bugs = new ArrayList<>();
    private HashMap<Integer, BugReport> bugMap = new HashMap<>();

    // Add bug to list and hashmap (for fast lookup by ID)
    void addBug(BugReport b) {
        if (b != null) {
            bugs.add(b);
            bugMap.put(b.getId(), b);
        }
    }

    // Retrieve bug efficiently using ID
    BugReport getBugById(int id) {
        return bugMap.get(id);
    }

    // Update status of a bug by ID
    void updateStatus(int id, Status newStatus) {
        BugReport b = bugMap.get(id);
        if (b != null) {
            b.setStatus(newStatus);
        }
    }

    // Print all bugs in system
    void printAll() {
        for (BugReport b : bugs) {
            System.out.println(
                "ID: " + b.getId() +
                " | " + b.getSeverity() +
                " | " + b.getStatus() +
                " | " + b.getDescription()
            );
        }
    }

    // Count bugs by status (used for analytics)
    int countByStatus(Status status) {
        int count = 0;
        for (BugReport b : bugs) {
            if (b.getStatus() == status) count++;
        }
        return count;
    }

    // Print summary statistics of bug statuses
    void printSummary() {
        System.out.println("Open: " + countByStatus(Status.OPEN));
        System.out.println("In Progress: " + countByStatus(Status.IN_PROGRESS));
        System.out.println("Closed: " + countByStatus(Status.CLOSED));
    }
}

///////Test Analytics////////

// Tracks pass/fail statistics for a single feature
class FeatureStats {
    private int passed = 0;
    private int failed = 0;

    // Record test result
    void record(boolean pass) {
        if (pass) passed++;
        else failed++;
    }

    int getTotal() { return passed + failed; }
    int getFailed() { return failed; }

    // Compute pass rate for feature
    double passRate() {
        int total = getTotal();
        if (total == 0) return 0.0;
        return (double) passed / total;
    }
}

// Tracks test coverage across multiple features
class FeatureCoverageTracker {
    private HashMap<String, FeatureStats> stats = new HashMap<>();

    // Record a test result for a feature
    void recordTest(String feature, boolean pass) {
        stats.putIfAbsent(feature, new FeatureStats());
        stats.get(feature).record(pass);
    }

    // Return list of features with at least one failure
    ArrayList<String> failingFeatures() {
        ArrayList<String> out = new ArrayList<>();
        for (Map.Entry<String, FeatureStats> e : stats.entrySet()) {
            if (e.getValue().getFailed() > 0) {
                out.add(e.getKey());
            }
        }
        return out;
    }

    // Compute overall pass rate across all features
    double overallPassRate() {
        int total = 0;
        int passed = 0;

        for (FeatureStats fs : stats.values()) {
            total += fs.getTotal();
            passed += fs.passRate() * fs.getTotal();
        }

        if (total == 0) return 0.0;
        return (double) passed / total;
    }
}


// Represents a user/system action with timestamp
class UserAction {
    private String name;
    private long time;

    UserAction(String name) {
        this.name = name;
        this.time = System.currentTimeMillis();
    }

    String getName() { return name; }
    long getTime() { return time; }
}

// Logs actions performed in the system
class ActionLogger {
    private ArrayList<UserAction> history = new ArrayList<>();

    // Add new action to log
    void log(String action) {
        history.add(new UserAction(action));
    }

    // Print full action history
    void printHistory() {
        for (UserAction a : history) {
            System.out.println(a.getName() + " @ " + a.getTime());
        }
    }
}


// Main
public class BugAnalysis {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        BugTracker bugTracker = new BugTracker();
        FeatureCoverageTracker coverage = new FeatureCoverageTracker();
        ActionLogger logger = new ActionLogger();

        // Menu-driven loop
        while (true) {
            System.out.println("\n1. Add Bug");
            System.out.println("2. Add Test Result");
            System.out.println("3. View Bugs");
            System.out.println("4. View Failing Features");
            System.out.println("5. Overall Pass Rate");
            System.out.println("6. Action Log");
            System.out.println("7. Update Bug Status");
            System.out.println("8. Bug Summary");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 0) break;

            switch (choice) {

                // Add a new bug report
                case 1:
                    System.out.print("Bug ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Description: ");
                    String desc = sc.nextLine();

                    System.out.print("Severity: ");
                    String sev = sc.nextLine();

                    bugTracker.addBug(new BugReport(id, desc, sev));
                    logger.log("Bug added");
                    break;

                // Record test result for a feature
                case 2:
                    System.out.print("Feature name: ");
                    String feat = sc.nextLine();

                    System.out.print("Passed? (true/false): ");
                    boolean pass = sc.nextBoolean();

                    coverage.recordTest(feat, pass);
                    logger.log("Test recorded");
                    break;

                case 3:
                    bugTracker.printAll();
                    break;

                case 4:
                    System.out.println("Failing features:");
                    for (String f : coverage.failingFeatures())
                        System.out.println(f);
                    break;

                case 5:
                    System.out.println("Overall pass rate: " + coverage.overallPassRate());
                    break;

                case 6:
                    logger.printHistory();
                    break;

                // Update bug status by ID
                case 7:
                    System.out.print("Bug ID: ");
                    int bid = sc.nextInt();
                    sc.nextLine();

                    System.out.print("New Status (OPEN/IN_PROGRESS/CLOSED): ");
                    String s = sc.nextLine();

                    bugTracker.updateStatus(bid, Status.valueOf(s.toUpperCase()));
                    logger.log("Status updated");
                    break;

                case 8:
                    bugTracker.printSummary();
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }

        sc.close();
    }
}