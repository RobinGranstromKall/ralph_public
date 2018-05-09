package se.homii.domain.predicate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class UserPredicate {

  /**
   * Used to filter out distinct elements by property in a collection
   */
  public static  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
}
