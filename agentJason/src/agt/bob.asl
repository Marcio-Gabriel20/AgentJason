// Agent bob in project agentJason

/* Initial beliefs and rules */

myName("bob").

/* Initial goals */

!start.

/* Plans */

+!start : true <-
    .print("hello world.").

+hello <- .print("Bob recebeu a mensagem 'hello' de Alice.").
