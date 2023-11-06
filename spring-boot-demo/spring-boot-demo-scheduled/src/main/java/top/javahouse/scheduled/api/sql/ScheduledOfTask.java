package top.javahouse.scheduled.api.sql;

public interface ScheduledOfTask extends Runnable{

    void execute();

    @Override
    default void run() {
        execute();
    }
}