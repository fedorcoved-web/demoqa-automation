# Code Review: demoqa-automation

> Повний аудит проєкту рівня Senior/Staff engineer.
> Дата: 2026-07-05. Охоплення: усі 37 Java-файлів, pom.xml, testng-суїти, ресурси, .gitignore, docker-compose, git-історія.

---

## 🔴 КРИТИЧНІ ПРОБЛЕМИ

### 1. Проєкт не компілюється зі свіжого клону — ключові файли не в git

❌ **ПРОБЛЕМА:** `git status` показує, що ці файли **untracked** (ніколи не закомічені):
- `pages/DynamicPropertiesPage.java` — при цьому `tests/DynamicPropertiesTest.java` **закомічений** і імпортує його → **compile error на свіжому клоні**
- `utils/RetryAnalyzerExtension.java` — на нього посилається `testng.xml` як на listener
- `resources/log4j2.xml`, `resources/allure.properties` — без них логування і Allure мовчки деградують
- `resources/test-upload.txt` — без нього `UploadDownloadTest` падає

📍 **МІСЦЕ:** корінь репозиторію (git-стан, не код)

💡 **РІШЕННЯ:**
```bash
git add demoqa-automation/src/test/java/pages/DynamicPropertiesPage.java \
        demoqa-automation/src/test/java/utils/RetryAnalyzerExtension.java \
        demoqa-automation/src/test/resources/log4j2.xml \
        demoqa-automation/src/test/resources/allure.properties \
        demoqa-automation/src/test/resources/test-upload.txt \
        demoqa-automation/src/test/resources/testng.xml
```
І додати в `.gitignore`: `test-output/`, `.allure/`, `*.py` (або перенести python-скрипти в `tools/`). Для портфоліо «клонував → `mvn test` → працює» — це перше, що перевірить рев'юер.

---

### 2. Захардкоджений API-ключ, закомічений в git

❌ **ПРОБЛЕМА:** ключ reqres.in лежить у коді і вже в git-історії (присутній у HEAD). Так, це безкоштовний ключ, але для рев'юера це маркер: «кандидат коміть секрети». Іронія в тому, що поруч уже є правильний механізм — `config.properties` у `.gitignore`, і `TestinyUploader` ним користується.

📍 **МІСЦЕ:** `ApiBaseTest.java:10`

💡 **РІШЕННЯ:**
```java
public class ApiBaseTest {
    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        String apiKey = System.getenv().getOrDefault("REQRES_API_KEY",
                ConfigReader.get("reqres.api.key")); // fallback на config.properties
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in/api")
                .addHeader("x-api-key", apiKey)
                .build();
    }
}
```
Ключ ротувати (він уже скомпрометований історією).

---

### 3. False-positive у testEditRow: sendKeys без clear() + assertion за `contains`

❌ **ПРОБЛЕМА:** подвійний баг, який маскує сам себе:
1. `editSalary()` робить `salaryInput.sendKeys("11000")` **без очистки поля**. Форма редагування предзаповнена зарплатою Cierra (`10000`), тож у полі стає `1000011000`.
2. Тест перевіряє `isNamePresentInTable("11000")` через `row.getText().contains("11000")` — рядок `1000011000` **містить** `11000`, тому тест **зелений, хоча редагування виконалось неправильно**.

📍 **МІСЦЕ:** `WebTablesPage.java:78-80`, `WebTablesTest.java:66`

💡 **РІШЕННЯ:**
```java
// WebTablesPage
public void editSalary(String salary) {
    salaryInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
    salaryInput.sendKeys(salary);
}

public String getCellText(int rowIndex, int colIndex) {
    return tableRows.get(rowIndex).findElements(By.tagName("td")).get(colIndex).getText();
}

// WebTablesTest — точна перевірка колонки Salary, а не contains по всьому рядку
Assert.assertEquals(page.getCellText(0, 4), "11000",
        "Salary column of edited row should equal exactly 11000");
```

---

### 4. False-positive у testDeleteRow: тест пройде, навіть якщо нічого не видалено

❌ **ПРОБЛЕМА:** `deleteFirstRow()` мовчки нічого не робить, якщо кнопок немає (`if (!deleteButtons.isEmpty())`). Якщо пошук «Cierra» нічого не знайшов (сайт змінив дані, пошук зламався) — видалення не відбулось, а `assertFalse(isNamePresentInTable("Cierra"))` все одно зелений, бо таблиця порожня. Тест перевіряє відсутність, не перевіривши присутність до дії.

