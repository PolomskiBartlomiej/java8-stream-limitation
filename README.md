# java8-stream-limitation
Limitation of usage Stream API in Java 8

# project description
Project present the limitation in Java 8 Stream API

# Stream iterate

Java 8 provides `Stream.iterate()`  with singaute :

 	Stream.iterate(T seed, UnaryOperator<T> f)

 which creates infinite stream where we provide first element of the stream and funtion that return the next element. To close those stream Java 8 provide `limit()` :
    
    stream.limit(long maxSize)

 but this limit only accept the max sixe element in stream and this is big issue in creating the stream 

 For example :
    
    LocalDate start = LocalDate.of(2018,1,1);
    LocalDate end = LocalDate.of(2019,1,1);

    chronology2018Date = Stream.iterate(start , date -> date.plusMonths(1))
    .limit(ChronoUnit.MONTHS.between(start, end))
    .collect(Collectors.toList());
  
 Where :   

    ChronoUnit.MONTHS.between(start, end) 

  calcualtes the number of stream element

Solution was provide in Java 9, where `Stream.iterate` has new static method factory :

    Stream.iterate​(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)

or 

    stream.takeWhile​(Predicate<? super T> predicate);    

# Mutable datasource in Stream

Stream allow you to mutable the source of the stream

Example:

Let we have list of person and clone of this list :

    List<Person> persons =
     Arrays.asList(
                new Person("John", 40),
                new Person("Anna", 20),
                new Person("Martins", 25)
                );

    List<Person> copyOfPerson = persons.stream()
                                       .map(Person::clone)
                                       .collect(Collectors.toList());

When we use :

    persons.stream()
            .map(person -> person.increaseAge(1))             .count();

then we can see that :

    assertNotEquals(persons, copyOfPerson);

where :

    public class Person {
       ...
      Person increaseAge(int age) {
        this.age += age;
        return this;
        }
      ...
    } 

# Reuse Stream throw IllegalStateException

Reusing of completed stream throw IllegalStateException

Example:
      
    @Test(expected = IllegalStateException.class)
    public void reuse_of_stream_throw_exception() {

      final Stream<LocalDate> stream =         chronology2018Date.stream();

      stream.count();
      stream.collect(Collectors.toList());
    }

#  Stream itterate all data source

In stream we cannot provide functionality like :

    for(bool : bools) {
       if(bool) return bool;
    }

Let we have list of chronology dates :
      
    LocalDate start = LocalDate.of(2018,1,1);
    LocalDate end = LocalDate.of(2019,1,1);

    chronologyDates = 
    Stream.iterate(start , date -> date.plusMonths(1))
          .limit(ChronoUnit.MONTHS.between(start, end))
          .collect(Collectors.toList());

And we want only dates before `2018-03-01` :

  final LongAdder adder = new LongAdder();

    chronologyDate.stream()
      .peek(localDate -> adder.increment())
      .filter(isBeforeMarch)
      .collect(Collectors.toList());

then we can see that :

    assertThat(adder.intValue(), is(12));  

Solution to this issue is provide in Java 9 Api Stream where we can use :

    stream.takeWhile​(Predicate<? super T> predicate);

where element is fetching by Stream since predicate retrun true;

# Stream flatmap is not lazy
  
In java 8 `Stream.flatMap()` is method thats not fetching element lazy, this problem was solved in Java 10.
