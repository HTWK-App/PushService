package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller {

  def register(id: String) = Action {
    if (pushID(id)){
      println("Registered: " + id)
      Created
    } else {
	    println("Already exists: " + id)
      Conflict
    }
  }

  def unregister(id: String) = Action {
    removeID(id)
    println("Removed ID: " + id)
    Ok
  }

  def pushto(id: String) = Action{
  	GCM.sendPush(id)
  	Ok
  }

  var ids = Set.empty[String]

  def pushID(id: String): Boolean = {
    if (!ids.contains(id)) {
      ids = ids + id
      true
    } else
      false
  }

  def removeID(id: String) = if (ids.contains(id)) ids = ids - id
}
