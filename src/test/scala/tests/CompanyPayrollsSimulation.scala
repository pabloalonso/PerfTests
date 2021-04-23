package tests

import com.bonitasoft.engine.perf.Predef._
import com.bonitasoft.engine.perf.protocol.BonitaProtocolBuilder
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
class CompanyPayrollsSimulation extends Simulation{

  val bonitaProtocol: BonitaProtocolBuilder = bonita()
    .remote()
    .setupTest(client => { })

  val scn: ScenarioBuilder = scenario("Login logout") // A scenario is a chain of requests and pauses
    .exec(login()
      .withUser(System.getProperty("tenantAdmin"), System.getProperty("tenantPassword")))
    .exec(startProcess("CompanyPayrolls", "1.0"))
    .exec(executeTaskWhenReady("Confirmation",null,"walter.bates"))
    .exec(waitForProcessCompletion())
    .exec(logout())


  //setUp(scn.inject(rampUsers(20).during(1)).protocols(bonitaProtocol))
  setUp(scn.inject(atOnceUsers(10)).protocols(bonitaProtocol))

}



//mvn gatling:test -DbonitaUrl=http://localhost:8080 -DbonitaContext=bonita -Dgatling.simulationClass=tests.CompanyPayrollsSimulation