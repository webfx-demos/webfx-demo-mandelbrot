# Live demo

The demo is published on [this page][demo-live-link].

This application proposes 9 Mandelbrot places to visit and computes for each a series of frames to play a zoom effect.
In the settings, you can adjust the number of web workers and switch between JavaScript and WebAssembly for the computation.

# Highlighted features

## Reusability

This demo demonstrates how you can reuse some java code from existing java applications, even if not written in JavaFX.
For example, the Mandelbrot computation code was taken from this [Java Swing application][mandelbrot-computation-source].

## Web workers and WebAssembly

Since JavaFX was not originally designed for the web, WebFX provides some additional APIs to work with web-specific concepts
such as web workers and WebAssembly modules, which can be useful for applications requiring heavy background tasks.
WebFX can interact with third-party web workers and WebAssembly modules or you can write your own in Java.
In this demo, they are written in Java and compiled with [TeaVM][teavm-website].

[demo-live-link]: https://mandelbrot.webfx.dev
[mandelbrot-computation-source]: http://math.hws.edu/eck/js/mandelbrot/java/xMandelbrotSource-1-2/edu/hws/eck/umb/
[teavm-website]: http://teavm.org/
