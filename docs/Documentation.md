###Zadanie: Rysowanie grafu na płaszczyźnie

####Autorzy:

Tomasz Nazarewicz
Konrad Sikorski
Kacper Harasim

####Treść:
Program powinien przestawić zadany graf na płaszczyźnie w jak najbardziej czytelny sposób.
Rysowane krawędzie są łukami trój-przegubowymi, a wierzchołki kołami o zadanej średnicy.
Kryteria czytelności:

* krawędzie się nie przecinają
* krawędzie są krótkie
* krawędzie nie przechodzą przez wierzchołki ani w ich bardzo bliskim sąsiedztwie
* wierzchołki są rozmieszczone równomiernie na całym obszarze rysowania
* kąty pomiędzy krawędziami incydentnymi z danymi wierzchołkiem są jak największe
* krawędzie są jak najprostsze (mogą być łukami, ale najlepiej jak będą proste)
Grafy wczytywane są z plików w formacie GraphML (http://thinkaurelius.github.io/titan/).
Użytkownik określa średnicę wierzchołka, wielkość obszaru rysowania oraz jakiś limit
czasowy lub inne kryterium stopu. Wizualizacja powinna być pokazana oraz zapisana (jako
specjalne atrybuty poszczególnych wierzchołków i krawędzi).




####Opis działania algorytmu:

Algorytm używany do wykonania projektu do lekko zmodyfikowany algorytm ewolucyjny.


######Generacja
Pierwszym krokiem jest wygenerowanie populacji startowej. Generowana ona jest za pomocą rozkładu jednostajnego, z obszaru dozwolonego. Generacja obejmuje wylosowanie dla każdego wierzchołka startowych wartości współrzędnych X oraz Y.


######Selekcja
Drugim krokiem jest selekcja. Zastosowana została selekcja turniejowa.
Jej przebieg jest następujący: n razy, gdzie n to rozmiar populacji, następuje losowanie dwóch osobników z niej z rozkładem jednostajnym.
Między osobnikami odbywa się turniej - tj. lepszy z nich przedostaje się do nowej populacji.



######Krzyżowanie

*UWAGA*Krzyżowanie w obecnej wersji jest wyłączone - uznaliśmy że działa b. podobnie do mutacji i jest zbędne.

Zastosowaliśmy krzyżowanie, w którym graf wynikowy powstaje następująco:
Przyjmijmy, że r1 oraz r2 to rodzice grafu c.
Dla każdego wierzchołka w c ustawiamy x wierzchołka jako wartość wylosowaną generatorem liczb z rozkładem normalnym o średniej równej średniej arytmetycznej odpowiadających współrzędnych x rodziców, oraz wariancji w. Analogicznie postępujemy dla zmiennej y.


######Mutacja
Kolejnym etapem jest zmutowanie nowej populacji.
Mutacji podlegają wszystkie osobniki.
Dla wszystkich osobników, które podlegają mutacji przechodzimy po ich wierzchołkach.
Z prawdopodobieństwem zadanym w aplikacji wierzchołek podlega następującej operacji:
Do współrzędnej dodawany jest mały jitter uzyskany za pomocą zmiennej o rozkładzie normalnym o wariancji proporcjonalnej do rozmiarów okna.


###### Funkcja fitness
Wartość funkcji fitness dla danego osobnika mówi, jak *ładny* jest to graf. Wartość ta jest obliczana jako suma wartości wprowadzonych przez użytkownika współczynników pomnożonych przez *jakość* odpowiadającego mu parametru. Jakość jest obliczana funkcją *F(x) = 1/(1+x)*, gdzie *x* to wielkość przewinienia dla danego parametru.


#### Opis przeprowadzonych testów:

Testy zostały wykonane na grafach generowanych w generatorze znajdującym się w pakiecie.
Generator tworzył grafy o podanej liczbie wierzchołków oraz prawdopodobieństwie połączenia dwóch wierzchołków krawędzią.
Testy polegały na podaniu odpowiednich współczynników w czasie działania programu a następnie porównania wyświetlonego
w okienku grafu z wynikami (wartościami kar oraz wystąpieniami konkretnych zdarzeń np. przecięć krawędzi) wyświetlanymi
w konsoli.


TODO Tomek - jakieś przykłady, wspomnij generator.

#### Sposób budowania i uruchomienia:
Projekt budowany jest za pomocą narzędzia maven. Wszystkie używane biblioteki wyspecyfikowane są w pliku pom.xml.
Głównym plikiem programu jest GraphMain.java, który odpowiada za wyświetlenie okna użytkownikowi.
Wyświetlanie odbywa się za pomocą generowania pliku SVG na podstawie wewnętrznej struktury grafu w programie.


#### Opis  parametrów:
|Parametr|Przewinienie|
|--------|------------|
|Przecięcie krawędź-krawędź|Liczba przecięć|
|Przecięcie krawędź-wierzchołek|Liczba par (V,E), gdzie V jest rysowane na E, ale V nie jest żadnym z końców E|
|Przecięcie wierzchołek-wierzchołek|Liczba par (V1,V2) takich, że V1 jest rysowane na V2|
|Odchylenie od zadanej przez użytkownika długości krawędzi|Suma wariancji krawędzi w stosunku do zadanej przez uzytkownika wartości podzielona przez iloczyn tej wartości z liczbą krawędzi|




#####Modyfikowalne:
* promień wierzchołka
* szerokość ekranu wizualizującego graf
* wysokość ekranu wizualizującego graf
* limit czasowy działania algorytmu
* wielkość populacji


#####Niemodyfikowalne(nastawy dla algorytmu):
* współczynnik długości - współczynnik, który jest używany do obliczenia nagrody za prawidłową (jak najmniej odbiegającą od zadanej przez użytkownika) długości krawędzi
* współczynnik krzyżowania krawędzi - współczynnik który jest używany do obliczenia nagrody za jak najmniej krawędzi oraz  wierzchołków które się przecinają
* współczynnik kątów - współczynnik używany do obliczenia nagrody za najbardziej odpowiednie kąty(tj. najrówniej rozłożone)
* prawdopodobieństwo mutacji - prawdopodobieństwo, z jakim osobnik będzie poddany mutacji.
* wariancja krzyżowania - wartość wariancji używana do losowania nowych współrzędnych dziecka.


####Opis mocnych i słabych stron projektu:




####Podział pracy:

Podział pracy był następujący:

Tomasz Nazarewicz - panel użytkownika, generator grafów.
Konrad Sikorski - generator SVG, obliczenia geometryczne, obliczenia funkcji fitness.
Kacper Harasim - stworzenie struktury modelu, konwersji pomiędzy obiektami, algorytm.
