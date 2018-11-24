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

# Mutable datasource in Stream

1. Return or asign reference to Stream can throw IllegalStateException
2. Steam can mutable the data source
3. Cannot filter only part of data source reapit in Java 9
4. Stream flatmap arent lazy repair in Java 10
5. Closable of Steam
