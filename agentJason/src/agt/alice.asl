// Agent alice in project agentJason

/* Initial beliefs and rules */

myName("alice").

/* Initial goals */

!start.

/* Plans */

+!start : true <- .send(bob,tell,hello).
