package com.github.upthewaterspout.jpfgradle;

import java.util.function.Supplier;

/**
 * Mimics the behavior of gradle 4.0s PropertyState class
 *
 * Used so that this plugin and run against old versions of gradle.
 */
public class Property<T> implements Supplier<T> {
  private Supplier<T> delegate = () -> null;

  public T get() {
    return delegate.get();
  }

  public void set(Supplier<T> delegate) {
    this.delegate = delegate;
  }

  public void set(T value) {
    this.delegate = () -> value;
  }


}
