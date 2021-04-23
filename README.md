## Context
This program can be used for performance tests on a Bonita platform

## Requirements
Minimum Java version required: Java 11
Tested with Maven 3.6.3 and java 11
Frameworks used: Gatling, Scala
Access to a bonita server with a test process installed 

## Compiling testing and running the program
```
mvn gatling:test -DbonitaUrl=http://localhost:8080 -DbonitaContext=bonita -DtenantAdmin=install -DtenantPassword=install -Dgatling.simulationClass=tests.CompanyPayrollsSimulation -DnThreads=10
```

The simulation classes provided require specific bonita bos files, only some of them are provided.
You can create your own quite easily just checking the existing simulation classes as examples

## Expected results
Once executed you should get a report, also available under target/gatling/report/