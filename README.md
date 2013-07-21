# NitrogenTD

A ClojureScript tower defense.

## Running

As of writing this is not hosted anywhere. The only reason you would be running it is if you are working to develop it.

I use leinengen 2. If you use something else you're on your own.

After installing leinengen run "lein cljsbuild once" to compile the clojurescript to javascript. This will take a while the first time because it will download all of the dependencies. I haven't been shy about including dependencies. Then run "lein ring server" to start the server. You can now play the game by going to "localhost:3000" in your browser.

## Developing

While actually developing run "lein cljsbuild auto" to continuously rebuild the code when you save.

To run the tests run "lein test". The test coverage isn't all that great, but you shouldn't break them. You will need phantomjs to run the tests.

## License

Copyright © 2013 Jake Stothard <stothardj@gmail.com>

Distributed under the Eclipse Public License, the same as Clojure.
