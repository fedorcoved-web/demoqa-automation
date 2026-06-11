package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryAnalyzer implements IRetryAnalyzer {

    public static final int MAX_RETRY = 2;
    // Keyed by method name + parameters so each DataProvider row gets its own counter.
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    private String key(ITestResult result) {
        return result.getMethod().getMethodName() + Arrays.toString(result.getParameters());
    }

    @Override
    public boolean retry(ITestResult result) {
        return counters.computeIfAbsent(key(result), k -> new AtomicInteger(0))
                       .incrementAndGet() <= MAX_RETRY;
    }

    public boolean isRetryAvailable(ITestResult result) {
        AtomicInteger counter = counters.get(key(result));
        return counter == null || counter.get() <= MAX_RETRY;
    }
}