📍 **МІСЦЕ:** `WebTablesPage.java:105-109`, `WebTablesTest.java:42-51`

💡 **РІШЕННЯ:**
```java
// Page: fail-fast замість тихого no-op
public void deleteFirstRow() {
    if (deleteButtons.isEmpty()) {
        throw new IllegalStateException("No delete buttons found — search returned no rows");
    }
    safeClick(deleteButtons.get(0));
}

// Test: arrange-перевірка перед дією
page.searchFor("Cierra");
Assert.assertTrue(page.waitForNameInTable("Cierra"), "Precondition: row must exist before delete");
page.deleteFirstRow();
Assert.assertFalse(page.isNamePresentInTable("Cierra"), "...");
```

---

### 5. Хардкод Windows-шляху — тест гарантовано падає на Linux/CI

❌ **ПРОБЛЕМА:** `"\\src\\test\\resources\\test-upload.txt"` — бекслеші зламаються на Linux, а `System.getProperty("user.dir")` вказує на директорію запуску JVM: з IDE у корені репо це `D:\MvnProject\DemoQA` (без сегмента `demoqa-automation`) — шлях не існує.

📍 **МІСЦЕ:** `UploadDownloadTest.java:29`

💡 **РІШЕННЯ:** брати файл із classpath — працює всюди:
```java
String filePath = Paths.get(
        getClass().getClassLoader().getResource("test-upload.txt").toURI()
).toAbsolutePath().toString();
page.uploadFile(filePath);
```

---

## 🟡 ВАЖЛИВІ ПРОБЛЕМИ

### 6. Логіка «retry → пропустити скріншот» у onTestFailure — найімовірніше мертвий код

❌ **ПРОБЛЕМА:** у TestNG 7.x спроба, яку буде повторено (retry() повернув true), отримує статус **SKIP** і викликає `onTestSkipped`, а не `onTestFailure`. Тобто:
- гілка `isRetryAvailable` + Allure-хак зі `Status.SKIPPED` у `onTestFailure` не виконується ніколи (на фінальному падінні лічильник уже `3 > MAX_RETRY`);
- retried-спроби потрапляють в Extent через `onTestSkipped` як звичайні «skipped» без пояснення.

📍 **МІСЦЕ:** `TestListener.java:44-56`, `TestListener.java:74-83`

💡 **РІШЕННЯ:** прибрати мертву гілку і використати штатний API `wasRetried()`:
```java
@Override
public void onTestSkipped(ITestResult result) {
    ExtentTest test = getTest();
    if (test == null) return;
    if (result.wasRetried()) {
        test.skip("Failed attempt — will be retried. Cause: " + result.getThrowable());
        return;
    }
    test.skip(result.getThrowable() != null ? result.getThrowable() : new Exception("Test skipped"));
}
```
Тоді ж можна видалити `isRetryAvailable()` з `RetryAnalyzer` — і клас стає тривіальним. (Рекомендація: перевірити фактичну поведінку одним прогоном із навмисним падінням — це визначить, який шлях реально виконується.)

### 7. Healenium жорстко вшитий у BasePage без можливості вимкнути

❌ **ПРОБЛЕМА:** `SelfHealingDriver.create(driver)` викликається в конструкторі **кожного** page object:
- кожен `new XxxPage()` створює новий proxy-обгортку (в одному тесті їх може бути кілька);
- без піднятого Docker-бекенда (localhost:7878) кожна дія генерує помилки з'єднання в лог і сповільнює прогін;
- `hlm.properties` немає в ресурсах — конфігурація неявна, на дефолтах;
- версії розсинхронізовані: клієнт `healenium-web 3.4.4` (pom.xml:24) проти бекенда `hlm-backend:3.4.2` (docker-compose.yml:24).

📍 **МІСЦЕ:** `BasePage.java:20`

💡 **РІШЕННЯ:** обгортати один раз у `BaseTest.setUp()` під прапорцем:
```java
// BaseTest.setUp()
WebDriver driver = new ChromeDriver(options);
if (Boolean.parseBoolean(System.getProperty("healenium.enabled", "false"))) {
    driver = SelfHealingDriver.create(driver);
}
driverThread.set(driver);

// BasePage — приймає вже готовий драйвер
protected BasePage(WebDriver driver) {
    this.driver = driver;
    ...
}
```

### 8. Немає жодної конфігурації браузера — CI неможливий

