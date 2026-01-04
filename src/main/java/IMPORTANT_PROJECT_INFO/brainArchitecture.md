
ARCHITEKTURA MÓZGU I PRZEPŁYW DANYCH

Mózg jest osobnym komponentem, niepowiązanym z renderem ani z fizyką. Ma tylko jeden obowiązek: na podstawie percepcji i stanu wewnętrznego wybrać intencję akcji, a po wykonaniu akcji zaktualizować pamięć.

1. Podstawowe pojęcia

A) Sygnał (Trait/Feature)
Cecha to „atom informacji”, ale agent nie powinien polegać na nazwach typu “RED”. W kodzie cecha może mieć:

* stabilny identyfikator (int/short),
* kanał (np. wzrok / węch / dotyk),
* ewentualnie parametry (np. intensywność).

To jest ważne: system świata może używać enumów/nazw, ale mózg może widzieć tylko ID.

B) Percepcja (Observation)
Percepcja w jednej klatce / turze to zbiór “bodźców”:

* bodziec = {relativePos(dx,dy), set<traitId>, opcjonalnie intensywności}
  Agent musi rozumieć przestrzeń: to jest część obserwacji. Nie tylko “co widzę”, ale “gdzie”.

C) Stan wewnętrzny (Needs/Stats)
Np. energy, integrity, hydration. Mózg widzi:

* aktualne wartości,
* ich tempo zmian (opcjonalnie),
* progi (np. “głód”).

D) Akcja (Action)
Akcja to:

* typ (MOVE, INSPECT, CONSUME, ESCAPE, SLEEP)
* parametry (np. target tile dx/dy lub konkretne współrzędne)
  Każda akcja ma koszt (np. energia) i czas trwania.

E) Skutek (Outcome / Delta)
Po wykonaniu akcji agent dostaje outcome:

* delta statystyk (Δenergy, Δintegrity...)
* dane środowiskowe wykryte dopiero po akcji (np. “ujawniły się ukryte cechy po zjedzeniu”)
* wynik “czy akcja była możliwa” (np. próbował zjeść pusty kafelek)

F) Epizod (Episode / Experience)
Epizod to zapis doświadczenia:

* kontekst przed akcją (obserwacja + stan)
* akcja
* rezultat (delta statystyk + nowe bodźce)
* “moc” doświadczenia (np. suma bezwzględnych zmian + kara za śmierć)

2. Interfejsy komponentów mózgu

A) Mind (główny interfejs)

* decide(observation, internalState) -> ActionIntent
* onActionResult(preContext, action, outcome) -> void
* tick(dt) -> void (np. do liczenia senności)
* onSleepTick() -> void (konsolidacja)

B) PerceptionEncoder (kodowanie percepcji)
Wejście: Observation (lista bodźców z pozycją)
Wyjście: tokeny/cechy do pamięci:

* “feature key” = (traitId, relativePosBucket, contextFlags)
  Przykład: trait=“MOVING”, dx=+1, dy=0, needState=HUNGRY -> key

C) EpisodicMemory (magazyn doświadczeń)

* addEpisode(Episode)
* sampleSimilar(context) -> list<Episode>
* prune(forgettingPolicy)

D) ConsolidationEngine (sen)

* consolidate(episodes) -> list<RuleOrPrototype>
* updateModels(sharedModel)

E) ValueModel / AssociationModel (uogólniona pamięć)
Model “pół-reguł” oparty o statystyki, np.:

* expectedDelta(featureKey, actionType) -> E[Δenergy], E[Δintegrity]
* uncertainty(featureKey, actionType) -> np. liczność próbek, wariancja

To nie są twarde reguły, tylko tabela/estymator.

F) Policy / InstinctController (wybór akcji)
Bierze:

* aktualne potrzeby,
* przewidywania ValueModel,
* ciekawość i ryzyko,
* koszt akcji,
  I wybiera akcję (czasem losowo, z błędami).

3. Przepływ danych w pętli życia

Krok 1: Sensing
Agent zbiera Observation tylko w granicach zmysłów i zasięgu (FOV). Observation zawiera też pozycję agenta i relacje przestrzenne.

Krok 2: Encoding
PerceptionEncoder tworzy “feature keys” uwzględniające:

* cecha
* pozycja względna
* kontekst (np. hungry / safe / hurt)

