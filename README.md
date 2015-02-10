# LatteCompiler
===========
Compiler for Latte Programming Language

Zawartosc paczki:
Foldery:
lib   - katalog lib zawiera plik runtime.c. Po wykonaniu polecania make w katalogu pojawie sie plik runtime.o
	
src	  - zrodla projektu 
tests - katalog z testami. Dodatkowo w katalogu tests/good znajduje sie skrypt test.sh ktory sprawdza poprawnosc
        plikow wynikowych z plikami output.

Pliki:
.gitignore	
Latte.cf	- plik z gramatyka
Latte.dvi	- plik z dokumentacja do gramatyki
Makefile
README.md
generate_ASM.sh	- skypt generujacy pliki asm dla kazdego z testow
latc_x86_64.sh	- plik wykonywalny uruchamiajacy kompilator
pom.xml         - plik pom dla projektu maven



Tresc zadania:
Laboratorium MRJP - projekt zaliczeniowy

Zaliczenie laboratorium odbywa się na podstawie projektu obejmującego implementację kompilatora dla prostego jezyka imperatywnego Latte.


Punkty mozna uzyskać za:

front-end (4)
back-end LLVM (8) albo x86 (10)
kod LLVM w postaci SSA - dodatkowo 1p
wykorzystanie rejestrów dla x86 - dodatkowo do 4p (na 4p alokacja rejestrów)
optymalizacje - do 3p
Rozszerzenia (notacja x/y oznacza punkty za backend w wersji odpowiednio LLVM/x86)
tablice (2)
struktury (2)
obiekty (atrybuty, metody, dziedziczenie bez zastępowania metod) - dodatkowo (3/4)
metody wirtualne - dodatkowo (3/4), czyli za obiekty z metodami wirtualnymi mozna uzyskać 8/10 punktów.
odśmiecanie (2)
Szczegółowy opis rozszerzeń zawarty jest w opisie języka.
Uwaga: podane liczby punktów są wartościami maksymalnymi, sprawdzający może przydzielić mniejsza liczbę punktów w zależności od jakości rozwiązania i generowanego kodu. Oczekujemy, że kod będzie generowany zgodnie z regułami sztuki poznanymi na zajęciach (albo lepiej).

Zasady

Projekt zaliczeniowy ma być pisany samodzielnie. Wszelkie przejawy niesamodzielności będą karane. W szczególności:
nie wolno oglądać kodu innych studentów, pokazywać, ani w jakikolwiek sposób udostępniać swojego kodu
wszelkie zapożyczenia powinny być opisane z podaniem źródła.
Wymagania techniczne

Projekt powinien być oddany w postaci spakowanego archiwum TAR (.tar.gz lub .tgz)
W korzeniu projektu muszą się znajdować co najmniej:
Plik tekstowy README opisujący szczegóły kompilacji i uruchamiania programu, używane narzędzia i biblioteki, zaimplementowane rozszerzenia, strukturę katalogów projektu, ewentualnie odnośniki do bardziej szczegółowej dokumentacji.
Plik Makefile pozwalający na zbudowanie programu.
katalog src zawierający wyłącznie pliki źródłowe projektu (plus ewentualnie dostarczony przez nas plik Latte.cf); pliki pomocnicze takie jak biblioteki itp powinny być umieszczone w inych katalogach.
Program musi się kompilować na students poleceniem make (które oczywiście może wywoływać inne programy).
Wszelkie używane biblioteki (poza biblioteką standardową używanego jezyka programowania) muszą być opisane w README
Po zbudowaniu kompilatora, w korzeniu musi się znajdować plik wykonywalny o nazwie latc (może być skryptem uruchamiającym inne programy)
Kompilator musi akceptować wszystkie programy testowe z katalogu good i odrzucać ze stosownym komunikatem wszystkie programy z katalogu bad.Komunikaty o błędach muszą umnożliwiać lokalizację błędu (przez numer linii lub kontekst). Dla rozszerzeń musi akceptować wszystkie programy z odpowiednich podkatalogów extension. Uruchomienie poprawnego programu testowego ma dawać wyjście takie jak w odpowiednim pliku .output (dla wejścia zawartego w odpowiednim pliku .input, o ile istnieje)
Gdy kompilator akceptuje program, musi wypisać w pierwszej linii stderr napis OK ("OK\n"). Dalsze linie i stdout - dowolne. Kompilator musi się w takiej sytuacji zakończyć z kodem powrotu 0.
Gdy kompilator odrzuca program musi wypisać w pierwszej linii stderr napis ERROR ("ERROR\n"). Dalsze linie powinny zawierać stosowne informacje o błędach. Kompilator musi się w takiej sytuacji zakończyć z kodem powrotu różnym od 0.