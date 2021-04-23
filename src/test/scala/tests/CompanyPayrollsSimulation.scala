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
      .withUser("walter.bates", "bpm"))
    .exec(startProcess("CompanyPayrolls", "1.0"))
    .exec(waitForProcessCompletion())
    .exec(logout())

  setUp(scn.inject(atOnceUsers(2)).protocols(bonitaProtocol))

}
