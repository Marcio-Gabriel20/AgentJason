// Agente bob

/* Initial beliefs and rules */
myName(bob).
mensagem_recebida(_).
myScoreBob(0).

/* Initial goals */
!start.

/* Plans */

+myScore(Score)[source(percept)] <-
    -+myScoreBob(Score);
    .print("Meu Score: ", Score);
    !start.

/*+hello(Mensagem)[source(alice)] <-
    -+mensagem_recebida(Mensagem);
    .print("Mensagem recebida de Alice: ", Mensagem);
    !start.*/

/*+!start <- .print("Teste");
           mensagem_recebida(Msg);
           .print("Bob recebeu a mensagem: ", Msg).*/

/*+!start:
    mensagem_recebida(Msg) <-
        .print("Mensagem de Alice para Bob: ", Msg).*/
+!move: true <-
    moveRight;
    .print("Movendo para direita...").
+!start: true <-
    !move;
    .print("Estou jogando!").
