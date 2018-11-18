import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class StreamLimitationTest {

    private List<LocalDate> chronology2018Date;

    @Before
    public void setUp() {
        LocalDate start = LocalDate.of(2018,1,1);
        LocalDate end = LocalDate.of(2019,1,1);

        chronology2018Date = Stream.iterate(start , date -> date.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(start, end))
                .collect(Collectors.toList());
    }

    @Test(expected = IllegalStateException.class)
    public void variable_of_stream_throw_exception() {

        final Stream<LocalDate> stream = chronology2018Date.stream();

        stream.count();
        stream.collect(Collectors.toList());
    }

    @Test
    public void cannot_stop_filtering_stream() {
        final LongAdder adder = new LongAdder();

        Predicate<LocalDate> isBeforeMarch =
                localDate -> localDate.isBefore(LocalDate.of(2018,3,1));

        chronology2018Date.stream()
        .peek(localDate -> adder.increment())
        .filter(isBeforeMarch)
        .collect(Collectors.toList());

        assertThat(adder.intValue(), is(12));
    }

    @Test
    public void stream_allow_to_side_effect() {
        AtomicInteger one = new AtomicInteger(1);

        List<AtomicInteger> atomicIntegers = getAtomicIntegers(one);


        List<AtomicInteger> copyOfIntegers = getAtomicIntegers(one);

        atomicIntegers.stream()
                .map(integer -> { integer.set(2); return integer;})
                .count();


        assertNotEquals(toIntegers(atomicIntegers), toIntegers(copyOfIntegers));
    }

    private List<AtomicInteger> getAtomicIntegers(AtomicInteger one) {
        return Stream.iterate(one,
                integer -> new AtomicInteger(integer.getAndIncrement()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<Integer> toIntegers(List<AtomicInteger> atomicIntegers) {
        return atomicIntegers
                .stream()
                .map(AtomicInteger::get)
                .collect(Collectors.toList());
    }

}
