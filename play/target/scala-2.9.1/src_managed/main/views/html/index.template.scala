
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.api.i18n._
import play.api.templates.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import com.avaje.ebean._
import play.mvc.Http.Context.Implicit._
import views.html._
/**/
object index extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[List[String],play.api.templates.Html] {

    /**/
    def apply/*1.2*/(tasks: List[String]):play.api.templates.Html = {
        _display_ {import helper._


Seq[Any](format.raw/*1.23*/("""

"""),format.raw/*4.1*/("""
"""),_display_(Seq[Any](/*5.2*/main("Todo list")/*5.19*/ {_display_(Seq[Any](format.raw/*5.21*/("""
    
    <h1>"""),_display_(Seq[Any](/*7.10*/tasks/*7.15*/.size())),format.raw/*7.22*/(""" task(s)</h1>
    
    <ul>
        """),_display_(Seq[Any](/*10.10*/for(task <- tasks) yield /*10.28*/ {_display_(Seq[Any](format.raw/*10.30*/("""
            <li>
                """),_display_(Seq[Any](/*12.18*/task)),format.raw/*12.22*/("""

            </li>
        """)))})),format.raw/*15.10*/("""
    </ul>
    
    
    
""")))})),format.raw/*20.2*/("""
"""))}
    }
    
    def render(tasks:List[String]) = apply(tasks)
    
    def f:((List[String]) => play.api.templates.Html) = (tasks) => apply(tasks)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Sat Oct 13 18:23:02 EST 2012
                    SOURCE: /home/daniel/testingPlay/app/views/index.scala.html
                    HASH: 5b4136b58234d3b88a7ac4c6d8979bc2ec589b0c
                    MATRIX: 761->1|875->22|903->41|939->43|964->60|1003->62|1053->77|1066->82|1094->89|1167->126|1201->144|1241->146|1312->181|1338->185|1399->214|1457->241
                    LINES: 27->1|31->1|33->4|34->5|34->5|34->5|36->7|36->7|36->7|39->10|39->10|39->10|41->12|41->12|44->15|49->20
                    -- GENERATED --
                */
            