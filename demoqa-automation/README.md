# DemoQA Automation

![CI](https://github.com/fedorcoved-web/demoqa-automation/actions/workflows/ci.yml/badge.svg)

UI and API test automation suite for [demoqa.com](https://demoqa.com), built with Selenium WebDriver and TestNG.

📊 [Latest Allure report (GitHub Pages)](https://fedorcoved-web.github.io/demoqa-automation/) — published automatically after every successful run on `master`.

## Stack

- Java 17
- Selenium WebDriver 4.18
- TestNG 7.9
- REST Assured (API tests)
- Allure Report
- ExtentReports (HTML report with screenshots on failure)
- WebDriverManager
- Healenium (self-healing locators)
- Log4j2

## Project structure

- `src/test/java/pages` — page objects
- `src/test/java/tests` — UI and API test classes
- `src/test/java/base` — base classes for UI (`BaseTest`) and API (`ApiBaseTest`) tests
- `src/test/java/utils` — listeners, retry analyzer, config/report utilities
- `src/test/resources` — TestNG suite files (`testng.xml`, `testng-smoke.xml`, `testng-sanity.xml`, `testng-regression.xml`)

## Prerequisites

- JDK 17
- Maven 3.9+
- Google Chrome
- A `reqres.in` API key (see below), required by `ApiBaseTest` for the API tests

## Configuration

Copy the API key into one of:

- `src/test/resources/config.properties` (gitignored, for local runs): `reqres.api.key=<your-key>`
- or the `REQRES_API_KEY` environment variable (used on CI)

Get a free key at [reqres.in/signup](https://reqres.in/signup).

## Running tests

Full regression suite, visible browser:

```bash
mvn test
```

Smoke suite only, headless (same command CI runs):

```bash
mvn test -Dsuite.file=testng-smoke -Dheadless=true
```

Other suites: swap `-Dsuite.file` for `testng-sanity` or `testng-regression`.

## Reports

- **Allure**: results are written to `target/allure-results`; view them locally with `mvn io.qameta.allure:allure-maven:serve`, or check the [published report](https://fedorcoved-web.github.io/demoqa-automation/) on GitHub Pages.
- **ExtentReports**: generated at `test-output/ExtentReport.html` after each run, with screenshots attached on failure.

## CI

Every push and pull request runs the smoke suite headlessly on GitHub Actions (`.github/workflows/ci.yml`). Allure results and the ExtentReport are uploaded as workflow artifacts, and a pass/fail summary is posted to the run summary. On a successful run on `master`, the Allure HTML report is published to GitHub Pages.