Krok 3: Decision
Policy korzysta z ValueModel:

* ocenia dla każdej możliwej akcji oczekiwany zysk/ryzyko
* dodaje koszt energii za akcję
* dodaje komponent ciekawości: preferuj niepewne, ale nie ryzykowne
  Wynikiem jest ActionIntent.

Krok 4: Execution (poza mózgiem)
System świata sprawdza, czy akcja jest możliwa, wykonuje ją w czasie, nalicza koszt, tworzy Outcome.

Krok 5: Learning (episodic)
Mind zapisuje epizod:

* preContext: observation + stats
* action
* outcome
* intensity
  Episod trafia do EpisodicMemory.

Krok 6: Sleep trigger
Mind zwiększa “sleep pressure” gdy:

* jest dużo nowych epizodów
* epizody są intensywne
  Gdy przekroczy próg -> wybiera akcję SLEEP.

Krok 7: Consolidation
Podczas snu ConsolidationEngine:

* bierze batch epizodów (np. ostatnie N + próbka starszych)
* grupuje podobne konteksty (clustering po feature keys)
* aktualizuje ValueModel (uśrednianie, bayes, EMA)
* usuwa słabe/pojedyncze zależności (forgetting)

4. Wymuszenie “świadomości przestrzeni” i “analizy ruchu”

Żeby agent rozumiał ruch i przestrzeń, epizod musi przechowywać:

* pozycję przed i po
* relację: “czy zbliżyłem się do bodźca?”
  Podczas konsolidacji można tworzyć hipotezy typu:
* “MOVE powoduje zmianę obserwacji; czasem ujawnia nowe bodźce”
  Nie jako regułę, tylko jako statystykę: MOVE zwiększa entropię obserwacji i szansę znalezienia pozytywnych bodźców.

PSEUDOKOD: PIERWSZA WERSJA PAMIĘCI EPIZODYCZNEJ I SNU

Założenie: najpierw działa prosty system:

* pamięć epizodów jako lista
* prosta konsolidacja do tabeli asocjacji (featureKey × actionType -> statystyka Δ)
* zapominanie przez progi i limit pojemności

1. Struktury danych

type TraitId = int

struct Stimulus:
dx, dy: int               // pozycja względna
traits: set<TraitId>      // co wykryto
strength: optional float  // opcjonalnie

struct Observation:
selfX, selfY: int
stimuli: list<Stimulus>

struct InternalState:
energy: float
integrity: float
hydration: float

struct Action:
type: ActionType
targetDx, targetDy: int  // np. ruch / interakcja w kierunku
durationMs: int

struct Outcome:
success: bool
deltaEnergy: float
deltaIntegrity: float
deltaHydration: float
revealedTraits: set<TraitId>   // np. po konsumpcji
newObservation: optional Observation

struct FeatureKey:
traitId: TraitId
dxBucket, dyBucket: int     // bucketyzacja przestrzeni
contextFlags: bitset        // np. hungry, lowIntegrity, etc.

struct Episode:
time: long
preObs: Observation
preState: InternalState
action: Action
outcome: Outcome
intensity: float
encodedKeys: list<FeatureKey>

2. Kodowanie percepcji

function encode(observation, state) -> list<FeatureKey>:
flags = 0
if state.energy < HUNGER_THRESHOLD: flags |= HUNGRY
if state.integrity < DANGER_THRESHOLD: flags |= INJURED

keys = []
for stim in observation.stimuli:
dxB = bucket(stim.dx)
dyB = bucket(stim.dy)
for trait in stim.traits:
keys.add( FeatureKey(trait, dxB, dyB, flags) )
return keys

bucket(v):
// np. -inf..-2 => -2, -1 => -1, 0 => 0, +1 => 1, +2..inf => 2
return clamp(v, -2, +2)

3. Zapis epizodu

function onActionResult(preObs, preState, action, outcome):
keys = encode(preObs, preState)
intensity = computeIntensity(preState, outcome)

ep = Episode(now(), preObs, preState, action, outcome, intensity, keys)
episodicMemory.add(ep)

sleepPressure += intensity
sleepPressure += 1  // stały przyrost za epizod
if episodicMemory.size % 20 == 0: sleepPressure += 5

