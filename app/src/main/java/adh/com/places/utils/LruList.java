package adh.com.places.utils;

import java.util.ArrayList;

public class LruList<T> extends ArrayList<T> {

  private int mLimit;

  public LruList(int capacity) {
    mLimit = capacity - 1;
  }

  @Override
  public boolean add(T t) {
    while (size() > mLimit) {
      remove(0);
    }
    return super.add(t);
  }

}
