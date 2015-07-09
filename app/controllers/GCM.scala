package controllers

import scala.concurrent.Future

import play.api.Play.current
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.JsPath
import play.api.libs.json.JsResult
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.Reads
import play.api.libs.json.Reads.IntReads
import play.api.libs.json.Reads.functorReads
import play.api.libs.ws.WS
import play.api.libs.ws.WSRequestHolder
import play.api.libs.ws.WSResponse

object GCM {

  private implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  private val gcm: WSRequestHolder = WS
    .url("https://android.googleapis.com/gcm/send")
    .withHeaders("Content-Type" -> "application/json", "Authorization" -> "key=<YOUR_KEY>")
    .withRequestTimeout(3000)
    .withFollowRedirects(true)

  private case class GCMResponse(failure: Int, canonical_ids: Int)
  private implicit val gcmReads: Reads[GCMResponse] = (
    (JsPath \ "failure").read[Int] and
    (JsPath \ "canonical_ids").read[Int])(GCMResponse.apply _)

  def sendPush(id: String) = {

    val data = Json.obj("data" -> Json.obj("message" -> "Hallo there!"), "registration_ids" -> Json.arr(id)) //Up to 1000 IDS, time_to_live: in seconds
    val futureResponse: Future[WSResponse] = gcm.post(data)

    futureResponse.map { response =>
      if (response.status == 200)
        evaluateGCMResult(response.json.validate[GCMResponse], response)
      else
        println("Failure: Sending Push to GCM - " + response.statusText)
    }
  }

  private def evaluateGCMResult(result: JsResult[GCMResponse], response: WSResponse) {

    result.fold(
      errors => {
        println("Can't validate json")
      },
      res => {
        if (res.failure != 0) {
          searchForErrors(response)
        }
        if (res.canonical_ids != 0) {
          replaceIDs(response)
        }
      })
  }

  private def searchForErrors(response: WSResponse) {
    val errors = (response.json \\ "error").map { _.toString().replace("\"", "") }
    errors.map {
      _ match {
        case "NotRegistered" => /*remove ID from Database*/ println("Your ID is not registered at GCM")
        case "InvalidRegistration" => /*also remove ID from Database*/ println("Your ID is not registered at GCM")
        case "Unavailable" => /*Device is not available, resend later*/ println("Please resend this later again")
      }
    }
  }

  private def replaceIDs(response: WSResponse) {
    val newIDs = (response.json \\ "registration_id").map { x => x.as[Int] }
    newIDs.map { x => println("Replace ID Y with " + x) }
  }

}