computeIntensity(preState, outcome):
// proste i stabilne:
return abs(outcome.deltaEnergy) + abs(outcome.deltaIntegrity) + abs(outcome.deltaHydration)

4. Wybór akcji z modelu asocjacji (przed snem i po śnie)

Trzymamy prosty model:
Assoc[actionType][featureKey] = Stats(meanDeltaEnergy, meanDeltaIntegrity, count, variance)

function predict(actionType, keys):
// sumujemy wpływy wielu keys, ale z clampem żeby nie eksplodowało
predE = 0
predI = 0
uncertainty = 0

for k in keys:
stats = Assoc[actionType].get(k)
if stats exists:
predE += stats.meanDeltaEnergy
predI += stats.meanDeltaIntegrity
uncertainty += 1 / sqrt(stats.count)
else:
uncertainty += 1  // nieznane = ciekawe

return (predE, predI, uncertainty)

function decide(observation, state):
keys = encode(observation, state)

best = null
bestScore = -INF

for candidateAction in enumeratePossibleActions(observation):
(predE, predI, unc) = predict(candidateAction.type, keys)

```
needWeight = if state.energy low then 2 else 1
riskPenalty = max(0, -predI) * 2         // negatywna integralność = ryzyko
curiosityBonus = unc * 0.3               // kontrolowany
costPenalty = actionCost(candidateAction) // każda akcja kosztuje energię

score = needWeight * predE - riskPenalty + curiosityBonus - costPenalty

// błąd/losowość
score += randomGaussian(0, 0.2)

if score > bestScore:
  bestScore = score
  best = candidateAction
```

// jeśli sleepPressure wysokie i nie ma krytycznego głodu -> SLEEP
if sleepPressure > SLEEP_THRESHOLD and state.energy > STARVATION_THRESHOLD:
return Action(SLEEP)

return best

5. Sen: konsolidacja + zapominanie

function sleepTick():
// agent odzyskuje energię per tick snu (to poza mózgiem, ale tu spada presja)
sleepPressure -= SLEEP_DECAY

batch = episodicMemory.sampleRecentAndSalient(N=200)
consolidate(batch)
episodicMemory.prune()

function consolidate(batch):
// aktualizujemy Assoc przez proste uśrednianie z wagą intensywności
for ep in batch:
a = ep.action.type
dE = ep.outcome.deltaEnergy
dI = ep.outcome.deltaIntegrity

```
for k in ep.encodedKeys:
  stats = Assoc[a].getOrCreate(k)
  stats.update(dE, dI, weight=ep.intensity)
```

// opcjonalnie: wykrywanie sprzeczności i zwiększanie "uncertainty"

Stats.update(dE, dI, weight):
// np. EMA albo Welford
count += weight
meanDeltaEnergy = meanDeltaEnergy + (weight/count) * (dE - meanDeltaEnergy)
meanDeltaIntegrity = meanDeltaIntegrity + (weight/count) * (dI - meanDeltaIntegrity)

6. Zapominanie (pruning)

function prune():
// limit pamięci epizodów
if episodicMemory.size > EPISODE_CAP:
remove oldest until size == EPISODE_CAP

// usuwanie słabych zależności z Assoc
for each actionType in Assoc:
for each (key, stats) in Assoc[actionType]:
if stats.count < MIN_COUNT and abs(stats.meanDeltaEnergy) < EPS and abs(stats.meanDeltaIntegrity) < EPS:
remove (key)

7. “Kawa z cytryną” i unikanie nadmiernej generalizacji

Żeby uniknąć prostych błędów:

* FeatureKey zawiera kontekst (hungry vs not hungry)
* zawiera pozycję względną (gdzie bodziec był)
* i dopiero suma wielu keys daje predykcję

Dodatkowo:

* model nie daje 100% pewności: wynik ma uncertainty
* ciekawość promuje eksplorację, ale ryzyko blokuje samobójcze testy
* “puste akcje” też są epizodami: uczą kosztu i braku efektu

To jest pierwsza wersja. Dalszy rozwój:

* lepszy clustering epizodów w śnie (prototypy sytuacji)
* wykrywanie zależności sekwencyjnych (akcja→akcja→skutek)
* uczenie “affordances” (co w ogóle jest możliwe z obiektem)
* osobne modele dla różnych NeedType i różnych zmysłów
