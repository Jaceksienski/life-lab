Cel projektu

Celem projektu jest stworzenie minimalnego, ale badawczego symulatora życia, w którym agent nie posiada z góry zakodowanej wiedzy o świecie (np. „to jest jedzenie”, „to jest wróg”), lecz samodzielnie uczy się zależności między:

tym, co postrzega,

tym, jakie akcje wykonuje,

oraz tym, jakie skutki te akcje przynoszą dla jego stanu wewnętrznego.

Projekt nie ma ambicji „robić kolejnego RL-a”, tylko badać alternatywną architekturę poznawczą: bardziej epizodyczną, kontekstową i stopniowo uogólniającą doświadczenia.

Filozofia architektury
1. Świat bez typów obiektów

W świecie nie istnieją klasy typu „jedzenie”, „wróg”, „ściana”.
Wszystko jest bytem fizycznym posiadającym zestaw cech.

Agent:

nie zna nazw tych cech,

nie wie, które są „dobre” lub „złe”,

poznaje je wyłącznie poprzez interakcję.

Znaczenie obiektu nie jest w nim, tylko powstaje w mózgu agenta jako efekt doświadczeń.

2. Agent jako system: ciało + mózg

Agent składa się z:

ciała (pozycja, możliwość wykonania akcji),

zmysłów (ograniczone kanały percepcji),

mózgu (decyzje, pamięć, uczenie),

stanu wewnętrznego (energia, integralność, itp.).

Mózg nie steruje bezpośrednio światem – jedynie wybiera akcje, które potem są weryfikowane przez system (czy są możliwe, ile kosztują, jaki mają efekt).

Model poznawczy (mózg)
3. Uczenie wyłącznie przez doświadczenie

Agent nie uczy się online przez funkcję straty.
Uczy się wyłącznie poprzez zapisywanie doświadczeń w postaci epizodów:

„Byłem w stanie X, widziałem Y, wykonałem akcję Z, a moje statystyki zmieniły się o Δ”

Każde doświadczenie zawiera:

kontekst percepcyjny (cechy, pozycje względne),

wybraną akcję,

obserwowany skutek (np. zmiana energii),

intensywność skutku.

4. Pamięć epizodyczna zamiast reguł

Podstawową pamięcią jest lista epizodów (wspomnień), a nie:

reguły typu IF–THEN,

ani wagi sieci neuronowej.

Wnioski nie są zapisywane od razu.
Mózg najpierw gromadzi surowe doświadczenia.

5. Sen i konsolidacja

Agent posiada pojęcie snu jako stanu obliczeniowego, a nie biologicznej metafory.

Sen uruchamia się, gdy:

nagromadzi się dużo doświadczeń,

albo pojawi się bardzo silne doświadczenie (duża zmiana stanu).

Podczas snu mózg:

filtruje wspomnienia (zapominanie),

łączy podobne epizody,

próbuje znaleźć uogólnione zależności.

Przykład:

nie „RED daje energię”,

tylko „kombinacja cech A+B+C w kontekście głodu zwykle zwiększa energię”.

Wnioski są niepewne, probabilistyczne i mogą się mylić.

6. Brak twardych kategorii

Mózg nie tworzy pojęć typu „jedzenie” czy „wróg”.
Zamiast tego operuje na:

przewidywanych skutkach akcji,

zależnych od kontekstu (stan wewnętrzny, sytuacja przestrzenna).

To oznacza, że:

ten sam obiekt może być „dobry” w jednym stanie i „zły” w innym,

agent może się mylić i korygować przekonania.

7. Decyzje jako kompromis

Decyzja nie polega na maksymalizacji jednej nagrody.

Mózg waży:

aktualne potrzeby (np. energia),

ciekawość (brak wiedzy),

ryzyko (negatywne wspomnienia),

koszt akcji.

Dzięki temu:

agent czasem eksploruje,

czasem unika,

czasem wykonuje „bezsensowne” akcje i uczy się na błędach.

8. Zapominanie jako mechanizm kluczowy

Zapominanie nie jest błędem – jest konieczne.

Mózg:

usuwa rzadkie, słabe, niespójne doświadczenia,

zachowuje tylko te, które:

często się powtarzają,

lub miały silny wpływ na przetrwanie.

Bez tego system:

nie skaluje się,

przeucza się,

nie uogólnia.

9. Umysł roju

Agent może przekazywać skondensowane wnioski do wspólnej pamięci roju:

„ta kombinacja cech bywa niebezpieczna”,

„ta akcja często kończy się źle”.

Rój:

nie steruje agentami,

jedynie wpływa na ich początkowe uprzedzenia.

To pozwala badać:

emergencję kultury,

przyspieszoną adaptację,

różnice indywidualne.

Aktualny stan projektu

Projekt posiada już działający kod bazowy, obejmujący:

świat 2D oparty o tilemapę,

poprawny viewport świata + UI,

system inputu (kamera, klik, hover),

renderowanie świata i agentów,

prostego agenta z akcją MOVE,

architekturę podzieloną zgodnie z SOLID.

Kod traktujemy jako fundament eksperymentalny, a dalsze prace koncentrują się na:

rozwoju mózgu,

pamięci epizodycznej,

systemie akcji,

uczeniu i konsolidacji doświadczeń.

Dlaczego ten projekt jest interesujący badawczo

Ten system:

nie jest klasycznym RL,

nie jest symbolicznym AI,

nie jest czystą siecią neuronową.

Jest próbą zbadania:

jak daleko można dojść na prostych mechanizmach pamięci i doświadczenia,

jak powstają znaczenia bez ontologii,

jak zachowanie staje się „sensowne” bez jawnych reguł.

To dobry poligon do:

eksperymentów,

wizualnej analizy zachowania,

testowania nietypowych hipotez o uczeniu.