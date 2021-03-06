package org.istanbuljug.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by usta on 24.09.2016.
 */
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode({Mode.Throughput})
@State(value = Scope.Benchmark)
public class SerializeBenchmark {

    private static final int ITERATION_COUNT = 10;
    private static final int WARMUP_COUNT = 10;
    private static final int FORK_COUNT = 1;

    private Jsonb jsonb;
    private ObjectMapper objectMapper;
    private Jug jug;

    @Setup(Level.Trial)
    public void init() {
        jsonb = JsonbBuilder.create();
        objectMapper = new ObjectMapper();
        jug = new Jug("İstanbul JUG", 2010);
    }

    @Benchmark
    @Fork(value = FORK_COUNT)
    @Warmup(iterations = WARMUP_COUNT)
    @Measurement(iterations = ITERATION_COUNT)
    public void jsonb_to_json(Blackhole blackhole) {
        String json = jsonb.toJson(jug);
        blackhole.consume(json);
    }

    @Benchmark
    @Fork(value = FORK_COUNT)
    @Warmup(iterations = WARMUP_COUNT)
    @Measurement(iterations = ITERATION_COUNT)
    public void jackson_to_json(Blackhole blackhole) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(jug);
        blackhole.consume(json);
    }

    public static void main(String[] args) throws RunnerException {
        String name = SerializeBenchmark.class.getSimpleName();
        Options opt = new OptionsBuilder()
                .include(name)
                .output("results/" + name + ".txt")
                .build();

        Collection<RunResult> result = new Runner(opt).run();
    }

}
