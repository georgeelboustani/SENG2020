// @SOURCE:/home/daniel/testingPlay/conf/routes
// @HASH:64e0d8ed86b1900f9fd2ae4ac532028587fc4ae7
// @DATE:Sat Oct 13 18:22:59 EST 2012

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString

object Routes extends Router.Routes {


// @LINE:6
val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart("/"))))
                    

// @LINE:9
val controllers_Application_tasks1 = Route("GET", PathPattern(List(StaticPart("/tasks"))))
                    

// @LINE:13
val controllers_Assets_at2 = Route("GET", PathPattern(List(StaticPart("/assets/"),DynamicPart("file", """.+"""))))
                    
def documentation = List(("""GET""","""/""","""controllers.Application.index()"""),("""GET""","""/tasks""","""controllers.Application.tasks()"""),("""GET""","""/assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""))
             
    
def routes:PartialFunction[RequestHeader,Handler] = {        

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(_root_.controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Nil))
   }
}
                    

// @LINE:9
case controllers_Application_tasks1(params) => {
   call { 
        invokeHandler(_root_.controllers.Application.tasks(), HandlerDef(this, "controllers.Application", "tasks", Nil))
   }
}
                    

// @LINE:13
case controllers_Assets_at2(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(_root_.controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String])))
   }
}
                    
}
    
}
                