❌ **ПРОБЛЕМА:** Chrome захардкоджений, headless-режиму немає, `--start-maximized` марний на CI. Base URL `https://demoqa.com` продубльований у 14 page-класах.

📍 **МІСЦЕ:** `BaseTest.java:34-41`, всі `*Page.navigateTo()`

💡 **РІШЕННЯ:**
```java
ChromeOptions options = new ChromeOptions();
if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
    options.addArguments("--headless=new", "--window-size=1920,1080");
} else {
    options.addArguments("--start-maximized");
}
```
І централізувати URL: `navigateTo(ConfigReader.baseUrl() + "/text-box")`.

### 9. dismissAds() видаляє ВСІ iframe на сторінці

❌ **ПРОБЛЕМА:** селектор починається з `iframe` без уточнення — зноситься будь-який iframe. Сьогодні це працює, але щойно додати тести на `/frames` чи `/nestedframes` (очевидний наступний крок для DemoQA-проєкту) — `navigateTo()` знищить об'єкт тестування, і дебажити це буде боляче. `[id*="ad-"]` теж ризиковано широкий.

📍 **МІСЦЕ:** `BasePage.java:38-45`

💡 **РІШЕННЯ:**
```java
js.executeScript(
    "var ads = document.querySelectorAll(" +
    "  'iframe[id^=\"google_ads\"], [id*=\"google_ads\"], [class*=\"adsby\"], #adplus-anchor'" +
    ");" +
    "ads.forEach(function(ad) { ad.parentNode && ad.parentNode.removeChild(ad); });"
);
```

### 10. testng.xml: тести загублені, назви з помилками

❌ **ПРОБЛЕМА:**
- `UploadDownloadTest` і `DynamicPropertiesTest` **відсутні у головній суїті** — «повний» прогін їх не запускає (у smoke вони є, тобто smoke ширший за full — нонсенс);
- `BrokenImagesTest` не має `groups`, тому випадає з усіх групових суїт (smoke/sanity/regression);
- `<test name="Elem ents">` — одруківка з пробілом (testng.xml:10);
- `BrokenImagesTest` живе під `"Book Store Application"` і `@Epic("Book Store Application")` — але `/broken` це розділ Elements (BrokenImagesTest.java:15).

💡 **РІШЕННЯ:** додати обидва класи у відповідні `<test>`-блоки, дати `groups = {"smoke", "regression"}` BrokenImagesTest, виправити назви/епік.

### 11. safeClick(): catch (Exception) приховує реальні проблеми

❌ **ПРОБЛЕМА:** будь-який збій (включно зі `StaleElementReferenceException`, де JS-клік теж впаде, але вже з незрозумілим стеком) мовчки конвертується у JS-клік. JS-клік оминає перевірки видимості/перекриття — тест може «клікнути» елемент, по якому користувач фізично не може клікнути.

📍 **МІСЦЕ:** `BasePage.java:51-55`

💡 **РІШЕННЯ:** звузити до очікуваного винятку і залогувати fallback:
```java
try {
    element.click();
} catch (ElementClickInterceptedException e) {
    log.warn("Click intercepted, falling back to JS click: {}", e.getMessage());
    js.executeScript("arguments[0].click();", element);
}
```

### 12. DynamicPropertiesTest — тавтологічний assert

❌ **ПРОБЛЕМА:** `clickVisibleAfterButton()` уже дочекався видимості й клікнув; далі `assertTrue(isButtonVisible())` не може впасти інакше як через баг самого тесту. Клік нічого не змінює на сторінці — тест нічого не перевіряє. А поле `enableAfterButton` (справжній сенс сторінки — дочекатись `elementToBeClickable`) не використовується взагалі.

📍 **МІСЦЕ:** `DynamicPropertiesTest.java:29-32`, `DynamicPropertiesPage.java:10-11`

💡 **РІШЕННЯ:**
```java
// Page
public boolean waitForEnableAfterButtonEnabled() {
    try {
        wait.until(ExpectedConditions.elementToBeClickable(enableAfterButton));
        return true;
    } catch (TimeoutException e) { return false; }
}
// Test
Assert.assertTrue(page.waitForEnableAfterButtonEnabled(),
        "enableAfter button should become enabled within 5 seconds");
```

### 13. Локатор кнопки Edit ігнорує результат пошуку

