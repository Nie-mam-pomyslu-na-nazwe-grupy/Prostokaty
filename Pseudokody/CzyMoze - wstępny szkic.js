CzyMoze - wstępny szkic



Co funkcja dostanie:
- objekt Klocek
- PlanszaMain[][] // w zasadzie trzeba w ogóle by to imporotować do funkcji?


boollean CzyMoze(Klocek,PlanszaMain[][])
{
	funkcja0=> czyJestWZasieguMapy() // -> w zasadzie ta funkcja może być użyta przy systemie ruchu, a nie tu
	funckja1=> czyJestWKontakcieZeSwoimi() // sprawdza czy jest w kontakcie ze swoimi klockami
	funckja2=> czyJestNaWolnychPolach() // sprawdza, czy pod polami klocka nie ma już innych klocków
}

!!![funkcja0=> może nie być używana w tym miejscu]!!!


funkcja0 => jest w zasięgu mapy!
funkcja1 => jest kontakt z własnymi klockami!
funkcja2 => jest na wolnych polach!

CzyMoze => klocek może być postawiony w tym miejscu!

		^coś takiego


