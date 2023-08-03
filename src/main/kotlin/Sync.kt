import com.jillesvangurp.ktsearch.*
import com.jillesvangurp.searchdsls.querydsl.Script
import io.ktor.http.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    println(">>> sync subscriber offers from rater to app-view ..")
    val parser = ArgParser("sync")
    val raterUrl by parser.option(ArgType.String, shortName = "ru", description = "Rater Url").required()
    val raterPort by parser.option(ArgType.Int, shortName = "rp", description = "Rater Port").required()
    val raterIsHttps by parser.option(ArgType.Boolean, shortName = "rhttps", description = "Rater Is Https Connection").default(false)

    val simIndex by parser.option(ArgType.String, shortName = "si", description = "Sim Index").required()
    val elasticHost by parser.option(ArgType.String, shortName = "eh", description = "Elastic Host").required()
    val elasticPort by parser.option(ArgType.Int, shortName = "eport", description = "Elastic Port").required()
    val elasticIsHttps by parser.option(ArgType.Boolean, shortName = "ehttps", description = "Elastic Is Https Connection").default(false)
    val elasticUser by parser.option(ArgType.String, shortName = "eu", description = "Elastic User").default("")
    val elasticPass by parser.option(ArgType.String, shortName = "epass", description = "Elastic Password").default("")

    parser.parse(args)

    val client = SearchClient(KtorRestClient(host = elasticHost, port = elasticPort, https = elasticIsHttps, user = elasticUser, password = elasticPass))

    runBlocking {
        if (client.clusterHealth().status.usable) {
            println(">>> Connection to Elastic has been established.")
        } else {
            println(">>> Connection to Elastic has not succeed. Exiting ...")
            exitProcess(1)
        }


        val fromParentSubscriberOffer = client.search(
            simIndex, rawJson = """
                  {
                    "query": {
                        "nested": {
                            "path": "fromParentSubscriberOffer",
                            "query": {
                                "bool": {
                                    "must_not": {
                                        "exists": {
                                            "field": "fromParentSubscriberOffer.productOfferingId"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                """.trimIndent(),
            size = 10000
        )

        val toChildSubscriberOffer = client.search(
            simIndex, rawJson = """
                  {
                    "query": {
                        "nested": {
                            "path": "toChildSubscriberOffer",
                            "query": {
                                "bool": {
                                    "must_not": {
                                        "exists": {
                                            "field": "toChildSubscriberOffer.productOfferingId"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                """.trimIndent(),
            size = 10000
        )


        // ============== DEBUG ======
       /* val fromParentSubscriberOfferToFix =
            fromParentSubscriberOffer.parseHits<Sim>().map { it.fromParentSubscriberOffer }
                .flatten().filter { subscriberOffer -> subscriberOffer.productOfferingId.isNullOrBlank() }
                .map { subscriberOffer -> subscriberOffer.id  }
        println(">>> fromParentSubscriberOfferToFix => $fromParentSubscriberOfferToFix")


        val toChildSubscriberOfferToFix =
            toChildSubscriberOffer.parseHits<Sim>().map { it.toChildSubscriberOffer }
                .flatten().filter { subscriberOffer -> subscriberOffer.productOfferingId.isNullOrBlank() }
                .map { subscriberOffer -> subscriberOffer.id  }
        println(">>> toChildSubscriberOfferToFix => $toChildSubscriberOfferToFix")*/
        // ===================== END DEBUG

        val raterUrl = KtorRestClient(host = raterUrl, port = raterPort, https = raterIsHttps)

        fromParentSubscriberOffer.parseHits<Sim>().forEachIndexed {idx, sim ->
            sim.fromParentSubscriberOffer.filter { subscriberOffer -> subscriberOffer.productOfferingId.isNullOrBlank() }.map { subscriberOffer -> subscriberOffer.id}.forEach {subscriberOfferId ->
                val response = GlobalScope.async { raterUrl.doRequest(listOf("/subscribers/offers/$subscriberOfferId/sync"), HttpMethod.Get) }.await()
                println(">>>>> [fromParentSubscriberOffer] Sync executed for subscriberOfferId [$subscriberOfferId] ${response.status} ${response.text}")

                if (response.status == 400){
                    println(">>> REMOVE offerID: $subscriberOfferId  for docId: ${fromParentSubscriberOffer.ids[idx]}")
                    client.updateDocument(target = simIndex, id = fromParentSubscriberOffer.ids[idx], script = Script.create {
                        source = """ctx._source.fromParentSubscriberOffer.removeIf(obj -> obj.id == params.object_id)"""
                        params = mapOf(
                            "object_id" to "$subscriberOfferId")
                    }, source = "true" ,
                        refresh = Refresh.WaitFor)
                }
            }
        }
        toChildSubscriberOffer.parseHits<Sim>().forEachIndexed {idx, sim ->
            sim.toChildSubscriberOffer.filter { subscriberOffer -> subscriberOffer.productOfferingId.isNullOrBlank() }.map { subscriberOffer -> subscriberOffer.id}.forEach {subscriberOfferId ->
                val response = GlobalScope.async { raterUrl.doRequest(listOf("/subscribers/offers/$subscriberOfferId/sync"), HttpMethod.Get) }.await()
                println(">>>>> [toChildSubscriberOffer] Sync executed for subscriberOfferId [$subscriberOfferId] ${response.status} ${response.text}")

                if (response.status == 400){
                    println(">>> REMOVE offerID: $subscriberOfferId  for docId: ${toChildSubscriberOffer.ids[idx]}")
                    client.updateDocument(target = simIndex, id = toChildSubscriberOffer.ids[idx], script = Script.create {
                        source = """ctx._source.toChildSubscriberOffer.removeIf(obj -> obj.id == params.object_id)"""
                        params = mapOf(
                            "object_id" to "$subscriberOfferId")
                    }, source = "true" ,
                        refresh = Refresh.WaitFor)
                }
            }
        }


    }


}