❌ **ПРОБЛЕМА:** `span[id='edit-record-1'] svg path` — завжди «record-1», незалежно від того, що знайшов пошук «Cierra». Збіг, що Cierra і є record-1. Плюс клік по `path` замість `span`, і raw `.click()` замість `safeClick` (єдине місце в проєкті).

📍 **МІСЦЕ:** `WebTablesPage.java:47-48`, `WebTablesPage.java:64-66`

💡 **РІШЕННЯ:**
```java
@FindBy(css = "span[title='Edit']")
private List<WebElement> editButtons;

public void clickFirstEditButton() {
    if (editButtons.isEmpty()) throw new IllegalStateException("No edit buttons — search returned no rows");
    safeClick(editButtons.get(0));
    wait.until(ExpectedConditions.visibilityOf(firstNameInput));
}
```

---

## 🟢 ПОКРАЩЕННЯ

### 14. JsonDataProvider: два методи-близнюки

📍 `JsonDataProvider.java:15-47` — `readTextBoxData` і `readApiUserData` відрізняються лише шляхом і списком ключів.

💡 **РІШЕННЯ:**
```java
public static Object[][] read(String path, String... keys) {
    try (InputStream is = open(path)) {
        List<Map<String, String>> rows = mapper.readValue(is, new TypeReference<>() {});
        return rows.stream()
                .map(r -> Arrays.stream(keys).map(r::get).toArray())
                .toArray(Object[][]::new);
    } catch (IOException e) {
        throw new UncheckedIOException("Failed to read " + path, e);
    }
}
// Використання: JsonDataProvider.read("testdata/users.json", "name", "email", "currentAddress", "permanentAddress")
```
Ще краще — десеріалізувати в POJO (`User.class`) і передавати об'єкт у тест.

### 15. WebDriverManager.setup() на кожен тест-метод

📍 `BaseTest.java:34` — резолв драйвера (з перевіркою версій) виконується перед кожним тестом. Перенести в `@BeforeSuite`. А з Selenium 4.18 вбудований Selenium Manager робить WDM взагалі зайвою залежністю.

### 16. Дубльований boilerplate у кожному тесті

- `log.info("Starting test...")/log.info("Test completed...")` × 20 — це робота `TestListener.onTestStart/onTestSuccess`;
- текст дубльований у `@Description` і `@Test(description=...)` — залишити одне;
- `private static final Logger log` у кожному класі — можна protected logger у `BaseTest`.

### 17. pom.xml

- суїта захардкоджена (pom.xml:103): smoke/sanity/regression-суїти неможливо запустити без `-Dsurefire.suiteXmlFiles`. Додати property: `<suiteXmlFile>src/test/resources/${suite.file}.xml</suiteXmlFile>` + `<suite.file>testng</suite.file>` у properties → `mvn test -Dsuite.file=testng-smoke`;
- `jackson-databind` версія інлайном (pom.xml:85), решта через properties — непослідовно;
- Selenium 4.18.1 (лютий 2024) — оновити до актуальної 4.2x, інакше CDP-warnings із новим Chrome.

### 18. ExtentManager: звіт пишеться у `test-output/` відносно робочої директорії

📍 `ExtentManager.java:13` — залежить від того, звідки запущено JVM (та сама проблема, що з upload-шляхом). Писати в `target/extent-report/ExtentReport.html` — воно автоматично чиститься `mvn clean`.

### 19. DatePicker: `Keys.chord(Keys.CONTROL, "a")` не працює на macOS

📍 `DatePickerPage.java:25` — на Mac потрібен `Keys.COMMAND`. Платформо-незалежний варіант: `datePickerInput.clear()` або JS `arguments[0].value=''`.

### 20. Дрібниці

- `UserApiTest.java:105` — `"Junior  QA Engineer"` з подвійним пробілом (симетричний, тому тест проходить, але це одруківка);
- `docker-compose.yml:10` — пароль Postgres у відкритому вигляді; для локальної інфри некритично, але правильніше через `.env`;
- `TestinyUploader.java:24` — захардкоджені `toDelete = {10..15}`: одноразовий деструктивний скрипт закомічений як постійний код; винести ID в аргументи або видалити крок;
- `PracticeFormPage.java:42-46` — `react-select-3-input`/`-4-input` залежать від порядку рендера; стабільніше `#state input`, `#city input`;
- `PracticeFormPage.java:115-119` — зайві порожні рядки та з'їхала закриваюча дужка класу.

---

# ЗАГАЛЬНА ОЦІНКА ПРОЄКТУ: 6.5/10

