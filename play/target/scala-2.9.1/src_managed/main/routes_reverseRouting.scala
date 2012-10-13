// @SOURCE:/home/daniel/testingPlay/conf/routes
// @HASH:64e0d8ed86b1900f9fd2ae4ac532028587fc4ae7
// @DATE:Sat Oct 13 18:22:59 EST 2012

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString


// @LINE:13
// @LINE:9
// @LINE:6
package controllers {

// @LINE:9
// @LINE:6
class ReverseApplication {
    


 
// @LINE:9
def tasks() = {
   Call("GET", "/tasks")
}
                                                        
 
// @LINE:6
def index() = {
   Call("GET", "/")
}
                                                        

                      
    
}
                            

// @LINE:13
class ReverseAssets {
    


 
// @LINE:13
def at(file:String) = {
   Call("GET", "/assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                        

                      
    
}
                            
}
                    


// @LINE:13
// @LINE:9
// @LINE:6
package controllers.javascript {

// @LINE:9
// @LINE:6
class ReverseApplication {
    


 
// @LINE:9
def tasks = JavascriptReverseRoute(
   "controllers.Application.tasks",
   """
      function() {
      return _wA({method:"GET", url:"/tasks"})
      }
   """
)
                                                        
 
// @LINE:6
def index = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"/"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:13
class ReverseAssets {
    


 
// @LINE:13
def at = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"/assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                                                        

                      
    
}
                            
}
                    


// @LINE:13
// @LINE:9
// @LINE:6
package controllers.ref {

// @LINE:9
// @LINE:6
class ReverseApplication {
    


 
// @LINE:9
def tasks() = new play.api.mvc.HandlerRef(
   controllers.Application.tasks(), HandlerDef(this, "controllers.Application", "tasks", Seq())
)
                              
 
// @LINE:6
def index() = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq())
)
                              

                      
    
}
                            

// @LINE:13
class ReverseAssets {
    


 
// @LINE:13
def at(path:String, file:String) = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]))
)
                              

                      
    
}
                            
}
                    
                