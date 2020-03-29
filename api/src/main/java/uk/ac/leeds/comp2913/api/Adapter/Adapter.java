package uk.ac.leeds.comp2913.api.Adapter;

public interface Adapter<T, S> {
  public S map(T t);
}
