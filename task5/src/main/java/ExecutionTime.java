import java.time.Duration;
import java.time.Instant;

public class ExecutionTime {
    private long durations;
    private final int measuring;

    public ExecutionTime() {
        this(16);
    }

    public ExecutionTime(int measuring) {
        this.durations = 0;
        this.measuring = measuring;
    }

    public long measure(Runnable task) {
        this.durations = 0;
        for(int i = 0; i < this.measuring; i++) {
            this.durations += getDuration(task);
        }
        return this.durations / this.measuring;
    }

    private long getDuration(Runnable task) {
        Instant start = Instant.now();
        try {
            task.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();
    }
}