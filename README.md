# Kitapyurdu.com - Software Quality Assurance & Test Automation Project

This project represents a full-cycle Quality Assurance (QA) lifecycle implementation for Kitapyurdu.com, one of the leading e-commerce platforms for literature and educational materials . The objective was to build a robust testing architecture that combines automated regression suites with deep manual audits.

**🛠️ Engineering Stack**
Automation Engines: Selenium WebDriver (Cross-language implementation in Java & Python) .
Testing Frameworks: JUnit 5 for structured Java validation and Unittest for Python scripting .
Performance Engineering: Apache JMeter for stress testing and Google Lighthouse for web vitals auditing .

**🧪 Advanced Testing Strategies**
The testing suite was architected using specialized Black-box methodologies to ensure maximum fault detection :

**Boundary Value Analysis (BVA):** Precision testing of search input buffers (0-100+ characters) and numerical boundaries for price filters .
**Equivalence Partitioning (ECP):** Optimized test data sets for registration modules and category filtering to ensure high coverage with minimal redundancy .
**Decision Table Logic:** Systematic validation of user authentication flows under varied credential states .
**End-to-End Use Case Testing:** Comprehensive validation of the critical "Checkout to Order Summary" workflow .

**📊 Technical Audit & Performance Analytics**
A rigorous evaluation of the production environment revealed critical insights:
Performance Bottlenecks: Lighthouse score of 49/100, primarily due to unoptimized assets and main-thread blocking .
Latency Analysis: JMeter load tests identified the Home Page as a primary bottleneck with response times peaking at ~19 seconds.
UX & Accessibility: Comprehensive audit showing a score of 64/100, with detailed reports on contrast ratios and ARIA label missingness .

**📁 Repository Organization**
src/java_tests: Selenium scripts integrated with JUnit 5 .
src/python_tests: Python-based automation scripts and configuration .
report/: Comprehensive Test Completion Report and diagnostic data .
presentation/: Technical walkthrough and executive summary.

## 👥 Contributors
This project was a collaborative effort by:
- [Miray Yıldırım](https://github.com/mirayyldrm)
- [İsmail Tekin](https://github.com/IsmailTekin05)
- [Dilara Kuyar](https://github.com/DilaraKuyar)
- [İlayda Gürbüzerol](https://github.com/)