Для рівня Junior QA Automation це **добротний проєкт вище середнього** — видно розуміння ThreadLocal, retry-механіки, listener'ів, data-driven підходу. Але критичні проблеми з git-гігієною (проєкт не збирається зі свіжого клону!) і false-positive тести (найгірший гріх тестувальника) не дозволяють поставити вище.

## ТОП-5 виправлень за пріоритетом

1. **Закомітити untracked-файли** (DynamicPropertiesPage, RetryAnalyzerExtension, log4j2.xml, test-upload.txt) — без цього проєкт зламаний для будь-кого, крім автора.
2. **Прибрати API-ключ з коду** → env/config.properties, ключ ротувати.
3. **Виправити false-positives у WebTables** (clear перед sendKeys, точні assertions по колонці, fail-fast замість тихих no-op).
4. **Додати конфігураційний шар**: headless/browser через system properties, централізований base URL — це відкриває шлях до CI.
5. **Кросплатформні шляхи** (classpath замість `user.dir` + `\\`) і параметризація суїт у pom — після цього `mvn test` працює на будь-якій машині.

## Що вже зроблено добре ✅

- **ThreadLocal WebDriver** із захисним quit у setUp — коректна основа для паралельності;
- **Retry з окремим лічильником на кожен рядок DataProvider** (`method + params` як ключ) — нетривіальний нюанс, який більшість junior-проєктів пропускає;
- **Свідома робота з демо-сайтом**: dismissAds із поясненням причин, nativeInputValueSetter для React-слайдера, EAGER page load strategy, коментар про rct→rc-tree міграцію DemoQA — видно, що людина дебажила і розуміє *чому*, а не просто копіювала;
- **Два стеки звітності** (Allure + Extent) зі скріншотами на падіння;
- **Групи smoke/sanity/regression** + окремі суїти;
- **config.properties у .gitignore** і завантаження ключа Testiny з нього — правильний патерн (треба лише застосувати його і в ApiBaseTest);
- Чисті Page Object'и без assertions всередині сторінок, явні waits без жодного `Thread.sleep` у тестах.

---

# ПРОПОЗИЦІЇ ПО РОЗВИТКУ

## Найбільший вплив на портфоліо (робити в цьому порядку)

1. **GitHub Actions CI** — фіча №1 для портфоліо. Workflow: headless Chrome → smoke-прогін на push → публікація Allure-звіту на GitHub Pages → бейдж у README. Роботодавець бачить живий зелений білд і клікабельний звіт без клонування. Все інше в проєкті вже до цього готове (після фіксів №4-5 з топу).
2. **README.md** — його зараз немає. Структура проєкту, як запустити, скріншоти звітів, бейджі CI. Це вітрина, без якої решту ніхто не побачить.
3. **Selenium Grid / Testcontainers** — підняти Grid у docker-compose (він уже є для Healenium) і запускати кросбраузерно (Chrome + Firefox). Демонструє розуміння масштабування.
4. **Розширити API-шар**: POJO-моделі замість `Map<String,String>`, окремий `ApiClient`, негативні кейси (400 на невалідне тіло), JSON Schema validation (`rest-assured json-schema-validator`). Зараз API-частина найслабша — 5 тестів на happy path.
5. **SoftAssert + Faker**: у TextBox/PracticeForm перевірки логічно незалежні — SoftAssert покаже всі розбіжності за раз; JavaFaker замість статичних JSON-даних покаже вміння генерувати дані (JSON лишити для boundary-кейсів).

## Архітектурно далі

- **Fluent Page Objects** (`return this`) → тести читаються як сценарій: `page.navigate().fillForm(user).submit().assertOutput(user)`;
- **Крок до фреймворку**: винести `base/`, `utils/` у окремий maven-модуль `framework-core`, тести — у `tests`-модуль. Це вже Staff-рівень структури і сильно виділяє портфоліо;
- **Environment-профілі** (dev/qa через Owner library або власний ConfigReader) — стандарт індустрії;
- **Allure TestOps-стиль зв'язки**: `@TmsLink` на кейси в Testiny, які вже заливаються — замкне цикл «кейс ↔ автотест».

## Що НЕ варто додавати

Ще один звіт, BDD/Cucumber заради галочки (без живих стейкхолдерів це лише шар складності), мобільні тести в цей самий репозиторій. Краще менше інструментів, але кожен — обґрунтований, як зараз із Healenium.
