package com.bonitasoft.engine.perf.protocol

import java.util
import java.util.concurrent.{ExecutorService, Executors}

import io.gatling.core
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolComponents, ProtocolKey}
import io.gatling.core.session.Session
import org.bonitasoft.engine.api.{APIClient, ApiAccessType}
//import org.bonitasoft.engine.test.{TestEngine, TestEngineImpl}
import org.bonitasoft.engine.util.APITypeManager

object BonitaProtocol {
  def apply(): BonitaProtocol = BonitaProtocol(
    true, // is local bonita server
    null,
    _ => Unit
  )


  val BonitaProtocolKey: ProtocolKey[BonitaProtocol, BonitaComponents] = new ProtocolKey[BonitaProtocol, BonitaComponents] {

    type Protocol = BonitaProtocol
    type Components = BonitaComponents

    def protocolClass: Class[core.protocol.Protocol] = classOf[BonitaProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    def defaultProtocolValue(configuration: GatlingConfiguration): BonitaProtocol = throw new IllegalStateException("Can't provide a default value for UpperProtocol")

    def newComponents(coreComponents: CoreComponents): BonitaProtocol => BonitaComponents = {

      bonitaProtocol => {

        APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, bonitaProtocol.settings);

        val waitersThreadPool = Executors.newFixedThreadPool(Integer.parseInt(System.getProperty("nThreads")))

        val client = new APIClient()
        client.login(System.getProperty("tenantAdmin"), System.getProperty("tenantPassword"))
        println("Setup engine test")
        bonitaProtocol.setupTest(client)
        client.logout()

        BonitaComponents(bonitaProtocol,  waitersThreadPool)
      }
    }
  }
}

case class BonitaProtocol(
                           var local: Boolean,
                           var settings: util.HashMap[String,String],
                           var setupTest: APIClient => Unit
                         ) extends Protocol {
  type Components = BonitaComponents
}

case class BonitaComponents(bonitaProtocol: BonitaProtocol,
                            executor: ExecutorService) extends ProtocolComponents {

  def client: APIClient = new APIClient()

  override def onStart: Session => Session = ProtocolComponents.NoopOnStart

  override def onExit: Session => Unit = ProtocolComponents.NoopOnExit
}

