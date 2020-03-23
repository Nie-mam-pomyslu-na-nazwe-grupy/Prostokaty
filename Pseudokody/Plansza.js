Plansza - mechanika

Użytkowanie funkcji lub metody wyświetl(Plansza[][]); 


tablica dwuwymiarowa Plansza[][] używana jako wzór przy wyświetlaniu graficznym

każda kratka posiada identyfikator płytki wyrażony w liczbach (?)

NIE PRZECHOWUJE PŁYTEK !!!!!

można założyć że są dwie Plansze[][]
1. główna - przechowuje ZATWIERDZONY i ZGODNY układ, podlega edycji jedynie przy zatwierdzenu poprawnego ruchu;
2. wyświetlana - aktualnie wyświetlana, uwzględnia niemożliwe {nieprawidłowe} ruchy. Tworzona na podstwie głównej z uwzględnieniem klocka do wstawiania


1. PlanszaMain[][];
2. PlanszaTMP[][];