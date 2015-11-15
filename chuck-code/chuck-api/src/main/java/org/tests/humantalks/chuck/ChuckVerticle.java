package org.tests.humantalks.chuck;

import com.google.common.collect.ObjectArrays;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author ctranxuan
 */
public class ChuckVerticle extends AbstractVerticle {

  private static final String[] TOP_QUOTES = {
          "Les suisses ne sont pas neutres, ils attendent de savoir de quel coté Chuck Norris se situe.",
          "Il n'y a pas de théorie de l'évolution. Juste une liste d'espèces que Chuck Norris autorise à survivre.",
          "La seule chose qui arrive à la cheville de Chuck Norris... c'est sa chaussette.",
          "Chuck Norris mange les emballages des carambars. On ne blague pas avec Chuck Norris.",
          "Un jour, au restaurant, Chuck Norris a commandé un steak. Et le steak a obéi.",
          "Chuck Norris peut t'étrangler avec un téléphone sans fil.",
          "Chuck Norris fait pleurer les oignons.",
          "Dieu a dit: que la lumiere soit! et Chuck Norris répondit : On dit s'il vous plaît.",
          "Chuck Norris a déjà compté jusqu'à l'infini. Deux fois.",
          "Chuck Norris peut diviser par zéro.",
          "Quand Google ne trouve pas quelque chose, il demande à Chuck Norris.",
          "Chuck Norris peut encercler ses ennemis. Tout seul.",
          "Chuck Norris connaît la dernière décimale de Pi.",
          "Si Chuck Norris avait été pris dans le film \"300\" il l'aurait renommé en \"1\".",
          "Chuck Norris peut taguer le mur du son.",
          "Quand la tartine de Chuck Norris tombe, la confiture change de côté.",
          "Dieu voulait créer l'univers en 10 jours. Chuck Norris lui en a donné 6.",
          "Quand Chuck Norris passe devant un miroir, il n'y a pas de reflet: il n'y a qu'un seul Chuck Norris."
  };

  private static final String[] QUOTES = ObjectArrays.concat(TOP_QUOTES, new String[] {
            "Chuck Norris a fini son forfait illimité.",
            "Quand Chuck Norris utilise Windows, il ne plante pas.",
            "Chuck Norris peut claquer une porte fermée...",
            "Chuck Norris peut applaudir d'une seule main.",
            "Lorsque Chuck Norris crie au bord d'une falaise, il n'y a pas d'écho. On ne répond pas à Chuck Norris.",
            "Chuck Norris a déja frôlé la mort....elle ne s'en est jamais remise.",
            "Un jour, les PowerRangers ont rencontré Chuck Norris. Maintenant on les appelle les Télétubbies.",
            "Chuck Norris ne se mouille pas, c'est l'eau qui se Chuck Norris.",
            "Chuck Norris peut gagner une partie de puissance 4 en trois coups.",
            "Chuck Norris ne porte pas de montre. Il décide de l'heure qu'il est.",
            "Chuck Norris sait parler le braille.",
            "Chuck Norris est contre les radars automatiques : ça l'éblouit lorsqu'il fait du vélo",
            "Chuck Norris a déjà été sur Mars, c'est pour cela qu'il n'y a pas de signes de vie là bas."
    }, String.class);



    private static final Random RANDOM = new Random();

    @Override
    public void start(final Future<Void> aFuture) throws Exception {
        Router router;
        router = Router.router(vertx);

        router.get("/api/quote").handler(this::getQuoteOfTheDay);
        router.get("/api/top-quotes").handler(this::getTopQuotes);

        vertx
            .createHttpServer()
            .requestHandler(router::accept)
            .listen(
                    // Retrieve the port from the configuration,
                    // default to 8080.
                    config().getInteger("http.port", 8080),
                    result -> {
                        if (result.succeeded()) {
                            aFuture.complete();
                        } else {
                            aFuture.fail(result.cause());
                        }
                    }
            );
    }

    private void getQuoteOfTheDay(final RoutingContext aRoutingContext) {
        int index;
        index = RANDOM.nextInt(QUOTES.length - 1);

        HttpServerResponse response;
        response = aRoutingContext.response();

        response
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(new JsonObject().put("quote", QUOTES[index]).encode());
    }

    private void getTopQuotes(final RoutingContext aRoutingContext) {
        int index;
        index = RANDOM.nextInt(QUOTES.length - 6);

        final JsonArray quotes;
        quotes = new JsonArray();

        IntStream.range(index, index + 5).forEach(i -> {
            quotes.add(new JsonObject().put("quote", TOP_QUOTES[i]));
        });


        HttpServerResponse response = aRoutingContext.response();
        response
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(quotes.encodePrettily());
    }

}
