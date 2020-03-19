Wstawianie klocka

"		y\x| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11| 12| "
"		---------------------------------------------------- "
"		1  | s |   |   |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		2  |   |\S | S |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		3  |   | S | S |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		4  |   | S | S |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		5  |   | S | S |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		6  |   |   |   |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		7  |   |   |   |   |   |   |   |   | O | O |   |   | "
"		---------------------------------------------------- "
"		8  |   |   |   |   |   |   |   |   | O | O |   |   | "
"		---------------------------------------------------- "
"		9  |   |   |   |   |   |   |   |   | O |\O |   |   | "
"		---------------------------------------------------- "
"		10 |   |   |   |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		11 |   |   |   |   |   |   |   |   |   |   |   |   | "
"		---------------------------------------------------- "
"		12 |   |   |   |   |   |   |   |   |   |   |   | o | "
"		---------------------------------------------------- "

objekt Klocek
{
	zwrot;		// lewo-góra/prawo-dół itp. np.: [0,1] - [lewo , góra] 	 [ {1-prawo,0-lewo} , {1-góra,0-dół} ]	//pomocnicza stała
	wymiary;		// warotośći jak 1, 3, -2 itp. przypisywane na podstawie zwrotu // prawo-lewo , góra-dół // tablica [x,y]
	pozycja;		// pozycja wyrażona w tablicy dwuelementowej [x,y], miejsce gdzie jest "kratka" nawigacyjna
}


dla powyższej planszy i klocka 'O':

Klocek 'O'
{
	zwrot = [0,1];
	wymiary = [-2,3];
	pozycja = [10,9];
}


dla klocka 'S':

Klocek 'S'
{
	zwrot = [1,0];
	wymiary = [2,-4];
	pozycja = [2,2];
}


'zwrot' sam w sobie może być przypisany do konkretnego gracza, co ułatwi wyświetlanie klocków, mam nadzieję
	ponadto nie musi być użyty w obiekcie 'Klocek', ale w profilu gracza, na podstawie czego byłyby tworzone klocki; !!!do ustalenia!!!
  
*przenieszczanie klocka po planszy odbywa się poprzez manipulację zmiennej "pozycja"
*wyświetlanie klocka na podstawie pozycji i wymiarów
*ustalanie + i - dla wymiarów za pomocą wartości 'zwrot'