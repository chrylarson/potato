# potato

generated using Luminus version "4.47"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

Set an environment variable for the Giant Bomb url and include your API key

    export GAMES_URL="https://www.giantbomb.com/api/games/?api_key={{api_key}}&format=json"

To start a web server for the application, run:

    lein run 

The search page will display up to 100 results and filters on the `name` of the games.

    localhost:3000/search?query=puzzle%20game

## License

Copyright © 2022 FIXME
