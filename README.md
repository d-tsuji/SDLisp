# SDLisp

Mini lisp interpreter written in Java. The source repository url is omitted in the README because it seems to be written only in the book.

An explanation can be found in the following books.

https://gihyo.jp/magazine/SD/archive/2017/201709

SDLisp is written in about 500 lines. It is simple and cool.

```
$ gocloc src
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            12             72             92            497
-------------------------------------------------------------------------------
TOTAL                           12             72             92            497
-------------------------------------------------------------------------------
```

## Some examples

```lisp
> (+ 1 2)
3
```

```lisp
> (cons 1 '(2 3))
(1 2 3)
```

```lisp
> (defun fact (n) (if (< n 1) 1 (* n (fact (- n 1)))))
FACT
> (fact 10)
3628800
```

## LICENSE

These codes are licensed under CC0.

[![CC0](http://i.creativecommons.org/p/zero/1.0/88x31.png "CC0")](https://creativecommons.org/publicdomain/zero/1.0/)
